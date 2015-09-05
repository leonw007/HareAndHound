package com.todoapp;

public class PieceJson {
	
	private String gameId, playerId;
	private int fromX, fromY, toX, toY;
	
	public PieceJson(String gameId, String playerId, int fromX, int fromY,
			int toX, int toY) {
		
		this.gameId = gameId;
		this.playerId = playerId;
		this.fromX = fromX;
		this.fromY = fromY;
		this.toX = toX;
		this.toY = toY;
	}

	public String getGameId() {
		return gameId;
	}

	public String getPlayerId() {
		return playerId;
	}

	public int getFromX() {
		return fromX;
	}

	public int getFromY() {
		return fromY;
	}

	public int getToX() {
		return toX;
	}

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
