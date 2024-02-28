package uz.developers.musicplayer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.developers.musicplayer.domain.AppRepository
import uz.developers.musicplayer.domain.impl.AppRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
interface AppRepositoryModule {

    @Binds
    fun getAppRepository(impl: AppRepositoryImpl) : AppRepository
}
