package com.example.NotesApp.Controllers

import com.example.NotesApp.Models.Note
import com.example.NotesApp.Repositories.NoteRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/notes")
class NoteController(
    private val repository: NoteRepository
) {
    @GetMapping("/all")
    fun getAllNotes() = repository.findAll()

    @PostMapping("/add")
    fun addNote(@RequestBody note: Note){
        repository.save(note)
    }
}