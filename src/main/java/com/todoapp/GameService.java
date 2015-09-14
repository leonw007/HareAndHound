package com.todoapp;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GameService is a class which provides service for the game running.
 * It is initialized when the server starts.
 * All boards info is stored here. This is a Model. 
 * 
 * @author chenwang
 *
 */
public class GameService {
	
    private List<Board> boards = new ArrayList<Board>();
    private List<State> states = new ArrayList<State>();
	
    private final Logger logger = LoggerFactory.getLogger(GameController.class);

	/**
	 * Fetch the board: get board info and return it to the front end. It keeps updating.
	 * @param gameId
	 * @return a list of four piece objects, which show the current board status.
	 * @throws GameServiceException
	 */
    public List<Piece> fetchBoard(String gameId) throws GameServiceIdException {    	
    	//To prevent the case that server has been restarted but client keeps running
    	if(Integer.parseInt(gameId)>= boards.size()) {
    		logger.error("INVALID_GAME_ID.");
    		throw new GameServiceIdException("INVALID_GAME_ID.", null);
    	}
    	
    	int[][] board = boards.get(Integer.parseInt(gameId)).getBoard();
    	List<Piece> pieces = new ArrayList<Piece>();
    	
    	for (int i=0; i<board.length; i++) {
    		for (int j=0; j<board[1].length; j++) {
    			if(board[i][j] == 1 ) {
    				pieces.add(new Piece(gameId, null, "Hound", j,i));
    			} else if (board[i][j] == 2) {
    				pieces.add(new Piece(gameId, null, "Hare", j, i));
    			} else {		
    			}
    		}
    	}  	
    	return pieces;
    }
    
    /**
     * Fetch State: get the current state info and return it to client
     * @param gameId
     * @return a State object
     * @throws GameServiceException
     */
    public State fetchState(String gameId) throws GameServiceIdException{
    	
    	if(Integer.parseInt(gameId)>= states.size()) {
    		logger.error("INVALID_GAME_ID");
    		throw new GameServiceIdException("INVALID_GAME_ID", null);
    	}
    		
    	return states.get(Integer.parseInt(gameId));
    }
	
    /**
     * Start a new game
     * @param body This is a string which shows the piece type beginner wants to use.
     * @return a Piece object to carry initiator‘s "gameId", "playerId" and "pieceType".
     */
	public Piece createNewGame(String body) {
        int gameid = boards.size();
        String pieceType = new Gson().fromJson(body, Piece.class).getPieceType();
        boards.add(new Board(Integer.toString(gameid), pieceType)); 
        states.add(new State(Integer.toString(gameid), pieceType));
        return new Piece(Integer.toString(gameid), "1", pieceType, 0, 0);
	}
	
	/**
	 * Join a game
	 * @param gameId A string game Id to identify which game to join
	 * @return a Piece object to carry joiner's "gameId", "playerId", playerId".
	 */
	public Piece joinGame(String gameId) throws GameServiceIdException, GameServiceJoinException {		
        State state = states.get(Integer.parseInt(gameId));
        String joiner_Type = state.getDifferentPieceType();
        
        if(states.size() <= Integer.parseInt(gameId)){
        	//invalid game id, because it has not been created yet
    		logger.error("INVALID_GAME_ID");
    		throw new GameServiceIdException("INVALID_GAME_ID", null);
        } else if (!states.get(Integer.parseInt(gameId)).state.equals("WAITING_FOR_SECOND_PLAYER")) {
    		logger.error("GAME_JOINED");
    		throw new GameServiceJoinException("GAME_JOINED", null);
        }
        
        //no matter which type the joiner choose, just let Hound go first
        state.setTurn_Hound();
		return new Piece(gameId, "2", joiner_Type, 0, 0);
	} 
	
