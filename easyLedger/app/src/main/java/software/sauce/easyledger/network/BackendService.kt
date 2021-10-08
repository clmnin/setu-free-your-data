package software.sauce.easyledger.network

import retrofit2.http.GET
import retrofit2.http.Path

interface BackendService {

    @GET("api/v1/consent/{phone}")
    suspend fun requestConsent(@Path("phone") phone: String): String
}