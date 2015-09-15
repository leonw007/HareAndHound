package com.oose2015.cwang107.hareandhounds;

public class State {
	
	String state;
	String gameId;
	String piece_type;
	
	public State( String gameId, String piece_type) {
		// Initialization: only called when a new game is created in ;
		this.state = "WAITING_FOR_SECOND_PLAYER";
		this.gameId = gameId;
		this.piece_type = piece_type;
	}
	

	
	public String getDifferentPieceType() {
		if(piece_type.equals("HOUND")){
			return "HARE";
		} else {
			return "HOUND";
		}
	}
	
	// no matter which type the joiner choose, just let Hound go first
	public void setTurn_Hound() {
		state = "TURN_HOUND";
	}
	
	public void setTurn_Hare() {
		state = "TURN_HARE";
	}
	
	public void setWin_Hound(){
		state = "WIN_HOUND";
	}
	
	public void setWin_HareEscape(){
		state = "WIN_HARE_BY_ESCAPE";
	}
	
	public void setWin_HareStalling(){
		state = "WIN_HARE_BY_STALLING";
	}
	
	
}
