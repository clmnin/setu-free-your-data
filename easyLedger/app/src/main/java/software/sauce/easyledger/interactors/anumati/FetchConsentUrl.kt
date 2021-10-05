package software.sauce.easyledger.interactors.anumati

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import software.sauce.easyledger.domain.data.DataState
import software.sauce.easyledger.network.BackendService

class FetchConsentUrl(
    private val backendService: BackendService,
) {
    fun execute(
        phone: String,
        isNetworkAvailable: Boolean,
    ): Flow<DataState<String>> = flow {
        try {
            emit(DataState.loading())
            if(isNetworkAvailable){
                val consentUrl = getConsentUrlFromBackend(phone)
                emit(DataState.success(consentUrl))
            } else {
                throw Exception("No Internet")
            }
        } catch (e: Exception){
            emit(DataState.error<String>(e.message?: "Unknown Error"))
        }
    }

    private suspend fun getConsentUrlFromBackend(phone: String): String {
        return backendService.requestConsent(phone)
    }
}