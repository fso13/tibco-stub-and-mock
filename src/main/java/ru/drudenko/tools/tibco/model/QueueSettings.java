package ru.drudenko.tools.tibco.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_queue_settings")
public class QueueSettings {
    @Id
    @SequenceGenerator(name = "tb_queue_settings_seq", sequenceName = "seq_tb_queue_settings_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tb_queue_settings_seq")
    private Long id;
    @Column(name = "queue_from")
    private String from;
    @Column(name = "queue_to")
    private String to;
    private String xpath;
    private boolean enable = true;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "settings", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<QueueResponse> responses = new ArrayList<>();
}
