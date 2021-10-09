package software.sauce.easyledger.network.model

import com.google.gson.annotations.SerializedName

data class LedgerResponse(
    @SerializedName("data")
    var data: List<Ledger>
)
