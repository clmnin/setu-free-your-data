package software.sauce.easyledger.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import software.sauce.easyledger.cache.dao.AADao
import software.sauce.easyledger.cache.dao.CompanyDao
import software.sauce.easyledger.cache.dao.UserDao
import software.sauce.easyledger.cache.dao.UserWithCompanyDao
import software.sauce.easyledger.cache.model.entities.AA.AAAccountEntity
import software.sauce.easyledger.cache.model.entities.AA.FiDeposit.BankTransactionLineEntity
import software.sauce.easyledger.cache.model.entities.AA.FiDeposit.DepositTransactionCrossRef
import software.sauce.easyledger.cache.model.entities.AA.FiDeposit.FiDepositEntity
import software.sauce.easyledger.cache.model.entities.AA.FiRecurringEntity
import software.sauce.easyledger.cache.model.entities.AA.FiTermEntity
import software.sauce.easyledger.cache.model.entities.CompanyEntity
import software.sauce.easyledger.cache.model.entities.UserCompanyCrossRef
import software.sauce.easyledger.cache.model.entities.UserEntity

@Database(
    entities = [
        UserEntity::class,
        CompanyEntity::class,
        UserCompanyCrossRef::class,
        AAAccountEntity::class,
        FiRecurringEntity::class,
        FiTermEntity::class,
        FiDepositEntity::class,
        BankTransactionLineEntity::class,
        DepositTransactionCrossRef::class,
    ],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun companyDao(): CompanyDao
    abstract fun userWithCompanyDao(): UserWithCompanyDao
    abstract fun aADao(): AADao

    companion object{
        val DATABASE_NAME: String = "ez_db"
    }
}