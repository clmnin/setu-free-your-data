package software.sauce.easyledger.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import software.sauce.easyledger.cache.dao.CompanyDao
import software.sauce.easyledger.cache.dao.UserDao
import software.sauce.easyledger.cache.dao.UserWithCompanyDao
import software.sauce.easyledger.cache.model.entities.CompanyEntity
import software.sauce.easyledger.cache.model.entities.UserCompanyCrossRef
import software.sauce.easyledger.cache.model.entities.UserEntity

@Database(
    entities = [
        UserEntity::class,
        CompanyEntity::class,
         UserCompanyCrossRef::class
    ],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun companyDao(): CompanyDao
    abstract fun userWithCompanyDao(): UserWithCompanyDao

    companion object{
        val DATABASE_NAME: String = "ez_db"
    }
}