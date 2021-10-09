package software.sauce.easyledger.presentation.ui.splash

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import software.sauce.easyledger.cache.dao.AADao
import software.sauce.easyledger.cache.dao.CompanyDao
import software.sauce.easyledger.cache.dao.UserDao
import software.sauce.easyledger.cache.dao.UserWithCompanyDao
import software.sauce.easyledger.cache.model.entities.AA.FiDeposit.DepositTransactionCrossRef
import software.sauce.easyledger.cache.model.entities.CompanyEntity
import software.sauce.easyledger.cache.model.entities.UserCompanyCrossRef
import software.sauce.easyledger.cache.model.entities.UserEntity
import software.sauce.easyledger.cache.model.entities.relation.UserWithCompanies
import software.sauce.easyledger.cache.model.mapper.AAMapper
import software.sauce.easyledger.interactors.app.Auth
import software.sauce.easyledger.interactors.app.SyncCompanyAA
import software.sauce.easyledger.interactors.app.SyncCompanyBank
import software.sauce.easyledger.network.model.UserProfile
import software.sauce.easyledger.presentation.BaseApplication.Companion.prefs
import software.sauce.easyledger.presentation.navigation.Screen
import software.sauce.easyledger.presentation.utils.AsyncEvent
import software.sauce.easyledger.presentation.utils.ConnectivityManager
import javax.inject.Inject

