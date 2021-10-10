package software.sauce.easyledger.presentation.ui.ledger

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import software.sauce.easyledger.R
import software.sauce.easyledger.presentation.components.DateSelector
import software.sauce.easyledger.presentation.theme.DeepBlue
import software.sauce.easyledger.presentation.theme.EasyLedgerTheme
import software.sauce.easyledger.presentation.theme.TextWhite
import software.sauce.easyledger.presentation.ui.home.CompanyViewModel

@Composable
fun LedgerScreen(
    onNavigation: (String) -> Unit,
    viewModel: CompanyViewModel,
    onLoad: Boolean
) {
    EasyLedgerTheme()
    {
        Scaffold(backgroundColor = DeepBlue,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = R.string.app_name),
                            style = MaterialTheme.typography.h2
                        )
                    },
                    backgroundColor = DeepBlue,
                    contentColor = TextWhite,
                    elevation = 12.dp
                )
            }
        ) {
            LedgerScreenComponent(viewModel, onNavigation, onLoad)
        }
    }
}

@Composable
fun LedgerScreenComponent(
    viewModel: CompanyViewModel,
    onNavigation: (String) -> Unit,
    onLoad: Boolean
) {
    val currentDate = viewModel.currentDate.collectAsState().value

    Box(
        modifier = Modifier
            .background(DeepBlue)
            .fillMaxSize()
    ) {
        Column {
            DateSelector(currentDate.text, onClick = {
                viewModel.nextDate()
            })
        }
    }
}