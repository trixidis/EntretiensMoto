package fr.nextgear.mesentretiensmoto.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.nextgear.mesentretiensmoto.data.repositories.FakeBikeRepositoryImpl
import fr.nextgear.mesentretiensmoto.data.repositories.FakeLoginRepositoryImpl
import fr.nextgear.mesentretiensmoto.repository.BikeRepository
import fr.nextgear.mesentretiensmoto.repository.LoginRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindsBikeRepository(bikeRepositoryImpl : FakeBikeRepositoryImpl):BikeRepository

    @Binds
    abstract fun bindsLoginRepository(loginRepositoryImpl : FakeLoginRepositoryImpl):LoginRepository
}