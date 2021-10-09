package software.sauce.easyledger.presentation.ui.home

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import software.sauce.easyledger.presentation.theme.DeepBlue
import software.sauce.easyledger.presentation.theme.EasyLedgerTheme

@Composable
fun HomeScreen() {
    EasyLedgerTheme()
    {
        Scaffold(backgroundColor = DeepBlue) {
           Text("Home Screen")
        }
    }
}