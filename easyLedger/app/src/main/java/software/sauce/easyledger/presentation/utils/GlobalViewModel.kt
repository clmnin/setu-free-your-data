package software.sauce.easyledger.presentation.utils

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import software.sauce.easyledger.interactors.app.Auth
import software.sauce.easyledger.network.model.UserProfile
import software.sauce.easyledger.presentation.BaseApplication.Companion.prefs
import software.sauce.easyledger.presentation.navigation.Screen
import software.sauce.easyledger.utils.Constants
import javax.inject.Inject

@HiltViewModel
class GlobalViewModel
@Inject
constructor(
    private val auth: Auth,
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

    fun authUser(phone: String, otp: String) {
        auth.execute(phone, otp, connectivityManager.isNetworkAvailable.value).onEach { dataState ->
            _isLoading.emit(dataState.loading)

            dataState.data?.let { data ->
                Log.e(Constants.TAG, "global vm auth: $data")
                prefs?.token = data.token.token
                prefs?.userProfile = Gson().toJson(data.userProfile)
                if (data.userProfile.companies.isNullOrEmpty().not()) {
                    prefs?.selectedCompanyUUID = data.userProfile.companies[0].uuid
                }
            }

            dataState.error?.let { error ->
                Log.e(Constants.TAG, "global vm auth: $error")
            }
        }.launchIn(viewModelScope)
    }
}