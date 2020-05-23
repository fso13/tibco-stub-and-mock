package ru.drudenko.tools.tibco.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueueSettingsDto {
    @ApiModelProperty(value = "Идентификатор")
    private Long id;
    @ApiModelProperty(value = "Очередь для чтения сообщения")
    private String from;
    @ApiModelProperty(value = "Очередь для ответа")
    private String to;
    @ApiModelProperty(value = "xpath для опрределения ответа в зависимости от запроса")
    private String xpath;
    @ApiModelProperty(value = "Включена ли настройка")
    private boolean enable;
    @ApiModelProperty(value = "Список ответов")
    private List<QueueResponseDto> responses;
}
