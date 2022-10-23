package com.onlinestore.stockmangement.integration;

import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.onlinestore.stockmangement.StockMangementApplication;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = StockMangementApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {

	@LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();
    
    /**
     * Test 1: petición a las 10:00 del día 14 del producto 35455 para la brand 1
     * 
     * There is a only one price complies the search parameters and it's returned (Price= 35.5)
     * 
     * @throws JSONException
     */
    @Test
    public void test1() throws JSONException {

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                buildUrl("/prices/brandId/1/productId/35455/date/2020-06-14T10:00:00"),
                HttpMethod.GET, entity, String.class);

        String expected = "{"
        		+ "\"brandId\":1,"
        		+ "\"productId\":35455,"
        		+ "\"appliedRate\":1,"
        		+ "\"startDate\":\"2020-06-14T00:00:00\","
        		+ "\"endDate\":\"2020-12-31T23:59:59\","
        		+ "\"price\":35.5"
        		+ "}";
        
        JSONAssert.assertEquals(expected, response.getBody(), false);
        Assertions.assertEquals(200, response.getStatusCodeValue());
    }
    
    /**
     * Test 2: petición a las 16:00 del día 14 del producto 35455 para la brand 1
     * 
     * There are two prices comply the search parameters and highest priority price is returned (Price= 25.45)
     * 
     * @throws JSONException
     */
    @Test
    public void test2() throws JSONException {

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                buildUrl("/prices/brandId/1/productId/35455/date/2020-06-14T16:00:00"),
                HttpMethod.GET, entity, String.class);

        String expected = "{"
        		+ "\"brandId\":1,"
        		+ "\"productId\":35455,"
        		+ "\"appliedRate\":2,"
        		+ "\"startDate\":\"2020-06-14T15:00:00\","
        		+ "\"endDate\":\"2020-06-14T18:30:00\","
        		+ "\"price\":25.45"
        		+ "}";
        
        JSONAssert.assertEquals(expected, response.getBody(), false);
        Assertions.assertEquals(200, response.getStatusCodeValue());
    }
    
    /**
     * Test 3: petición a las 21:00 del día 14 del producto 35455 para la brand 1
     * 
     * There is a only one price complies the search parameters and it's returned (Price= 35.5)
     * 
     * @throws JSONException
     */
    @Test
    public void test3() throws JSONException {

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                buildUrl("/prices/brandId/1/productId/35455/date/2020-06-14T21:00:00"),
                HttpMethod.GET, entity, String.class);

        String expected = "{"
        		+ "\"brandId\":1,"
        		+ "\"productId\":35455,"
        		+ "\"appliedRate\":1,"
        		+ "\"startDate\":\"2020-06-14T00:00:00\","
        		+ "\"endDate\":\"2020-12-31T23:59:59\","
        		+ "\"price\":35.5"
        		+ "}";
        
        JSONAssert.assertEquals(expected, response.getBody(), false);
        Assertions.assertEquals(200, response.getStatusCodeValue());
    }
    
    /**
     * Test 4: petición a las 10:00 del día 15 del producto 35455 para la brand 1)
     * 
     * There are two prices comply the search parameters and highest priority price is returned (Price= 30.50)
     * 
     * @throws JSONException
     */
    @Test
    public void test4() throws JSONException {

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                buildUrl("/prices/brandId/1/productId/35455/date/2020-06-15T10:00:00"),
                HttpMethod.GET, entity, String.class);

        String expected = "{"
        		+ "\"brandId\":1,"
        		+ "\"productId\":35455,"
        		+ "\"appliedRate\":3,"
        		+ "\"startDate\":\"2020-06-15T00:00:00\","
        		+ "\"endDate\":\"2020-06-15T11:00:00\","
        		+ "\"price\":30.50"
        		+ "}";
        
        JSONAssert.assertEquals(expected, response.getBody(), false);
        Assertions.assertEquals(200, response.getStatusCodeValue());
    }
    
    /**
     * Test 5: petición a las 21:00 del día 16 del producto 35455   para la brand 1
     * 
     * There are two prices comply the search parameters and highest priority price is returned (Price= 38.95)
     * 
     * @throws JSONException
     */
    @Test
    public void test5() throws JSONException {

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                buildUrl("/prices/brandId/1/productId/35455/date/2020-06-16T21:00:00"),
                HttpMethod.GET, entity, String.class);

        String expected = "{"
        		+ "\"brandId\":1,"
        		+ "\"productId\":35455,"
        		+ "\"appliedRate\":4,"
        		+ "\"startDate\":\"2020-06-15T16:00:00\","
        		+ "\"endDate\":\"2020-12-31T23:59:59\","
        		+ "\"price\":38.95"
        		+ "}";
        
        JSONAssert.assertEquals(expected, response.getBody(), false);
        Assertions.assertEquals(200, response.getStatusCodeValue());
    }
    
    
    ///// Another use cases not requested /////
    /**
     * Test 6: petición a las 13:00 del día 10 del producto 35455 para la brand 1
     * 
     * ERROR: There are two prices comply the search parameters but both are the same priority. BAD_REQUEST (400) returns
     * 
     * @throws JSONException
     */
    @Test
    public void test6() throws JSONException {

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                buildUrl("/prices/brandId/1/productId/35455/date/2020-06-10T13:00:00"),
                HttpMethod.GET, entity, String.class);

        String expected = "[{"
        		+ "\"code\":-3,"
        		+ "\"errorMessage\":\"there is several prices overlap in time with the same priority (brandId:'1', productId:'35455', date:'2020-06-10T13:00')\""
        		+ "}]";
        
        JSONAssert.assertEquals(expected, response.getBody(), false);
        Assertions.assertEquals(400, response.getStatusCodeValue());
    }
    
    /**
     * Test 7: petición a las 13:00 del día 5 del producto 35455 para la brand 1
     * 
     * ERROR: There are no any prices complies with search parameters. NO_CONTENT (204) returns
     * 
     * @throws JSONException
     */
    @Test
    public void test7() throws JSONException {

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                buildUrl("/prices/brandId/1/productId/35455/date/2020-06-05T13:00:00"),
                HttpMethod.GET, entity, String.class);
        
        Assertions.assertEquals(204, response.getStatusCodeValue());
    }
    
    
    private String buildUrl(String uri) {
        return "http://localhost:" + port + uri;
    }
}
