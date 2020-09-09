import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;

public class Board extends GeneralEnvironment{
    String boardUrl;
    String newBoardName = "Board";
    String updatedName = "updatedBoard";
    private String boardID;
    private String firstListID;
    private String secondListID;
    private String firstListFirstCardID;
    private String secondListFirstCardID;
    JSONArray jsonList = new JSONArray();
    JSONObject jsonObject = new JSONObject();
    String filePathBoard = "/Users/testinium/Desktop/Projects/TrelloAPITesting/src/main/resources/board.json";
    String filePathList = "/Users/testinium/Desktop/Projects/TrelloAPITesting/src/main/resources/lists.json";
    String filePathCard = "/Users/testinium/Desktop/Projects/TrelloAPITesting/src/main/resources/cards.json";


    @Test
    public void createBoard(){
      boardUrl = "/1/boards/?name=" + newBoardName + "&token=" + trelloToken + "&key=" + trelloKey;

      RestAssured.baseURI = trelloAPIURL;
      RequestSpecification request = RestAssured.given();
      request.header("Content-Type", "application/json");

      Response response = request.post(boardUrl);

      Assert.assertEquals(response.getStatusCode(),200);
      System.out.println(response.asString());

      boardID = response.jsonPath().get("id");
      System.out.println("created board id is: " + boardID);
      jsonObject.put("boardID", boardID);
      writeToFile(filePathBoard);
    }

    @Test
    public void updateBoardName(){
        readFromFile(filePathBoard, "boardID");
        boardUrl = "/1/boards/" + boardID  +"?key=" + trelloKey + "&token=" + trelloToken + "&name=" + updatedName;

        RestAssured.baseURI = trelloAPIURL;
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");

        Response response = request.put(boardUrl);


        Assert.assertEquals(200, response.getStatusCode());
        System.out.println(response.asString());


        String boardName = response.jsonPath().get("name");
        System.out.println(boardName);
        Assert.assertEquals( boardName, updatedName);

    }

    @Test
    public void enablePowerUpsOnBoard(){
        readFromFile(filePathBoard, "boardID");
        boardUrl = "/1/boards/" + boardID + "/boardPlugins?key=" + trelloKey + "&token=" + trelloToken;

        RestAssured.baseURI = trelloAPIURL;
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");

        Response response = request.get(boardUrl);

        Assert.assertEquals(200, response.getStatusCode());
        System.out.println(response.asString());

    }

    @Test
    public void getPowerUpInfo(){
        readFromFile(filePathBoard, "boardID");
        boardUrl = "/1/boards/" + boardID + "/plugins?key=" + trelloKey + "&token=" + trelloToken;

        RestAssured.baseURI = trelloAPIURL;
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");

        Response response = request.get(boardUrl);
        Assert.assertEquals(200, response.getStatusCode());
        System.out.println(response.asString());

        ArrayList<String> array = response.jsonPath().get("listing");
/**
        JsonObject jsonObject = new Gson().toJsonTree(array).getAsJsonObject();
     //   System.out.println(jsonObject.get("name"));

        String json = new Gson().toJson(array);
        System.out.println(json);


     //   System.out.println("Power-up Name is: " + array.get(0).toString());
*/

    }
    @Test
    public void getTwoListNameFromBoard(){
        readFromFile(filePathBoard, "boardID");
        boardUrl = "/1/boards/" + boardID + "/lists?key=" + trelloKey + "&token=" + trelloToken;

        RestAssured.baseURI = trelloAPIURL;
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");

        Response response = request.get(boardUrl);
        Assert.assertEquals(200, response.getStatusCode());
        System.out.println(response.asString());

        ArrayList nameOfArray = response.jsonPath().get("name");

        System.out.println("First List Name: " + nameOfArray.get(0));
        System.out.println("Second List Name: " + nameOfArray.get(1));

        ArrayList idOfArray = response.jsonPath().get("id");

        firstListID = (String) idOfArray.get(0);
        System.out.println("First List ID: " + idOfArray.get(0));
        secondListID = (String) idOfArray.get(1);
        System.out.println("Second List ID: " + idOfArray.get(1));

        jsonObject.put("firstListID", firstListID);
        jsonObject.put("secondListID", secondListID);
        writeToFile(filePathList);

    }

    @Test
    public void addFirstCardToFirstList(){
        String cardName = "card 1";
        readFromFile(filePathList, "firstListID");
        boardUrl = "/1/cards?key=" + trelloKey + "&token=" + trelloToken + "&idList=" + firstListID +"&name=" + cardName;

        RestAssured.baseURI = trelloAPIURL;
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");

        Response response = request.post(boardUrl);

        Assert.assertEquals(200, response.getStatusCode());
        System.out.println(response.asString());

        Assert.assertEquals(cardName, response.jsonPath().get("name"));
        System.out.println("Created Card Name: " + response.jsonPath().get("name"));
        System.out.println(response.jsonPath().get("id").toString());
        firstListFirstCardID = response.jsonPath().get("id").toString();
        jsonObject.put("firstListFirstCardID", firstListFirstCardID);
        addFirstCardToSecondList();
        writeToFile(filePathCard);
    }


