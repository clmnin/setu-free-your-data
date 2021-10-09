package software.sauce.easyledger.network.model

import com.google.gson.annotations.SerializedName

data class UserProfile(
    @SerializedName("id")
    var uuid: String,

    @SerializedName("phone")
    var phone: String,

    @SerializedName("company")
    var companies: List<Company>
)
