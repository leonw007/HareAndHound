package com.todoapp;

import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import javax.sql.DataSource;

import java.util.ArrayList;
import java.util.List;

public class GameService {
    
    public List<Piece> getFourObj(Board b) { 
    	int[][] board = b.getBoard();
    	List<Piece> pieces = new ArrayList<Piece>();
    	
    	for (int i=0; i<board.length; i++) {
    		for (int j=0; j<board[1].length; j++) {
    			if(board[i][j] == 1 ) {
    				pieces.add(new Piece(b.getGameId(), null, "Hound", j,i));
    			} else if (board[i][j] == 2) {
    				pieces.add(new Piece(b.getGameId(), null, "Hare", j, i));
    			} else {
    				
    			}
    		}
    	}  	
    	return pieces;
    }
    
    
    //Move function. All condiction check should be here.   
    //Note that this X Y is the opposite compare the array index
    public void move(String body, Board board, State state) {
    	PieceJson piece_data = new Gson().fromJson(body, PieceJson.class);
    	
    	int start = board.getBoard()[piece_data.getFromY()][piece_data.getFromX()];
    	int destination =  board.getBoard()[piece_data.getToY()][piece_data.getToX()];
    	
    	String houndPlayerID;
    	if(board.getFirstPlayerType().equals("HOUND")) {
    		houndPlayerID = "1";
    	} else {
    		houndPlayerID = "2";
    	}
    	// Conditions:
    	// 1. selected point cannot be empty and the destination must be empty 
    	if (start !=0 & destination==0) {
    		// 2. detect the selected piece is hound or hare
    		// Note hound is represented as 1, and hare is represented as 2
    		
    		// It means a hound is selected and it is turn_hound state,it is the right player, and it doesn't go back
    		if(start ==1 
    				&& state.state.equals("TURN_HOUND") 
    				&& piece_data.getFromX()<= piece_data.getToX() 
    				&& piece_data.getPlayerId().equals(houndPlayerID)
    				){
    			board.changeBoard(piece_data.getFromY(), piece_data.getFromX(), piece_data.getToY(), piece_data.getToX(), start);
        		state.setTurn_Hare();
    		} 
    		
    		// hare
    		if (start == 2 
    				&& state.state.equals("TURN_HARE")
    				&& !piece_data.getPlayerId().equals(houndPlayerID)) {
    			board.changeBoard(piece_data.getFromY(), piece_data.getFromX(),piece_data.getToY(), piece_data.getToX(), start);
    			state.setTurn_Hound();
    		}
    		
    		//check whether win, to change the state
    		
    		String checkWinInfo = board.checkWin();
    		
    		switch (checkWinInfo) {
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

    }

    //-----------------------------------------------------------------------------//
    // Helper Classes and Methods
    //-----------------------------------------------------------------------------//

    public static class PieceServiceException extends Exception {
        public PieceServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
