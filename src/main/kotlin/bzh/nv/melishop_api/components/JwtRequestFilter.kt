package bzh.nv.melishop_api.components

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseToken
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtRequestFilter : OncePerRequestFilter() {

    private val excludedPaths = listOf("/api/authenticate", "/api/anonymous-token")

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        val requestPath = request.requestURI
        if (excludedPaths.contains(requestPath)) {
            chain.doFilter(request, response)
            return
        }

        val token = request.getHeader("Authorization")?.substring(7)
        if (token != null) {
            try {
                val decodedToken: FirebaseToken = FirebaseAuth.getInstance().verifyIdToken(token)
                request.setAttribute("firebaseToken", decodedToken)
            } catch (e: Exception) {
                response.status = HttpServletResponse.SC_UNAUTHORIZED
                return
            }
        } else {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            return
        }
        chain.doFilter(request, response)
    }
}
