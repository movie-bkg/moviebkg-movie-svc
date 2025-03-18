package com.sid.moviebkg.movie.flow.theater.repository;

import com.sid.moviebkg.common.model.theater.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TheaterRepository extends JpaRepository<Theater, String> {
}
