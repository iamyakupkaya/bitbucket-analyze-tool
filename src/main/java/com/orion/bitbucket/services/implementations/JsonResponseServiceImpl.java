package com.orion.bitbucket.services.implementations;

import com.orion.bitbucket.services.IJsonResponceService;
import org.springframework.stereotype.Service;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@Service
public class JsonResponseServiceImpl implements IJsonResponceService {

    public HttpResponse<JsonNode> getResponse(String url, String token) throws UnirestException {
        try {
            HttpResponse<JsonNode> response = Unirest.get(url)
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + token + "")
                    .asJson();

            return response;
        }
        catch (Exception err){
            System.out.println("There is a ERROR..!" + err);
            return null;
        }
    }
}