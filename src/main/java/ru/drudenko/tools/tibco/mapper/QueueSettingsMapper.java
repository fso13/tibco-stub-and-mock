package ru.drudenko.tools.tibco.mapper;

import ru.drudenko.tools.tibco.dto.QueueSettingsDto;
import ru.drudenko.tools.tibco.model.QueueSettings;

import java.util.List;
import java.util.stream.Collectors;

public final class QueueSettingsMapper {
    QueueSettingsMapper() {
    }

    public static QueueSettings toEntity(QueueSettingsDto dto) {
        return new QueueSettings(dto.getId(), dto.getFrom(), dto.getTo(), dto.getXpath(), dto.isEnable(),
                dto.getResponses().stream().map(QueueResponseMapper::toEntity).collect(Collectors.toList()));
    }

    public static QueueSettingsDto toDto(QueueSettings entity) {
        return new QueueSettingsDto(entity.getId(), entity.getFrom(), entity.getTo(), entity.getXpath(), entity.isEnable(),
                entity.getResponses().stream().map(QueueResponseMapper::toDto).collect(Collectors.toList()));
    }

    public static List<QueueSettingsDto> toListDto(List<QueueSettings> entities) {
        return entities.stream().map(QueueSettingsMapper::toDto).collect(Collectors.toList());
    }
}
