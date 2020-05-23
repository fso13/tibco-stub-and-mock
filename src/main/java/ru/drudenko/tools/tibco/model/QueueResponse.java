package ru.drudenko.tools.tibco.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_queue_response")
public class QueueResponse {
    @Id
    @SequenceGenerator(name = "tb_queue_response_seq", sequenceName = "seq_tb_queue_response_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tb_queue_response_seq")
    private Long id;
    private String name;
    private String xml;
    @ManyToOne
    @JoinColumn(name = "settings_id")
    private QueueSettings settings;
}
