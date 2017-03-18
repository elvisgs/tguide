package br.com.tguide.util;

import android.location.Location;

import java.util.Locale;

public class DistanceUtil {

    public static String formatDistanceBetween(Location start, Location dest) {
        return formatDistance(start.distanceTo(dest));
    }

    public static String formatDistance(float distance) {
        String unit = "m";
        if (distance >= 1000) {
            distance /= 1000;
            unit = "km";
        }

        return String.format(Locale.getDefault(), "~%.1f%s", distance, unit);
    }
}
