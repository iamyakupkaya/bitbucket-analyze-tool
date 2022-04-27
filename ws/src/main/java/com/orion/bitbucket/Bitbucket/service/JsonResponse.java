
package com.orion.bitbucket.Bitbucket.service;

import org.springframework.stereotype.Service;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@Service
public class JsonResponse {

   public HttpResponse<JsonNode> getResponse(String url, String token) throws UnirestException {
      HttpResponse<JsonNode> response = Unirest.get(url)
                                               .header("Accept", "application/json")
                                               .header("Authorization", "Bearer " + token + "")
                                               .asJson();
      
      return response;
   }
}

