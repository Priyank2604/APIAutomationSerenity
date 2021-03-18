package com.methods;

import com.fasterxml.uuid.Generators;
import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.thucydides.core.annotations.Step;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

public class ApiMethods {

    Response response;
    String baseurl = "http://192.168.2.130:9100";
//    String baseurl = "http://192.168.2.125:8000";
    Faker faker = new Faker();

    String fname = faker.name().firstName();
    String lname = faker.name().lastName();
    String emailAddress = fname+lname+"@bosleo.com";
    String itemName = faker.food().spice();
    String nonitemName = faker.food().spice();
    String clinic_id;
    String result1;
    String result;



    @Step
    public String generatetoken() {
//        String token = given().headers("Content-Type", "application/x-www-form-urlencoded")
//                .params("client_id", "1",
//                "client_secret", "oDdXqt2R04nAFzr2vKDHDuA8X3gYFoRzvhxvEf3b",
//                        "scope", "*",
//                        "grant_type", "client_credentials")
//                .when().post(baseurl+"/oauth/token")
//                .then().body(containsString("access_token"))
//                .extract().path("access-token");
//        String access_token = "Bearer"+" "+token;
//
//        System.out.println(access_token);
//        return access_token;
        String token = given().headers("Content-Type", "application/x-www-form-urlencoded","Accept", ContentType.JSON)
                .params("client_id", "1",
                        "client_secret", "oDdXqt2R04nAFzr2vKDHDuA8X3gYFoRzvhxvEf3b",
//                        "client_secret", "aHwE9RwDfEUz21jBcR7YhapIbIXdZkbPC16nHyoe",
                        "scope", "*",
                        "grant_type", "client_credentials")
                .when().post(baseurl + "/oauth/token")
                .then().body(containsString("access_token"))
                .extract().path("access_token");
        String access_token = token;
        System.out.println(access_token);
        return access_token;
    }

    @Step
    public String  signup() {
        response = given()
                .headers( "Authorization", generatetoken(),"Content-Type", "application/x-www-form-urlencoded","platform","web","Accept","application/json")
                .params("service_id", "582f1dfe-a8c4-47b2-b4ae-b067ad05b787",
                        "company_name", "PQR Pets",
//                        "phone", "+91"+faker.number().digits(10),
                        "phone", faker.number().digits(10),
                        "password", "bosleo",
//                "email", "pqr911@bosleo.com",
                        "email", emailAddress,
                        "first_name", fname,
                        "last_name", lname,
                        "sales_code","324520",
                        "terms",true,
                        "ip_address","103.249.120.50",
                        "is_company_admin", 1,
                        "country_code","+91",
//                        "only_phone",faker.number().digits(10),
                        "timezone","Asia/Calcutta")
                .when().post(baseurl + "/api/v1/user/signup")
                .then().contentType("application/json").extract().response();
//                .then().contentType(ContentType.JSON).extract().response();
//        System.out.println(response);
        System.out.println(response.jsonPath().getString("$"));
        System.out.println(emailAddress);
        return emailAddress;
//        String responseBody = response.getBody().asString();
//        System.out.println("Response Body is: " + responseBody);

//        System.out.println(response.getBody().asString());

    }

    @Step
    public void otpVerify() {
        //Validate Email Address
        given()
                .headers("Authorization", generatetoken(), "Content-Type", "application/x-www-form-urlencoded", "Accept", ContentType.JSON, "Platform", "web")
                .params(
                        "user_id", response.jsonPath().getString("user_id"),
                        "otp", response.jsonPath().getString("email_otp"),
                        "type", "email")
                .when().post(baseurl + "/api/v1/otp/verify")
                .then().contentType(ContentType.JSON).extract().response().jsonPath().getString("$");
//        System.out.println(response.jsonPath().getString("$"));


    }

