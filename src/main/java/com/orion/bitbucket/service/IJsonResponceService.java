package com.orion.bitbucket.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;

public interface IJsonResponceService {

    public abstract HttpResponse<JsonNode> getResponse(String url, String token) throws UnirestException;
}
