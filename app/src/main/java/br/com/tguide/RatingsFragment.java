package br.com.tguide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.VisibleRegion;

import java.util.List;

import br.com.tguide.domain.PlaceRatingAverage;
import br.com.tguide.domain.PlaceRatingRepository;

public class RatingsFragment extends Fragment {

    PlaceRatingRepository repository = PlaceRatingRepository.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ratings, container, false);
    }

    public void loadRatings(VisibleRegion visibleRegion) {
        List<PlaceRatingAverage> averages = repository
                .getAveragesBetween(visibleRegion.latLngBounds);

        TextView emptyText = (TextView) getView().findViewById(R.id.emptyText);

        ListView list = (ListView) getView().findViewById(R.id.ratings);
        list.setEmptyView(emptyText);

        RatingsListAdapter adapter = new RatingsListAdapter(getContext(), averages);
        list.setAdapter(adapter);
    }

}
