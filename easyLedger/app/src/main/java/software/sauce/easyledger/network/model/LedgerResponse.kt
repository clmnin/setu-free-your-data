package software.sauce.easyledger.network.model

import com.google.gson.annotations.SerializedName
import software.sauce.easyledger.network.model.AA.BankTransactionLine

data class LedgerResponse(
    @SerializedName("data")
    var data: List<Ledger>
)
