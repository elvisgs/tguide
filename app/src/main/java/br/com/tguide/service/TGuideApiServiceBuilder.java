package br.com.tguide.service;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class TGuideApiServiceBuilder {

    public TGuideApi build() {
        String apiUrl = "http://lowcost-env.pwpn25tq37.us-west-2.elasticbeanstalk.com";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiUrl)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        return retrofit.create(TGuideApi.class);
    }
}
