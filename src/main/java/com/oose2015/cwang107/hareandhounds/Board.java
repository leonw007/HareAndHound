 package com.oose2015.cwang107.hareandhounds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

/**
 * A board class which holds basic board info for each game
 * @author chenwang
 *
 */
public class Board {
	private String gameId;
	private int[][] board;	// use a 5*3 int array to represent the status of the board
	private final String firstPlayerType;	
	private Hashtable<String, Integer> boards = new Hashtable<String, Integer>();
	
	/**
	 * Constructor, which can build the initial board position
	 * @param gameId game id 
	 * @param firstPlayerType record the type of first player, which is choose before the user start the game
	 */
	public Board(String gameId, String firstPlayerType) {
		// represent hound as int 1, and hare as int 2, 0 represents empty
		board = new int[3][5];
		// initialize three hounds 
		board[1][0] = 1;
		board[0][1] = 1;
		board[2][1] = 1;
		board[1][4] = 2;	
		this.gameId = gameId;
		this.firstPlayerType = firstPlayerType;
	}
	
	/**
	 * Another contructor which is used by restart.
	 * When the server restarts, to persist across server restarts, this constructor is called to reconstruct 
	 * the previous board status according to the database info
	 * @param gameId game id 
	 * @param firstPlayerType the player type of the first player 
	 * @param hound1_x x of hound1 
	 * @param hound1_y y of hound1
	 * @param hound2_x x of hound2
	 * @param hound2_y y of hound2
	 * @param hound3_x x of hound3
	 * @param hound3_y y of hound3
	 * @param hare_x x of hare
	 * @param hare_y y of hare
	 */
	public Board(String gameId, String firstPlayerType, int hound1_x, int hound1_y, 
			int hound2_x, int hound2_y, int  hound3_x, int hound3_y, 
			int hare_x, int hare_y) {
		this.gameId = gameId;
		this.firstPlayerType = firstPlayerType;
		board = new int[3][5];
		board[hound1_x][hound1_y] = 1;
		board[hound2_x][hound2_y] = 1;
		board[hound3_x][hound3_y] = 1;
		board[hare_x][hare_y] = 2;
	
	}
	
	/**
	 * Get board positions info
	 * @return positions info
	 */
	public int[][] getBoard() {
		return board;
	}
	
	/**
	 * Get gameID
	 * @return game id
	 */
	public String getGameId(){
		return gameId;
	}
	
	/**
	 * Because we record the first player type, this method would return the other type
	 * @return return "Hare" if first is "HOUND", return "HOUND" if first is "HARE"
	 */
	public String getDifferentPieceType() {
		if(firstPlayerType.equals("HOUND")){
			return "HARE";
		} else {
			return "HOUND";
		}
	}
	
	/**
	 * set from point as 0, and to point as new value
	 * @param fromX the x-coordinate of start point
	 * @param fromY the y-coordinate of start point
	 * @param toX the x-coordinate of destination
	 * @param toY the y-coordinate of destination
	 * @param value indicates it is hare or hound
	 */
	public void changeBoard(int fromX, int fromY, int toX, int toY, int value) {
		board[fromX][fromY] = 0;
		board[toX][toY] = value;
		
		//record this new board, this is used to detect stalling
		if(boards.containsKey(getHashString())){
			boards.put(getHashString(), boards.get(getHashString())+1);
		} else {
			boards.put(getHashString(), 1);
		}
	}
	
	/**
	 * get boards appearnce times as hash table
	 * @return a hash table which contains how many times each board position appears
	 */
	public Hashtable<String, Integer> getBoards() {
		return boards;
	}

	/**
	 * get the player type of first player 
	 * @return first player type
	 */
	public String getFirstPlayerType() {
		return firstPlayerType;
	}

	/**
	 * Use the content of an array as a hash code
	 * @return a hash string
	 */
	public String getHashString() {
		return Arrays.deepToString(getBoard());
	}

}
