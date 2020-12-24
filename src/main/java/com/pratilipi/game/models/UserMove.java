package com.pratilipi.game.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserMove {
    private String user;
    private int selectedColumn;
    private int sequence;
}
