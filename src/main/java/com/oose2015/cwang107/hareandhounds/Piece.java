package com.oose2015.cwang107.hareandhounds;

/**
 * A piece class used to generated object to receive Json info
 * Each piece object contains all attribute for a piece
 * @author chenwang
 *
 */
public class Piece {

    private String gameId;
    private String playerId;
    private String pieceType;
    private int x;
    private int y;
    
    /**
     * Constructor
     * @param gameId game id 
     * @param playerId player id 
     * @param pieceType   hare or hound
     * @param x	  x-coordinate
     * @param y   x-coordinate
     */
    public Piece(String gameId, String playerId, String pieceType, int x, int y) {
        this.gameId = gameId;
        this.playerId = playerId;
        this.pieceType = pieceType;
        this.x = x;
        this.y = y;
    }

    /**
     * get game id 
     * @return game id 
     */
	public String getGameId() {
		return gameId;
	}

	/**
	 * set game id 
	 * @param gameId game id to set
	 */
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	
	/**
	 * get player id 
	 * @return player id 
	 */
	public String getPlayerId() {
		return playerId;
	}

	/**
	 * set the player id
	 * @param playerId player id
	 */
	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	/**
	 * get piece type
	 * @return piece type hare or hound
	 */
	public String getPieceType() {
		return pieceType;
	}

	/**
	 * set the piece type 
	 * @param pieceType piece type
	 */
	public void setPieceType(String pieceType) {
		this.pieceType = pieceType;
	}

	/**
	 * get this piece's x-coordinate
	 * @return piece's x-coordinate
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * set this piece's x-coordinate
	 * @param x x-coordiante
	 */
	public void setX(int x) {
		this.x = x;
	}
	
	/**
	 * get this piece's y-coordinate
	 * @return piece's y-coordinate
	 */
	public int getY() {
		return y;
	}

	/**
	 * set the piece's y-coordinate
	 */
	public void setY(int y) {
		this.y = y;
	}
    
    @Override
    public String toString() {
        return "Piece {" +
                "gameId='" + gameId + '\'' +
                ", playerId='" + playerId + '\'' +
                ", pieceType='" + pieceType + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
