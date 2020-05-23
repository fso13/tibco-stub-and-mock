package ru.drudenko.tools.tibco.route;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.jms.JmsMessage;
import org.apache.camel.model.FromDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import ru.drudenko.tools.tibco.dto.QueueResponseDto;
import ru.drudenko.tools.tibco.dto.QueueSettingsDto;
import ru.drudenko.tools.tibco.service.QueueSettingsService;
import ru.drudenko.tools.tibco.service.UpdateRoutingConfigurationRoute;

import javax.jms.ConnectionFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class TibcoMockRoute extends RouteBuilder implements UpdateRoutingConfigurationRoute {
    private static final String ENDPOINT_TEMPLATE = "jms:queue:%s";
    private static final XPathFactory X_PATH_FACTORY = XPathFactory.newInstance();
    private final Map<String, QueueSettingsDto> msgs = new HashMap<>();

    private final QueueSettingsService queueSettingsService;
    private final ConnectionFactory connectionFactory;
    private final CamelContext camelContext;

    @Autowired
    public TibcoMockRoute(QueueSettingsService queueSettingsService,
                          ConnectionFactory connectionFactory,
                          CamelContext camelContext) {
        this.queueSettingsService = Objects.requireNonNull(queueSettingsService);
        this.connectionFactory = Objects.requireNonNull(connectionFactory);
        this.camelContext = Objects.requireNonNull(camelContext);
    }

    @Override
    public void configure() {
        log.info("Start configuration routes...");
        camelContext.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
        List<QueueSettingsDto> params = queueSettingsService.getAll();

        String[] endpoints = new String[params.size()];
        log.info("Queue params: " + params.size());

        for (int i = 0; i < params.size(); i++) {
            QueueSettingsDto param = params.get(i);
            endpoints[i] = String.format(ENDPOINT_TEMPLATE, param.getFrom());
            msgs.put(param.getFrom(), param);
            log.info("Queue param: " + param);
        }

        from(endpoints).autoStartup(true)
                .to("log:ru.drudenko.tools.tibco.route.in?level=INFO&showAll=true&multiline=true")
                .process(TibcoMockRoute.this::process)
                .to("log:ru.drudenko.tools.tibco.route.out?level=INFO&showAll=true&multiline=true")
                .to("jms:queue");

    }

    @Override
    public void delete(QueueSettingsDto param) {
        this.getRouteCollection().getRoutes().get(0)
                .getInputs()
                .removeIf(input -> String.format(ENDPOINT_TEMPLATE, param.getFrom()).replace("jms:", "jms://").equals(input.getEndpointUri().replace("jms:", "jms://")));
    }

    @Override
    public void add(QueueSettingsDto param) {
        String endpointUri = String.format(ENDPOINT_TEMPLATE, param.getFrom());
        if (this.getRouteCollection().getRoutes().get(0).getInputs()
                .stream()
                .map(FromDefinition::getUri)
                .noneMatch(uri -> endpointUri.replace("jms:", "jms://").equals(uri.replace("jms:", "jms://")))) {

            this.getRouteCollection().getRoutes().get(0).getInputs().add(new FromDefinition(endpointUri));
            msgs.put(param.getFrom(), param);
            log.info("Queue param: " + param);
        }
    }

    private void process(Exchange exchange) throws Exception {
        String endpointName = exchange.getFromEndpoint().getEndpointUri();
        QueueSettingsDto queue = msgs.get(endpointName.substring("jms//:queue:".length()));

        JmsMessage inMessage = ((JmsMessage) exchange.getIn());
        InputSource xmlInputSource = new InputSource(new StringReader(inMessage.getBody(String.class)));
        DocumentBuilder xmlDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = xmlDocumentBuilder.parse(xmlInputSource);

        String text;
        if (StringUtils.isEmpty(queue.getXpath())) {
            text = queue.getResponses().stream().filter(dto -> dto.getName().equals("default")).findFirst().map(QueueResponseDto::getXml).get();
        } else {
            try {
                XPath xPath = X_PATH_FACTORY.newXPath();
                String node = (String) xPath.compile(queue.getXpath()).evaluate(document, XPathConstants.STRING);
                text = queue.getResponses().stream().filter(dto -> dto.getName().equals(node)).findFirst().map(QueueResponseDto::getXml).get();

            } catch (Exception e) {
                text = queue.getResponses().stream().filter(dto -> dto.getName().equals("default")).findFirst().map(QueueResponseDto::getXml).get();
            }

        }
        exchange.getIn().setHeader("CamelJmsDestinationName", queue.getTo());
        exchange.getIn().setBody(text);
    }
}
