package software.sauce.easyledger.cache.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import software.sauce.easyledger.cache.model.entities.AA.FiDeposit.BankTransactionLineEntity
import software.sauce.easyledger.cache.model.entities.CompanyEntity
import software.sauce.easyledger.cache.model.entities.LedgerEntity
import software.sauce.easyledger.cache.model.entities.relation.CompanyWithAA

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

    @Transaction
    @Query("SELECT * FROM companies WHERE uuid = :uuid")
    fun getStreamAA(uuid: String): Flow<CompanyWithAA>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLedger(ledgers: List<LedgerEntity>): LongArray

}