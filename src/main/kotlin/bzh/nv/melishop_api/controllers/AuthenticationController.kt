package bzh.nv.melishop_api.controllers

import bzh.nv.melishop_api.components.JwtUtil
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class AuthenticationController(
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JwtUtil,
    private val userDetailsService: UserDetailsService
) {

    @PostMapping("/authenticate")
    fun createAuthenticationToken(@RequestBody authenticationRequest: AuthenticationRequest): String {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(authenticationRequest.username, authenticationRequest.password)
        )
        val userDetails = userDetailsService.loadUserByUsername(authenticationRequest.username)
        return jwtUtil.generateToken(userDetails.username)
    }

    @GetMapping("/anonymous-token")
    fun getAnonymousToken(): String {
        return jwtUtil.generateAnonymousToken()
    }
}

data class AuthenticationRequest(val username: String, val password: String)