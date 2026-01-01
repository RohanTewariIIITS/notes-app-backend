package com.example.NotesApp.Models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("NotesCollection")
data class Note(
    @Id var id: String? = null,
    val userId: String,
    var heading:String?= "Heading" ,
    var contents: String,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now()
)
