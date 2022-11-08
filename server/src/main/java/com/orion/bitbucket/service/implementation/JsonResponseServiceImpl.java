package com.orion.bitbucket.service.implementation;

import com.orion.bitbucket.helper.LogHelper;
import com.orion.bitbucket.service.IJsonResponceService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@Log4j2
@Service
public class JsonResponseServiceImpl implements IJsonResponceService {

    public HttpResponse<JsonNode> getResponse(String url, String token) throws UnirestException {
        try {
            if (LogHelper.IS_BASE_LOGGING){
                log.info("getResponse method in JsonResponseServiceImpl class invoked for {} url.", url);
            }
            HttpResponse<JsonNode> response = Unirest.get(url)
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + token + "")
                    .asJson();

            return response;
        }
        catch (Exception err){
            if (LogHelper.IS_BASE_LOGGING){
                log.error("There is an error in getResponse method in JsonResponseServiceImpl class. Error: {}",err );
            }
            return null;
        }
        finally {
            if (LogHelper.IS_BASE_LOGGING){
                log.info("getResponse method in JsonResponseServiceImpl class executing has finished");
            }
        }
    }
}