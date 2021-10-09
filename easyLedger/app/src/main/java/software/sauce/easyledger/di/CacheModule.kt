package software.sauce.easyledger.di

import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import software.sauce.easyledger.cache.dao.CompanyDao
import software.sauce.easyledger.cache.dao.UserDao
import software.sauce.easyledger.cache.dao.UserWithCompanyDao
import software.sauce.easyledger.cache.database.AppDatabase
import software.sauce.easyledger.presentation.BaseApplication
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {
    @Singleton
    @Provides
    fun provideDb(app: BaseApplication): AppDatabase {
        return Room
            .databaseBuilder(app, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideUserDao(db: AppDatabase): UserDao {
        return db.userDao()
    }

    @Singleton
    @Provides
    fun provideCompanyDao(db: AppDatabase): CompanyDao {
        return db.companyDao()
    }

    @Singleton
    @Provides
    fun provideUserWithCompanyDao(db: AppDatabase): UserWithCompanyDao {
        return db.userWithCompanyDao()
    }
}