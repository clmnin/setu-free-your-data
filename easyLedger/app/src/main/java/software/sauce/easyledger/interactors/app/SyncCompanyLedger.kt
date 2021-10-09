package software.sauce.easyledger.interactors.app

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import software.sauce.easyledger.domain.data.DataState
import software.sauce.easyledger.network.BackendService
import software.sauce.easyledger.network.model.LedgerResponse

class SyncCompanyLedger(
    private val backendService: BackendService,
) {
    fun execute(
        companies: List<String>,
        isNetworkAvailable: Boolean,
    ): Flow<DataState<Map<String, LedgerResponse>>> = flow {
        try {
            emit(DataState.loading())
            if(isNetworkAvailable){
                val companyAAMap = fetchCompanyLedger(companies)
                emit(DataState.success(companyAAMap))
            } else {
                throw Exception("No Internet")
            }
        } catch (e: Exception){
            emit(DataState.error<Map<String, LedgerResponse>>(e.message?: "Unknown Error"))
        }
    }

    private suspend fun fetchCompanyLedger(companies: List<String>): Map<String, LedgerResponse> = coroutineScope {
        companies.map { companyUUID ->
            async {
                companyUUID to backendService.getLedger(companyUUID)
            }
        }
            .map { it.await() }
            .toMap()
    }
}