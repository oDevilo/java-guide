package com.devil.guide.graphql.model;

import lombok.Data;

/**
 * @author Devil
 * @date 2021/7/20 10:33 上午
 */
@Data
public class Card {
    private String cardNumber;
    private Long userId;

    public Card(String cardNumber, Long userId) {
        this.cardNumber = cardNumber;
        this.userId = userId;
    }
}
