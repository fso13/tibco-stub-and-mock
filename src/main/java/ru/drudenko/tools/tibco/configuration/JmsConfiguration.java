package ru.drudenko.tools.tibco.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.util.ReflectionUtils;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Configuration
public class JmsConfiguration {
    private final ConnectionFactory connectionFactory;

    @Autowired
    public JmsConfiguration(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Bean
    public WebApiJmsTemplate jmsTemplateForWebApi() {
        return new WebApiJmsTemplate(connectionFactory);
    }

    public static class WebApiJmsTemplate extends JmsTemplate {

        WebApiJmsTemplate(ConnectionFactory connectionFactory) {
            super(connectionFactory);
        }

        public Message receiveSelectedForWebApi(final String destinationName, final String messageSelector) throws JmsException {
            return execute(session -> {
                Destination destination = resolveDestinationName(session, destinationName);
                return doReceiveForWebApi(session, destination, messageSelector);
            }, true);
        }

        Message doReceiveForWebApi(Session session, Destination destination, String messageSelector)
                throws JMSException {

            MessageConsumer messageConsumer = createConsumer(session, destination, messageSelector);
            Message msg = doReceive(session, messageConsumer);
            messageConsumer.close();

            Class<?> classMessageConsumer = messageConsumer.getClass();
            Field target = ReflectionUtils.findField(classMessageConsumer, "target");
            if (target != null) {
                ReflectionUtils.makeAccessible(target);
                Object targetMessageConsumer = ReflectionUtils.getField(target, messageConsumer);
                if (targetMessageConsumer != null) {
                    Class<?> classTargetMessageConsumer = targetMessageConsumer.getClass();
                    Method closeMethodForTargetMessageConsumer = ReflectionUtils.findMethod(classTargetMessageConsumer, "close");
                    ReflectionUtils.makeAccessible(closeMethodForTargetMessageConsumer);
                    ReflectionUtils.invokeMethod(closeMethodForTargetMessageConsumer, targetMessageConsumer);
                }
            }
            return msg;
        }
    }
}
