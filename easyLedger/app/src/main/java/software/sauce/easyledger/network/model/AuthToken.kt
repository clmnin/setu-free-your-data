package software.sauce.easyledger.network.model

import com.google.gson.annotations.SerializedName

data class AuthToken(

    @SerializedName("access_token")
    var token: String,

    @SerializedName("token_type")
    var tokenType: String
)
