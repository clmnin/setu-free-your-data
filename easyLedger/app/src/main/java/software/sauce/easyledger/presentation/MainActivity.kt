package software.sauce.easyledger.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
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
import software.sauce.easyledger.presentation.ui.select_company.SelectCompanyScreen
import software.sauce.easyledger.presentation.ui.sign_in.SignInAndOtp
import software.sauce.easyledger.presentation.ui.splash.GlobalViewModel
import software.sauce.easyledger.presentation.ui.splash.SplashScreen
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
                val viewModel: GlobalViewModel = viewModel()

                NavHost(navController = navController, startDestination = Screen.Splash.route) {
                    composable(route = Screen.Splash.route) { navBackStackEntry ->
                        SplashScreen(
                            onNavigation = navController::navigate,
                            viewModel
                        )
                    }
                    composable(route = Screen.SignIn.route) { navBackStackEntry ->
                        SignInAndOtp(
                            onNavigation = navController::navigate,
                            viewModel
                        )
                    }
                    composable(route = Screen.SelectCompany.route) { navBackStackEntry ->
                        viewModel.onLoad.value = false
                        SelectCompanyScreen(
                            onNavigation = navController::navigate,
                            viewModel
                        )
                    }
                    composable(route = Screen.Home.route) { navBackStackEntry ->
                        HomeScreen()
                    }
                    composable(
                        route = Screen.Anumati.route + "/{phone}",
                        arguments = listOf(navArgument("phone") { type = NavType.StringType }),
                    ) { navBackStackEntry ->
                        val anumatiViewModel: AnumatiViewModel by viewModels()
                        AnumatiWebView(
                            onNavigation = navController::navigate,
                            phone = navBackStackEntry.arguments?.getString("phone") ?: "",
                            webViewModel=anumatiViewModel
                        )
                    }
                }
            }
        }
    }
}