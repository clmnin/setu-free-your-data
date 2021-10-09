package software.sauce.easyledger.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun MobileNumberInputCard(
    modifier: Modifier,
    also_otp: Boolean = false,
    button_text: String = "Send OTP",
    is_loading: Boolean = false,
    handleOnClick: (String, String) -> Unit,
) {
    var mobileNumber by remember {
        mutableStateOf("")
    }
    var otpNumber by remember {
        mutableStateOf("")
    }
    var buttonText by remember {
        mutableStateOf(button_text)
    }
    var otpTextFieldEnabled by remember {
        mutableStateOf(false)
    }
    val isFormValid by derivedStateOf {
        mobileNumber.isNotBlank() && mobileNumber.length == 10 &&
            if (otpTextFieldEnabled)
                (otpNumber.isNotBlank() && otpNumber.length == 6)
            else true
    }
    Card(
        modifier.padding(8.dp),
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
                    enabled = otpTextFieldEnabled.not() && is_loading.not(),
                    onValueChange = { mobileNumber = it },
                    label = { Text(text = "Mobile Number") },
                    singleLine = true,
                    trailingIcon = {
                        if (mobileNumber.isNotBlank() && otpTextFieldEnabled.not())
                            IconButton(onClick = { mobileNumber = "" }) {
                                Icon(imageVector = Icons.Filled.Clear, contentDescription = "")
                            }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
                if (otpTextFieldEnabled) {
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = otpNumber,
                        enabled = is_loading.not(),
                        onValueChange = { otpNumber = it },
                        label = { Text(text = "OTP") },
                        singleLine = true,
                        trailingIcon = {
                            if (otpNumber.isNotBlank())
                                IconButton(onClick = { otpNumber = "" }) {
                                    Icon(imageVector = Icons.Filled.Clear, contentDescription = "")
                                }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (also_otp) {
                            if (otpTextFieldEnabled.not()) {
                                otpTextFieldEnabled = true
                                buttonText = "Submit OTP"
                            } else {
                                handleOnClick(mobileNumber, otpNumber)
                            }
                        } else {
                            handleOnClick(mobileNumber, otpNumber)
                        }
                    },
                    enabled = isFormValid && is_loading.not(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(text = if (is_loading) "Loading..." else buttonText)
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}
