package software.sauce.easyledger.cache.model.entities.AA

import androidx.room.*
import software.sauce.easyledger.cache.model.entities.AA.FiDeposit.FiDepositEntity

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = FiDepositEntity::class,
            parentColumns = ["uuid"],
            childColumns = ["fi_deposit"],
        ),
        ForeignKey(
            entity = FiTermEntity::class,
            parentColumns = ["uuid"],
            childColumns = ["fi_term"],
        ),
        ForeignKey(
            entity = FiRecurringEntity::class,
            parentColumns = ["uuid"],
            childColumns = ["fi_recurring"],
        ),
//        ForeignKey(
//            entity = FiCreditCardEntity::class,
//            parentColumns = ["id"],
//            childColumns = ["fi_credit_card"],
//        ),
//        ForeignKey(
//            entity = FiInsuranceEntity::class,
//            parentColumns = ["id"],
//            childColumns = ["fi_insurance"],
//        )
    ],
    indices = [
        Index(value = ["uuid"], unique = true),
        Index(value = ["fi_deposit"]),
    ])
data class AAAccountEntity(
    @PrimaryKey
    @ColumnInfo(name = "uuid")  var uuid: String,
    @ColumnInfo(name = "phone") var phone: String,
    @ColumnInfo(name = "name")  var name: String?,
    @ColumnInfo(name = "email") var email: String?,
    @ColumnInfo(name = "dob")   var dob: String?,
    @ColumnInfo(name = "pan")   var pan: String?,
    @ColumnInfo(name = "fi_deposit")   var FiDepositUUID: String?,
    @ColumnInfo(name = "fi_term")   var FiTermUUID: String?,
    @ColumnInfo(name = "fi_recurring")   var FiRecurringUUID: String?,
//    @ColumnInfo(name = "fi_credit_card")   var FiCreditCardUUID: String?,
//    @ColumnInfo(name = "fi_insurance")   var FiInsuranceUUID: String?,
)