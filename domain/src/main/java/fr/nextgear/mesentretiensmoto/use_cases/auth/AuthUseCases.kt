package fr.nextgear.mesentretiensmoto.use_cases.auth

data class AuthUseCases(
    val signIn: SignInUseCase,
    val oneTapSignInUseCase: OneTapSignInUseCase,
    val signOut: SignOutUseCase,
    val getAuthState: GetAuthStateUseCase
)