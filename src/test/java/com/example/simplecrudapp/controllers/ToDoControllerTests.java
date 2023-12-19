package com.example.simplecrudapp.controllers;

import com.example.simplecrudapp.jpa.respository.ToDoRepository;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.stream.IntStream;

import static com.example.simplecrudapp.TestHelpers.readFileAsString;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ToDoControllerTests {

    @LocalServerPort
    private Integer port;
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );

    @BeforeAll
    static void beforeAll() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    ToDoRepository toDoRepository;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        toDoRepository.deleteAll();
    }

    public int createToDoData() {
        String body = readFileAsString("json/CreateToDoBody.json");
        var response = given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/to-do")
                .then()
                .extract().response();
        return response.jsonPath().getInt("id");
    }

    @Test
    @DisplayName("Should create enough to-do ")
    public void shouldCreateToDo() {
        String body = readFileAsString("json/CreateToDoBody.json");
        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/to-do")
                .then()
                .statusCode(201)
                .body("id", notNullValue(),
                        "title", equalTo("Test"),
                        "completed", equalTo(false));
    }

    @Test
    @DisplayName("Should get enough todos")
    public void shouldGetListToDo() {
        String body = readFileAsString("json/CreateToDoBody.json");
        IntStream.range(1, 3).forEach((i) -> given()
                .contentType(ContentType.JSON)
                .when()
                .body(body)
                .post("/to-do")
                .then()
                .statusCode(201));
        var response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/to-do")
                .then()
                .extract().response();
        assertEquals(200, response.statusCode());
        assertEquals(2, response.jsonPath().getInt("size()"));
    }

    @Test
    public void shouldUpdateExistingTodo() {
        var createdToDoId = createToDoData();

        String requestBody = readFileAsString("json/UpdateToDoExistedBody.json");

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .put("/to-do/{id}", createdToDoId)
                .then()
                .statusCode(200)
                .body("id", equalTo(createdToDoId),
                        "title", equalTo("Updated"),
                        "completed", equalTo(false));
    }

    @Test
    public void shouldNotUpdateNonExistedToDo() {
        var createdToDoId = createToDoData();

        String requestBody = readFileAsString("json/UpdateToDoExistedBody.json");

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .put("/to-do/{id}", createdToDoId + 1)
                .then()
                .statusCode(404);
    }

    @Test
    public void shouldDeleteExistedToDo() {
        var createdToDoId = createToDoData();
        given()
                .when()
                .delete("/to-do/{id}", createdToDoId)
                .then()
                .statusCode(204);
        assertEquals(0, toDoRepository.count());
    }

    @Test
    public void shouldNotDeleteExistedToDo() {
        var createdToDoId = createToDoData();
        given()
                .when()
                .delete("/to-do/{id}", createdToDoId+1)
                .then()
                .statusCode(204);
        assertEquals(1, toDoRepository.count());
    }

    @Test
    void shouldGetExistingTodo() {
        int createdTodoId = createToDoData();
        given()
                .when()
                .get("/to-do/{id}", createdTodoId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(createdTodoId),
                        "title", notNullValue(),
                        "completed", notNullValue());
    }

    @Test
    void shouldNotGetNonExistentTodo() {
        given()
                .when()
                .get("/to-do/{id}", 999)
                .then()
                .statusCode(404);
    }
}
