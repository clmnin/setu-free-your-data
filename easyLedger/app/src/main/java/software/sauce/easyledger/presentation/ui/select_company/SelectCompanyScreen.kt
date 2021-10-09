package software.sauce.easyledger.presentation.ui.select_company

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import software.sauce.easyledger.presentation.theme.EasyLedgerTheme

@Composable
fun SelectCompanyScreen() {
    EasyLedgerTheme()
    {
        Scaffold(backgroundColor = MaterialTheme.colors.primary) {
            Text("Select Company")
        }
    }
}