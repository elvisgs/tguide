package br.com.tguide;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.VisibleRegion;

import java.util.List;

import br.com.tguide.domain.PlaceRatingAverage;
import br.com.tguide.domain.PlaceRatingRepository;

import static br.com.tguide.DetailsActivity.GO_LOCATION_RESULT;

public class RatingsFragment extends Fragment implements OnItemClickListener {

    public static final String LAT_LNG_KEY = "latLng";
    private static final int SHOW_DETAILS_REQUEST = 1;
    private PlaceRatingRepository repository = PlaceRatingRepository.getInstance();
    private List<PlaceRatingAverage> averages;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ratings, container, false);
    }

    public void loadRatings(VisibleRegion visibleRegion, Location myLocation) {
        averages = repository.getAveragesBetween(visibleRegion.latLngBounds);

        TextView emptyText = (TextView) getView().findViewById(R.id.emptyText);

        ListView list = (ListView) getView().findViewById(R.id.ratings);
        list.setEmptyView(emptyText);

        RatingsListAdapter adapter = new RatingsListAdapter(getContext(), averages);
        adapter.setMyLocation(myLocation);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PlaceRatingAverage average = averages.get(position);
        Intent intent = new Intent(getContext(), DetailsActivity.class);
        intent.putExtra(LAT_LNG_KEY, average.getLatLng());

        startActivityForResult(intent, SHOW_DETAILS_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == GO_LOCATION_RESULT) {
            LatLng latLng = data.getParcelableExtra(LAT_LNG_KEY);

            TabActivity tabActivity = (TabActivity) getContext();
            tabActivity.showMapTab();

            for (Fragment fragment : getFragmentManager().getFragments()) {
                if (fragment instanceof MapFragment) {
                    ((MapFragment) fragment).showLocation(latLng);
                    break;
                }
            }
        }
    }
}
