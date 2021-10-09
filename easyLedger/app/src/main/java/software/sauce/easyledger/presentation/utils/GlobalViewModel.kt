package software.sauce.easyledger.presentation.utils

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import software.sauce.easyledger.interactors.app.Auth
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

    fun authUser(phone: String, otp: String) {
        auth.execute(phone, otp, connectivityManager.isNetworkAvailable.value).onEach { dataState ->
            _isLoading.emit(dataState.loading)

            dataState.data?.let { data ->
                Log.e(Constants.TAG, "global vm auth: $data")
            }

            dataState.error?.let { error ->
                Log.e(Constants.TAG, "global vm auth: $error")
            }
        }.launchIn(viewModelScope)
    }
}