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
import software.sauce.easyledger.cache.model.mapper.AAMapper
import software.sauce.easyledger.interactors.app.Auth
import software.sauce.easyledger.interactors.app.SyncCompanyAA
import software.sauce.easyledger.interactors.app.SyncCompanyBank
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

    private var _companyCurrentBalance: MutableStateFlow<Long> = MutableStateFlow(0)
    val companyCurrentBalance: StateFlow<Long> get() = _companyCurrentBalance
    private var _companyTodayCredit: MutableStateFlow<Long> = MutableStateFlow(0)
    val companyTodayCredit: StateFlow<Long> get() = _companyTodayCredit
    private var _companyTodayDebit: MutableStateFlow<Long> = MutableStateFlow(0)
    val companyTodayDebit: StateFlow<Long> get() = _companyTodayDebit

    private val allowedDates = listOf(
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

    fun getCompanyDeposit() {
        viewModelScope.launch {
            val companyUUID = BaseApplication.prefs?.selectedCompanyUUID
            if (companyUUID != null) {
                val companyAAStream = companyDao.getStreamAA(companyUUID)
                try {
                    companyAAStream.collectLatest { companyWithAAandLedger ->
                        // sort bank transaction
                        val bankTransactions = companyWithAAandLedger.aa?.deposit?.transactions?.sortedByDescending { it.transactionTimestamp }
                        bankTransactions?.let {
                            _companyBankTransactions.value = it
                            updateBankSummary(it)
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