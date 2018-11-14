package com.example.rabbitmq;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "message")
    private String message;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private Type type;

}
