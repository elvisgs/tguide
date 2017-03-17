package br.com.tguide.domain;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class WeatherRepository {

    private List<String> weathers = new ArrayList<>();
    private Random rand = new Random();

    public WeatherRepository() {
        weathers.addAll(Arrays.asList(
                "Nublado", "Parcialmente nublado", "Chuvoso", "Ensolarado", "Sol entre nuvens",
                "Céu limpo", "Tempestade"
        ));
    }

    public String getWeatherForPosition(LatLng latLng) {
        String weather = weathers.get(rand.nextInt(weathers.size()));
        int temperature = 25 + rand.nextInt(13);

        return String.format("%d° - %s", temperature, weather);
    }
}
