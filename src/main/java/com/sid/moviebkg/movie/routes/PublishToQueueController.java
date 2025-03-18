package com.sid.moviebkg.movie.routes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sid.moviebkg.common.logging.MBkgLogger;
import com.sid.moviebkg.common.logging.MBkgLoggerFactory;
import com.sid.moviebkg.common.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PublishToQueueController {

    private MBkgLogger logger = MBkgLoggerFactory.getLogger(PublishToQueueController.class);

    @Value("${moviebkg.mq.publish.demo.demo.queue-manager-id}")
    private String routeQueueManagerId;
    @Value("${moviebkg.mq.publish.demo.demo.queue}")
    private String routeQueue;
    private final JsonUtils jsonUtils;
    private final ProducerTemplate producerTemplate;

    @PostMapping("/publish")
    public String publish(@RequestBody MessageDto messageDto) {
        try {
            String queue = routeQueueManagerId + ":queue:" + routeQueue;
            String json = jsonUtils.convertObjectAsJson(messageDto);
            producerTemplate.sendBody(queue, json);
            logger.info("Message published to queue:{}", routeQueue);
        } catch (JsonProcessingException e) {
            logger.warn("Unable to parse object to json:{}", e);
        } catch (CamelExecutionException e) {
            logger.warn("Camel exception occurred:{}", e);
        } catch (Exception e) {
            logger.warn("Exception:{}", e);
        }
        return "Message has been published successfully";
    }

}
