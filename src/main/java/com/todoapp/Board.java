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
	
	public String checkWin(){
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
	
	public String getFirstPlayerType() {
		return firstPlayerType;
	}

	public String getHashString() {
		return Arrays.deepToString(getBoard());
	}

}