@HiltViewModel
class GlobalViewModel
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

    private var _userWithCompany: MutableStateFlow<List<CompanyEntity>> = MutableStateFlow(ArrayList())
    val companies: StateFlow<List<CompanyEntity>> get() = _userWithCompany

    val onLoad: MutableState<Boolean> = mutableStateOf(false)
    val nextScreen: MutableState<Screen?> = mutableStateOf(null)

    init {
        isUserSignUpApprovedByBackend()
    }

    private fun isUserSignUpApprovedByBackend() {
        viewModelScope.launch {
            val userProfile = prefs?.userProfile
            delay(1000)
            if (userProfile.isNullOrBlank().not()) {
                val sessionUser = Gson().fromJson(userProfile, UserProfile::class.java)
                if (sessionUser.companies.isEmpty().not()) {
                    nextScreen.value = Screen.SelectCompany
                } else {
                    nextScreen.value =  Screen.SignIn
                }
            } else {
                nextScreen.value = Screen.SignIn
            }
        }
    }

    fun getUserCompanies() {
        viewModelScope.launch {
            val userProfile = prefs?.userProfile
            if (userProfile.isNullOrBlank().not()) {
                val sessionUser = Gson().fromJson(userProfile, UserProfile::class.java)
                val userCompaniesStream = userWithCompanyDao.getStreamUserWithCompanies(sessionUser.uuid)
                try {
                    userCompaniesStream.collectLatest {
                        _userWithCompany.emit(it.companies)
                    }
                } catch (e: Exception) {
                    FirebaseCrashlytics.getInstance().recordException(e)
                }
            }
        }
    }



    fun authenticateUserAndPopulateDB(phone: String, otp: String, callback: (String?, String?) -> Unit) {
        val authEventChannel = Channel<AsyncEvent<List<String>>>(Channel.BUFFERED)
        val authEventsFlow = authEventChannel.receiveAsFlow()

        val syncAAEventChannel = Channel<AsyncEvent<List<String>>>(Channel.BUFFERED)
        val syncAAEventsFlow = syncAAEventChannel.receiveAsFlow()

        val syncBankEventChannel = Channel<AsyncEvent<Boolean>>(Channel.BUFFERED)
        val syncBankEventsFlow = syncBankEventChannel.receiveAsFlow()
        authEventsFlow.onEach {
            when(it) {
                is AsyncEvent.Success -> {
                    getCompanyLinkedData(it.value, syncAAEventChannel)
                }
                is AsyncEvent.Error -> {
                    callback(null, "Failed to login. Please check your OTP.")
                }
            }
        }.launchIn(viewModelScope)
        syncAAEventsFlow.onEach {
            when(it) {
                is AsyncEvent.Success -> {
                    getCompanyBankTransactions(it.value, syncBankEventChannel)
                }
                is AsyncEvent.Error -> {
                    callback(null, "Failed to sync data.")
                }
            }
        }.launchIn(viewModelScope)
        syncBankEventsFlow.onEach {
            when(it) {
                is AsyncEvent.Success -> {
                    callback(null, null)
                }
                is AsyncEvent.Error -> {
                    callback(null, "Failed to sync data.")
                }
            }
        }.launchIn(viewModelScope)
        authUser(phone, otp, authEventChannel)
    }

    private fun authUser(phone: String, otp: String, channel: Channel<AsyncEvent<List<String>>>) {
        auth.execute(phone, otp, connectivityManager.isNetworkAvailable.value).onEach { dataState ->
            _isLoading.emit(dataState.loading)

            dataState.data?.let { data ->
                prefs?.token = data.token.token
                prefs?.userProfile = Gson().toJson(data.userProfile)
                // insert the user and the companies associated with the user
                val userUUID = data.userProfile.uuid
                userDao.insertUser(UserEntity(uuid=userUUID, phone=data.userProfile.phone))
                if (data.userProfile.companies.isNullOrEmpty().not()) {
                    prefs?.selectedCompanyUUID = data.userProfile.companies[0].uuid
                    val companies = data.userProfile.companies.map {
                        CompanyEntity(it.uuid, it.name, it.displayName, null)
                    }
                    companyDao.insertCompany(companies)
                    val userWithCompaniesCrossRef: List<UserCompanyCrossRef> = companies.map{UserCompanyCrossRef(userUUID, it.uuid)}
                    userWithCompanyDao.insert(userWithCompaniesCrossRef)
                }
                channel.send(AsyncEvent.Success(data.userProfile.companies.map { it.uuid }))
            }

            dataState.error?.let { error ->
                channel.send(AsyncEvent.Error(error))
            }
        }.launchIn(viewModelScope)
    }

    private fun getCompanyLinkedData(companies: List<String>, channel: Channel<AsyncEvent<List<String>>>) {
        syncCompanyAA.execute(companies, connectivityManager.isNetworkAvailable.value).onEach { dataState ->
            _isLoading.emit(dataState.loading)

            dataState.data?.let { data ->
                data.forEach{
                    val companyUUID = it.key
                    val companyAA_DTO = it.value

                    val term = companyAA_DTO.aa.FiTerm
                    if (term != null) {
                        aADao.insertFiTerm(AAMapper.mapTermToEntity(term))
                    }
                    val recurring = companyAA_DTO.aa.FiRecurring
                    if (recurring != null) {
                        aADao.insertFiRecurring(AAMapper.mapRecurringToEntity(recurring))
                    }
                    val deposit = companyAA_DTO.aa.FiDeposit
                    if (deposit != null) {
                        aADao.insertFiDeposit(AAMapper.mapDepositToEntity(deposit))
                    }
                    aADao.insertAA(AAMapper.mapAAToEntity(
                        companyAA_DTO.aa,
                        deposit?.uuid,
                        term?.uuid,
                        recurring?.uuid,
                    ))
                    val company = companyDao.getCompany(companyUUID)
                    if (company != null) {
                        company.aaUUID = companyAA_DTO.aa.uuid
                        companyDao.updateCompany(company)
                    }
                }
                channel.send(AsyncEvent.Success(data.map { it.key }))
            }

            dataState.error?.let { error ->
                channel.send(AsyncEvent.Error(error))
            }
        }.launchIn(viewModelScope)
    }

    private fun getCompanyBankTransactions(companies: List<String>, channel: Channel<AsyncEvent<Boolean>>) {
        syncCompanyBank.execute(companies, connectivityManager.isNetworkAvailable.value).onEach { dataState ->
            _isLoading.emit(dataState.loading)

            dataState.data?.let { data ->
                data.forEach{
                    val companyUUID = it.key
                    val bankTransaction = it.value
                    aADao.insertBankTransactions(AAMapper.mapBankTransactionToEntity(bankTransaction.data))
                    // get company and it's linked AA
                    val company = companyDao.getCompany(companyUUID)
                    if (company != null) {
                        val aa = company.aaUUID
                        if (aa != null) {
                            val aaEntity = aADao.getAA(aa)
                            val depositUUID = aaEntity.FiDepositUUID
                            if (depositUUID != null) {
                                bankTransaction.data.map {
                                    aADao.insertDepositBankCrossRef(
                                        DepositTransactionCrossRef(
                                            depositUUID = depositUUID,
                                            bankTransactionLineUUID = it.uuid
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
                channel.send(AsyncEvent.Success(true))
            }

            dataState.error?.let { error ->
                channel.send(AsyncEvent.Error(error))
            }
        }.launchIn(viewModelScope)
    }
}