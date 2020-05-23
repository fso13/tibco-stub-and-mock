package ru.drudenko.tools.tibco.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.drudenko.tools.tibco.dto.QueueResponseDto;
import ru.drudenko.tools.tibco.dto.QueueSettingsDto;
import ru.drudenko.tools.tibco.mapper.QueueResponseMapper;
import ru.drudenko.tools.tibco.mapper.QueueSettingsMapper;
import ru.drudenko.tools.tibco.model.QueueResponse;
import ru.drudenko.tools.tibco.model.QueueResponseRepository;
import ru.drudenko.tools.tibco.model.QueueSettings;
import ru.drudenko.tools.tibco.model.QueueSettingsRepository;

import java.util.List;

@Service
public class QueueSettingsServiceImpl implements QueueSettingsService {

    private final QueueSettingsRepository queueSettingsRepository;
    private final QueueResponseRepository queueResponseRepository;
    private final UpdateRoutingConfigurationRoute updateRoutingConfigurationRoute;

    @Autowired
    public QueueSettingsServiceImpl(QueueSettingsRepository queueSettingsRepository,
                                    QueueResponseRepository queueResponseRepository,
                                    UpdateRoutingConfigurationRoute updateRoutingConfigurationRoute) {
        this.queueSettingsRepository = queueSettingsRepository;
        this.queueResponseRepository = queueResponseRepository;
        this.updateRoutingConfigurationRoute = updateRoutingConfigurationRoute;
    }

    @Override
    public List<QueueSettingsDto> getAll() {
        return QueueSettingsMapper.toListDto(queueSettingsRepository.findAll());
    }

    @Override
    public QueueSettingsDto add(QueueSettingsDto dto) {
        updateRoutingConfigurationRoute.add(dto);
        return QueueSettingsMapper.toDto(queueSettingsRepository.save(QueueSettingsMapper.toEntity(dto)));
    }

    @Override
    public void delete(QueueSettingsDto dto) {
        updateRoutingConfigurationRoute.delete(QueueSettingsMapper.toDto(queueSettingsRepository.getOne(dto.getId())));
        queueSettingsRepository.deleteById(dto.getId());

    }

    @Override
    public void deleteResponse(QueueResponseDto dto) {
        queueResponseRepository.deleteById(dto.getId());
    }

    @Override
    public QueueResponseDto addResponse(QueueSettingsDto queueSettingsDto) {
        QueueSettings queueSettings = queueSettingsRepository.getOne(queueSettingsDto.getId());
        QueueResponse queueResponse = QueueResponseMapper.toEntity(queueSettingsDto.getResponses().get(0));
        queueResponse.setSettings(queueSettings);

        return QueueResponseMapper.toDto(queueResponseRepository.save(queueResponse));
    }

    @Override
    public QueueSettingsDto setEnableQueue(QueueSettingsDto dto) {
        QueueSettings settings = queueSettingsRepository.getOne(dto.getId());
        settings.setEnable(dto.isEnable());

        if (dto.isEnable()) {
            updateRoutingConfigurationRoute.add(dto);
        } else {
            updateRoutingConfigurationRoute.delete(dto);
        }

        return QueueSettingsMapper.toDto(queueSettingsRepository.save(settings));
    }
}
