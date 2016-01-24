package com.oose2015.cwang107.hareandhounds;

/**
 * State mainly to maintain the state status for each game
 * @author chenwang
 *
 */
public class State {
	
	String state;
	String gameId;
	
	/**
	 * The constructor, it only needs a game Id.
	 * Because when a new game starts, the first is always "WAITING_FOR_SECOND_PLAYER"
	 * @param gameId
	 */
	public State(String gameId) {
		// Initialization: only called when a new game is created in ;
		this.state = "WAITING_FOR_SECOND_PLAYER";
		this.gameId = gameId;
	}
	
	/**
	 * The constructor for persist across server restarts
	 * @param gameId
	 * @param state
	 */
	public State(String gameId, String state) {
		this.state = state;
		this.gameId = gameId;
	}
	
	
	/**
	 * no matter which type the joiner choose, just let Hound go first
	 * set the state as turn hound 
	 */
	public void setTurn_Hound() {
		state = "TURN_HOUND";
	}
	
	/**
	 * set the state as turn hare
	 */
	public void setTurn_Hare() {
		state = "TURN_HARE";
	}
	
	/**
	 * set the state as hound win
	 */
	public void setWin_Hound(){
		state = "WIN_HOUND";
	}
	
	/**
	 * set the state as hare win by escape
	 */
	public void setWin_HareEscape(){
		state = "WIN_HARE_BY_ESCAPE";
	}
	
	/**
	 * set the state as hare win by stalling 
	 */
	public void setWin_HareStalling(){
		state = "WIN_HARE_BY_STALLING";
	}
	
}
