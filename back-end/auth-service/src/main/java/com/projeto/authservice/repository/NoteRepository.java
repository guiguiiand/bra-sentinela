package com.projeto.authservice.repository;

import com.projeto.authservice.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note,Long> {
}
