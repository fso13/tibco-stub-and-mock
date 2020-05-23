package ru.drudenko.tools.tibco.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.drudenko.tools.tibco.util.JmsService;

@RestController
@RequestMapping(value = "/tibco-mock/jms-push")
@Api(description = "Отправка и чтение jms сообщений")
public class JmsPushController {

    private final JmsService jmsService;

    @Autowired
    public JmsPushController(JmsService jmsService) {
        this.jmsService = jmsService;
    }

    @RequestMapping(method = RequestMethod.POST,
            produces = MediaType.APPLICATION_XML_VALUE, consumes = MediaType.APPLICATION_XML_VALUE)
    @ApiOperation(value = "Отправка jms сообщения")
    public ResponseEntity pushAndReceive(@ApiParam(value = "Название очереди куда отправить", required = true)
                                         @RequestParam("queueTo") String queueTo,
                                         @ApiParam(value = "Название очереди откуда получить ответ", required = true)
                                         @RequestParam("queueFrom") String queueFrom,
                                         @ApiParam(value = "xml сообщение для отправки", required = true)
                                         @RequestBody String file) {

        String s = jmsService.sendAndReceiveMessage(queueTo, queueFrom, file);
        return ResponseEntity.ok(s);
    }
}
