package com.sid.moviebkg.movie.flow.movie.repository;

import com.sid.moviebkg.common.model.movie.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, String> {
}
