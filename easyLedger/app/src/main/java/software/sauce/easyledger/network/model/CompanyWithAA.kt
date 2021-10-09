package software.sauce.easyledger.network.model

import com.google.gson.annotations.SerializedName

data class CompanyWithAA (
    @SerializedName("id")
    var uuid: String,

    @SerializedName("name")
    var name: String,

    @SerializedName("display_name")
    var displayName: String,

    var aa: AAAccount
)