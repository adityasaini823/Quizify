package com.example.Quizify.activities

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.Button
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.Quizify.navigation.SystemBackButtonHandler
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.Quizify.navigation.Quizapprouter
import com.example.Quizify.navigation.Screen

// Define a data class to represent a question
data class Question(
    val question: String,
    val answers: List<String>,
    val correctAnswer: String
)

@Composable
fun QuestionItem(
    question: Question,
    questionNumber: Int,
    selectedAnswer: MutableState<String?>,
    onAnswerSelected: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Question $questionNumber:",
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = question.question,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        val isCorrect = selectedAnswer.value == question.correctAnswer

        question.answers.forEach { option ->
            val isSelected = option == selectedAnswer.value
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                RadioButton(
                    selected = isSelected,
                    enabled = selectedAnswer.value == null,
                    onClick = {
                        if (selectedAnswer.value == null) {
                            selectedAnswer.value = option
                            onAnswerSelected(option == question.correctAnswer)
                        }
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = option)
                if (isSelected) {
                    if (isCorrect) {
                        Text(text = "Correct! ", color = Color.Green)
                    } else {
                        Text(text = "Incorrect!", color = Color.Red)
                    }
                }
            }
        }
    }
}

@Composable
fun ListWithButtons(questions: List<Question>, userAnswers: MutableList<String?>, onAnswerSelected: (Boolean) -> Unit) {
    var currentIndex by remember { mutableStateOf(0) }
    val selectedAnswer = remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (questions.isNotEmpty()) {
            QuestionItem(
                question = questions[currentIndex],
                questionNumber = currentIndex + 1,
                selectedAnswer = selectedAnswer,
                onAnswerSelected = onAnswerSelected
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    if (currentIndex > 0) {
                        currentIndex--
                        selectedAnswer.value = userAnswers[currentIndex]
                    }
                },
                enabled = currentIndex > 0
            ) {
                Text(text = "Back")
            }

            Button(
                onClick = {
                    if (currentIndex < questions.size - 1) {
                        currentIndex++
                        selectedAnswer.value = null
                    }
                },
                enabled = currentIndex < questions.size - 1
            ) {
                Text(text = "Next")
            }
        }
    }
}

@Composable
fun Questionsactivity() {
    // Initialize Firebase database reference
    val database: DatabaseReference = Firebase.database.reference

    // Initialize shuffledQuestions and userAnswers using remember
    var shuffledQuestions by remember { mutableStateOf<List<Question>>(emptyList()) }
    var userAnswers by remember { mutableStateOf(mutableListOf<String?>()) }

    // Other variables
    var currentIndex by remember { mutableStateOf(0) }
    val selectedAnswer = remember { mutableStateOf<String?>(null) }
    var isQuizCompleted by remember { mutableStateOf(false) }

    // Count of correct answers
    var correctAnswersCount by remember { mutableStateOf(0) }

    // Callback to update correctAnswersCount
    val onAnswerSelected: (Boolean) -> Unit = { isCorrect ->
        if (isCorrect) {
            correctAnswersCount++
        }
    }

    // Fetch the questions and shuffle them
    LaunchedEffect(Unit) {
        database.child("Aptitude").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val fetchedQuestions = mutableListOf<Question>()
                for (childSnapshot in snapshot.children) {
                    val question = childSnapshot.child("question").getValue(String::class.java) ?: ""
                    val answers = childSnapshot.child("answers")
                        .getValue(object : GenericTypeIndicator<List<String>>() {}) ?: emptyList()
                    val correctAnswer = childSnapshot.child("correct_answers").child("0")
                        .getValue(String::class.java) ?: ""
                    val questionItem = Question(question, answers, correctAnswer)
                    fetchedQuestions.add(questionItem)
                }
                // Update shuffledQuestions and userAnswers
                shuffledQuestions = fetchedQuestions.shuffled().take(10)
                userAnswers = MutableList(shuffledQuestions.size) { null }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (shuffledQuestions.isNotEmpty() && currentIndex < 10) {
            QuestionItem(
                question = shuffledQuestions[currentIndex],
                questionNumber = currentIndex + 1,
                selectedAnswer = selectedAnswer,
                onAnswerSelected = onAnswerSelected
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isQuizCompleted) {
            Text(
                text = "Quiz Completed! You got $correctAnswersCount out of 10 questions correct.",
                fontSize = 20.sp
            )
            Button(onClick = {
                // Handle navigation to Main Menu
                Quizapprouter.navigateTo(Screen.PracticeSets)

            }) {
                Text(text = "Main Menu")
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        if (currentIndex > 0) {
                            currentIndex--
                            selectedAnswer.value = userAnswers[currentIndex]
                        }
                    },
                    enabled = currentIndex > 0
                ) {
                    Text(text = "Back")
                }

                // Check if the user has answered all 10 questions
                if (currentIndex == 9) {
                    Button(
                        onClick = {
                            isQuizCompleted = true
                            // No need to count here, it's already done in onAnswerSelected
                        }
                    ) {
                        Text(text = "Submit")
                    }
                } else {
                    Button(
                        onClick = {
                            if (currentIndex < 9) {
                                currentIndex++
                                selectedAnswer.value = null
                            }
                        },
                        enabled = currentIndex < 9
                    ) {
                        Text(text = "Next")
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun QuestionsActivityPreview() {
    val sampleQuestions = listOf(
        Question(
            "Sample question 1",
            listOf("Option 1", "Option 2", "Option 3"),
            "Option 2"
        ),
        // Add more sample questions here
    )

    ListWithButtons(questions = sampleQuestions, userAnswers = MutableList(sampleQuestions.size) { null }) { _ -> }
}