    @Step
    public void otpPhoneVerify(){
        JSONObject json=new JSONObject();
        json.put("user_id", response.jsonPath().getString("user_id"));
        json.put("type","phone");
        json.put("otp",response.jsonPath().getString("phone_otp"));
        // Validate Phone number
      Response response3= given()
                .headers("Authorization", generatetoken(), "Content-Type", "application/json", "Accept", ContentType.JSON,"Platform","web")
//                .params(
//                        "user_id", response.jsonPath().getString("user_id"),
//                        "type","phone",
//                        "otp",response.jsonPath().getString("phone_otp")
//                        )


                .when().post(baseurl + "/api/v1/otp/verify")
//                .then().body(containsString("OTP verified successfully."));
                .then().contentType(ContentType.JSON).extract().response();
//            System.out.println(response3);
        System.out.println(response3.jsonPath().getString("$"));


    }

//
//    @Step
//    public void subscription(){
//                given()
//                .headers("Authorization", generatetoken(),"Accept" , ContentType.JSON)
//                .params("name", "Plan-1",
//                        "currency", "usd",
//                        "amount", "100000.00",
//                        "interval", "month",
//                        "money_back_in_days", "20")
//                        .when().post(baseurl+"/api/v1/service/582f1dfe-a8c4-47b2-b4ae-b067ad05b787/plan")
//                        .then().contentType(ContentType.JSON).extract().response();


//    }

    @Step
    public void payment() {
        given()
                .headers("Authorization", generatetoken(), "Content-Type", "application/x-www-form-urlencoded", "Accept", ContentType.JSON,"Platform","web")
                .params("plan_id", "76cb8d4f-069e-4c36-85b5-2666affae903",
                        "user_id", response.jsonPath().getString("user_id"),
//                        "user_id", "dc183302-d52e-4a80-936e-234ec74135ff",
                        "card_number", "4242424242424242",
                        "card_cvc", 520,
                        "exp_month", 10,
                        "exp_year", 2020)
                .when().post(baseurl + "/api/v1/subscription")
                .then().contentType(ContentType.JSON).extract().response();
    }

    @Step
    public String loginAccess() {
        String Token = given()
                .headers("Authorization", generatetoken(), "Accept", "application/json","Content-Type","application/x-www-form-urlencoded","Platform","web")
                .param("email", emailAddress)
//        System.out.println(emailAddress);
                .param("password", "bosleo")
                .when().post(baseurl + "/api/v1/app-login")
                .then().body(containsString("access_token"))
                .extract().path("access_token");
//                .then().extract().response().path("access_token");
//        System.out.println(Token);
        String loginToken = "Bearer" + " " + Token;
        System.out.println(loginToken);
        return loginToken;
    }

    @Step
    public void company_details() {
        System.out.println(loginAccess());
        Response response2 = given().headers( "Authorization", loginAccess(),"Content-Type", "application/x-www-form-urlencoded","platform", "web")
//                .param("company_id","bf0673c3-d4ea-4794-864f-1e5deb296024")
//                        "company_id", response.jsonPath().getString("company_id"))
                .when().get(baseurl + "/api/v1/company/"+response.jsonPath().getString("company_id"))
//                        response.jsonPath().getString("company_id"))
                .then().assertThat().statusCode(200).extract().response();
       System.out.println(response2.jsonPath().getString("$"));

    }

    @Step
    public void addCompany(){
        JSONObject json=new JSONObject();

        json.put("company_name", "PQR Pets");
        json.put("email", emailAddress);
        json.put("website", "https://www.google.com");
        json.put("phone", faker.number().digits(10));
        json.put("address_line_1", "white house");
        json.put("street", "belgium west");
        json.put("city", "New York");
        json.put("state", "New York");
        json.put("country", "USA");
        json.put("zip_code", 45622);
        json.put("service_id","582f1dfe-a8c4-47b2-b4ae-b067ad05b787");
        json.put("country_code","+91");
        Response rs=given()
                .headers("Authorization", loginAccess(), "Accept", ContentType.JSON, "Content-Type", ContentType.JSON,"platform","web")
                .body(json.toString())
                .when().put(baseurl + "/api/v1/company/"+response.jsonPath().getString("company_id"))
                .then().contentType(ContentType.JSON).extract().response();
        System.out.println(response.jsonPath().getString("company_id"));
        System.out.println(json);
        System.out.println(rs.jsonPath().getString("$"));


    }

