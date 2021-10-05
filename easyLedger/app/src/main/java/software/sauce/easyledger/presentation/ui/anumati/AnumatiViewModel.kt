package software.sauce.easyledger.presentation.ui.anumati

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import software.sauce.easyledger.interactors.anumati.FetchConsentUrl
import software.sauce.easyledger.presentation.utils.ConnectivityManager
import software.sauce.easyledger.presentation.utils.DialogQueue
import software.sauce.easyledger.utils.ConstantUrls.Companion.TAG
import javax.inject.Inject

const val STATE_KEY_CONSENT = "consent.state.url.value"

@HiltViewModel
class AnumatiViewModel
@Inject
constructor(
    private val getConsentUrl: FetchConsentUrl,
    private val connectivityManager: ConnectivityManager,
    private val state: SavedStateHandle,
) : ViewModel() {

    private var _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    val onLoad: MutableState<Boolean> = mutableStateOf(false)
    val consentUrl: MutableState<String?> = mutableStateOf(null)

    val dialogQueue = DialogQueue()

    fun onTriggerEvent(event: AnumatiStateEvent){
        viewModelScope.launch {
            try {
                when(event){
                    is AnumatiStateEvent.GetConsentUrl -> {
                        if(consentUrl.value == null){
                            getConsentUrl(event.phone)
                        }
                    }
                }
            } catch (e: Exception){
                Log.e(TAG, "launchJob: Exception: ${e}, ${e.cause}")
                e.printStackTrace()
            }
        }
    }

    fun getConsentUrl(phone: String) {
        getConsentUrl.execute(phone, connectivityManager.isNetworkAvailable.value).onEach { dataState ->
            _isLoading.emit(dataState.loading)

            dataState.data?.let { data ->
                consentUrl.value = data
                state.set(STATE_KEY_CONSENT, data)
            }

            dataState.error?.let { error ->
                Log.e(TAG, "getRecipe: ${error}")
                dialogQueue.appendErrorMessage("An Error Occurred", error)
            }
        }.launchIn(viewModelScope)
    }

    fun onPageFinished() = viewModelScope.launch {
        _isLoading.emit(false)
    }
}