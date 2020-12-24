package com.pratilipi.game.constants;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum Ball {
    NONE("None"),
    YELLOW("Yellow"),
    RED ("Red");

    public final String color;

    Ball(String color) {
        this.color = color;
    }

    public static Ball of(String ballType) {
        return Stream.of(Ball.values())
                .filter(type -> type.name().equals(ballType))
                .findFirst()
                .orElse(null);
    }

}
