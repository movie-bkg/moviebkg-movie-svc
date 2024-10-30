package com.sid.moviebkg.movie.tmdb.repository;

import com.sid.moviebkg.common.model.genre.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Integer> {
}
