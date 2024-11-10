package bzh.nv.melishop_api.services

import bzh.nv.melishop_api.data.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CustomUserDetailsService : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user =
            if (username == "anonymous")
                User(
                    UUID.randomUUID().toString(),
                    "anonymous",
                    "anonymous",
                    "ROLE_ANONYMOUS"
                )
            else throw UsernameNotFoundException("User not found with username: $username")

        val authorities: List<GrantedAuthority> = user.roles.split(",")
            .map { SimpleGrantedAuthority(it) }

        return org.springframework.security.core.userdetails.User(
            user.username,
            user.password,
            authorities
        )
    }
}