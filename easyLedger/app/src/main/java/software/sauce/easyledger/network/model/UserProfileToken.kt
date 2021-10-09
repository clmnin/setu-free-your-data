package software.sauce.easyledger.network.model

import com.google.gson.annotations.SerializedName

data class UserProfileToken (
    @SerializedName("token")
    var token: AuthToken,

    @SerializedName("profile")
    var userProfile: UserProfile
)