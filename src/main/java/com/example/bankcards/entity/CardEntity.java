package com.example.bankcards.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.NonFinal;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.math.BigDecimal;

@Entity
@Table(name = "cards")
@Data
public class CardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_number")
    @NotNull
    private Long cardNumber;

    @Column(name = "card_status", columnDefinition = "ENUM('ACTIVE', 'BLOCKED', 'EXPIRED')")
    @NotNull
    @Enumerated(EnumType.STRING)
    @JdbcType(value = PostgreSQLEnumJdbcType.class)
    private CardStatus status;

    @Column(name = "amount", columnDefinition = "NUMERIC(19,2)")
    @NotNull
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
