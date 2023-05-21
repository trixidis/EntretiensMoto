package fr.nextgear.mesentretiensmoto.core.di

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.nextgear.mesentretiensmoto.data.repositories.BikeRepositoryImpl
import fr.nextgear.mesentretiensmoto.repository.AuthRepository
import fr.nextgear.mesentretiensmoto.repository.BikeRepository
import fr.nextgear.mesentretiensmoto.use_cases.auth.AuthUseCases
import fr.nextgear.mesentretiensmoto.use_cases.auth.GetAuthStateUseCase
import fr.nextgear.mesentretiensmoto.use_cases.auth.OneTapSignInUseCase
import fr.nextgear.mesentretiensmoto.use_cases.auth.SignInUseCase
import fr.nextgear.mesentretiensmoto.use_cases.auth.SignOutUseCase

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindsBikeRepository(bikeRepositoryImpl: BikeRepositoryImpl): BikeRepository

}

@Module
@InstallIn(SingletonComponent::class)
class DataModuleConcrete {

    @Provides
    fun providesDatabaseReference(): DatabaseReference  {
        return Firebase.database.reference
    }


    @Provides
    fun provideUseCases(
        repo: AuthRepository
    ) = AuthUseCases(
        getAuthState = GetAuthStateUseCase(repo),
        signIn = SignInUseCase(repo),
        signOut = SignOutUseCase(repo),
        oneTapSignInUseCase = OneTapSignInUseCase(repo)
    )
}
