package software.sauce.easyledger.cache.model.entities.AA.FiDeposit

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index

@Entity(indices = [
    Index(value = ["bank_trans_uuid"]),
    Index(value = ["deposit_uuid"])
],
    primaryKeys = ["bank_trans_uuid", "deposit_uuid"])
data class DepositTransactionCrossRef(

    @ColumnInfo(name = "deposit_uuid")
    val depositUUID: String,

    @ColumnInfo(name = "bank_trans_uuid")
    val bankTransactionLineUUID: String,
)