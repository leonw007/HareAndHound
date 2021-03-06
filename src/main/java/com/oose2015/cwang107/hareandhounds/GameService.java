package com.oose2015.cwang107.hareandhounds;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

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
    private Sql2o db;
	
    private final Logger logger = LoggerFactory.getLogger(GameController.class);
 
	public GameService(DataSource dataSource) throws GameServiceException {
		db = new Sql2o(dataSource);
		
        //Create the schema for the database if necessary.
		//the following code in this constructor is mainly for restarting
        try (Connection conn = db.open()) {
            String sql = "CREATE TABLE IF NOT EXISTS gameinfo (gameId INTEGER PRIMARY KEY, " +
                         "                                 state TEXT, firstPlayerType TEXT, hound1_x INTEGER, hound1_y INTEGER, hound2_x INTEGER, hound2_y INTEGER, hound3_x INTEGER, hound3_y INTEGER, hare_x INTEGER, hare_y INTEGER)" ;
            
            conn.createQuery(sql).executeUpdate();         
            String sql_query = "SELECT * FROM gameinfo" ;
     
            List<BoardAndState> games =  conn.createQuery(sql_query)
                .addColumnMapping("gameId", "gameId")
                .addColumnMapping("state", "state")
                .addColumnMapping("firstPlayerType", "firstPlayerType")
                .addColumnMapping("hound1_x", "hound1_x")
                .addColumnMapping("hound1_y", "hound1_y")
                .addColumnMapping("hound2_x", "hound2_x")
                .addColumnMapping("hound2_y", "hound2_y")
                .addColumnMapping("hound3_x", "hound3_x")
                .addColumnMapping("hound3_y", "hound3_y")
                .addColumnMapping("hare_x", "hare_x")
                .addColumnMapping("hare_y", "hare_y")
                .executeAndFetch(BoardAndState.class);
                        
            //reload the data from database to memory when restarts
            for (int i=0; i<games.size(); i++){
            	BoardAndState gameinfo = games.get(i);
            	boards.add(new Board(Integer.toString( gameinfo.getGame_id() ), gameinfo.getFirstPlayerType(), gameinfo.getHound1_x(),  gameinfo.getHound1_y(), 
            			gameinfo.getHound2_x(),  gameinfo.getHound2_y(),  gameinfo.getHound3_x(),  gameinfo.getHound3_y(), 
            			gameinfo.getHare_x(), gameinfo.getHare_y()));
            	states.add(new State(Integer.toString( gameinfo.getGame_id() ), gameinfo.getState()));
            }       
           
        } catch(Sql2oException ex) {
            logger.error("Failed to create schema at startup", ex);
            throw new GameServiceException("Failed to create schema at startup", ex);
        }	
	}

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
     * @param gameId game id 
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
	public Piece createNewGame(String body) throws GameServiceException{
        int gameid = boards.size();
        String pieceType = new Gson().fromJson(body, Piece.class).getPieceType();
        boards.add(new Board(Integer.toString(gameid), pieceType)); 
        states.add(new State(Integer.toString(gameid)));
        
        //this try catch is to insert new game record to database
        String sql = "INSERT INTO gameinfo " + "VALUES (:gameId , :state, :pieceType, :hound1_x, :hound1_y, :hound2_x, :hound2_y, :hound3_x, :hound3_y, :hare_x, :hare_y )";
	    try (Connection conn = db.open()) {
	       conn.createQuery(sql)
	       .addParameter("gameId", gameid)
	       .addParameter("state", "WAITING_FOR_SECOND_PLAYER")
	       .addParameter("pieceType", pieceType)
	       .addParameter("hound1_x", 1)
	       .addParameter("hound1_y", 0)
	       .addParameter("hound2_x", 0)
	       .addParameter("hound2_y", 1)
	       .addParameter("hound3_x", 2)
	       .addParameter("hound3_y", 1)
	       .addParameter("hare_x", 1)
	       .addParameter("hare_y", 4)
	       .executeUpdate();
	    } catch(Sql2oException ex) {
	       logger.error("GameService.createNewGame: Failed to create new entry", ex);
	       throw new GameServiceException("GameService.createNewGame: Failed to create new entry", ex);
	    }
       
        return new Piece(Integer.toString(gameid), "1", pieceType, 0, 0);
	}
	
	/**
	 * Join a game
	 * @param gameId A string game Id to identify which game to join
	 * @return a Piece object to carry joiner's "gameId", "playerId", playerId".
	 */
	public Piece joinGame(String gameId) throws GameServiceException, GameServiceIdException, GameServiceJoinException {		
        State state = states.get(Integer.parseInt(gameId));
        Board board = boards.get(Integer.parseInt(gameId));
        String joiner_Type = board.getDifferentPieceType();
        
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
        
        //The following try catch is to update info of database
        String sql = "UPDATE gameinfo SET state = :state WHERE gameId = :gameId ";
        try (Connection conn = db.open()) {
            //Update the item
            conn.createQuery(sql)
                    .addParameter("gameId", Integer.parseInt(gameId))
                    .addParameter("state", "TURN_HOUND")
                    .executeUpdate();

        } catch(Sql2oException ex) {
            logger.error(String.format("GameService.update: Failed to update database for id: %s", gameId), ex);
            throw new GameServiceException("", ex);
        }
        
		return new Piece(gameId, "2", joiner_Type, 0, 0);
	} 
	
	/**
	 * Move, all conditions check should be here.  
	 * Note that this X Y is the opposite compare the array index.
	 * This method directly change the board, so there is no return.
	 * @param gameId A string game Id, then the system can identify the correct board and state info
	 * @param body a String contains all other info about this move:  playerId: <id>, fromX: <x>, fromY: <y>, toX: <x>, toY: <y>
	 */
    public void move(String gameId, String body) throws GameServiceException, GameServiceIdException, GameServiceMoveException{
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
    		String state_sql;
    		if (start == 1) {
    			state.setTurn_Hare();
    			state_sql = "TURN_HARE";
    		} else {
    			state.setTurn_Hound();
    			state_sql = "TURN_HOUND";
    		}
    		          		
    		board.changeBoard(piece_data.getFromY(), piece_data.getFromX(), piece_data.getToY(), piece_data.getToX(), start);
    		
    		updateStateAfterMove(board, state);	
    		
			//change the state from the database
    		//The following try catch is just to update info from database
	        String sql_query = "SELECT hound1_x, hound1_y, hound2_x, hound2_y, hound3_x, hound3_y, hare_x, hare_y FROM gameinfo";
	        String sql_update = null;
	        try (Connection conn = db.open()) {
	        	
	            List<BoardAndState> games =  conn.createQuery(sql_query)
	                    .addColumnMapping("hound1_x", "hound1_x")
	                    .addColumnMapping("hound1_y", "hound1_y")
	                    .addColumnMapping("hound2_x", "hound2_x")
	                    .addColumnMapping("hound2_y", "hound2_y")
	                    .addColumnMapping("hound3_x", "hound3_x")
	                    .addColumnMapping("hound3_y", "hound3_y")
	                    .addColumnMapping("hare_x", "hare_x")
	                    .addColumnMapping("hare_y", "hare_y")
	                    .executeAndFetch(BoardAndState.class);
	        	
	            BoardAndState game = games.get(Integer.parseInt(gameId));
	            int x=0,y=0;
            	x = piece_data.getToY();
            	y = piece_data.getToX();
            	
            	//decide which attribute to modify in the database
	            if(game.getHound1_x() == piece_data.getFromY() && game.getHound1_y() == piece_data.getFromX()){

	            	 sql_update = "UPDATE gameinfo SET state = :state, hound1_x = :piece_x, hound1_y = :piece_y WHERE gameId = :gameId ";
	            }
	            
	            if(game.getHound2_x() == piece_data.getFromY() && game.getHound2_y() == piece_data.getFromX()){

	            	sql_update = "UPDATE gameinfo SET state = :state, hound2_x = :piece_x, hound2_y = :piece_y WHERE gameId = :gameId ";
	            }
	            if(game.getHound3_x() == piece_data.getFromY() && game.getHound3_y() == piece_data.getFromX()){

	            	sql_update = "UPDATE gameinfo SET state = :state, hound3_x = :piece_x, hound3_y = :piece_y WHERE gameId = :gameId ";
	            }
	            if(game.getHare_x() == piece_data.getFromY() && game.getHare_y() == piece_data.getFromX()){

	            	sql_update = "UPDATE gameinfo SET state = :state, hare_x = :piece_x, hare_y = :piece_y WHERE gameId = :gameId ";
	            }
    	
	            //Update the item
	            conn.createQuery(sql_update)
                	.addParameter("gameId", Integer.parseInt(gameId))
                	.addParameter("state", state_sql)
                	.addParameter("piece_x", x)
                	.addParameter("piece_y", y)
                	.executeUpdate();

	        } catch(Sql2oException ex) {
	            logger.error(String.format("GameService.update: Failed to update database for id: %s", gameId), ex);
	            throw new GameServiceException(String.format("GameService.update: Failed to update database for id: %s", gameId), ex);
	        }
    		
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
    			piece_data.getFromX() == 2 && piece_data.getFromY() == 0 ||
    			piece_data.getFromX() == 3 && piece_data.getFromY() == 1 || 
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
    public String updateStateAfterMove(Board board, State state){
		switch (checkWin(board)) {
		case "HareTrapped":
			state.setWin_Hound();
			return "HareTrapped";
		case "HareEscaped":
			state.setWin_HareEscape();
			return "HareEscaped";
		case "Stalling":
			state.setWin_HareStalling();
			return "Stalling";
		case "Continue":
			break;
		}
		return "";
    }
    
    /**
     * Through checking a board's info to see whether hare or hound win
     * @param b a board
     * @return a string which indicates who wins because of what reason
     */
	public String checkWin(Board b){
		int[][] board = b.getBoard();
		Hashtable<String, Integer> boards = b.getBoards();
		
		//get 4 piece location
		//use a arraylist to represent the y index of three hounds
		ArrayList<Integer> hounds_y = new ArrayList<Integer>();
		//use an int to represent the y index of the hare
		int hare_y = 0;
		for(int i=0; i<board.length; i++) {
			for(int j=0; j<board[1].length; j++) {
				if (board[i][j] == 1) {
					hounds_y.add(j);
				} else if (board[i][j] == 2) {
					hare_y = j;
				}
			}
		}
		
		//check if hare is trapped: 3 cases.
		if(board[0][2]==2 && board[0][1]==1 && board[0][3]==1 && board[1][2]==1) {
			return "HareTrapped";
		}
		if(board[2][2]==2 && board[2][1]==1 && board[2][3]==1 && board[1][2]==1) {
			return "HareTrapped";
		}
		if(board[1][4]==2 && board[0][3]==1 && board[1][3]==1 && board[2][3]==1) {
			return "HareTrapped";
		}
		
		// hare win by escape
		if(hounds_y.get(0) >= hare_y
				&& hounds_y.get(1) >= hare_y
				&& hounds_y.get(2) >= hare_y) {
			return "HareEscaped";
		}
		
		//check stalling
		if(boards.containsValue(3)){
			return "Stalling";
		}
		return "Continue";	
	}
   

    //-----------------------------------------------------------------------------//
    // Helper Classes and Methods
    //-----------------------------------------------------------------------------//

    public static class GameServiceException extends Exception {
        public GameServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
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
