package com.example.NotesApp.Config

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class FirebaseAuthFilter : OncePerRequestFilter() {

    private val publicPaths = listOf("/api/health", "/api/public")

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val path = request.requestURI
        logger.info("🔍 Processing request: $path")

        // Skip auth for public endpoints
        if (publicPaths.any { path.startsWith(it) }) {
            logger.info("🔍 Processing request: $path")
            filterChain.doFilter(request, response)
            return
        }

        try {
            // Extract token from Authorization header
            val authHeader = request.getHeader("Authorization")
            logger.info("📋 Authorization header: ${if (authHeader != null) "Present (${authHeader.length} chars)" else "Missing"}")

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                logger.warn("❌ Missing or invalid Authorization header")
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid token")
                return
            }

            val token = authHeader.substring(7)
            logger.info("🔑 Token extracted (length: ${token.length})")


            logger.info("🔐 Verifying token with Firebase...")
            // Verify token with Firebase Admin SDK
            val decodedToken = FirebaseAuth.getInstance().verifyIdToken(token)
            val uid = decodedToken.uid
            val email = decodedToken.email

            logger.info("✅ Token verified successfully - UID: $uid, Email: $email")

            // Attach UID to request for use in controllers
            request.setAttribute("uid", uid)
            request.setAttribute("email", email)

            logger.info("✅ Authentication set in SecurityContext")

            val authentication = UsernamePasswordAuthenticationToken(
                uid,
                null,
                listOf(SimpleGrantedAuthority("ROLE_USER"))
            )

            SecurityContextHolder.getContext().authentication = authentication

            // Continue the request
            filterChain.doFilter(request, response)

        } catch (e: FirebaseAuthException) {

            logger.error("❌ Firebase token verification failed: ${e.message}")
            logger.error("❌ Error code: ${e.authErrorCode}")
            logger.error("❌ Stack trace:", e)

            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Firebase token: ${e.message}")
        } catch (e: Exception) {
            logger.error("❌ Unexpected authentication error: ${e.message}", e)
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Authentication error: ${e.message}")
        }
    }
}