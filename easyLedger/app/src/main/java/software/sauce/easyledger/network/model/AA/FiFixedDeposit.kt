package software.sauce.easyledger.network.model.AA

import com.google.gson.annotations.SerializedName

data class FiFixedDeposit(
    @SerializedName("id")
    var uuid: String,

    @SerializedName("maskedAccNumber")
    var maskedAccNumber: String,

    @SerializedName("ifsc")
    var ifscCode: String,

    @SerializedName("branch")
    var branch: String,

    @SerializedName("tenureDays")
    var tenureDays: Long,

    @SerializedName("accountType")
    var accountType: String,

    @SerializedName("currentValue")
    var currentValue: Long,
)
