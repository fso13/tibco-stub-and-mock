package ru.drudenko.tools.tibco.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.drudenko.tools.tibco.dto.QueueResponseDto;
import ru.drudenko.tools.tibco.dto.QueueSettingsDto;
import ru.drudenko.tools.tibco.service.QueueSettingsService;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(value = "/queue/settings")
@Api(description = "Операции над файлами ответов мокнутых очередей")
public class QueueSettingsController {

    private final QueueSettingsService queueSettingsService;

    @Autowired
    public QueueSettingsController(QueueSettingsService queueSettingsService) {
        this.queueSettingsService = queueSettingsService;
    }

    @RequestMapping(method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Добавление настройки.")
    public QueueSettingsDto addFileConfiguration(QueueSettingsDto dto) {
        return queueSettingsService.add(dto);
    }

    @PostMapping("{queueSettingsId}/responses")
    @ApiOperation(value = "Добавление файла ответа для определенной очереди.")
    public QueueResponseDto addFileConfiguration(@ApiParam(value = "Идентификатор ответа", required = true)
                                                 @PathVariable("queueSettingsId") Long queueSettingsId, @RequestBody QueueResponseDto dto) {
        return queueSettingsService.addResponse(QueueSettingsDto.builder().id(queueSettingsId).responses(Collections.singletonList(dto)).build());
    }

    @DeleteMapping("/responses/{responseId}")
    @ApiOperation(value = "Удаление файла ответа для определенной очереди.")
    public void deleteFileConfiguration(@ApiParam(value = "Идентификатор ответа", required = true)
                                        @PathVariable("responseId") Long responseId) {
        queueSettingsService.deleteResponse(QueueResponseDto.builder().id(responseId).build());
    }

    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Получить список настроек")
    public List<QueueSettingsDto> getQueuesAndFilesNameConfiguration() {
        return queueSettingsService.getAll();
    }

    @PatchMapping("/{queueSettingsId}/{enable}")
    @ApiOperation(value = "Выключить mock для определенной очереди.")
    public QueueSettingsDto setEnableQueueSettings(@ApiParam(value = "Идентификатор настройки", required = true)
                                                   @PathVariable("queueSettingsId") Long queueSettingsId,
                                                   @ApiParam(value = "Включить/выключить", required = true)
                                                   @PathVariable("enable") boolean enable) {
        return queueSettingsService.setEnableQueue(QueueSettingsDto.builder().id(queueSettingsId).enable(enable).build());
    }

    @DeleteMapping("/{queueSettingsId}")
    @ApiOperation(value = "Удалить mock для очереди")
    public void delete(@ApiParam(value = "Название очереди", required = true)
                       @PathVariable("queueSettingsId") Long queueSettingsId) {
        queueSettingsService.delete(QueueSettingsDto.builder().id(queueSettingsId).build());
    }
}
