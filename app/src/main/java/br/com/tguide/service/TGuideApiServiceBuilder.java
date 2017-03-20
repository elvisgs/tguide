package br.com.tguide.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.Date;

import br.com.tguide.domain.PlaceRating;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class TGuideApiServiceBuilder {

    public TGuideApi build() {
        String apiUrl = "http://lowcost-env.pwpn25tq37.us-west-2.elasticbeanstalk.com";

        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(PlaceRating.class, new LatLngDeserializer());
        mapper.registerModule(module);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiUrl)
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .build();

        return retrofit.create(TGuideApi.class);
    }

    private static class LatLngDeserializer extends JsonDeserializer<PlaceRating> {

        @Override
        public PlaceRating deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode node = p.getCodec().readTree(p);
            double latitude = node.get("latitude").asDouble();
            double longitude = node.get("longitude").asDouble();

            return new PlaceRating(
                    new LatLng(latitude, longitude),
                    node.get("placeName").asText(),
                    node.get("value").floatValue(),
                    node.get("comment").asText(),
                    new Date(node.get("collectedAt").asLong())
            );
        }
    }
}
