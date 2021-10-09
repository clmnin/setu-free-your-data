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
import software.sauce.easyledger.presentation.components.MobileNumberInputCard
import software.sauce.easyledger.presentation.navigation.Screen
import software.sauce.easyledger.presentation.theme.EasyLedgerTheme
import software.sauce.easyledger.utils.Constants.Companion.acceptedOTP
import software.sauce.easyledger.utils.Constants.Companion.acceptedPhones

@Composable
fun SignInAndOtp(
    onNavigateToRecipeDetailScreen: (String) -> Unit,
) {
    val openDialog = remember { mutableStateOf(false) }

    EasyLedgerTheme {
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
                MobileNumberInputCard(
                    modifier = Modifier.weight(2f),
                    also_otp = true,
                    handleOnClick = { phone, otp ->
                        if (acceptedPhones.contains(phone) && otp == acceptedOTP) {
                            onNavigateToRecipeDetailScreen(Screen.Home.route)
                        } else {
                            openDialog.value = true
                        }
                    }
                )
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