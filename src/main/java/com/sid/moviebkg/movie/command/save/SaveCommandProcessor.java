package com.sid.moviebkg.movie.command.save;

import com.sid.moviebkg.common.command.dto.CommandDto;
import com.sid.moviebkg.common.command.utils.CommandContext;
import com.sid.moviebkg.movie.util.MovieCmnConstants;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.sid.moviebkg.movie.util.MovieCmnConstants.COMMANDS_TO_PUBLISH;

@Service
public class SaveCommandProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        List<CommandDto> commands = CommandContext.getCommands();
        if (!CollectionUtils.isEmpty(commands)) {
            exchange.setProperty(COMMANDS_TO_PUBLISH, commands);
            CommandContext.unset();
        }
    }
}
