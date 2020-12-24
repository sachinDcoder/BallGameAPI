package com.pratilipi.game.models;

import com.pratilipi.game.constants.Ball;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name= "game")
public class UserExecutedMove implements Serializable {
    @Id
    @Column(name = "user_id")
    private String userId;

    @Type(type = "jsonb")
    @Column(name = "moves", columnDefinition = "jsonb")
    private Map<Ball, List<Board> > moves;
}
