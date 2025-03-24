package tests;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Rest Assured Template Tests")
public class RestAssuredTemplateTest {
    private static Response response;
    @Test
    @DisplayName("GET Example")
    @Order(1)
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

    @Test
    @DisplayName("POST Example")
    @Order(2)
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

    @Test
    @DisplayName("PUT Example")
    @Order(3)
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

    @Test
    @DisplayName("PATCH Example")
    @Order(4)
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

    @Test
    @DisplayName("DELETE Example")
    @Order(5)
    public void deleteUsers() {
        response = (Response) given()
                .log().all()
                .delete("https://reqres.in/api/users/2")
                .prettyPeek().then()
                .assertThat()
                .statusCode(HttpStatus.SC_NO_CONTENT)
                .extract();
    }

    @Test
    @DisplayName("GET Not Found Example")
    @Order(6)
    public void getUsersNotFound() {
        response = (Response) given()
                .log().all()
                .get("https://reqres.in/api/users/23")
                .prettyPeek().then()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .extract();
    }

    @Test
    @DisplayName("POST Failed Register Example")
    @Order(7)
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