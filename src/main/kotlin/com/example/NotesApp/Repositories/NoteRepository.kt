package com.example.NotesApp.Repositories

import com.example.NotesApp.Models.Note
import org.springframework.data.mongodb.repository.MongoRepository

interface NoteRepository : MongoRepository<Note,String>{
    fun findByUserId(userId: String): List<Note>
    fun findByUserIdAndId(userId:String, id:String): Note?
    fun deleteByUserIdAndId(userId: String, id: String)
}