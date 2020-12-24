package com.pratilipi.game.controllers;

import com.pratilipi.game.constants.ApplicationConstants;
import com.pratilipi.game.exceptions.InvalidMoveException;
import com.pratilipi.game.models.UserMove;
import com.pratilipi.game.models.UserStatus;
import com.pratilipi.game.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class GameController  {

    @Autowired
    private GameService gameService;

    @PostMapping(path = "start")
    public ResponseEntity<?> startGame(){
        try {
            UserStatus userStatus = gameService.start();
            return new ResponseEntity<>(userStatus, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "play")
    public ResponseEntity<String> playGame(@RequestBody UserMove userMove){
        try {
            return new ResponseEntity<>(gameService.play(userMove), HttpStatus.OK);
        } catch (InvalidMoveException e) {
            return new ResponseEntity<>(ApplicationConstants.INVALID, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "moves")
    public ResponseEntity<?> getMovesByUserId(@RequestParam("userId") String userId){
        try {
            return new ResponseEntity<>(gameService.getMovesByUserId(userId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
