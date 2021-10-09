package software.sauce.easyledger.interactors.app

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import software.sauce.easyledger.domain.data.DataState
import software.sauce.easyledger.network.BackendService
import software.sauce.easyledger.network.model.UserProfileToken

class Auth(
    private val backendService: BackendService,
) {
    fun execute(
        phone: String,
        otp: String,
        isNetworkAvailable: Boolean,
    ): Flow<DataState<UserProfileToken>> = flow {
        try {
            emit(DataState.loading())
            if(isNetworkAvailable){
                val authToken = authUserWithBackend(phone, otp)
                // set loading to true as we have to get company data after auth.
                // so it should stay in the loading state
                emit(DataState.success(authToken, true))
            } else {
                throw Exception("No Internet")
            }
        } catch (e: Exception){
            emit(DataState.error<UserProfileToken>(e.message?: "Unknown Error"))
        }
    }

    private suspend fun authUserWithBackend(phone: String, otp: String): UserProfileToken {
        return backendService.userLogin(phone, otp)
    }
}