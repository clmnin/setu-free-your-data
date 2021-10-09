package software.sauce.easyledger.network.model.AA

import com.google.gson.annotations.SerializedName

data class FIDeposit(
    @SerializedName("id")
    var uuid: String,

    @SerializedName("maskedAccNumber")
    var maskedAccNumber: String,

    @SerializedName("type_")
    var typeSavingOrCurrent: String,

    @SerializedName("branch")
    var branch: String,

    @SerializedName("status")
    var status: Boolean,

    @SerializedName("pending_amt")
    var pendingAmount: Long,

    @SerializedName("ifscCode")
    var ifscCode: String,

    @SerializedName("micrCode")
    var micrCode: String,
)
