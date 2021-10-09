package software.sauce.easyledger.network

import retrofit2.http.*
import software.sauce.easyledger.network.model.AA.CompanyWithAA
import software.sauce.easyledger.network.model.UserProfileToken

interface BackendService {

    @FormUrlEncoded
    @POST("api/v1/login")
    suspend fun userLogin(@Field("username")phone: String, @Field("password")otp: String): UserProfileToken

    @GET("api/v1/consent/{phone}")
    suspend fun requestConsent(@Path("phone") phone: String): String

    @GET("api/v1/company")
    suspend fun getCompanyAA(@Query("company_id") companyUUID: String): CompanyWithAA
}