package com.Enotes.Enotes_Backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Enotes.Enotes_Backend.model.Note;
import com.Enotes.Enotes_Backend.repository.NoteRepository;
import com.Enotes.Enotes_Backend.service.GroqService;
import com.Enotes.Enotes_Backend.util.PdfUtil;

@RestController
@RequestMapping("/api/notes")
@CrossOrigin
public class NoteController {

    @Autowired
    private NoteRepository repo;

    @Autowired
    private GroqService groqService;

@PostMapping("/upload")
public ResponseEntity<?> uploadNote(
        @RequestParam(required = false) MultipartFile file,
        @RequestParam(required = false) String text,
        @RequestParam String title) {

    try {

        String content = "";

        if (file != null && !file.isEmpty()) {
            content = PdfUtil.extractText(file);
        } else if (text != null && !text.isEmpty()) {
            content = text;
        } else {
            return ResponseEntity.badRequest().body("No valid input provided");
        }

        String summary = groqService.summarize(content);

        Note note = new Note();
        note.setTitle(title);
        note.setOriginalText(content);
        note.setSummaryText(summary);

        return ResponseEntity.ok(repo.save(note));

    } catch (Exception e) {
        return ResponseEntity.status(500).body("Error processing note");
    }
}
    @GetMapping
    public List<Note> getAllNotes() {
        return repo.findAll();
    }
}