package software.sauce.easyledger.presentation.ui.select_company

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import software.sauce.easyledger.R
import software.sauce.easyledger.presentation.components.CompanyBasicCard
import software.sauce.easyledger.presentation.components.NothingHere
import software.sauce.easyledger.presentation.theme.EasyLedgerTheme
import software.sauce.easyledger.presentation.ui.splash.GlobalViewModel

@Composable
fun SelectCompanyScreen(
    onNavigation: (String) -> Unit,
    viewModel: GlobalViewModel
) {
    val companies = viewModel.companies.collectAsState().value
    val onLoad = viewModel.onLoad.value
    if (!onLoad) {
        viewModel.onLoad.value = true
        viewModel.getUserCompanies()
    }
    EasyLedgerTheme() {
        Scaffold(backgroundColor = MaterialTheme.colors.primary) {
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_cashbook),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .weight(1f)
                        .size(200.dp)
                )
                Text(
                    text = stringResource(id = R.string.app_name),
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                )
                Box(modifier = Modifier
                    .background(color = MaterialTheme.colors.surface)
                ) {
                    if (companies.isEmpty()) {
                        NothingHere()
                    } else {
                        LazyColumn {
                            itemsIndexed(
                                items = companies
                            ) { index, company ->
                                CompanyBasicCard(
                                    company = company,
                                    onClick = {}
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}