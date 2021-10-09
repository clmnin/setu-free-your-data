package software.sauce.easyledger.cache.model.entities.AA.FiDeposit

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FiDepositEntity(
    @PrimaryKey
    @ColumnInfo(name = "uuid")
    var uuid: String,
    var maskedAccNumber: String,
    var typeSavingOrCurrent: String,
    var branch: String,
    var status: Boolean,
    var pendingAmount: Long,
    var ifscCode: String,
    var micrCode: String,
)
