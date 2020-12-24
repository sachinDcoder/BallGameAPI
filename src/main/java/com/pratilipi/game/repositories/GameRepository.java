package com.pratilipi.game.repositories;

import com.pratilipi.game.models.UserExecutedMove;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<UserExecutedMove, String> {
    UserExecutedMove findByUserId(String userId);
}
