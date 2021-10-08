package software.sauce.easyledger.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import software.sauce.easyledger.presentation.navigation.Screen
import software.sauce.easyledger.presentation.theme.EasyLedgerTheme
import software.sauce.easyledger.presentation.ui.anumati.AnumatiViewModel
import software.sauce.easyledger.presentation.ui.anumati.AnumatiWebView
import software.sauce.easyledger.presentation.ui.home.HomeScreen
import software.sauce.easyledger.presentation.ui.sign_in.MobileNumberScreen
import software.sauce.easyledger.presentation.utils.ConnectivityManager
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    @Inject
    lateinit var connectivityManager: ConnectivityManager

    override fun onStart() {
        super.onStart()
        firebaseAnalytics = Firebase.analytics
        connectivityManager.registerConnectionObserver(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        connectivityManager.unregisterConnectionObserver(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EasyLedgerTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Screen.SignIn.route) {
                    composable(route = Screen.SignIn.route) { navBackStackEntry ->
                        MobileNumberScreen(
                            onNavigateToRecipeDetailScreen = navController::navigate,
                            nextScreen = { path ->
                                if (path != "9999999999") {
                                    null
                                } else {
                                    Screen.ValidateOTP.route + "/$path"
                                }
                            }
                        )
                    }
                    composable(
                        route = Screen.ValidateOTP.route + "/{phone}",
                        arguments = listOf(navArgument("phone") { type = NavType.StringType }),
                    ) { navBackStackEntry ->
                        MobileNumberScreen(
                            onNavigateToRecipeDetailScreen = navController::navigate,
                            textFieldText = "Enter OTP send to ${navBackStackEntry.arguments?.getString("phone") ?: ""}",
                            buttonText = "Submit OTP",
                            textFieldLength = 6,
                            nextScreen = { otp ->
                                if (otp != "123456") {
                                    null
                                } else {
                                    Screen.Home.route
                                }
                            }
                        )
                    }
                    composable(route = Screen.Home.route) { navBackStackEntry ->
                        HomeScreen()
                    }
                    composable(
                        route = Screen.Anumati.route + "/{phone}",
                        arguments = listOf(navArgument("phone") { type = NavType.StringType }),
                    ) { navBackStackEntry ->
                        val viewModel: AnumatiViewModel by viewModels()
                        AnumatiWebView(
                            onNavigateToRecipeDetailScreen = navController::navigate,
                            phone = navBackStackEntry.arguments?.getString("phone") ?: "",
                            webViewModel=viewModel
                        )
                    }
                }
            }
        }
    }
}