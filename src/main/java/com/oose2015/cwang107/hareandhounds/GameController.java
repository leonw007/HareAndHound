//-------------------------------------------------------------------------------------------------------------//
// Code based on a tutorial by Shekhar Gulati of SparkJava at
// https://blog.openshift.com/developing-single-page-web-applications-using-java-8-spark-mongodb-and-angularjs/
//-------------------------------------------------------------------------------------------------------------//

package com.oose2015.cwang107.hareandhounds;

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

/**
 * GameController is used to communicate with the Javascript.
 * It calls different methods from the GamerService according to route.
 * 
 * @author chenwang
 *
 */
public class GameController {
	
    private final GameService gameService;
    private final Logger logger = LoggerFactory.getLogger(GameController.class);
    
    /**
     * Initialize game service
     * @param gameService generated at the main method
     */
    public GameController(GameService gameService) {
        this.gameService = gameService;
        setupEndpoints();
    }

    /**
     * the interface communicating with the front end javascript
     */
    private void setupEndpoints() {	
    	//fetch board
        get("/hareandhounds/api/games/:id" + "/board", "application/json", (request, response) -> {
            //decide which game board by ID
			try {
				response.status(200);
				return gameService.fetchBoard(request.params(":id"));
			} catch (GameService.GameServiceIdException ex) {
				response.status(404);
				return Collections.EMPTY_MAP;
			}
        }, new JsonTransformer());

    	//fetch state 
        get("/hareandhounds/api/games/:id" + "/state", "application/json", (request, response) -> {
			try {
				response.status(200);
				return gameService.fetchState(request.params(":id"));
			} catch (GameService.GameServiceIdException ex) {
				response.status(404);
				return Collections.EMPTY_MAP;
			}
        }, new JsonTransformer());
        

        //Start a new game.
        post("/hareandhounds/api/games", "application/json", (request, response) -> {              	
        	try {
				response.status(201);
				return gameService.createNewGame(request.body());   
			} catch (Exception e) {
				logger.error("Unable to create a new game.");
				return Collections.EMPTY_MAP;
			}
        }, new JsonTransformer());
        
        //Join a game
        put("/hareandhounds/api/games/:id", "application/json", (request, response) -> {
            try {
				response.status(200);
				return gameService.joinGame(request.params(":id"));
			} catch (GameService.GameServiceIdException ex) {
				response.status(404);
			} catch (GameService.GameServiceJoinException ex) {
				response.status(410);
			}
            return Collections.EMPTY_MAP;		     
        }, new JsonTransformer());
        
        
    	//Move 
        post("/hareandhounds/api/games/:id/turns", "application/json", (request, response) -> {
            try {
				//decide which game board by ID
            	response.status(201);
				gameService.move(request.params(":id"), request.body());
				
			} catch (GameService.GameServiceIdException ex){
				response.status(404);
				return new ResponseContent(ex.getMessage());
			} catch (GameService.GameServiceMoveException ex) {
				response.status(422);
				return new ResponseContent(ex.getMessage());
			}
            return Collections.EMPTY_MAP;
        }, new JsonTransformer());
    }
}
