import io.qameta.allure.Epic;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.*;

@Epic("Rest Assured Template Tests")
public class RestAssuredTemplateTest {
    private static Response response;
    @Test(priority = 1, groups = {"JIRA-101"})
    public void getUsers() {
        response = (Response) given()
                .log().all()
                .queryParam("page", 2)
                .get("https://reqres.in/api/users")
                .prettyPeek().then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract();

        assertEquals("Tired of writing endless social media content? " +
                "Let Content Caddy generate it for you.", response.body().jsonPath().getString("support.text"));
    }

    @Test(priority = 2, groups = {"JIRA-101"})
    public void postUsers() {
        String stringUser = "{\"name\": \"gracius\", \"job\": \"devops\"}";
        JSONObject userJson = new JSONObject(stringUser);

        response = (Response) given()
                .log().all()
                .body(userJson)
                .post("https://reqres.in/api/users")
                .prettyPeek().then()
                .assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .extract();

        assertFalse(response.body().jsonPath().get("id").toString().isEmpty());
        assertTrue(response.body().jsonPath().get("createdAt").toString()
                .matches("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z$"));
    }

    @Test(priority = 3, groups = {"JIRA-101"})
    public void putUsers() {
        String stringUser = "{\"name\": \"gracius\", \"job\": \"TL\"}";
        JSONObject userJson = new JSONObject(stringUser);

        response = (Response) given()
                .log().all()
                .body(userJson)
                .put("https://reqres.in/api/users/2")
                .prettyPeek().then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract();

        assertTrue(response.body().jsonPath().get("updatedAt").toString()
                .matches("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z$"));
    }

    @Test(priority = 4, groups = {"JIRA-101"})
    public void patchUsers() {
        String stringUser = "{\"name\": \"gracius\", \"job\": \"dev\"}";
        JSONObject userJson = new JSONObject(stringUser);

        response = (Response) given()
                .log().all()
                .body(userJson)
                .patch("https://reqres.in/api/users/2")
                .prettyPeek().then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract();

        assertTrue(response.body().jsonPath().get("updatedAt").toString()
                .matches("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z$"));
    }

    @Test(priority = 5, groups = {"JIRA-101"})
    public void deleteUsers() {
        response = (Response) given()
                .log().all()
                .delete("https://reqres.in/api/users/2")
                .prettyPeek().then()
                .assertThat()
                .statusCode(HttpStatus.SC_NO_CONTENT)
                .extract();
    }

    @Test(priority = 6, groups = {"JIRA-101"})
    public void getUsersNotFound() {
        response = (Response) given()
                .log().all()
                .get("https://reqres.in/api/users/23")
                .prettyPeek().then()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .extract();
    }

    @Test(priority = 7, groups = {"JIRA-101"})
    public void postUsersRegisterFail() {
        String stringUser = "{\"email\": \"eve.holt@reqres.in\"}";
        JSONObject userJson = new JSONObject(stringUser);

        response = (Response) given()
                .log().all()
                .body(userJson)
                .post("https://reqres.in/api/register")
                .prettyPeek().then()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract();

        assertEquals("Missing email or username", response.body().jsonPath().get("error"));
    }
}