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
public class GenreResponseDto {
    @JsonProperty(value = "genres")
    private List<GenreDto> genres;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class GenreDto {
        @JsonProperty(value = "id")
        private Integer id;
        @JsonProperty(value = "name")
        private String name;
    }
}
