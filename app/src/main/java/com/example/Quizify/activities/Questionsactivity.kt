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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.Quizify.navigation.SystemBackButtonHandler
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import androidx.compose.ui.tooling.preview.Preview

// Define a data class to represent a question
data class Question(
    val question: String,
    val answers: List<String>,
    val correctAnswer: String
)

@Composable
fun QuestionItem(question: Question, questionNumber: Int, selectedAnswer: MutableState<String?>) {
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

        val radioOptions = rememberUpdatedState(question.answers)
        val selectedOption = selectedAnswer.value
        val isCorrect= selectedOption==question.correctAnswer
        radioOptions.value.forEach { option ->
            val isSelected = option == selectedOption
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = isSelected,
                        onClick = {
                            if (!isSelected) {
                                selectedAnswer.value = option
                            }
                        }
                    )
                    .padding(8.dp)
            ) {
                RadioButton(
                    selected = isSelected,
                    onClick = {
                        if (!isSelected) {
                            selectedAnswer.value = option
                        }
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = option)
                if(isSelected && isCorrect){
                    Text(text = "Correct! ${question.correctAnswer} ", color = Color.Green)
                }
                else if (isSelected && !isCorrect) {
                    Text(text = "Incorrect! ${question.correctAnswer}", color = Color.Red)
                }
            }
        }
    }
}

@Composable
fun ListWithButtons(questions: List<Question>) {
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
                selectedAnswer = selectedAnswer
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
                        selectedAnswer.value = null // Reset selected answer
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
                        selectedAnswer.value = null // Reset selected answer
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

    // Fetch questions from Firebase
    var questions by remember { mutableStateOf(emptyList<Question>()) }

    LaunchedEffect(Unit) {
        database.child("MCQS").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val fetchedQuestions = mutableListOf<Question>()
                for (childSnapshot in snapshot.children) {
                    val question = childSnapshot.child("question").getValue(String::class.java) ?: ""
                    val answers = childSnapshot.child("answers").getValue(object : GenericTypeIndicator<List<String>>() {}) ?: emptyList()
                    val correctAnswer = childSnapshot.child("correct_answers").child("0").getValue(String::class.java) ?: ""
                    val questionItem = Question(question, answers, correctAnswer)
                    fetchedQuestions.add(questionItem)
                }
                questions = fetchedQuestions
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    ListWithButtons(questions = questions)
    SystemBackButtonHandler()
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

    ListWithButtons(questions = sampleQuestions)
}
