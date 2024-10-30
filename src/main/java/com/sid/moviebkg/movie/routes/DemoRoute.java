package com.sid.moviebkg.movie.routes;

import com.sid.moviebkg.common.logging.MBkgLogger;
import com.sid.moviebkg.common.logging.MBkgLoggerFactory;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class DemoRoute extends RouteBuilder {

    private MBkgLogger logger = MBkgLoggerFactory.getLogger(DemoRoute.class);

    @Override
    public void configure() throws Exception {
        sampleRouteForQueue();
    }

    public void sampleRouteForQueue() {
        from("{{moviebkg.mq.consume.demo.queue-manager-id}}:queue:{{moviebkg.mq.consume.demo.queue}}?disableReplyTo=true")
                .routeId("demo")
                .log("${bodyOneLine}");
    }

}
