package software.sauce.easyledger.presentation.ui.splash

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import software.sauce.easyledger.cache.dao.CompanyDao
import software.sauce.easyledger.cache.dao.UserDao
import software.sauce.easyledger.cache.dao.UserWithCompanyDao
import software.sauce.easyledger.cache.model.entities.CompanyEntity
import software.sauce.easyledger.cache.model.entities.UserCompanyCrossRef
import software.sauce.easyledger.cache.model.entities.UserEntity
import software.sauce.easyledger.interactors.app.Auth
import software.sauce.easyledger.interactors.app.SyncCompany
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
    private val syncCompany: SyncCompany,
    private val userDao: UserDao,
    private val companyDao: CompanyDao,
    private val userWithCompanyDao: UserWithCompanyDao,
    private val connectivityManager: ConnectivityManager,
    private val state: SavedStateHandle,
): ViewModel(){

    private var _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

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
                    nextScreen.value = Screen.Home
                } else {
                    nextScreen.value =  Screen.SignIn
                }
            } else {
                nextScreen.value = Screen.SignIn
            }
        }
    }

    fun authenticateUserAndPopulateDB(phone: String, otp: String, callback: (String?, String?) -> Unit) {
        val authEventChannel = Channel<AsyncEvent<List<String>>>(Channel.BUFFERED)
        val authEventsFlow = authEventChannel.receiveAsFlow()

        val syncEventChannel = Channel<AsyncEvent<Boolean>>(Channel.BUFFERED)
        val syncEventsFlow = syncEventChannel.receiveAsFlow()
        authEventsFlow.onEach {
            when(it) {
                is AsyncEvent.Success -> {
                    getCompanyLinkedData(it.value, syncEventChannel)
                }
                is AsyncEvent.Error -> {
                    callback(null, "Failed to login. Please check your OTP.")
                }
            }
        }.launchIn(viewModelScope)
        syncEventsFlow.onEach {
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

    private fun getCompanyLinkedData(companies: List<String>, channel: Channel<AsyncEvent<Boolean>>) {
        syncCompany.execute(companies, connectivityManager.isNetworkAvailable.value).onEach { dataState ->
            _isLoading.emit(dataState.loading)

            dataState.data?.let { data ->
                Log.e("Company AA", "$data")
                channel.send(AsyncEvent.Success(true))
            }

            dataState.error?.let { error ->
                channel.send(AsyncEvent.Error(error))
            }
        }.launchIn(viewModelScope)
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
                        CompanyEntity(it.uuid, it.name, it.displayName)
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
}