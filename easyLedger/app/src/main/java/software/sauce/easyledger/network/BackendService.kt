package software.sauce.easyledger.network

import retrofit2.http.*
import software.sauce.easyledger.network.model.AA.CompanyBankTransaction
import software.sauce.easyledger.network.model.AA.CompanyWithAA
import software.sauce.easyledger.network.model.ConsentUrlRequest
import software.sauce.easyledger.network.model.LedgerResponse
import software.sauce.easyledger.network.model.UserProfileToken

interface BackendService {

    @FormUrlEncoded
    @POST("api/v1/login")
    suspend fun userLogin(@Field("username")phone: String, @Field("password")otp: String): UserProfileToken

    @POST("api/v1/consent")
    suspend fun requestConsent(@Body phone: ConsentUrlRequest): String

    @GET("api/v1/company")
    suspend fun getCompanyAA(@Query("company_id") companyUUID: String): CompanyWithAA

    @GET("api/v1/company/bank-transaction")
    suspend fun getCompanyBankTransactions(@Query("company_id") companyUUID: String): CompanyBankTransaction

    @GET("api/v1/company/ledger")
    suspend fun getLedger(@Query("company_id") companyUUID: String): LedgerResponse
}