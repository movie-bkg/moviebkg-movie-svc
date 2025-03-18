package com.sid.moviebkg.movie.command.publish;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sid.moviebkg.common.command.dto.CommandDto;
import com.sid.moviebkg.common.logging.MBkgLogger;
import com.sid.moviebkg.common.logging.MBkgLoggerFactory;
import com.sid.moviebkg.common.utils.JsonUtils;
import com.sid.moviebkg.movie.command.exception.WebSocketPublishException;
import com.sid.moviebkg.movie.util.MovieUtils;
import lombok.RequiredArgsConstructor;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.sid.moviebkg.movie.util.MovieCmnConstants.COMMANDS_TO_PUBLISH;

@Service
@RequiredArgsConstructor
public class PublishWebsocketProcessor implements Processor {
    private MBkgLogger logger = MBkgLoggerFactory.getLogger(PublishWebsocketProcessor.class);
    private final ProducerTemplate producerTemplate;
    private final JsonUtils jsonUtils;
    private final MovieUtils movieUtils;

    @Value("${moviebkg.mq.publish.command.mbkg-websocket-pub-svc.queue-manager-id}")
    private String queueManager;
    @Value("${moviebkg.mq.publish.command.mbkg-websocket-pub-svc.queue}")
    private String queueName;

    @Override
    public void process(Exchange exchange) throws Exception {
        Map<String, Object> properties = exchange.getProperties();
        if (properties.containsKey(COMMANDS_TO_PUBLISH)) {
            List<CommandDto> commands = (List<CommandDto>) properties.get(COMMANDS_TO_PUBLISH);
            publishCommands(commands);
            exchange.removeProperty(COMMANDS_TO_PUBLISH);
        }
    }

    private void publishCommands(List<CommandDto> commands) {
        if (!CollectionUtils.isEmpty(commands)) {
            boolean isDtmNotPresent = movieUtils.isDateTimePresent(commands, CommandDto::getDateTime);
            if (!isDtmNotPresent) {
                String queue = new StringBuilder(queueManager).append(":queue:").append(queueName).toString();
                logger.info("Publishing commands:{} to queue:{}", commands, queue);
                List<CommandDto> sortedCommands = commands.stream()
                        .filter(commandDto -> commandDto.getDateTime() != null)
                        .sorted(Comparator.comparing(CommandDto::getDateTime))
                        .toList();
                publishSortedCmnds(sortedCommands, queue);
            }
        }
    }

    private void publishSortedCmnds(List<CommandDto> sortedCommands, String queue) {
        try {
            String json = jsonUtils.convertObjectAsJson(sortedCommands);
            producerTemplate.sendBody(queue, json);
        }  catch (CamelExecutionException | JsonProcessingException e) {
            logger.warn("Publishing commands:{} to queue:{} failed", sortedCommands, queue, e);
            if (e instanceof CamelExecutionException) {
                // call API to publish
            }
        } catch (WebSocketPublishException e) {
            logger.warn("Publishing commands:{} to queue:{} failed", sortedCommands, queue, e);
        }
    }
}
