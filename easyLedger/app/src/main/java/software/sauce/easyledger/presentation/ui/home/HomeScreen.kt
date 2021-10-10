package software.sauce.easyledger.presentation.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import software.sauce.easyledger.R
import software.sauce.easyledger.cache.model.entities.CompanyEntity
import software.sauce.easyledger.presentation.components.CompanyBasicCard
import software.sauce.easyledger.presentation.components.DateSelector
import software.sauce.easyledger.presentation.components.NothingHere
import software.sauce.easyledger.presentation.navigation.Screen
import software.sauce.easyledger.presentation.theme.*

@Composable
fun HomeScreen(
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
            HomeScreenComponents(viewModel, onNavigation, onLoad)
        }
    }
}

@Composable
fun HomeScreenComponents(
    viewModel: CompanyViewModel,
    onNavigation: (String) -> Unit,
    onLoad: Boolean
) {
    val isConsentRequired = viewModel.isConsentRequired.value
    val currentDate = viewModel.currentDate.collectAsState().value
    val todayCredit = viewModel.companyTodayCredit.collectAsState().value
    val todayDebit = viewModel.companyTodayDebit.collectAsState().value
    val currentBalance = viewModel.companyCurrentBalance.collectAsState().value
    val partners = viewModel.companyLedgerCompanies.collectAsState().value
    if (!onLoad) {
        viewModel.onLoad.value = true
        viewModel.getCompanyDeposit()
    }
    Box(
        modifier = Modifier
            .background(DeepBlue)
            .fillMaxSize()
    ) {
        Column {
            DateSelector(currentDate.text, onClick = {
                viewModel.nextDate()
            })
            if (isConsentRequired) {
                LinkBankAccount(
                    onClick = {
                        onNavigation(Screen.ConsentMobileNumber.route)
                    }
                )
            } else {
                BankBalanceComponent(
                    bankBalance = currentBalance,
                    todayIn = todayCredit.toString(),
                    todayOut = todayDebit.toString(),
                    onClick = {
                        val currentCompany = viewModel.selectedCompanyUUID
                        if (currentCompany != null) {
                            val route = Screen.Bank.route + "/${currentBalance}"
                            onNavigation(route)
                        }
                    }
                )
            }
            PartnerLedger(partners, onNavigation)
        }
    }
}

@Composable
fun PartnerLedger(companies: List<CompanyEntity>, onNavigation: (String) -> Unit,) {
    Column(modifier = Modifier.background(color = GhostWhite)) {
        Text(
            text = "Ledgers",
            style = MaterialTheme.typography.h2,
            modifier = Modifier.padding(15.dp),
            color = Gray
        )
        if (companies.isEmpty()) {
            NothingHere()
        } else {
            LazyColumn {
                itemsIndexed(
                    items = companies
                ) { _, company ->
                    CompanyBasicCard(
                        company = company,
                        onClick = {
                            val route = Screen.Ledger.route + "/${company.uuid}"
                            onNavigation(route)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun LinkBankAccount(
    color: Color = GhostWhite,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(15.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color)
            .padding(horizontal = 15.dp, vertical = 20.dp)
            .fillMaxWidth().clickable(onClick = onClick)
    ) {
        Text(
            text = "Link Bank Account",
            style = MaterialTheme.typography.h2,
            color = Gray
        )
    }
}

@Composable
fun BankBalanceComponent(
    color: Color = GhostWhite,
    bankBalance: Long = 1000,
    todayIn: String = "100",
    todayOut: String = "200",
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(15.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color)
            .padding(horizontal = 15.dp, vertical = 20.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Text(
            text = "In Bank",
            style = MaterialTheme.typography.h2,
            color = Gray
        )
        Text(
            text = bankBalance.toString(),
            style = MaterialTheme.typography.h2,
            color = if (bankBalance > 0) CurrencyGreen else CurrencyRed
        )
        Divider(color = LightGray, thickness = 1.dp)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                Text(
                    text = todayIn,
                    style = MaterialTheme.typography.h2,
                    color = CurrencyGreen
                )
                Text(
                    text = "Today's Out",
                    style = MaterialTheme.typography.h3,
                    color = Gray
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                Text(
                    text = todayOut,
                    style = MaterialTheme.typography.h2,
                    color = CurrencyRed
                )
                Text(
                    text = "Today's In",
                    style = MaterialTheme.typography.h3,
                    color = Gray
                )
            }
        }
    }
}