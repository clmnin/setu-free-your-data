package software.sauce.easyledger.presentation.ui.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import software.sauce.easyledger.cache.converter.DateTimeConverters
import software.sauce.easyledger.cache.dao.AADao
import software.sauce.easyledger.cache.dao.CompanyDao
import software.sauce.easyledger.cache.dao.UserDao
import software.sauce.easyledger.cache.dao.UserWithCompanyDao
import software.sauce.easyledger.cache.model.entities.AA.FiDeposit.BankTransactionLineEntity
import software.sauce.easyledger.cache.model.entities.CompanyEntity
import software.sauce.easyledger.cache.model.entities.LedgerEntity
import software.sauce.easyledger.cache.model.mapper.AAMapper
import software.sauce.easyledger.interactors.app.Auth
import software.sauce.easyledger.interactors.app.SyncCompanyAA
import software.sauce.easyledger.interactors.app.SyncCompanyBank
import software.sauce.easyledger.network.model.Ledger
import software.sauce.easyledger.presentation.BaseApplication
import software.sauce.easyledger.presentation.utils.ConnectivityManager
import javax.inject.Inject

@HiltViewModel
class CompanyViewModel
@Inject
constructor(
    private val auth: Auth,
    private val syncCompanyAA: SyncCompanyAA,
    private val syncCompanyBank: SyncCompanyBank,
    private val userDao: UserDao,
    private val companyDao: CompanyDao,
    private val aADao: AADao,
    private val userWithCompanyDao: UserWithCompanyDao,
    private val AAMapper: AAMapper,
    private val connectivityManager: ConnectivityManager,
    private val state: SavedStateHandle,
): ViewModel(){
    private var _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading
    val onLoad: MutableState<Boolean> = mutableStateOf(false)

    private var _companyBankTransactions: MutableStateFlow<List<BankTransactionLineEntity>> = MutableStateFlow(ArrayList())
    val companyBankTransactions: StateFlow<List<BankTransactionLineEntity>> get() = _companyBankTransactions

    private var _companyLedgerEntry: MutableStateFlow<List<LedgerEntity>> = MutableStateFlow(ArrayList())
    val companyLedgerEntry: StateFlow<List<LedgerEntity>> get() = _companyLedgerEntry

    private var _companyLedgerCompanies: MutableStateFlow<List<CompanyEntity>> = MutableStateFlow(
        emptyList())
    val companyLedgerCompanies: StateFlow<List<CompanyEntity>> get() = _companyLedgerCompanies

    private var _companyCurrentBalance: MutableStateFlow<Long> = MutableStateFlow(0)
    val companyCurrentBalance: StateFlow<Long> get() = _companyCurrentBalance
    private var _companyTodayCredit: MutableStateFlow<Long> = MutableStateFlow(0)
    val companyTodayCredit: StateFlow<Long> get() = _companyTodayCredit
    private var _companyTodayDebit: MutableStateFlow<Long> = MutableStateFlow(0)
    val companyTodayDebit: StateFlow<Long> get() = _companyTodayDebit

    private val allowedDates = listOf(
        TodayDate("Sep 13", DateTimeConverters.stringToDate("2021-09-13T01:00:17+00:00")),
        TodayDate("Sep 14", DateTimeConverters.stringToDate("2021-09-14T01:00:17+00:00")),
        TodayDate("Sep 15", DateTimeConverters.stringToDate("2021-09-15T01:00:17+00:00")),
        TodayDate("Sep 16", DateTimeConverters.stringToDate("2021-09-16T01:00:17+00:00")),
        TodayDate("Sep 17", DateTimeConverters.stringToDate("2021-09-17T01:00:17+00:00")),
        TodayDate("Sep 18", DateTimeConverters.stringToDate("2021-09-18T01:00:17+00:00")),
        TodayDate("Sep 19", DateTimeConverters.stringToDate("2021-09-19T01:00:17+00:00")),
        TodayDate("Sep 20", DateTimeConverters.stringToDate("2021-09-20T01:00:17+00:00")),
        TodayDate("Sep 21", DateTimeConverters.stringToDate("2021-09-21T01:00:17+00:00")),
        TodayDate("Sep 22", DateTimeConverters.stringToDate("2021-09-22T01:00:17+00:00")),
        TodayDate("Sep 23", DateTimeConverters.stringToDate("2021-09-23T01:00:17+00:00")),
        TodayDate("Sep 24", DateTimeConverters.stringToDate("2021-09-24T01:00:17+00:00")),
        TodayDate("Sep 25", DateTimeConverters.stringToDate("2021-09-25T01:00:17+00:00")),
        TodayDate("Sep 26", DateTimeConverters.stringToDate("2021-09-26T01:00:17+00:00")),
        TodayDate("Sep 27", DateTimeConverters.stringToDate("2021-09-27T01:00:17+00:00")),
        TodayDate("Sep 28", DateTimeConverters.stringToDate("2021-09-28T01:00:17+00:00")),
        TodayDate("Sep 29", DateTimeConverters.stringToDate("2021-09-29T01:00:17+00:00")),
        TodayDate("Sep 30", DateTimeConverters.stringToDate("2021-09-30T01:00:17+00:00")),
        TodayDate("Oct 01", DateTimeConverters.stringToDate("2021-10-01T01:00:17+00:00")),
        TodayDate("Oct 02", DateTimeConverters.stringToDate("2021-10-02T01:00:17+00:00")),
        TodayDate("Oct 03", DateTimeConverters.stringToDate("2021-10-03T01:00:17+00:00")),
        TodayDate("Oct 04", DateTimeConverters.stringToDate("2021-10-04T01:00:17+00:00")),
        TodayDate("Oct 05", DateTimeConverters.stringToDate("2021-10-05T01:00:17+00:00")),
        TodayDate("Oct 06", DateTimeConverters.stringToDate("2021-10-06T01:00:17+00:00")),
        TodayDate("Oct 07", DateTimeConverters.stringToDate("2021-10-07T01:00:17+00:00")),
    )

    private var dateIterator = allowedDates.listIterator()

    private var _currentDate: MutableStateFlow<TodayDate> = MutableStateFlow(allowedDates.first())
    val currentDate: StateFlow<TodayDate> get() = _currentDate

    var selectedCompanyUUID: String? = null

    fun setCompanyUUID(uuid: String) {
        selectedCompanyUUID = uuid
    }

    fun getCompanyDeposit() {
        viewModelScope.launch {
            val x = selectedCompanyUUID
            if (x != null) {
                val companyAAStream = companyDao.getStreamAA(x)
                try {
                    companyAAStream.collectLatest { companyWithAAandLedger ->
                        // sort bank transaction
                        val bankTransactions = companyWithAAandLedger.aa?.deposit?.transactions?.sortedByDescending { it.transactionTimestamp }
                        bankTransactions?.let {
                            _companyBankTransactions.value = it
                            updateBankSummary(it)
                        }

                        val ledgerTransactions = companyWithAAandLedger.ledger?.sortedByDescending { it.writeDate }
                        ledgerTransactions?.let {
                            _companyLedgerEntry.value = it
                            updateLedgerEntries(it)
                        }
                    }
                } catch (e: Exception) {
                    FirebaseCrashlytics.getInstance().recordException(e)
                }
            }
        }
    }

    fun nextDate() {
        if (dateIterator.hasNext()) {
            _currentDate.value = dateIterator.next()
            updateBankSummary(_companyBankTransactions.value)
        } else {
            dateIterator = allowedDates.listIterator()
        }
    }

    private fun updateLedgerEntries(ledgerEntries: List<LedgerEntity>) {
        val partyUUIDs = ledgerEntries.map { it.partyUUID }.distinct()
        viewModelScope.launch {
            val parties = companyDao.getCompanies(partyUUIDs)
            parties?.let { _companyLedgerCompanies.value = it }
        }
    }

    private fun updateBankSummary(bankTransaction: List<BankTransactionLineEntity>) {
        val visibleBankTransactions = bankTransaction.filter {
            it.transactionTimestamp <  _currentDate.value.date
        }
        val todayCredit = visibleBankTransactions.filter {
            it.transactionTimestamp == _currentDate.value.date
        }.map { if (it.transaType == "CREDIT") it.amount else 0 }
        val todayDebit = visibleBankTransactions.filter {
            it.transactionTimestamp == _currentDate.value.date
        }.map { if (it.transaType == "DEBIT") it.amount else 0 }
        _companyCurrentBalance.value = if (visibleBankTransactions.isNullOrEmpty()) {
            0
        } else {
            visibleBankTransactions.first().currentBalance
        }
        _companyTodayCredit.value = if (todayCredit.isNullOrEmpty()) {
            0
        } else {
            todayCredit.first()
        }
        _companyTodayDebit.value = if (todayDebit.isNullOrEmpty()) {
            0
        } else {
            todayDebit.first()
        }
    }


}