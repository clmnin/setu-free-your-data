package software.sauce.easyledger.presentation.navigation

sealed class Screen(
    val route: String,
){
    object SignIn: Screen("signIn")
    object Home: Screen("home")
    object Anumati: Screen("anumati")
}