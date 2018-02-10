package com.shrikanthravi.newslly.data.model.remote;

/**
 * Created by shrikanthravi on 13/12/17.
 */


public class ApiUtils {

    private ApiUtils() {}


    public static final String BASE_URL = "https://newsapi.org/v2/";

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }

}
