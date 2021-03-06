package software.sauce.easyledger.cache.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import software.sauce.easyledger.cache.model.entities.UserCompanyCrossRef
import software.sauce.easyledger.cache.model.entities.relation.UserWithCompanies

@Dao
interface UserWithCompanyDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(userCompanies: List<UserCompanyCrossRef>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(userCompanies: UserCompanyCrossRef)

    @Delete
    suspend fun deleteOne(userCompanies: UserCompanyCrossRef)

    @Transaction
    @Query("SELECT * FROM users WHERE uuid = :uuid")
    fun getStreamUserWithCompanies(uuid: String): Flow<UserWithCompanies>
}