package br.com.tguide;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import br.com.tguide.domain.PlaceRatingAverage;

public class RatingsListAdapter extends BaseAdapter {

    private final Context context;
    private final List<PlaceRatingAverage> items;
    private final LayoutInflater inflater;
    private NumberFormat numberFormat = new DecimalFormat("#0.0");

    public RatingsListAdapter(@NonNull Context context, @NonNull List<PlaceRatingAverage> items) {
        this.context = context;
        this.items = items;
        inflater = LayoutInflater.from(context);
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
        TextView placeCoords = (TextView) view.findViewById(R.id.placeCoords);
        TextView rating = (TextView) view.findViewById(R.id.rating);

        PlaceRatingAverage item = items.get(position);
        placeName.setText(item.getPlaceName());
        placeCoords.setText(String.format("%f, %f", item.getLatLng().latitude, item.getLatLng().longitude));
        rating.setText(numberFormat.format(item.getValue()));

        return view;
    }
}
