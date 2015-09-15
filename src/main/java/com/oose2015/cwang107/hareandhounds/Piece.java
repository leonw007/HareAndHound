package com.oose2015.cwang107.hareandhounds;

public class Piece {

    private String gameId;
    private String playerId;
    private String pieceType;
    private int x;
    private int y;
    
    
    public Piece(String gameId, String playerId, String pieceType, int x, int y) {
        this.gameId = gameId;
        this.playerId = playerId;
        this.pieceType = pieceType;
        this.x = x;
        this.y = y;
    }


	public String getGameId() {
		return gameId;
	}


	public void setGameId(String gameId) {
		this.gameId = gameId;
	}


	public String getPlayerId() {
		return playerId;
	}


	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}


	public String getPieceType() {
		return pieceType;
	}


	public void setPieceType(String pieceType) {
		this.pieceType = pieceType;
	}


	public int getX() {
		return x;
	}


	public void setX(int x) {
		this.x = x;
	}


	public int getY() {
		return y;
	}


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
