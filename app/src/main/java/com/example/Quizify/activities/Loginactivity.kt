package com.example.Quizify.activities
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.Quizify.components.ButtonComponent
import com.example.Quizify.components.ClickableTextComponent
import com.example.Quizify.components.DividerTextComponent
import com.example.Quizify.components.HeadingTextComponent
import com.example.Quizify.components.NormalTextComponent
import com.example.Quizify.components.PasswordTextFields
import com.example.Quizify.components.TextFields
import com.example.Quizify.components.UnderlinedTextComponent
import com.example.Quizify.data.loginData.LoginUIevents
import com.example.Quizify.data.loginData.LoginViewModel
import com.example.Quizify.navigation.Quizapprouter
import com.example.Quizify.navigation.Screen
import com.example.Quizify.navigation.SystemBackButtonHandler
import com.example.Quizify.R
import com.example.Quizify.data.SignupData.SignupUIevents

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Loginactivity(loginViewModel: LoginViewModel = viewModel()){
    val state = loginViewModel.loginuistate.value
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Surface(
            color= Color.White,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Scaffold(
                bottomBar = {
                    BottomAppBar(
                        containerColor = Color.White
                    ) {
                        UnderlinedTextComponent(value = "Admin Login?", onClick = {
                            Quizapprouter.navigateTo(Screen.AdminLoginActivity)
                        })
                    }
                }
            ) {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(28.dp)) {
                    NormalTextComponent(value = stringResource(id = R.string.heythere))
                    HeadingTextComponent(value = stringResource(id = R.string.welcomeback))
                    Spacer(modifier = Modifier.height(10.dp))

                    var blankError by remember { mutableStateOf(false) }
                    var emailpassError by remember { mutableStateOf(false) }
                    var errorMessage by remember { mutableStateOf("") }
                    val passwordvisibility = remember {
                        mutableStateOf(false)
                    }

                    if (blankError) {
                        Text(
                            text = "All fields are mandatory",
                            color = Color.Red,
                            fontSize = 12.sp
                        )
                    }else if (emailpassError) {
                        Text(
                            text = errorMessage,
                            color = Color.Red,
                            fontSize = 12.sp
                        )
                    }else if (loginViewModel.loginError.value != null) {
                        Text(
                            text = "Invalid Login Credentials",
                            color = Color.Red,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF7F8F8)),
                        value = state.email,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xFF92A3FD),
                            focusedLabelColor = Color(0xFF92A3FD),
                            cursorColor = Color(0xFF92A3FD)
                        ),
                        keyboardOptions = KeyboardOptions.Default,
                        onValueChange = {
                            loginViewModel.onEvent(LoginUIevents.EmailChanged(it))
                            emailpassError = false
                            blankError = false
                            errorMessage = ""
                        },
                        leadingIcon = {
                            Icon(painter = painterResource(R.drawable.email), contentDescription = "")
                        },
                        isError = emailpassError,
                        label = { Text("Email") }
                    )


                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF7F8F8)),
                        label = { Text(text = "Password") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xFF92A3FD),
                            focusedLabelColor = Color(0xFF92A3FD),
                            cursorColor = Color(0xFF92A3FD)
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        value = state.password,
                        onValueChange = {
                            loginViewModel.onEvent(LoginUIevents.PasswordChanged(it))
                            emailpassError = false
                            blankError = false
                            errorMessage = ""
                            // Additional validation if needed
                        },
                        leadingIcon = {
                            Icon(painter = painterResource(R.drawable.password), contentDescription = "")
                        },
                        trailingIcon={
                            val iconImage= if(passwordvisibility.value){
                                Icons.Filled.Visibility
                            }else{
                                Icons.Filled.VisibilityOff
                            }

                            val description=if(passwordvisibility.value){
                                "Hide Password"
                            }else{
                                "Show Password"
                            }

                            IconButton(onClick = { passwordvisibility.value=!passwordvisibility.value }) {
                                Icon(imageVector = iconImage, contentDescription = description)
                            }
                        },
                        visualTransformation = if (passwordvisibility.value) VisualTransformation.None
                        else PasswordVisualTransformation()
                    )

                    Spacer(modifier = Modifier.height(30.dp))
                    UnderlinedTextComponent(value = "Forget Password?", onClick = {
                        Quizapprouter.navigateTo(Screen.ForgetPassword)
                    })
                    Spacer(modifier = Modifier.height(40.dp))
                    ButtonComponent(value = "Login", onButtonClicked = {
                        if (state.email.isBlank() || state.password.isBlank()) {
                            blankError = true
                        }else if (!isEmailValid(state.email) || state.password.length < 8) {
                            emailpassError = true
                            errorMessage = "Invalid email or password"
                        }

                        if (!blankError && !emailpassError) {
                            loginViewModel.onEvent(LoginUIevents.loginButtonClicked)
                        }
                    })

                    Spacer(modifier = Modifier.height(30.dp))
                    DividerTextComponent()
                    ClickableTextComponent(value1 = "Don't have an account? ", value2 = "Create an account", onTextSelected = {
                        Quizapprouter.navigateTo(Screen.Signupactivity)
                    })

                    // Google Sign-In Button
                    ButtonComponent(value = "Sign in with Google", onButtonClicked = {
                        // Implement Google Sign-In logic here
                    })
                }
            }
        }

        if (loginViewModel.loginInProgress.value) {
            CircularProgressIndicator()
        }
    }

    SystemBackButtonHandler()
}



private fun isEmailValid(email: String): Boolean {
    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    return email.matches(emailPattern.toRegex())
}

@Preview
@Composable
fun Loginpreview() {
    Loginactivity()
}
