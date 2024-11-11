package bzh.nv.melishop_api.components

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class ApiKeyFilter : OncePerRequestFilter() {

    private val validApiKey = "MeliShop_apiKey_validation" //TODO Move key in a file in resources ?

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        request.headerNames.toList().forEach { println(it) }
        val apiKey = request.getHeader("X-API-KEY")
        if (apiKey == null || apiKey != validApiKey) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            return
        }

        filterChain.doFilter(request, response)
    }
}