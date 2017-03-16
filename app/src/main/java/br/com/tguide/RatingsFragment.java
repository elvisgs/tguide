package br.com.tguide;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import br.com.tguide.domain.PlaceRatingAverage;
import br.com.tguide.domain.PlaceRatingRepository;

public class RatingsFragment extends ListFragment {

    PlaceRatingRepository repository = PlaceRatingRepository.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ratings, container, false);
        loadRatings();
        return view;
    }

    public void loadRatings() {
        List<PlaceRatingAverage> averages = repository.getAveragesBetween(null, null);

        ArrayAdapter<PlaceRatingAverage> adapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_list_item_1, averages);
        this.setListAdapter(adapter);
    }

}
