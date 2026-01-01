package com.example.NotesApp.Controllers

import com.example.NotesApp.Models.Note
import com.example.NotesApp.Repositories.NoteRepository
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/api/notes")
class NoteController(
    private val repository: NoteRepository
) {

    private fun getUid(request: HttpServletRequest): String{
        return request.getAttribute("uid") as String
    }

    @GetMapping("/all")
    fun getAllNotes(request: HttpServletRequest): ResponseEntity<List<Note>>{
        val uid = getUid(request)
        val notes = repository.findByUserId(uid)
        return ResponseEntity.ok(notes)
    }

    @GetMapping("/{id}")
    fun getNoteById(
        @PathVariable id: String,
        request: HttpServletRequest
    ): ResponseEntity<Note> {
        val uid = getUid(request)
        val note = repository.findByUserIdAndId(uid, id)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(note)
    }

    @PostMapping("/add")
    fun addNote(
        @RequestBody note: NoteRequest,
        request: HttpServletRequest
    ): ResponseEntity<Note> {
        val uid = getUid(request)
        val note = Note(
            userId = uid,
            heading = note.title,
            contents = note.content
        )
        val savedNote = repository.save(note)
        return ResponseEntity.ok(savedNote)
    }

    @PutMapping("/update/{id}")
    fun updateNote(@PathVariable id: String, @RequestBody note: NoteRequest,request: HttpServletRequest): ResponseEntity<Note>{
        val uid = getUid(request)
        val existingNote = repository.findByUserIdAndId(uid, id)
            ?: return ResponseEntity.notFound().build()

        val updatedNote = existingNote.copy(
            heading = note.title,
            contents = note.content,
            updatedAt = Instant.now()
        )
        repository.save(updatedNote)
        return ResponseEntity.ok(updatedNote)
    }

    @DeleteMapping("/remove/{id}")
    fun removeNote(@PathVariable id: String,
                   request: HttpServletRequest): ResponseEntity<Void>{
        val uid = getUid(request)
        repository.deleteByUserIdAndId(uid,id)
        return ResponseEntity.noContent().build()
    }
}

data class NoteRequest(
    val title: String,
    val content: String
)