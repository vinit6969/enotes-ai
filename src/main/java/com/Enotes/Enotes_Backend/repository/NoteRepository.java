package com.Enotes.Enotes_Backend.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.Enotes.Enotes_Backend.model.Note;


public interface NoteRepository extends JpaRepository<Note, Long> {
}