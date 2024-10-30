package com.sid.moviebkg.movie.config.activemq;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.component.jms.JmsComponent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;

import java.util.List;

@Data
@RequiredArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "moviebkg.mq")
@ConditionalOnProperty(name = "mbkg.queue-type", havingValue = "activemq")
public class ActiveMQConfiguration {
    private List<MBkgActiveMQQueueManager> queueManagers;
    @Value("${moviebkg.jms.acknowledgement-mode}")
    private Integer acknowledgementMode;
    private final GenericApplicationContext genericApplicationContext;

    @PostConstruct
    public void init() {
        registerJmsComponents();
    }

    public void registerJmsComponents() {
        for (MBkgActiveMQQueueManager queueManager : queueManagers) {
            JmsComponent jmsComponent = getActiveMqConnectionFactory(queueManager);
            genericApplicationContext.registerBean(queueManager.getId(), JmsComponent.class, () -> jmsComponent);
        }
    }

    private JmsComponent getActiveMqConnectionFactory(MBkgActiveMQQueueManager queueManager) {
        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
        cf.setBrokerURL("tcp://" + queueManager.getHostname() + ":" + queueManager.getPort());
        JmsComponent jmsComponent = new JmsComponent();
        jmsComponent.setConnectionFactory(cf);
        jmsComponent.setMaxConcurrentConsumers(queueManager.getConcurrentConsumers());
        jmsComponent.setAcknowledgementMode(acknowledgementMode);
        return jmsComponent;
    }
}
