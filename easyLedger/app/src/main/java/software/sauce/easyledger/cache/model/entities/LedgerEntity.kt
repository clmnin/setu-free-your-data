package software.sauce.easyledger.cache.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import software.sauce.easyledger.cache.converter.DateTimeConverters
import java.util.*

@Entity(tableName = "ledger")
@TypeConverters(DateTimeConverters::class)
data class LedgerEntity(
    @PrimaryKey
    @ColumnInfo(name = "uuid")
    var uuid: String,

    @ColumnInfo(name = "owner")
    var ownerUUID: String,

    @ColumnInfo(name = "party")
    var partyUUID: String,
    @ColumnInfo(name = "type")
    var type: String,

    @ColumnInfo(name = "narration")
    var narration: String,

    var amt: Long,
    var bal: Long,
    var writeDate: Date = Date(),
)
