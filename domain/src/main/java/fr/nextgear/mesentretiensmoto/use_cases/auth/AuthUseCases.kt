package fr.nextgear.mesentretiensmoto.use_cases.auth

data class AuthUseCases(
    val signIn: SignInUseCase,
    val signOut: SignOutUseCase,
    val getAuthState: GetAuthStateUseCase
)