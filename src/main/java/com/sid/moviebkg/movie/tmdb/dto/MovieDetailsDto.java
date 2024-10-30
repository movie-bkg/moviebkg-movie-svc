package com.sid.moviebkg.movie.tmdb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieDetailsDto {
    @JsonProperty(value = "adult")
    private Boolean adult;
    @JsonProperty(value = "backdrop_path")
    private String backdropPath;
    @JsonProperty(value = "genre_ids")
    private List<Integer> genreIds;
    @JsonProperty(value = "id")
    private Long id;
    @JsonProperty(value = "original_language")
    private String originalLanguage;
    @JsonProperty(value = "original_title")
    private String originalTitle;
    @JsonProperty(value = "overview")
    private String overview;
    @JsonProperty(value = "popularity")
    private Double popularity;
    @JsonProperty(value = "poster_path")
    private String posterPath;
    @JsonProperty(value = "release_date")
    private String releaseDate;
    @JsonProperty(value = "title")
    private String title;
    @JsonProperty(value = "video")
    private Boolean video;
    @JsonProperty(value = "vote_average")
    private Double voteAverage;
    @JsonProperty(value = "vote_count")
    private Long voteCount;
}
