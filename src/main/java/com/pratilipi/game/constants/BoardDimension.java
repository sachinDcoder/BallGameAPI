package com.pratilipi.game.constants;

import lombok.Getter;

@Getter
public enum BoardDimension {
    ROW_LENGTH(6),
    MIN_ROW_INDEX(0),
    COLUMN_LENGTH(7),
    MIN_COLUMN_INDEX(0);

    public final int dimension;

    BoardDimension(int dimension){
        this.dimension = dimension;
    }
}
