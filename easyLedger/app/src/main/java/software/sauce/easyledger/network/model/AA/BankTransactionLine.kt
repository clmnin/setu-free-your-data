package software.sauce.easyledger.network.model.AA

import com.google.gson.annotations.SerializedName

data class BankTransactionLine (
    @SerializedName("id")
    var uuid: String,
    @SerializedName("mode")
    var mode: String,
    @SerializedName("trans_type")
    var transaType: String,
    @SerializedName("amount")
    var amount: Long,
    @SerializedName("text")
    var text: String?,
    @SerializedName("narration")
    var narration: String?,
    @SerializedName("reference")
    var reference: String,
    @SerializedName("valueDate")
    var valueDate: String,
    @SerializedName("currentBalance")
    var currentBalance: Long,
    @SerializedName("transactionTimestamp")
    var transactionTimestamp: String,

)