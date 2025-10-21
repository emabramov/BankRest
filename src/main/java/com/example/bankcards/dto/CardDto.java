package com.example.bankcards.dto;


import com.example.bankcards.entity.CardStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CardDto {
    private Long id;
    private Long cardNumber;
    private Long userId;
    private CardStatus status;
    private BigDecimal amount;

    @Override
    public String toString() {
        return toStringNumber(cardNumber);
    }

    public static String toStringNumber(Long number){
        return String.format("%s %s %s %s", "****", "****", "****", number.toString().substring(number.toString().length() - 4));
    }
}
