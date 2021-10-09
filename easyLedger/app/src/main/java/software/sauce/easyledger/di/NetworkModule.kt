package software.sauce.easyledger.di

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import software.sauce.easyledger.cache.model.mapper.AAMapper
import software.sauce.easyledger.network.BackendService
import software.sauce.easyledger.presentation.BaseApplication.Companion.prefs
import software.sauce.easyledger.utils.Constants.Companion.BASE_URL
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private fun buildRetrofitClient(): Retrofit {
        val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS)

        val httpClient: OkHttpClient.Builder = OkHttpClient
            .Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header("Accept", "application/json")
                    .header("Content-type", "application/json")
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(logging)

        val authInterceptor: Interceptor = Interceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("Authorization", "Bearer " + prefs?.token)
                .build()
            chain.proceed(request)
        }

        httpClient.addInterceptor(authInterceptor)

        // set retrofit builder
        val builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))

        // build retrofit instance
        return builder
            .client(httpClient.build())
            .build()
    }

    @Singleton
    @Provides
    fun provideBackendService(): BackendService {
        return buildRetrofitClient().create(BackendService::class.java)
    }

    @Singleton
    @Provides
    fun provideAAMapper(): AAMapper {
        return AAMapper()
    }

}