package software.sauce.easyledger.network.model

import com.google.gson.annotations.SerializedName

data class Ledger(
    @SerializedName("id")
    var uuid: String,
    @SerializedName("owner_")
    var ownerUUID: String,
    @SerializedName("party_")
    var partyUUID: String,
    @SerializedName("type_")
    var type: String,
    @SerializedName("narration")
    var narration: String,
    var amt: Long,
    var bal: Long,
    @SerializedName("write_date")
    var writeDate: String,
)
