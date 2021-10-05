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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import software.sauce.easyledger.R
import software.sauce.easyledger.presentation.navigation.Screen
import software.sauce.easyledger.presentation.theme.EasyLedgerTheme

@Composable
fun SignInScreen(
    onNavigateToRecipeDetailScreen: (String) -> Unit,
) {
    var mobileNumber by remember {
        mutableStateOf("")
    }
    val isFormValid by derivedStateOf {
        mobileNumber.isNotBlank() && mobileNumber.length == 10
    }

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
                Text(text = "Easy Ledger", fontWeight = FontWeight.Bold, fontSize = 32.sp)
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
                                label = { Text(text = "Mobile Number") },
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
                                    val route = Screen.Anumati.route + "/${mobileNumber}"
                                    onNavigateToRecipeDetailScreen(route)
                                },
                                enabled = isFormValid,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text(text = "Log In")
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                TextButton(onClick = {}) {
                                    Text(text = "Sign Up")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}