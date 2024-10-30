package com.sid.moviebkg.movie.config.activemq;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Data
@NoArgsConstructor
@ConditionalOnProperty(name = "mbkg.queue-type", havingValue = "activemq")
public class MBkgActiveMQQueueManager {
    private String id;
    private String channel;
    private String hostname;
    private Integer port;
    private String activemqManager;
    private Integer concurrentConsumers;
    private Boolean cacheProducers;
    private Boolean cacheConsumers;
    private Integer sessionCacheSize;
}
