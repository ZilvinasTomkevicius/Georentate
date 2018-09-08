package com.example.zilvinastomkevicius.georentate.APIClients;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactoryClass {

    public static RetrofitFactoryInterface Create() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitFactoryInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(RetrofitFactoryInterface.class);
    }
}
