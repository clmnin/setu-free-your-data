package software.sauce.easyledger.network.model

import com.google.gson.annotations.SerializedName

data class ConsentUrlRequest (
    @SerializedName("phone")
    var phone: String,
    @SerializedName("fi_types")
    var fiType: List<String>
)