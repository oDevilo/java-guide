package com.devil.guide.graphql.model;

import lombok.Data;

/**
 * @author Devil
 * @date 2021/7/20 10:33 上午
 */
@Data
public class User {
    private int age;
    private long id;
    private String name;
    private Card card;

    public User(int age, long id, String name, Card card) {
        this.age = age;
        this.id = id;
        this.name = name;
        this.card = card;
    }

    public User(int age, long id, String name) {
        this.age = age;
        this.id = id;
        this.name = name;
    }
}
