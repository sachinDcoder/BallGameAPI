package com.pratilipi.game.services;

import com.pratilipi.game.constants.ApplicationConstants;
import com.pratilipi.game.constants.Ball;
import com.pratilipi.game.constants.BoardDimension;
import com.pratilipi.game.exceptions.InvalidMoveException;
import com.pratilipi.game.models.Board;
import com.pratilipi.game.models.UserExecutedMove;
import com.pratilipi.game.models.UserMove;
import com.pratilipi.game.models.UserStatus;
import com.pratilipi.game.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    private Map<String, Ball[][]> userBoard;
    private Ball currentBall;

    public UserStatus start(){
        userBoard = new HashMap<>();
        UserStatus userStatus = new UserStatus();

        String user = UUID.randomUUID().toString();
        Ball[][] board = new Ball[BoardDimension.ROW_LENGTH.getDimension()][BoardDimension.COLUMN_LENGTH.getDimension()];
        for(int i = 0; i < BoardDimension.ROW_LENGTH.getDimension(); i++){
            for(int j = 0; j < BoardDimension.COLUMN_LENGTH.getDimension(); j++){
                board[i][j] = Ball.NONE;
            }
        }
        currentBall = Ball.NONE;
        userBoard.put(user, board);

        userStatus.setUser(user);
        userStatus.setStatus(ApplicationConstants.READY);

        return userStatus;
    }

    public void updateCurrentBall(int sequence){
        if(sequence % 2 == 0){
            currentBall = Ball.RED;
        } else{
            currentBall = Ball.YELLOW;
        }
    }

    public String play(UserMove userMove) throws InvalidMoveException {
        Ball[][] board = userBoard.get(userMove.getUser());
        updateCurrentBall(userMove.getSequence());

        for(int row = BoardDimension.ROW_LENGTH.getDimension() - 1; row >= BoardDimension.MIN_ROW_INDEX.getDimension(); row--){
            if(board[row][userMove.getSelectedColumn()] == Ball.NONE){
                insertBall(userMove, row);
                if(checkWinner(board, row, userMove.getSelectedColumn())){
                    UserExecutedMove userExecutedMove = getUserExecutedMove(userMove.getUser());
                    gameRepository.save(userExecutedMove);
                    return currentBall.color + ApplicationConstants.WINS;
                }
                return ApplicationConstants.VALID;
            }
        }
        throw new InvalidMoveException(ApplicationConstants.INVALID);
    }

    private UserExecutedMove getUserExecutedMove(String user) {
        UserExecutedMove userExecutedMove = new UserExecutedMove();
        userExecutedMove.setUserId(user);
        Map<Ball, List<Board>> moves = new HashMap<>();
        moves.put(Ball.YELLOW, new ArrayList<>());
        moves.put(Ball.RED, new ArrayList<>());

        Ball[][] board = userBoard.get(user);

        for(int row = 0; row < BoardDimension.ROW_LENGTH.getDimension(); row++){
            for(int col = 0; col < BoardDimension.COLUMN_LENGTH.getDimension(); col++){
                switch (board[row][col]){
                    case YELLOW:
                        List<Board> yellowMoves = moves.get(Ball.YELLOW);
                        yellowMoves.add(new Board(row, col));
                        moves.put(Ball.YELLOW, yellowMoves);
                        break;

                    case RED:
                        List<Board> redMoves = moves.get(Ball.RED);
                        redMoves.add(new Board(row, col));
                        moves.put(Ball.RED, redMoves);
                        break;
                }
            }
        }

        userExecutedMove.setMoves(moves);
        return userExecutedMove;
    }

    private boolean checkWinner(Ball[][] board, int row, int column) {
        return checkWinnerByRow(board, row, column) || checkWinnerByColumn(board, row, column) || checkWinnerByDiagonal(board, row, column);
    }

    private boolean checkWinnerByRow(Ball[][] board, int row, int column) {
        int count = 1;
        for(int i = row + 1; i < BoardDimension.ROW_LENGTH.getDimension(); i++){
            if(board[i][column] == currentBall){
                count += 1;
            } else {
                count = 0;
            }
        }
        return checkCountEqualsOrGreaterFour(count);
    }

    private boolean checkWinnerByColumn(Ball[][] board, int row, int column) {
        boolean forwardFlag;
        boolean backwardFlag;
        int count = 1;

        for(int i = column + 1; i < BoardDimension.COLUMN_LENGTH.getDimension(); i++){
            if(board[row][i] == currentBall){
                count += 1;
            } else {
                count = 0;
            }
        }

        forwardFlag = checkCountEqualsOrGreaterFour(count);

        count = 1;
        for(int i = column - 1 ; i >= BoardDimension.MIN_COLUMN_INDEX.getDimension(); i--){
            if(board[row][i] == currentBall){
                count += 1;
            } else {
                count = 0;
            }
        }

        backwardFlag = checkCountEqualsOrGreaterFour(count);
        return forwardFlag || backwardFlag;
    }

    private boolean checkWinnerByDiagonal(Ball[][] board, int row, int column) {
        boolean forwardFlag;
        boolean backwardFlag;
        int count = 1;

        for(int i = row + 1, j = column + 1; i < BoardDimension.ROW_LENGTH.getDimension() && j < BoardDimension.COLUMN_LENGTH.getDimension(); i++, j++){
            if(board[i][j] == currentBall){
                count += 1;
            } else {
                count = 0;
            }
        }

        forwardFlag = checkCountEqualsOrGreaterFour(count);

        count = 1;
        for(int i = column - 1, j = row - 1 ; i >= BoardDimension.MIN_COLUMN_INDEX.getDimension() && j >= BoardDimension.MIN_ROW_INDEX.getDimension(); i--, j--){
            if(board[i][j] == currentBall){
                count += 1;
            } else {
                count = 0;
            }
        }

        backwardFlag = checkCountEqualsOrGreaterFour(count);
        return forwardFlag || backwardFlag;
    }

    private boolean checkCountEqualsOrGreaterFour(int count) {
        return count >= ApplicationConstants.MAX_COUNT;
    }

    private void insertBall(UserMove userMove, int row) {
        Ball[][] board = userBoard.get(userMove.getUser());
        board[row][userMove.getSelectedColumn()] = currentBall;
        userBoard.put(userMove.getUser(), board);
    }

    public UserExecutedMove getMovesByUserId(String userId) {
        return gameRepository.findByUserId(userId);
    }
}
