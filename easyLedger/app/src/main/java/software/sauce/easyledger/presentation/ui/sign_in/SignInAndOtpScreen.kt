package software.sauce.easyledger.presentation.ui.sign_in

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import software.sauce.easyledger.R
import software.sauce.easyledger.presentation.components.MobileNumberInputCard
import software.sauce.easyledger.presentation.theme.EasyLedgerTheme
import software.sauce.easyledger.presentation.utils.GlobalViewModel
import software.sauce.easyledger.utils.Constants.Companion.acceptedOTP
import software.sauce.easyledger.utils.Constants.Companion.acceptedPhones

@Composable
fun SignInAndOtp(
    onNavigation: (String) -> Unit,
    vieModel: GlobalViewModel
) {
    val openDialog = remember { mutableStateOf(false) }

    val isLoading = vieModel.isLoading.collectAsState().value

    EasyLedgerTheme {
        Scaffold(backgroundColor = MaterialTheme.colors.primary) {
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_cashbook),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .weight(1f)
                        .size(200.dp)
                )
                Text(
                    text = stringResource(id = R.string.app_name),
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                )
                MobileNumberInputCard(
                    modifier = Modifier.weight(2f),
                    also_otp = true,
                    is_loading = isLoading,
                    handleOnClick = { phone, otp ->
                        if (acceptedPhones.contains(phone) && otp == acceptedOTP) {
                            vieModel.authUser(phone, otp)
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