package ru.drudenko.tools.tibco.service;

import ru.drudenko.tools.tibco.dto.QueueSettingsDto;

public interface UpdateRoutingConfigurationRoute {
    void delete(QueueSettingsDto dto);

    void add(QueueSettingsDto dto);
}
