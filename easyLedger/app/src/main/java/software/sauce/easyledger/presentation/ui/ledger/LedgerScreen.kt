package software.sauce.easyledger.presentation.ui.ledger

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import software.sauce.easyledger.R
import software.sauce.easyledger.cache.model.entities.LedgerEntity
import software.sauce.easyledger.presentation.components.DateSelector
import software.sauce.easyledger.presentation.components.NothingHere
import software.sauce.easyledger.presentation.theme.*
import software.sauce.easyledger.presentation.ui.home.CompanyViewModel

@Composable
fun LedgerScreen(
    onNavigation: (String) -> Unit,
    viewModel: CompanyViewModel,
    onLoad: Boolean,
    partnerUUID: String
) {
    val getTermDeposit = viewModel.getTermDeposit.value
    var hasTermDeposit by remember {
        mutableStateOf(false)
    }
    if (!getTermDeposit) {
        viewModel.getTermDeposit.value = true
        viewModel.getTermDeposit(partnerUUID) { _, error ->
            hasTermDeposit = error == null
        }
    }
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
            LedgerScreenComponent(viewModel, onNavigation, hasTermDeposit)
        }
    }
}

@Composable
fun LedgerScreenComponent(
    viewModel: CompanyViewModel,
    onNavigation: (String) -> Unit,
    hasTermDeposit: Boolean
) {
    val currentDate = viewModel.currentDate.collectAsState().value
    val currentBalance = viewModel.companyCurrentBalance.collectAsState().value
    val ledgerTrans = viewModel.companyLedgerEntry.collectAsState().value
    Box(
        modifier = Modifier
            .background(DeepBlue)
            .fillMaxSize()
    ) {
        Column {
            DateSelector(currentDate.text, onClick = {
                viewModel.nextDate()
            })
            LedgerSummaryComponent(
                bankBalance=currentBalance,
                hasTermDeposit=hasTermDeposit
            )
            LedgerEntries(ledgerTrans)
        }
    }
}


@Composable
fun LedgerEntries(entries: List<LedgerEntity>) {
    Column(modifier = Modifier.background(color = GhostWhite)) {
        Text(
            text = "Ledgers",
            style = MaterialTheme.typography.h2,
            modifier = Modifier.padding(15.dp),
            color = Gray
        )
        if (entries.isEmpty()) {
            NothingHere()
        } else {
            LazyColumn {
                itemsIndexed(
                    items = entries
                ) { _, entry ->
                    Box {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .padding(15.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(GhostWhite)
                                .padding(horizontal = 15.dp, vertical = 20.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = entry.writeDate.toString().substring(4..10),
                                style = MaterialTheme.typography.body1,
                                color = Gray
                            )
                            Divider(
                                color = LightGray,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(1.dp)
                            )
                            Text(
                                text = entry.narration,
                                style = MaterialTheme.typography.body1,
                                color = Gray
                            )
                            Divider(
                                color = LightGray,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(1.dp)
                            )
                            Text(
                                text = entry.type,
                                style = MaterialTheme.typography.body1,
                                color = Gray
                            )
                            Divider(
                                color = LightGray,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(1.dp)
                            )
                            Text(
                                text = entry.amt.toString(),
                                style = MaterialTheme.typography.body1,
                                color = if (entry.type == "CREDIT") CurrencyGreen else CurrencyRed
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LedgerSummaryComponent(
    color: Color = GhostWhite,
    bankBalance: Long = 1000,
    hasTermDeposit: Boolean
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
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Net Balance",
                style = MaterialTheme.typography.h1,
                color = Gray
            )
            Text(
                text = bankBalance.toString(),
                style = MaterialTheme.typography.h1,
                color = if (bankBalance > 0) CurrencyGreen else CurrencyRed
            )
        }
        Divider(color = LightGray, thickness = 1.dp)
        Spacer(modifier = Modifier.height(16.dp))
        if (hasTermDeposit) {
            Text(
                text = "No Credit Risk",
                style = MaterialTheme.typography.h3,
                color = Gray
            )
        } else {
            Text(
                text = "Credit Risk",
                style = MaterialTheme.typography.h3,
                color = CurrencyRed
            )
        }
    }
}