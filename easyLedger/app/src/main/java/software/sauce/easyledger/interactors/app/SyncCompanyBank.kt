package software.sauce.easyledger.interactors.app

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import software.sauce.easyledger.domain.data.DataState
import software.sauce.easyledger.network.BackendService
import software.sauce.easyledger.network.model.AA.CompanyBankTransaction

class SyncCompanyBank (
    private val backendService: BackendService,
) {
    fun execute(
        companies: List<String>,
        isNetworkAvailable: Boolean,
    ): Flow<DataState<Map<String, CompanyBankTransaction>>> = flow {
        try {
            emit(DataState.loading())
            if(isNetworkAvailable){
                val companyAAMap = fetchCompanyAAs(companies)
                emit(DataState.success(companyAAMap))
            } else {
                throw Exception("No Internet")
            }
        } catch (e: Exception){
            emit(DataState.error<Map<String, CompanyBankTransaction>>(e.message?: "Unknown Error"))
        }
    }

    private suspend fun fetchCompanyAAs(companies: List<String>): Map<String, CompanyBankTransaction> = coroutineScope {
        companies.map { companyUUID ->
            async {
                companyUUID to backendService.getCompanyBankTransactions(companyUUID)
            }
        }
            .map { it.await() }
            .toMap()
    }
}