package com.todoapp;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.Hashtable;

public class Board {
	private String gameId;
	private int[][] board;	// use a 5*3 int array to represent the status of the board
	private final String firstPlayerType;	
	private Hashtable<String, Integer> boards = new Hashtable<String, Integer>();
	
	public Board(String gameId, String piece_type) {
		// represent hound as int 1, and hare as int 2, 0 represents empty
		board = new int[3][5];
		// initialize three hounds 
		board[1][0] = 1;
		board[0][1] = 1;
		board[2][1] = 1;
		board[1][4] = 2;	
		this.gameId = gameId;
		this.firstPlayerType = piece_type;
	}

	public int[][] getBoard() {
		return board;
	}
	
	public String getGameId(){
		return gameId;
	}
	
	//set from point as 0, and to point as new value
	public void changeBoard(int fromX, int fromY, int toX, int toY, int value) {
		board[fromX][fromY] = 0;
		board[toX][toY] = value;
		
		//record this new board
		if(boards.containsKey(getHashString())){
			boards.put(getHashString(), boards.get(getHashString())+1);
		} else {
			boards.put(getHashString(), 1);
		}
	}
	
	
	public Hashtable<String, Integer> getBoards() {
		return boards;
	}

	public String getFirstPlayerType() {
		return firstPlayerType;
	}

	public String getHashString() {
		return Arrays.deepToString(getBoard());
	}

}
