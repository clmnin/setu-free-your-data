package software.sauce.easyledger.network.model.AA

import com.google.gson.annotations.SerializedName

data class AAAccount(
    @SerializedName("id")
    var uuid: String,

    @SerializedName("phone")
    var phone: String,

    @SerializedName("name")
    var name: String?,

    @SerializedName("email")
    var email: String?,

    @SerializedName("dob")
    var dob: String?,

    @SerializedName("pan")
    var pan: String?,

    @SerializedName("fi_deposit")
    var FiDeposit: FIDeposit?,
)
