package bzh.nv.melishop_api.components

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component
import java.util.*
import java.security.SecureRandom
import java.util.Base64

@Component
class JwtUtil {

    private fun generateSecretKey(): String {
        val random = SecureRandom()
        val bytes = ByteArray(32) // 256-bit key
        random.nextBytes(bytes)
        return Base64.getEncoder().encodeToString(bytes)
    }

    fun generateAnonymousToken(): String {
        return Jwts.builder()
            .setSubject("anonymous")
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 /*** 60 * 10**/))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()
    }

    private val secretKey = generateSecretKey()

    fun generateToken(username: String): String {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(Date())
            .setExpiration(
                Date(
                    System.currentTimeMillis() + 1000 * 60
                    /*** 60 * 10**/
                )
            ) // 10 hours
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()
    }

    fun validateToken(token: String, username: String): Boolean {
        val claims = extractAllClaims(token)
        val tokenUsername = claims.subject
        return (username == tokenUsername && !isTokenExpired(claims))
    }

    fun extractAllClaims(token: String): Claims {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body
    }

    private fun isTokenExpired(claims: Claims): Boolean {
        return claims.expiration.before(Date())
    }
}