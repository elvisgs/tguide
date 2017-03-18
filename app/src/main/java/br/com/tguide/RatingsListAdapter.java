package br.com.tguide;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import br.com.tguide.domain.PlaceRatingAverage;
import br.com.tguide.util.DistanceUtil;

public class RatingsListAdapter extends BaseAdapter {

    private final Context context;
    private final List<PlaceRatingAverage> items;
    private final LayoutInflater inflater;
    private NumberFormat numberFormat = new DecimalFormat("#0.0");
    private Location myLocation;

    public RatingsListAdapter(@NonNull Context context, @NonNull List<PlaceRatingAverage> items) {
        this.context = context;
        this.items = items;
        inflater = LayoutInflater.from(context);
    }

    public void setMyLocation(Location myLocation) {
        this.myLocation = myLocation;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = inflater.inflate(R.layout.ratings_list_item, parent, false);
        } else {
            view = convertView;
        }

        TextView placeName = (TextView) view.findViewById(R.id.placeName);
        TextView placeInfo = (TextView) view.findViewById(R.id.placeInfo);
        TextView rating = (TextView) view.findViewById(R.id.rating);

        PlaceRatingAverage item = items.get(position);
        placeName.setText(item.getPlaceName());
        placeInfo.setText(getInfo(item));
        rating.setText(numberFormat.format(item.getValue()));

        return view;
    }

    private String getInfo(PlaceRatingAverage item) {
        String latLng = String.format(Locale.getDefault(), "(%f, %f)",
                item.getLatLng().latitude, item.getLatLng().longitude);

        Location placeLocation = new Location("rating");
        placeLocation.setLatitude(item.getLatLng().latitude);
        placeLocation.setLongitude(item.getLatLng().longitude);

        String distance = DistanceUtil.formatDistanceBetween(myLocation, placeLocation);

        return latLng + " | " + distance;
    }
}