	/**
	 * Move, all conditions check should be here.  
	 * Note that this X Y is the opposite compare the array index.
	 * This method directly change the board, so there is no return.
	 * @param gameId A string game Id, then the system can identify the correct board and state info
	 * @param body a String contains all other info about this move:  playerId: <id>, fromX: <x>, fromY: <y>, toX: <x>, toY: <y>
	 */
    public void move(String gameId, String body) throws GameServiceIdException, GameServiceMoveException{
    	Board board = boards.get(Integer.parseInt(gameId));
    	State state = states.get(Integer.parseInt(gameId));
    	PieceJson piece_data = new Gson().fromJson(body, PieceJson.class);
    	
    	// get the piece from the start point and the end point
    	int start = board.getBoard()[piece_data.getFromY()][piece_data.getFromX()];
    	
    	if(states.size() <= Integer.parseInt(gameId)) {
			//invalid game id, because it has not been created yet
    		logger.error("INVALID_GAME_ID");
    		throw new GameServiceIdException("INVALID_GAME_ID", null);
		}
    	
    	if(!piece_data.getPlayerId().equals("1") && !piece_data.getPlayerId().equals("2")) {
    		logger.error("INVALID_PLAYER_ID");
    		throw new GameServiceIdException("INVALID_PLAYER_ID", null);
    	}
	
    	//check whether this is a valid move (include correct turn)
    	if (checkValidTurn(gameId, body) && checkValidMove(gameId, body)) {
    		if (start == 1) {
    			state.setTurn_Hare();
    		} else {
    			state.setTurn_Hound();
    		}
    		board.changeBoard(piece_data.getFromY(), piece_data.getFromX(), piece_data.getToY(), piece_data.getToX(), start);
    		updateStateAfterMove(board, state);
    	} else if (!checkValidTurn(gameId, body)) {
    		//not a valid turn
    		logger.error("INCORRECT_TURN");
    		throw new GameServiceMoveException("INCORRECT_TURN", null);
    	} else {
    		//not a valid move
    		logger.error("ILLEGAL_MOVE");
    		throw new GameServiceMoveException("ILLEGAL_MOVE", null);
    	}
    }
     
    
    /**
     * Check whether this is a correct turn
     * @param gameId A string game Id, then the system can identify the correct board and state info
	 * @param body a String contains all other info about this move:  playerId: <id>, fromX: <x>, fromY: <y>, toX: <x>, toY: <y>
     * @return if is valid turn, return true; if not, return false.
     */
    public boolean checkValidTurn(String gameId, String body) {
    	
    	Board board = boards.get(Integer.parseInt(gameId));
    	State state = states.get(Integer.parseInt(gameId));
    	PieceJson piece_data = new Gson().fromJson(body, PieceJson.class);
    	
    	// get the piece from the start point and the end point
    	int start = board.getBoard()[piece_data.getFromY()][piece_data.getFromX()];
    	
    	//decide hound is player 1 or 2 in this game
    	String houndPlayerID;
    	if(board.getFirstPlayerType().equals("HOUND")) {
    		houndPlayerID = "1";
    	} else {
    		houndPlayerID = "2";
    	}
    	
    	if(start ==1 && state.state.equals("TURN_HOUND") && piece_data.getPlayerId().equals(houndPlayerID)) {
    		return true;
    	} else if (start == 2 && state.state.equals("TURN_HARE") && !piece_data.getPlayerId().equals(houndPlayerID)) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    
    /**
     * Check whether this move is valid
 * @param gameId A string game Id, then the system can identify the correct board and state info
	 * @param body a String contains all other info about this move:  playerId: <id>, fromX: <x>, fromY: <y>, toX: <x>, toY: <y>
     * @return if it's a valid move, return true; if not, return false;
     */
    public boolean checkValidMove(String gameId, String body) {
    	
    	Board board = boards.get(Integer.parseInt(gameId));
    	PieceJson piece_data = new Gson().fromJson(body, PieceJson.class);
    	
    	// get the piece from the start point and the end point
    	int start = board.getBoard()[piece_data.getFromY()][piece_data.getFromX()];
    	int destination =  board.getBoard()[piece_data.getToY()][piece_data.getToX()];
    	
    	// there are four diagonals which need to be considered 
    	if(piece_data.getFromX() == 1 && piece_data.getFromY() ==1 || 
    			piece_data.getFromX() == 0 && piece_data.getFromY() == 2 ||
    			piece_data.getFromX() == 1 && piece_data.getFromY() == 3 || 
    			piece_data.getFromX() == 2 && piece_data.getFromY() == 2 ) {
    		if (Math.abs(piece_data.getToX() - piece_data.getFromX()) == 1 &&
    				Math.abs(piece_data.getToY() - piece_data.getFromY()) == 1) {
    			return false;
    		}
    	}
    	
    	
    	if (destination == 0 &&
    			Math.abs(piece_data.getToY() - piece_data.getFromY()) <= 1 && 
    			Math.abs(piece_data.getToX() - piece_data.getFromX()) <= 1) {	
        	if(start == 1 && piece_data.getFromX() <= piece_data.getToX() ) {
        		return true;
        	} else if (start == 2) {
        		return true;
        	} else {
        		return false;
        	}
    		
    	} else {
    		return false;
    	}
    	
    	
    }
    
    /**
     * To update the state
     * @param board the current Board object
     * @param state the state
     */
    public void updateStateAfterMove(Board board, State state){
		switch (board.checkWin()) {
		case "HareTrapped":
			state.setWin_Hound();
			break;
		case "HareEscaped":
			state.setWin_HareEscape();
			break;
		case "Stalling":
			state.setWin_HareStalling();
		case "Continue":
			break;
		}
    }
   

    //-----------------------------------------------------------------------------//
    // Helper Classes and Methods
    //-----------------------------------------------------------------------------//

    public static class GameServiceIdException extends Exception {
        public GameServiceIdException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    public static class GameServiceJoinException extends Exception {
        public GameServiceJoinException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    public static class GameServiceMoveException extends Exception {
        public GameServiceMoveException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    
    

}
