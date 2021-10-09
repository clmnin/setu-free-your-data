package software.sauce.easyledger.cache.model.entities.AA

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FiTermEntity(
    @PrimaryKey
    @ColumnInfo(name = "uuid")
    var uuid: String,
    var maskedAccNumber: String,
    var ifsc: String,
    var branch: String,
    var tenureDays: Long,
    var accountType: String,
    var currentValue: Long
)