    @Step
    public void company_details1() {
        System.out.println(loginAccess());
        Response response2 = given().headers("Authorization", loginAccess(), "Content-Type", "application/x-www-form-urlencoded", "platform", "web")
//                .param("company_id","bf0673c3-d4ea-4794-864f-1e5deb296024")
//                        "company_id", response.jsonPath().getString("company_id"))
                .when().get(baseurl + "/api/v1/company/" + response.jsonPath().getString("company_id"))
//                        response.jsonPath().getString("company_id"))
                .then().assertThat().statusCode(200).extract().response();
        System.out.println(response2.jsonPath().getString("$"));
    }

    @Step
    public void addClinic() {
        JSONObject json = new JSONObject();
        json.put("company_id",response.jsonPath().getString("company_id"));
//                "company_id", response.jsonPath().getString("company_id"));
//            json.put("logo","String");
        json.put("name", faker.company().name());
        json.put("email", emailAddress);
        json.put("website", "default.png");
        json.put("phone", faker.number().digits(10));
        json.put("address_line_1", faker.address().buildingNumber());
        json.put("street", faker.address().streetAddress());
        json.put("city", faker.address().cityName());
        json.put("state", faker.address().state());
        json.put("country", faker.address().country());
        json.put("zip_code", faker.number().digits(5));
//            json.put("lat", "String");
//            json.put("long", "String");
        json.put("accept_cash", 0);
        json.put("accept_cheque", 1);
        json.put("accept_cc", 0);
        json.put("appointment_slot_time", 15);
        json.put("is_24_7_available", true);
        json.put("service_id","582f1dfe-a8c4-47b2-b4ae-b067ad05b787");
//        json.put("clinic_speciality",({"id","2c499f42-2160-4a9b-a706-0598330e2050","speciality","acupuncture"}

//                ));
//        JSONObject ObjectSpe = new JSONObject();
//        JSONArray specialityarray = new JSONArray();
//
//        JSONObject spec = new JSONObject();
////        spec.put("id", uuid);
//        spec.put("speciality", faker.company().profession());
//        spec.put("user_defined", true);
//        spec.put("service_id","582f1dfe-a8c4-47b2-b4ae-b067ad05b787");
//        specialityarray.put(spec);
//
//        ObjectSpe.put("speciality", specialityarray);


        Response response=given()
                .headers("Authorization", loginAccess(), "Accept", ContentType.JSON, "Content-Type", ContentType.JSON,"platform","web")
                .body(json.toString())
                .when().post(baseurl + "/api/v1/clinic")
                .then().contentType(ContentType.JSON).extract().response();
            clinic_id=response.jsonPath().getString("id");
//        System.out.println(response);



//        clinic_id = clinic.jsonPath().getString("id");
        System.out.println(json);
        System.out.println(clinic_id);

//        return clinic_id;


    }

    @Step
    public void clinicSpeciality() {
//        UUID uuid = Generators.randomBasedGenerator().generate();
//
//        JSONObject specObj = new JSONObject();
//            JSONArray specialityarray = new JSONArray();
//
//            JSONObject speciality = new JSONObject();
//            speciality.put("id", uuid);
//            speciality.put("speciality", faker.company().profession());
//            speciality.put("user_defined", true);
//
//            specialityarray.put(speciality);
//            specObj.put("speciality",specialityarray);
//
//            given()
//                .headers("Authorization", loginAccess(), "Accept",ContentType.JSON, "Content-Type",ContentType.JSON )
//                .body(specObj.toString())
//                .when().post(baseurl+"/api/v1/clinic/"+clinic_id+"/speciality")
//                .then().assertThat().statusCode(200);
        UUID uuid = Generators.randomBasedGenerator().generate();

        JSONObject ObjectSpe = new JSONObject();
        JSONArray specialityarray = new JSONArray();

        JSONObject spec = new JSONObject();
        JSONObject spec1= new JSONObject();
//        spec.put("id", uuid);
//        spec.put("id","2c499f42-2160-4a9b-a706-0598330e2050");
//        spec.put("speciality", "acupuncture");
        spec.put("speciality", faker.company().profession());
        spec.put("user_defined", true);
        spec.put("service_id","582f1dfe-a8c4-47b2-b4ae-b067ad05b787");
        spec1.put("id","2c499f42-2160-4a9b-a706-0598330e2050");
        spec1.put("speciality","acupuncture");
        specialityarray.put(spec);
        specialityarray.put(spec1);

        ObjectSpe.put("speciality", specialityarray);

//
        System.out.println(spec);
        System.out.println(spec1);
        System.out.println(specialityarray);


        given()
                .headers("Authorization", loginAccess(),
                        "Accept", ContentType.JSON,
                        "Content-Type", "application/json",
                        "Platform","web")
                .body(ObjectSpe.toString())
                .when().post(baseurl + "/api/v1/clinic/" + clinic_id + "/speciality")
//                .then().assertThat().statusCode(200);
                .then().contentType(ContentType.JSON).extract().statusCode();

    }

