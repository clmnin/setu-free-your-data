package software.sauce.easyledger.cache.model.entities.AA.FiDeposit

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BankTransactionLineEntity(
    @PrimaryKey
    @ColumnInfo(name = "uuid")
    var uuid: String,
    var mode: String,
    var transaType: String,
    var amount: Long,
    var text: String?,
    var narration: String?,
    var reference: String,
    var valueDate: String,
    var currentBalance: Long,
    var transactionTimestamp: String,
)
