package com.oose2015.cwang107.hareandhounds;

/**
 * This class is used get the query data from the database
 * @author chenwang
 *
 */
public class BoardAndState {
	
	int gameId, hound1_x, hound1_y, hound2_x, hound2_y, hound3_x, hound3_y, hare_x, hare_y;
	String state, firstPlayerType;
	
	/**
	 * A contrustor to get data from DB
	 * @param game_id	game id 
	 * @param hound1_x  x of hound1
	 * @param hound1_y  y of hound1 
	 * @param hound2_x  x of hound2 
	 * @param hound2_y  y of hound2
	 * @param hound3_x  x of hound3
	 * @param hound3_y  y of hound3
	 * @param hare_x    x of hare
	 * @param hare_y    y of hare
	 * @param state    state status 
	 * @param firstPlayerType   first player type
	 */
	public BoardAndState(int game_id, int hound1_x, int hound1_y, int hound2_x,
			int hound2_y, int hound3_x, int hound3_y, int hare_x, int hare_y,
			String state, String firstPlayerType) {
		this.gameId = game_id;
		this.hound1_x = hound1_x;
		this.hound1_y = hound1_y;
		this.hound2_x = hound2_x;
		this.hound2_y = hound2_y;
		this.hound3_x = hound3_x;
		this.hound3_y = hound3_y;
		this.hare_x = hare_x;
		this.hare_y = hare_y;
		this.state = state;
		this.firstPlayerType = firstPlayerType;
	}

	/**
	 * Get game ID
	 * @return a int type game id 
	 */
	public int getGame_id() {
		return gameId;
	}

	/**
	 * get the x-coordinate of hound 1
	 * @return x-coordinate of hound 1
	 */
	public int getHound1_x() {
		return hound1_x;
	}

	/**
	 * get the y-coordinate of hound 1 
	 * @return  y-coordinate of hound 1 
	 */
	public int getHound1_y() {
		return hound1_y;
	}

	/**
	 * get the x-coordinate of hound 2
	 * @return x-coordinate of hound 2
	 */
	public int getHound2_x() {
		return hound2_x;
	}
	
	/**
	 * get the y-coordinate of hound 2
	 * @return y-coordinate of hound 2
	 */
	public int getHound2_y() {
		return hound2_y;
	}

	/**
	 * get the x-coordinate of hound 3
	 * @return x-coordinate of hound 3
	 */
	public int getHound3_x() {
		return hound3_x;
	}

	/**
	 * get the y-coordinate of hound 3
	 * @return y-coordinate of hound 3
	 */
	public int getHound3_y() {
		return hound3_y;
	}

	/**
	 * get the x-coordinate of hare
	 * @return x-coordinate of hare
	 */
	public int getHare_x() {
		return hare_x;
	}

	/**
	 * get the y-coordinate of hare
	 * @return y-coordinate of hare
	 */
	public int getHare_y() {
		return hare_y;
	}

	/**
	 * get the state string 
	 * @return state
	 */
	public String getState() {
		return state;
	}

	/**
	 * get the player type of the first player 
	 * @return player type of the first player 
	 */
	public String getFirstPlayerType() {
		return firstPlayerType;
	}
}
