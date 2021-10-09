package software.sauce.easyledger.presentation.navigation

sealed class Screen(
    val route: String,
){
    object Splash: Screen("splash")
    object SignIn: Screen("signIn")
    object SelectCompany: Screen("selectCompany")
    object Home: Screen("home")
    object ConsentMobileNumber: Screen("consentMobileNumber")
    object Anumati: Screen("anumati")

}