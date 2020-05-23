package ru.drudenko.tools.tibco.service;

import ru.drudenko.tools.tibco.dto.QueueResponseDto;
import ru.drudenko.tools.tibco.dto.QueueSettingsDto;

import java.util.List;

public interface QueueSettingsService {

    List<QueueSettingsDto> getAll();

    QueueSettingsDto add(QueueSettingsDto dto);

    void delete(QueueSettingsDto dto);

    QueueSettingsDto setEnableQueue(QueueSettingsDto dto);

    QueueResponseDto addResponse(QueueSettingsDto queueSettingsDto);

    void deleteResponse(QueueResponseDto dto);
}
