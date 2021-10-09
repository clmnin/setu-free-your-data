package software.sauce.easyledger.presentation.ui.home

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import software.sauce.easyledger.presentation.theme.DeepBlue
import software.sauce.easyledger.presentation.theme.EasyLedgerTheme

@Composable
fun HomeScreen(
    onNavigation: (String) -> Unit,
    company_uuid: String
) {
    EasyLedgerTheme()
    {
        Scaffold(backgroundColor = DeepBlue) {
           Text("Home Screen")
        }
    }
}