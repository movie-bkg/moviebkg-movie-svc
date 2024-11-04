package com.sid.moviebkg.movie.flow.user.service;

import com.sid.moviebkg.common.dto.user.UserDetailsDto;

public interface FetchUserService {
    UserDetailsDto fetchUser(String userId);
}
