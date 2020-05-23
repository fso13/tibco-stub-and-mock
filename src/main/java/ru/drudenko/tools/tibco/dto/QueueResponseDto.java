package ru.drudenko.tools.tibco.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueueResponseDto {
    @ApiModelProperty(value = "Идентификатор")
    private Long id;
    @ApiModelProperty(value = "Значение для выбора ответа по xpath")
    private String name;
    @ApiModelProperty(value = "Ответ в формате xml")
    private String xml;
}
