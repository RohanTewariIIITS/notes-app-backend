package com.example.NotesApp.Controllers

import com.example.NotesApp.Models.Note
import com.example.NotesApp.Repositories.NoteRepository
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
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

    @PutMapping("/update/{id}")
    fun updateNote(@PathVariable id: String, @RequestBody updatedNote: Note){
        repository.save(updatedNote)
    }

    @DeleteMapping("/remove/{id}")
    fun removeNote(@PathVariable id: String){
        repository.deleteById(id)
    }
}