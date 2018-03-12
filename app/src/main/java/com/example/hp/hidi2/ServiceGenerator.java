package com.example.hp.hidi2;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by HP on 23-Feb-18.
 */

public class ServiceGenerator {
    private static final String BASE_URL = "http://hidi.org.in/hidi/account/";
    private static final String BASE_URL1 = "http://hidi.org.in/hidi/post/";
    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());
    private static Retrofit.Builder builder1 =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL1)
                    .addConverterFactory(GsonConverterFactory.create());
    private static Retrofit retrofit = builder.build();
    private static Retrofit retrofit1 = builder1.build();
    private static OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder();

    public static <S> S createService(
            Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
    public static <S> S createService1(
            Class<S> serviceClass) {
        return retrofit1.create(serviceClass);
    }
}
