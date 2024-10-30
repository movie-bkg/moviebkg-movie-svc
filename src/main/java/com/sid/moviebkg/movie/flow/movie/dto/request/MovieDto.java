package com.sid.moviebkg.movie.flow.movie.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieDto {
    private String title;
    private String description;
    private String director;
    private String producer;
    private String releaseDate;
    private String language;
    private Integer duration;
    private Double rating;
    private String genre;
}
