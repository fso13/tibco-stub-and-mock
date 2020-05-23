package ru.drudenko.tools.tibco.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
import ru.drudenko.tools.tibco.configuration.JmsConfiguration;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.UUID;

@Slf4j
@Component
public class JmsService {
    private static final String MDC_CONTEXT_ID_KEY = "context-id";
    private final JmsConfiguration.WebApiJmsTemplate webApiTibcoMock;

    @Autowired
    public JmsService(JmsConfiguration.WebApiJmsTemplate webApiTibcoMock) {
        this.webApiTibcoMock = webApiTibcoMock;
    }

    public String sendAndReceiveMessage(String destinationQueueName, String replyQueueName, String xmlString) {
        try {
            final String jmsCorrelationId = getContextId();
            this.sendMessageMock(destinationQueueName, xmlString, jmsCorrelationId);
            ActiveMQTextMessage mqTextMessage = (ActiveMQTextMessage) this.receiveMessageMock(replyQueueName, "JMSCorrelationID=\'" + jmsCorrelationId + "\'");
            return mqTextMessage.getText();
        } catch (JMSException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    private void sendMessageMock(String destinationQueueName, String message, String messageId) {
        final String msg = message;
        final String id = messageId;

        MessageCreator messageCreator = (Session session) -> {
            final TextMessage textMessage = session.createTextMessage(msg);
            session.setMessageListener(null);
            textMessage.setJMSCorrelationID(id);
            return textMessage;
        };

        webApiTibcoMock.send(destinationQueueName, messageCreator);
    }

    private Object receiveMessageMock(String replyQueueName, String messageSelector) {
        return this.webApiTibcoMock.receiveSelectedForWebApi(replyQueueName, messageSelector);
    }

    private static String getContextId() {
        return MDC.get(MDC_CONTEXT_ID_KEY) != null ? MDC.get(MDC_CONTEXT_ID_KEY) : UUID.randomUUID().toString();
    }
}
