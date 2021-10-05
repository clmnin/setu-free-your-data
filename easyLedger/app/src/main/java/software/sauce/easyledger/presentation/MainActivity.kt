package software.sauce.easyledger.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import software.sauce.easyledger.presentation.navigation.Screen
import software.sauce.easyledger.presentation.ui.sign_in.SignInScreen

import software.sauce.easyledger.presentation.theme.EasyLedgerTheme
import software.sauce.easyledger.presentation.ui.home.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EasyLedgerTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Screen.SignIn.route) {
                    composable(route = Screen.SignIn.route) { navBackStackEntry ->
                        SignInScreen(
                            onNavigateToRecipeDetailScreen = navController::navigate,
                        )
                    }
                    composable(route = Screen.Home.route) { navBackStackEntry ->
                        HomeScreen()
                    }
                }
            }
        }
    }
}