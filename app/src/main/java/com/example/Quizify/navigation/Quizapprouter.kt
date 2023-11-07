package com.example.Quizify.navigation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlin.math.log

sealed class Screen(){
    object Signupactivity:Screen()
    object Termsandcondition:Screen()
    object Loginactivity:Screen()
    object Homeactivity:Screen()
    object Profileactivity:Screen()
    object PracticeSets:Screen()
    object Questionsactivity:Screen()
    object AdminActivities:Screen()
    object AddQuestions:Screen()
    object AdminLoginActivity:Screen()
    object ForgetPassword:Screen()
}

object Quizapprouter {
    private val screenStack = mutableListOf<Screen>()
//    val currentScreen: MutableState<Screen> = mutableStateOf(Screen.Loginactivity)
val currentScreen: MutableState<Screen> = mutableStateOf(Screen.PracticeSets)

    fun navigateTo(destination: Screen) {
        println("okokok")
        screenStack.add(currentScreen.value)
        currentScreen.value = destination
    }

    fun navigateBack() {
        if (screenStack.isNotEmpty()) {
            currentScreen.value = screenStack.removeAt(screenStack.size - 1)
        }
    }

    fun clearScreenStack() {
        screenStack.clear()
    }
}
