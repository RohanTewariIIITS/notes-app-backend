package com.example.NotesApp.Config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import javax.annotation.PostConstruct

@Configuration
class FirebaseConfig {

    @PostConstruct
    fun initFirebase() {
        try {
            val serviceAccount = ClassPathResource("firebase-credentials.json").inputStream

            val options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build()

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options)
                println("✅ Firebase Admin SDK initialized successfully")
            }
        } catch (e: Exception) {
            println("❌ Firebase initialization failed: ${e.message}")
            throw RuntimeException("Failed to initialize Firebase", e)
        }
    }
}