package fr.nextgear.mesentretiensmoto.repository

import fr.nextgear.mesentretiensmoto.model.LoginDomain
import fr.nextgear.mesentretiensmoto.model.Result


interface LoginRepository {

    suspend fun login(mail : String,password:String) : Result<LoginDomain>
}