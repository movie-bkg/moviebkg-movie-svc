package com.sid.moviebkg.movie.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sid.moviebkg.common.logging.MBkgLogger;
import com.sid.moviebkg.common.logging.MBkgLoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class MovieUtils {
    private MBkgLogger logger = MBkgLoggerFactory.getLogger(MovieUtils.class);
    private final ObjectMapper objectMapper;

    public <T> Map<String, Object> objectToMap(T obj) {
        return objectMapper.convertValue(obj, new TypeReference<>() {});
    }

    public <T> boolean isDateTimePresent(List<T> itemsToPublish, Function<T, LocalDateTime> getDateTimeFn) {
        boolean isDtmNotPresent = itemsToPublish.stream().anyMatch(item -> getDateTimeFn.apply(item) == null);
        if (isDtmNotPresent) {
            logger.info("Date Time is not populated:{}", itemsToPublish);
        }
        return isDtmNotPresent;
    }
}
