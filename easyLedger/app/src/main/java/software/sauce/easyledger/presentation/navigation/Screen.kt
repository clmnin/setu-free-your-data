package software.sauce.easyledger.presentation.navigation

sealed class Screen(
    val route: String,
){
    object SignIn: Screen("signIn")
    object ValidateOTP: Screen("validateOtp")
    object Home: Screen("home")
    object ConsentMobileNumber: Screen("consentMobileNumber")
    object Anumati: Screen("anumati")

}