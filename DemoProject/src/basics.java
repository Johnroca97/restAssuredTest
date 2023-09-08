import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.testng.Assert;

import files.payload;
import files.reusableMethods;

public class basics {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//validar si al agregar un lugar con el API funciona de acuerdo a lo esperado
		//given - all input details
		//when - submit the API
		//Then - validate response
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
		.body(payload.addPlace()).when().post("maps/api/place/add/json")
		.then().assertThat().statusCode(200).body("scope", equalTo("APP"))
		.header("server", "Apache/2.4.41 (Ubuntu)").extract().response().asString();
		
		//añadir lugar - actualizar el lugar a una nueva direccion - traer el lugar validando si la nueva direccion existe en el response
		String newAddress = "Summer Walk, Africa";
		
		System.out.println(response);
		JsonPath js = new JsonPath(response); //for pasing Json
		String placeId = js.getString("place_id");
		System.out.println(placeId);
		
		given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
		.body("{\n"
				+ "\"place_id\":\""+placeId+"\",\n"
				+ "\"address\":\""+newAddress+"\",\n"
				+ "\"key\":\"qaclick123\"\n"
				+ "}")
		.when().put("maps/api/place/update/json")
		.then().assertThat().log().all().statusCode(200).body("msg", equalTo("Address successfully updated"));
		
		//Get place
		
		String getPlaceResponse = given().log().all().queryParam("key", "qaclick123")
		.queryParam("place_id", placeId)
		.when().get("maps/api/place/get/json")
		.then().assertThat().log().all().statusCode(200).extract().response().asString();
		
		JsonPath js2 = reusableMethods.rawToJson(getPlaceResponse);
		String actualAddress = js2.getString("address");
		System.out.println(actualAddress);
		Assert.assertEquals(actualAddress, newAddress);
		
		//JUnit, TestNG, Cucumber

	}

}
