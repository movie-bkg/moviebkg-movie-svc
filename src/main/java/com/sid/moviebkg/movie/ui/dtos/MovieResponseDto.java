package com.sid.moviebkg.movie.ui.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MovieResponseDto {
    private String movieId;
    private Long id;
    private String title;
    private String originalTitle;
    private String description;
    private String posterPath;
    private String backdropPath;
    private String releaseDate;
    private String originalLanguage;
    private Long duration;
    private Double rating;
    private List<GenreResponseDto> genres;
    private boolean isAdult;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class GenreResponseDto {
        private Long genreId;
        private String genreName;
    }
}
