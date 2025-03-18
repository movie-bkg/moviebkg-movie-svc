package com.sid.moviebkg.movie.command.reset;

import com.sid.moviebkg.common.command.dto.CommandDto;
import com.sid.moviebkg.common.command.utils.CommandContext;
import com.sid.moviebkg.common.logging.MBkgLogger;
import com.sid.moviebkg.common.logging.MBkgLoggerFactory;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResetCommandProcessor implements Processor {
    private MBkgLogger logger = MBkgLoggerFactory.getLogger(ResetCommandProcessor.class);
    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info("Resetting CommandContext");
        List<CommandDto> commands = new ArrayList<>();
        CommandContext.setCommands(commands);
    }
}
