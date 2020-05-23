package ru.drudenko.tools.tibco.mapper;

import ru.drudenko.tools.tibco.dto.QueueResponseDto;
import ru.drudenko.tools.tibco.model.QueueResponse;

import java.util.List;
import java.util.stream.Collectors;

public final class QueueResponseMapper {
    QueueResponseMapper() {
    }

    public static QueueResponse toEntity(QueueResponseDto dto) {
        return new QueueResponse(dto.getId(), dto.getName(), dto.getXml(), null);
    }

    public static List<QueueResponse> toListEntity(List<QueueResponseDto> listDto) {
        return listDto.stream().map(QueueResponseMapper::toEntity).collect(Collectors.toList());
    }

    public static QueueResponseDto toDto(QueueResponse entity) {
        return new QueueResponseDto(entity.getId(), entity.getName(), entity.getXml());
    }
}
