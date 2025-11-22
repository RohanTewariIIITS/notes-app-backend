package com.example.NotesApp.Repositories

import com.example.NotesApp.Models.Note
import org.springframework.data.mongodb.repository.MongoRepository

interface NoteRepository : MongoRepository<Note,String>{

}