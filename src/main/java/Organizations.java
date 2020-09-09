import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

public class Organizations extends GeneralEnvironment{
    String organizationUrl;
    String displayName = "newOrganization";
    private String organizationID;
    String newBoardName = "From Org Created Board";
    String newOrganizationName = "Updated Organization Name";
    JSONArray jsonList = new JSONArray();
    JSONObject jsonObject = new JSONObject();
    String fileName = "/Users/testinium/Desktop/Projects/TrelloAPITesting/src/main/resources/organization.json";

    @Test
    public void createNewOrganization(){

        organizationUrl = "/1/organizations?key=" + trelloKey + "&token=" + trelloToken + "&displayName=" + displayName ;
        RestAssured.baseURI = trelloAPIURL;
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");

        Response response = request.post(organizationUrl);

        Assert.assertEquals(response.getStatusCode(),200);
        System.out.println(response.asString());

        String displayName = response.jsonPath().get("displayName");
        System.out.println(displayName);
        Assert.assertEquals( displayName, displayName);

        organizationID = response.jsonPath().get("id");
        System.out.println("created organization id is: " + organizationID);
        jsonObject.put("organizationID", organizationID);
        writeToFile();
    }

    @Test
    public void inCreatedOrgCreateBoard(){
        readFromFile();
        organizationUrl = "/1/boards/?name=" + newBoardName + "&token=" + trelloToken + "&key=" + trelloKey + "&idOrganization=" + organizationID;

        RestAssured.baseURI = trelloAPIURL;
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");

        Response response = request.post(organizationUrl);

        Assert.assertEquals(response.getStatusCode(),200);
        System.out.println(response.asString());

        String boardName = response.jsonPath().get("name");
        System.out.println(boardName);
        Assert.assertEquals( newBoardName, boardName);

    }
    @Test
    public void updateOrganizationName(){
        readFromFile();
        organizationUrl = "/1/organizations/" + organizationID + "?key=" + trelloKey + "&token=" + trelloToken + "&displayName=" + newOrganizationName;

        RestAssured.baseURI = trelloAPIURL;
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");

        Response response = request.put(organizationUrl);

        Assert.assertEquals(response.getStatusCode(),200);
        System.out.println(response.asString());

        Assert.assertEquals(newOrganizationName ,response.jsonPath().get("displayName"));
        System.out.println( "New Name of Organization: "+ response.jsonPath().get("displayName").toString());

    }

    public void writeToFile(){
        jsonList.add(jsonObject);
        try (FileWriter file = new FileWriter(fileName)) {

            file.write(jsonList.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFromFile(){
        JSONParser parser = new JSONParser();

        try (Reader reader = new FileReader(fileName)) {

            JSONArray jsonArray = (JSONArray) parser.parse(reader);
            //    System.out.println(jsonArray.get(0));
            JSONObject jsonObject1 = (JSONObject) jsonArray.get(0);
            System.out.println(jsonObject1.get("organizationID"));
            organizationID = (String) jsonObject1.get("organizationID");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

}
