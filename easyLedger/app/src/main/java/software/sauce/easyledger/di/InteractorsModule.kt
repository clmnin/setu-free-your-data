package software.sauce.easyledger.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import software.sauce.easyledger.interactors.anumati.FetchConsentUrl
import software.sauce.easyledger.interactors.app.Auth
import software.sauce.easyledger.interactors.app.SyncCompanyAA
import software.sauce.easyledger.interactors.app.SyncCompanyBank
import software.sauce.easyledger.network.BackendService

@Module
@InstallIn(ViewModelComponent::class)
object InteractorsModule {

  @ViewModelScoped
  @Provides
  fun provideFetchConsentUrl(
    backendService: BackendService,
  ): FetchConsentUrl {
    return FetchConsentUrl(
      backendService=backendService
    )
  }

  @ViewModelScoped
  @Provides
  fun provideAuthChecker(
    backendService: BackendService,
  ): Auth {
    return Auth(
      backendService=backendService
    )
  }

  @ViewModelScoped
  @Provides
  fun provideSyncCompanyAA(
    backendService: BackendService,
  ): SyncCompanyAA {
    return SyncCompanyAA(
      backendService=backendService
    )
  }

  @ViewModelScoped
  @Provides
  fun provideSyncCompanyBank(
    backendService: BackendService,
  ): SyncCompanyBank {
    return SyncCompanyBank(
      backendService=backendService
    )
  }
}