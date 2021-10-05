package software.sauce.easyledger.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import software.sauce.easyledger.interactors.anumati.FetchConsentUrl
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
}