    @Step
    public void clinicSchedule() {
        JSONObject schedule = new JSONObject();
        JSONObject day = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        JSONObject time = new JSONObject();
        time.put("from", "10:00:00");
        time.put("to", "12:00:00");
        jsonArray.put(time);

        schedule.put("schedule", day);
        day.put("sun", jsonArray);
        day.put("mon", jsonArray);
        day.put("tue", jsonArray);
        day.put("wed", jsonArray);
        day.put("thu", jsonArray);
        day.put("fri", jsonArray);
        day.put("sat", jsonArray);

        System.out.println(schedule.toString());
       Response res1= given()
                .headers("Authorization", loginAccess(), "Accept", ContentType.JSON, "Content-Type", "application/json","platform","web")
                .body(schedule.toString())
                .when().post(baseurl + "/api/v1/clinic/" + clinic_id + "/schedule")
                .then().contentType(ContentType.JSON).extract().response();
//        System.out.println(res1);
        System.out.println(res1.jsonPath().getString("$"));



    }

    @Test
    public Response addUser() {
        JSONObject json = new JSONObject();
        json.put("service_id", "582f1dfe-a8c4-47b2-b4ae-b067ad05b787");
        json.put("company_id", response.jsonPath().getString("company_id"));
        json.put("first_name", faker.name().firstName());
        json.put("last_name", faker.name().lastName());
        json.put("licence_no", faker.number().digits(10));
        json.put("email", faker.internet().emailAddress());
        json.put("phone", faker.number().digits(10));
        json.put("role", "VETERINARIAN");
        json.put("is_company_admin", false);
        json.put("country_code","+91");
        json.put("timezone","Asia/Calcutta");
        json.put("specialities"," ");
        JSONObject js=new JSONObject();
        JSONArray jk=new JSONArray();
        js.put("id", clinic_id);
        jk.put(js);
        json.put("clinics",jk);

//        System.out.println(json.toString(6));

        Response staffId = given()
                .headers("Authorization", loginAccess(), "Accept", ContentType.JSON, "Content-Type", "application/json","platform","web")
                .body(json.toString())
                .when().post(baseurl + "/api/v1/user")
                .then().contentType(ContentType.JSON).extract().response();
        System.out.println(json);
        System.out.println(staffId);
        System.out.println(staffId.jsonPath().getString("$"));
        return staffId;


    }

    @Test
    public Response addstaff() {
        JSONObject json = new JSONObject();
        json.put("service_id", "582f1dfe-a8c4-47b2-b4ae-b067ad05b787");
        json.put("company_id", response.jsonPath().getString("company_id"));
        json.put("first_name", faker.name().firstName());
        json.put("last_name", faker.name().lastName());
        json.put("licence_no", faker.number().digits(10));
        json.put("email", faker.internet().emailAddress());
        json.put("phone", faker.number().digits(10));
        json.put("role", "TECHNICIAN");
        json.put("is_company_admin", false);
        json.put("country_code","+91");
        JSONObject js=new JSONObject();
        JSONArray jk=new JSONArray();
        js.put("id", clinic_id);
        jk.put(js);
        json.put("clinics",jk);

//        System.out.println(json.toString(6));

        Response staffId = given()
                .headers("Authorization", loginAccess(), "Accept", ContentType.JSON, "Content-Type", ContentType.JSON,"platform","web")
                .body(json.toString())
                .when().post(baseurl + "/api/v1/user")
                .then().contentType(ContentType.JSON).extract().response();
        System.out.println(staffId.jsonPath().getString("$"));
        return staffId;
    }

