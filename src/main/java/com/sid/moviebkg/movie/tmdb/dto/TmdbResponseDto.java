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
public class TmdbResponseDto {
    @JsonProperty(value = "page")
    private Integer page;
    @JsonProperty(value = "results")
    private List<MovieDetailsDto> results;
    @JsonProperty(value = "total_pages")
    private Long totalPages;
    @JsonProperty(value = "total_results")
    private Long totalResults;
}
