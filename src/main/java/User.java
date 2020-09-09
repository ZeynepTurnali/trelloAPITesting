import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

public class User extends GeneralEnvironment{

    String userUrl;
    private String boardID;
    String userName = "zeynepturnali";
    String fullName = "Zeynep Turnalı";
    String fileName = "/Users/testinium/Desktop/Projects/TrelloAPITesting/src/main/resources/board.json";

    @Test
    public void getUserInfo(){
        readFromFile();
        userUrl = "/1/boards/" + boardID + "/members?key=" + trelloKey + "&token=" + trelloToken + "&userName=" + userName + "&fullName=" + fullName;

        RestAssured.baseURI = trelloAPIURL;
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");

        Response response = request.get(userUrl);

        Assert.assertEquals(response.getStatusCode(),200);
        System.out.println(response.asString());

        ArrayList nameFromArray = response.jsonPath().get("username");
        Assert.assertEquals( userName, nameFromArray.get(0));
        ArrayList fullNameFromArray = response.jsonPath().get("fullName");
        Assert.assertEquals( fullName , fullNameFromArray.get(0));

    }
    @Test
    public void getUserMailInfo(){
        userUrl = "/1/members/" + userName + "?key=" + trelloKey + "&token=" + trelloToken ;
        RestAssured.baseURI = trelloAPIURL;
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");

        Response response = request.get(userUrl);

        Assert.assertEquals(response.getStatusCode(),200);
        System.out.println(response.asString());

        System.out.println((String) response.jsonPath().get("aaEmail"));

    }

    @Test
    public void getAllOrganizations(){
        userUrl = "/1/members/" + userName + "/organizations?key=" + trelloKey + "&token=" + trelloToken;
        RestAssured.baseURI = trelloAPIURL;
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");

        Response response = request.get(userUrl);

        Assert.assertEquals(response.getStatusCode(),200);
        System.out.println(response.asString());
    //    System.out.println(response.jsonPath().get("displayName").toString());

        ArrayList arrayList = response.jsonPath().get("displayName");
        System.out.println("Number of total organizations: " + arrayList.size());
        for(int i = 0; i < arrayList.size(); i++)
        {
            System.out.println( (i+1) + ". organization name: " + arrayList.get(i));
        }


    }

    @Test
    public void deleteUsersAllBoards() {
        userUrl = "/1/members/" + userName + "?key=" + trelloKey + "&token=" + trelloToken;
        RestAssured.baseURI = trelloAPIURL;
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");

        Response response = request.get(userUrl);

        Assert.assertEquals(response.getStatusCode(), 200);
        System.out.println(response.asString());

        ArrayList arrayList = response.jsonPath().get("idBoards");
        System.out.println(arrayList);
        System.out.println(arrayList.size());

        for (int i = 0; i < arrayList.size(); i++) {
            boardID = (String) arrayList.get(i);
            userUrl = "/1/boards/" + boardID + "?key=" + trelloKey + "&token=" + trelloToken;
            RestAssured.baseURI = trelloAPIURL;
            RequestSpecification requestAll = RestAssured.given();
            requestAll.header("Content-Type", "application/json");

            Response responseAll = requestAll.delete(userUrl);
            if (responseAll.getStatusCode() == 401) {
                System.err.println("You are not the board admin, you can not delete this board!");
            } else if (responseAll.getStatusCode() == 200) {
                System.out.println("Deleting process is finished");

            }
        }

    }

    @Test
    public void deleteUsersAllOrganizations(){
        userUrl = "/1/members/" + userName + "?key=" + trelloKey + "&token=" + trelloToken;
        RestAssured.baseURI = trelloAPIURL;
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");

        Response response = request.get(userUrl);

        Assert.assertEquals(response.getStatusCode(), 200);
        System.out.println(response.asString());

        ArrayList arrayList = response.jsonPath().get("idOrganizations");
        System.out.println(arrayList);
        System.out.println(arrayList.size());

        for (int i = 0; i < arrayList.size(); i++) {
            String organizationID = (String) arrayList.get(i);
            userUrl = "/1/organizations/" + organizationID +"?key=" + trelloKey + "&token=" + trelloToken;
            RestAssured.baseURI = trelloAPIURL;
            RequestSpecification requestAll = RestAssured.given();
            requestAll.header("Content-Type", "application/json");

            Response responseAll = requestAll.delete(userUrl);
            Assert.assertEquals(responseAll.getStatusCode(), 200);
        }
        arrayList = response.jsonPath().get("idOrganizations");
        System.out.println("Currently Active Organization Number: " + arrayList.size());
    }



    public void readFromFile() {
        JSONParser parser = new JSONParser();

        try (Reader reader = new FileReader(fileName)) {

            JSONArray jsonArray = (JSONArray) parser.parse(reader);
            JSONObject jsonObjectRead = (JSONObject) jsonArray.get(0);
            System.out.println(jsonObjectRead.get("boardID"));
            boardID = (String) jsonObjectRead.get("boardID");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
