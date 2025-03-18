package com.sid.moviebkg.movie.flow.theater.dto.request;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TheaterRequestDto {
    private String name;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Integer totalScreens;
    private List<ScreenDto> screens;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ScreenDto {
        private Integer screenNumber;
        private Integer totalSeats;
    }
}