    @Test
    public Response addInventory() {
//        UUID uuid = Generators.randomBasedGenerator().generate();
        JSONObject json = new JSONObject();
        json.put("clinic_id", clinic_id);
        json.put("item_category_id", "5bc1c26c-1575-485a-973a-0b96c7e27ea0");
        json.put("item_type_id", "54f0bb04-d3d2-4a14-8976-b6ce677bea4a");
        json.put("proprietary_name", itemName);
        json.put("none_proprietary_name", nonitemName);
        json.put("ingredients", faker.food().ingredient());
        json.put("item_strength_uint", 10);
        json.put("dispensing_uint", 5);
        json.put("qty_of_dispensing_uint", 5);
        json.put("upc", 258462);
        json.put("labeler", 10);
        json.put("item_strength", 25);
//        json.put("company_id", response.jsonPath().getString("company_id"));

//        System.out.println(json.toString(6));
        Response response = given()
//                .headers("Authorization", loginAccess(), "Accept",ContentType.JSON)
                .headers("Accept", ContentType.JSON,
                        "Content-Type", ContentType.JSON,
                        "Authorization", loginAccess(),
                        "platform","web")
                .body(json.toString())
                .when().post(baseurl + "/api/v1/inventory-item")
                .then().statusCode(200).extract().response();

//        System.out.print(response.jsonPath().getString("$"));
        return response;
    }

    @Test
    public void addServiceCode() {
        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject inventories = new JSONObject();

//        System.out.println(addInventory().jsonPath().getString("id"));
        int tax = Integer.parseInt(faker.number().digits(2));
        int serviceCost = Integer.parseInt(faker.number().digits(2));
        int tsc = (serviceCost * tax / 100) + serviceCost;


        inventories.put("item_id", addInventory().jsonPath().getString("id"));
        inventories.put("qty", 10);
        jsonArray.put(inventories);

        json.put("service_category_id", "1b46bd1c-4127-42a2-a00b-ede47159dacc");
        json.put("service_name", faker.stock().nsdqSymbol());
        json.put("description", "details");
        json.put("billing_code", faker.code().asin());
        json.put("service_cost", serviceCost);
        json.put("tax_per", tax);
        json.put("total_service_cost", tsc);
        json.put("inventories", jsonArray);


//        System.out.println(json.toString(6));
        given()
                .headers("Accept", ContentType.JSON,
                        "Content-Type", ContentType.JSON,
                        "Authorization", loginAccess())
                .body(json.toString())
                .when().post(baseurl + "/api/v1/clinic/" + clinic_id + "/treatment-service")
                .then().assertThat().statusCode(200).extract().response();

    }

    @Test
    public String addOwner() {
        JSONObject json = new JSONObject();
        json.put("clinic_id", clinic_id);
        json.put("first_name", faker.name().firstName());
        json.put("last_name", faker.name().lastName());
        json.put("email", faker.internet().emailAddress());
        json.put("phone", faker.number().digits(10));
        json.put("note", "NothingToDisplay");
        json.put("profile_pic", "/home/bosleo/11.png");
        json.put("home_address_line_1", faker.address().streetAddress());
        json.put("home_street", faker.address().streetAddress());
        json.put("home_city", faker.address().cityName());
        json.put("home_state", faker.address().state());
        json.put("home_country", faker.address().country());
        json.put("home_zip", faker.number().digits(5));
        json.put("office_address_line_1", faker.address().streetAddress());
        json.put("office_street", faker.address().streetAddress());
        json.put("office_city", faker.address().cityName());
        json.put("office_state", faker.address().state());
        json.put("office_country", faker.address().country());
        json.put("office_zip", faker.number().digits(5));

//        System.out.println(json.toString(6));
        String ownerid = given()
                .headers("Accept", ContentType.JSON,
                        "Content-Type", ContentType.JSON,
                        "Authorization", loginAccess())
                .body(json.toString())
                .when().post(baseurl + "/api/v1/owner")
                .then().extract().path("id");
//       System.out.println(ownerid.jsonPath().getString("id"));
        return ownerid;
    }

