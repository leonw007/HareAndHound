package com.oose2015.cwang107.hareandhounds;

/**
 * Another class for Json, to get data more efficiently
 * @author chenwang
 *
 */
public class PieceJson {
	
	private String gameId, playerId;
	private int fromX, fromY, toX, toY;
	
	/**
	 * the constructor 
	 * @param gameId game id 
	 * @param playerId player id 
	 * @param fromX the x-coordinate of start point 
	 * @param fromY the y-coordinate of start point 
	 * @param toX the x-coordinate of end point 
	 * @param toY the y-coordinate of end point 
	 */
	public PieceJson(String gameId, String playerId, int fromX, int fromY,
			int toX, int toY) {
		
		this.gameId = gameId;
		this.playerId = playerId;
		this.fromX = fromX;
		this.fromY = fromY;
		this.toX = toX;
		this.toY = toY;
	}

	/**
	 * get game id 
	 * @return game id 
	 */
	public String getGameId() {
		return gameId;
	}

	/**
	 * get the player id
	 * @return player id
	 */
	public String getPlayerId() {
		return playerId;
	}

	/**
	 * get the x-coordinate of start point
	 * @return x-coordinate of start point
	 */
	public int getFromX() {
		return fromX;
	}

	/**
	 * get the y-coordinate of the start point 
	 * @return y-coordinate of the start point 
	 */
	public int getFromY() {
		return fromY;
	}

	/**
	 * get the x-coordinate of the end point
	 * @return  x-coordinate of the end point
	 */
	public int getToX() {
		return toX;
	}
	
	/**
	 * get the y-coordinate of the end point
	 * @return y-coordinate of the end point
	 */
	public int getToY() {
		return toY;
	}
	
    @Override
    public String toString() {
        return "Piece {" +
                "gameId='" + gameId + '\'' +
                ", playerId='" + playerId + '\'' +
                ", fromX='" + fromX + '\'' +
                ", fromY='" + fromY + '\'' +
                ", toX='" + toX + '\'' +
                ", toY='" + toY + '\'' +
                '}';
    }


}
