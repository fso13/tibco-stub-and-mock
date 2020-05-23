package ru.drudenko.tools.tibco.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QueueSettingsRepository extends JpaRepository<QueueSettings, Long> {
}
