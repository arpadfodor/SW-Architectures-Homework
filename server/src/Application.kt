package hu.gyeben.communityparking.server

import hu.gyeben.communityparking.server.routes.registerReportRoutes
import hu.gyeben.communityparking.server.routes.registerUserRoutes
import hu.gyeben.communityparking.server.services.UserService
import hu.gyeben.communityparking.server.services.bindServices
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.util.*
import org.kodein.di.instance
import org.kodein.di.ktor.di
import org.mindrot.jbcrypt.BCrypt

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalAPI
@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    initDatabase()

    di {
        bindServices()
    }

    install(Authentication) {
        basic("userAuth") {
            realm = "ktor"
            validate { authenticate(it) }
        }
    }

    registerReportRoutes()
    registerUserRoutes()
}

fun Application.authenticate(credential: UserPasswordCredential): Principal? {
    val userService by di().instance<UserService>()
    val passwordHash = userService.getUser(credential.name)?.password

    passwordHash?.let {
        if (BCrypt.checkpw(credential.password, passwordHash))
            return UserIdPrincipal(credential.name)
    }

    return null
}