    @Test
    public  String addPet() {
        JSONObject json = new JSONObject();
        json.put("owner_id", addOwner());
        json.put("name", faker.cat().name());
        json.put("species_master_id", "3f8f1c57-eb60-430a-bcbb-60cf6f033218");
        json.put("gender_master_id", "2d08239d-e223-488f-a30f-c0003c1a4c02");
        json.put("breed_master_id", "c14b4ae3-b2fb-4ab1-8c9d-326b89e71cee");
        json.put("color_master_id", "5c59d2ba-10b8-4188-b84e-10f7569bb768");
        json.put("use", "home");
        json.put("date_of_birth", "20-10-1990");
        json.put("age", "280");
        json.put("micro_chip", "micro");
        json.put("license", faker.number().digits(5));
        json.put("breed_reg", faker.cat().breed());
        json.put("note", "Pet is serious");
        json.put("location_type", "Home");
        json.put("stable_barn_number", faker.number().digits(5));
        json.put("cage_box_number", faker.number().digits(5));
        json.put("address_line_1", faker.address().streetAddress());
        json.put("street", faker.address().streetAddress());
        json.put("city", faker.address().cityName());
        json.put("state", faker.address().state());
        json.put("country", faker.address().country());
        json.put("zip", faker.number().digits(3));
//        json.put("lat", faker.number().randomDouble(2, 2, 4));
//        json.put("long", faker.number().randomDouble(2, 2, 4));
        json.put("lat", faker.address().latitude());
        json.put("long", faker.address().longitude());


//        System.out.println(json.toString(6));
        String petid = given()
                .headers("Accept", ContentType.JSON,
                        "Content-Type", ContentType.JSON,
                        "Authorization", loginAccess())
                .body(json.toString())
                .when().post(baseurl + "/api/v1/pet")
                .then().assertThat().statusCode(200).extract().path("pet.id");
//        System.out.println(petid.jsonPath().getString("$"));
        return petid;

    }

    @Test
    public void addAppointment() {
        JSONObject json = new JSONObject();
        JSONObject ownerData = new JSONObject();
        JSONObject petData = new JSONObject();

        json.put("name","Appointment");
        json.put("from", "2018-04-07 10:00:00");
        json.put("to", "2018-04-07 12:00:00");
        json.put("appointment_type_id", "c3628cf9-dc11-4dcb-9e8f-12352548bceb");
        json.put("appointment_status", "accepted");
        json.put("user_id", response.jsonPath().getString("user_id"));
        json.put("clinic_id", clinic_id);
        json.put("is_warning_displayed", "false");
        ownerData.put("id", addOwner());
        petData.put("id", addPet());
        json.put("owner_data",ownerData);
        json.put("pet_data",petData);
//        System.out.println(json.toString(4));
        String result = given()
                        .headers("Accept", ContentType.JSON,
                        "Content-Type", ContentType.JSON,
                        "Authorization", loginAccess())
                .body(json.toString())
                .when().post(baseurl + "/api/v1/event/appointment")
                .then().assertThat().statusCode(200).extract().path("event_id");
        System.out.println(result);
//        return result;
    }

    @Test
    public String addEvent(){
        JSONObject event = new JSONObject();
        event.put("name", "Event");
        event.put("from", "2018-04-07 10:00:00");
        event.put("to", "2018-04-07 12:00:00");
        event.put("description", "Event");
        event.put("user_id", response.jsonPath().getString("user_id"));
        event.put("clinic_id", clinic_id);
        event.put("is_notify_me", 0);
        event.put("is_warning_displayed", "true");
//                "is_warning_displayed", true)
//        System.out.println(event.toString(4));
        String result1 = given()
                .headers("Accept", ContentType.JSON,
                        "Content-Type", ContentType.JSON,
                        "Authorization", loginAccess())
                .body(event.toString())
                .when().post(baseurl + "/api/v1/event/general-event")
                .then().assertThat().statusCode(200).extract().path("$");
        System.out.println(result1);
        return result1;
    }

    @Test
    public Response getEvent(){
        System.out.println(baseurl + "/api/v1/event/"+result1);
        Response r2 = given().headers("Accept", ContentType.JSON,
//                "Content-Type", ContentType.JSON,
                "Authorization", loginAccess())
//                .param("event_id",result1)
                .body(addEvent().toString())
                .when().get(baseurl + "/api/v1/event/"+result1)
                .then().assertThat().statusCode(200).extract().response();
                 System.out.println(r2.jsonPath().getString("$"));
        return r2;
    }
}





