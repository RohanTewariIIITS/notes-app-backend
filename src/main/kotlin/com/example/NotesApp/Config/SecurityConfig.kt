package com.example.NotesApp.Config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(private val firebaseAuthFilter: FirebaseAuthFilter) {

    @Bean
    fun securityFilterChain(http:HttpSecurity):SecurityFilterChain{
        http.csrf{ it.disable() }
            .cors {  }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests{ auth->
                auth.requestMatchers("/api/public/**","/api/health").permitAll()
                    .anyRequest().authenticated()

            }.addFilterBefore(firebaseAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }
}