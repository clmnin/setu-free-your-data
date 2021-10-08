package software.sauce.easyledger.presentation.ui.sign_in

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import software.sauce.easyledger.R
import software.sauce.easyledger.presentation.navigation.Screen
import software.sauce.easyledger.presentation.theme.EasyLedgerTheme

@Composable
fun SignInAndOtp(
    onNavigateToRecipeDetailScreen: (String) -> Unit,
    textFieldText: String = "Mobile Number",
    textFieldLength: Int = 10,
    buttonText: String = "Send OTP",
    nextScreen: (String) -> String?,
) {
    var mobileNumber by remember {
        mutableStateOf("")
    }
    val isFormValid by derivedStateOf {
        mobileNumber.isNotBlank() && mobileNumber.length == textFieldLength
    }

    val openDialog = remember { mutableStateOf(false) }

    EasyLedgerTheme() {
        Scaffold(backgroundColor = MaterialTheme.colors.primary) {
            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top) {
                Image(
                    painter = painterResource(id = R.drawable.ic_cashbook),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .weight(1f)
                        .size(200.dp)
                )
                Text(text = stringResource(id = R.string.app_name), fontWeight = FontWeight.Bold, fontSize = 32.sp)
                Card(
                    Modifier
                        .weight(2f)
                        .padding(8.dp),
                    shape = RoundedCornerShape(32.dp)
                ) {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(32.dp)
                    ) {
                        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = mobileNumber,
                                onValueChange = { mobileNumber = it },
                                label = { Text(text = textFieldText) },
                                singleLine = true,
                                trailingIcon = {
                                    if (mobileNumber.isNotBlank())
                                        IconButton(onClick = { mobileNumber = "" }) {
                                            Icon(imageVector = Icons.Filled.Clear, contentDescription = "")
                                        }
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    // TODO: Add dialog asking for confirmation (send OTP, cancel)
                                    val route = nextScreen(mobileNumber)
                                    if (route == null) {
                                        openDialog.value = true
                                    } else {
                                        onNavigateToRecipeDetailScreen(route)
                                    }
                                },
                                enabled = isFormValid,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text(text = buttonText)
                            }
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
                if (openDialog.value) {
                    AlertDialog(
                        onDismissRequest = {
                            // Dismiss the dialog when the user clicks outside the dialog or on the back
                            // button. If you want to disable that functionality, simply use an empty
                            // onCloseRequest.
                            openDialog.value = false
                        },
                        title = {
                            Text(text = "Invalid Input")
                        },
                        text = {
                            Text("Please check the number you've typed and try again")
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    openDialog.value = false
                                }) {
                                Text("Ok")
                            }
                        }
                    )
                }
            }
        }
    }
}