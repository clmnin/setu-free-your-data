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

    fun getCompanyDeposit() {
        viewModelScope.launch {
            val companyUUID = BaseApplication.prefs?.selectedCompanyUUID
            if (companyUUID != null) {
                val companyAAStream = companyDao.getStreamAA(companyUUID)
                try {
                    companyAAStream.collectLatest {
                        it.aa?.deposit?.transactions?.let { t -> _companyBankTransactions.emit(t) }
                        val totalCredit = it.aa?.deposit?.transactions?.map { if (it.transaType == "CREDIT") it.amount else 0 }?.sum()
                        val totalDebit = it.aa?.deposit?.transactions?.map { if (it.transaType == "DEBIT") it.amount else 0 }?.sum()
                        val totalFT = it.aa?.deposit?.transactions?.map { if (it.mode == "FT") it.amount else 0 }?.sum()
                        val totalUPI = it.aa?.deposit?.transactions?.map { if (it.mode == "UPI") it.amount else 0 }?.sum()
                        val totalOTHER = it.aa?.deposit?.transactions?.map { if (it.mode == "OTHER") it.amount else 0 }?.sum()
                    }
                } catch (e: Exception) {
                    FirebaseCrashlytics.getInstance().recordException(e)
                }
            }
        }
    }
}