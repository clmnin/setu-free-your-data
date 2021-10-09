package software.sauce.easyledger.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import software.sauce.easyledger.cache.model.entities.AA.AAAccountEntity
import software.sauce.easyledger.cache.model.entities.AA.FiDeposit.BankTransactionLineEntity
import software.sauce.easyledger.cache.model.entities.AA.FiDeposit.DepositTransactionCrossRef
import software.sauce.easyledger.cache.model.entities.AA.FiDeposit.FiDepositEntity
import software.sauce.easyledger.cache.model.entities.AA.FiRecurringEntity
import software.sauce.easyledger.cache.model.entities.AA.FiTermEntity

@Dao
interface AADao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAA(aa: AAAccountEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFiDeposit(deposit: FiDepositEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFiTerm(term: FiTermEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFiRecurring(aa: FiRecurringEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBankTransactions(bankTransactionLine: List<BankTransactionLineEntity>): LongArray

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDepositBankCrossRef(crossRef: DepositTransactionCrossRef): Long

    @Query("SELECT * FROM AAAccountEntity WHERE uuid = :uuid")
    suspend fun getAA(uuid: String): AAAccountEntity

    @Query("SELECT * FROM FiDepositEntity WHERE uuid = :uuid")
    suspend fun getDeposit(uuid: String): FiDepositEntity
}