    public void addFirstCardToSecondList(){
        String cardName = "card 1";
        readFromFile(filePathList, "secondListID");
        boardUrl = "/1/cards?key=" + trelloKey + "&token=" + trelloToken + "&idList=" + secondListID +"&name=" + cardName;

        RestAssured.baseURI = trelloAPIURL;
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");

        Response response = request.post(boardUrl);

        Assert.assertEquals(200, response.getStatusCode());
        System.out.println(response.asString());

        Assert.assertEquals(cardName, response.jsonPath().get("name"));
        System.out.println("Created Card Name: " + response.jsonPath().get("name"));
        secondListFirstCardID = response.jsonPath().get("id");
        jsonObject.put("secondListFirstCardID", secondListFirstCardID);

    }

    @Test
    public void updateCardNameRandomly(){
        String updateCardName = "updated card name";
        String[] str = {"firstListFirstCardID", "secondListFirstCardID"};
        Random random = new Random();
        int index = random.nextInt(str.length);
        readFromFile(filePathCard, str[index]);
        if (str[index].equals("firstListFirstCardID"))
        {
            boardUrl = "/1/cards/" + firstListFirstCardID + "?key=" + trelloKey + "&token=" + trelloToken + "&name=" + updateCardName;
        }else{
            boardUrl = "/1/cards/" + secondListFirstCardID + "?key=" + trelloKey + "&token=" + trelloToken + "&name=" + updateCardName;
        }

        RestAssured.baseURI = trelloAPIURL;
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");

        Response response = request.put(boardUrl);

        Assert.assertEquals(200, response.getStatusCode());
        System.out.println(response.asString());

        Assert.assertEquals(updateCardName, response.jsonPath().get("name"));
        System.out.println("Updated Card Name: " + response.jsonPath().get("name"));

    }

    @Test
    public void addCommentToCard(){
        String comment = "My Comment!";
        readFromFile(filePathCard, "firstListFirstCardID");
        boardUrl = "/1/cards/" + firstListFirstCardID + "/actions/comments?key=" + trelloKey + "&token=" + trelloToken + "&text=" + comment;

        RestAssured.baseURI = trelloAPIURL;
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");

        Response response = request.post(boardUrl);

        Assert.assertEquals(200, response.getStatusCode());
        System.out.println(response.asString());
        LinkedHashMap commentResponse = response.jsonPath().get("data");
        Assert.assertEquals(comment, commentResponse.get("text"));
        System.out.println("Your comment is: " + commentResponse.get("text"));

    }

    @Test
    public void deleteAddedFirstCard(){
        readFromFile(filePathCard, "firstListFirstCardID");
        boardUrl = "/1/cards/" + firstListFirstCardID+ "?key=" + trelloKey + "&token=" + trelloToken;

        RestAssured.baseURI = trelloAPIURL;
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");

        Response response = request.delete(boardUrl);

        Assert.assertEquals(200, response.getStatusCode());
        System.out.println(response.asString());

    }

    @Test
    public void deleteAddedSecondCard(){
        readFromFile(filePathCard, "secondListFirstCardID");
        boardUrl = "/1/cards/" + secondListFirstCardID + "?key=" + trelloKey + "&token=" + trelloToken;

        RestAssured.baseURI = trelloAPIURL;
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");

        Response response = request.delete(boardUrl);

        Assert.assertEquals(200, response.getStatusCode());
        System.out.println(response.asString());

    }

    @Test
    public void deleteCreatedBoard(){
        readFromFile(filePathBoard, "boardID");
        boardUrl = "/1/boards/" + boardID + "?key=" + trelloKey + "&token=" + trelloToken;

        RestAssured.baseURI = trelloAPIURL;
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");

        Response response = request.delete(boardUrl);

        Assert.assertEquals(200, response.getStatusCode());
        System.out.println(response.asString());
    }

    public void writeToFile(String filePath){
        String fileName = filePath;
        jsonList.add(jsonObject);
        try (FileWriter file = new FileWriter(fileName)) {

            file.write(jsonList.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFromFile(String filePath, String key){
        String fileName = filePath;
        JSONParser parser = new JSONParser();

        try (Reader reader = new FileReader(fileName)) {

            JSONArray jsonArray = (JSONArray) parser.parse(reader);
            //    System.out.println(jsonArray.get(0));
            JSONObject jsonObjectRead = (JSONObject) jsonArray.get(0);
            System.out.println(jsonObjectRead.get(key));
            if (key.equals("boardID")){
                boardID = (String) jsonObjectRead.get(key);
            }else if (key.equals("firstListID")){
                firstListID = (String) jsonObjectRead.get(key);
            }else if (key.equals("secondListID")) {
                secondListID = (String) jsonObjectRead.get(key);
            }else if (key.equals("firstListFirstCardID")) {
                firstListFirstCardID = (String) jsonObjectRead.get(key);
            }else if (key.equals("secondListFirstCardID")){
                secondListFirstCardID = (String) jsonObjectRead.get(key);
            }else{
                System.err.println("Illegal input for key!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
