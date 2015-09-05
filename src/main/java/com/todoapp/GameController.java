//-------------------------------------------------------------------------------------------------------------//
// Code based on a tutorial by Shekhar Gulati of SparkJava at
// https://blog.openshift.com/developing-single-page-web-applications-using-java-8-spark-mongodb-and-angularjs/
//-------------------------------------------------------------------------------------------------------------//

package com.todoapp;

import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static spark.Spark.*;

public class GameController {


    private final GameService gameService;
    
    private List<Board> boards = new ArrayList<Board>();
    private List<State> states = new ArrayList<State>();
    

    public GameController(GameService gameService) {
        this.gameService = gameService;
        setupEndpoints();
        
    }

    private void setupEndpoints() {
    	
    	//fetch board
    	//input parameter is the gameId, it's supposed to return a list of four Obejcts Hare and Hounds
        get("/hareandhounds/api/games/:id" + "/board", "application/json", (request, response) -> {
            //decide which game board by ID
        	Board board = boards.get(Integer.parseInt(request.params(":id")));
			return gameService.getFourObj(board);
        }, new JsonTransformer());

    	//fetch state 
    	//input parameter is the gameId, it's supposed to return a state object with a state string
        get("/hareandhounds/api/games/:id" + "/state", "application/json", (request, response) -> {
            String gameId = request.params(":id");
            State state = states.get(Integer.parseInt(request.params(":id")));
            System.out.println(state.gameId + " is  "+ state.state);
			return state;
        }, new JsonTransformer());
        
    	//Move 
        post("/hareandhounds/api/games/:id/turns", "application/json", (request, response) -> {
            //decide which game board by ID
        	String gameId = request.params(":id");
        	Board board = boards.get(Integer.parseInt(gameId));
        	State state = states.get(Integer.parseInt(gameId));

            gameService.move(request.body(), board, state);
			response.status(201);
            return Collections.EMPTY_MAP;
        }, new JsonTransformer());

        
    	//Creat new Game
        post("/hareandhounds/api/games", "application/json", (request, response) -> {            
            int gameid = boards.size();
            String pieceType = new Gson().fromJson(request.body(), Piece.class).getPieceType();
            Board b = new Board(Integer.toString(gameid), pieceType);
            boards.add(b); 
            
            State state = new State(Integer.toString(gameid), pieceType);
            states.add(state);
            
            Piece piece = new Piece(Integer.toString(gameid), "1", pieceType, 0, 0);
			response.status(201);
            return piece;
        }, new JsonTransformer());
        
        //join game
        put("/hareandhounds/api/games/:id", "application/json", (request, response) -> {
            String gameId = request.params(":id");
            State state = states.get(Integer.parseInt(gameId));
            
            String joiner_Type = state.getDifferentPieceType();
            //no matter which type the joiner choose, just let Hound go first
            state.setTurn_Hound();
			Piece piece = new Piece(gameId, "2", joiner_Type, 0, 0);
			return piece;
        }, new JsonTransformer());
    }
}
