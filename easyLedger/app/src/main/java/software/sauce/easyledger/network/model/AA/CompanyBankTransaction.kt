package software.sauce.easyledger.network.model.AA

import com.google.gson.annotations.SerializedName

data class CompanyBankTransaction (
    @SerializedName("data")
    var data: List<BankTransactionLine>
)