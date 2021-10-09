package software.sauce.easyledger.cache.dao

import androidx.room.*
import software.sauce.easyledger.cache.model.entities.CompanyEntity

@Dao
interface CompanyDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCompany(company: CompanyEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCompany(company: List<CompanyEntity>): LongArray

    @Update
    suspend fun updateCompany(company: CompanyEntity): Int

    @Query("SELECT * FROM companies WHERE uuid = :id")
    suspend fun getCompany(id: String): CompanyEntity?

    @Query("SELECT * FROM companies WHERE uuid in (:id)")
    suspend fun getCompanies(id: List<String>): List<CompanyEntity>?

}