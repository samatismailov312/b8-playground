package deserialization;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public class Covid19 {
    @Test
    public void getCountriesTest() throws URISyntaxException, IOException {
        //costr client
        HttpClient client = HttpClientBuilder.create().build();
        // constr URL
        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setScheme("http").setPath("v2/countries").setHost("corona.lmao.ninja");
        //costr http method - GET
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        //headers - Accept
        httpGet.setHeader("Accept", "application/json");
        //execute request
        HttpResponse response = client.execute(httpGet);
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        //deserialization
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> parsedResponse = objectMapper.readValue(response.getEntity().getContent(),
                new TypeReference<List<Map<String, Object>>>() {
                });
        System.out.println("total number of countries: " + parsedResponse.size());

        long maxCases = 0;
        String maxCasesCountry = "";
        for (int i = 0; i < parsedResponse.size(); i++) {
            Map<String, Object> countryMap = parsedResponse.get(i);
            String countryName = (String) countryMap.get("country");
            long covidCases = (int) countryMap.get("cases");
            if (covidCases > maxCases) {
                maxCases = covidCases;
                maxCasesCountry = (String) countryMap.get("country");
            }
            // System.out.println("Country name: "+countryName);

        }
        System.out.println("Max cases: " + maxCases + ", country name " + maxCasesCountry);


    }

    @Test
    public void covidTest2() throws IOException, URISyntaxException {
        //costr client
        HttpClient client = HttpClientBuilder.create().build();
        // constr URL
        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setScheme("http").setHost("corona.lmao.ninja").setPath("v2/countries");
        //costr http method - GET
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        //headers - Accept
        httpGet.setHeader("Accept", "application/json");
        //execute request
        HttpResponse response = client.execute(httpGet);
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        //validate if response body is in JSON format
        Assert.assertTrue(response.getEntity().getContentType().getValue().contains("json"));

        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> countryList = objectMapper.readValue(response.getEntity().getContent(),
                new TypeReference<List<Map<String, Object>>>() {
                });
        for (int i = 0; i < countryList.size(); i++) {
            Map<String, Object> countryMap = countryList.get(i);
            Map<String, Object> countryInfo = (Map<String, Object>) countryMap.get("countryInfo");
            String countryCode = " ";
            String countryName = (String) countryMap.get("country");
            try {
                countryCode = countryInfo.get("iso3").toString();
            } catch (NullPointerException e) {
            }
            if (countryCode == null) {
                System.out.println(countryName + ": doesn't have country code");
            } else {
                System.out.println(countryName + " code is: " + countryCode);
            }
        }
    }
}
