package br.com.tguide;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import br.com.tguide.domain.PlaceRating;

public class DetailsListAdapter extends BaseAdapter {

    private final List<PlaceRating> items;
    private final LayoutInflater inflater;
    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy hh:mm", Locale.getDefault());
    private Resources resources;

    public DetailsListAdapter(@NonNull Context context, @NonNull List<PlaceRating> items) {
        this.items = items;
        inflater = LayoutInflater.from(context);
        resources = context.getResources();
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
            view = inflater.inflate(R.layout.details_list_item, parent, false);
        } else {
            view = convertView;
        }

        TextView dateTime = (TextView) view.findViewById(R.id.dateTime);
        TextView rating = (TextView) view.findViewById(R.id.rating);
        TextView comment = (TextView) view.findViewById(R.id.comment);

        PlaceRating item = items.get(position);
        dateTime.setText(dateFormat.format(item.getCollectedAt()));
        rating.setText(String.format(Locale.getDefault(), "%.1f", item.getValue()));
        comment.setText(formatComment(item.getComment()));

        return view;
    }

    private String formatComment(String comment) {
        if (comment != null && !"".equals(comment.trim()))
            return comment;

        return resources.getString(R.string.no_comment);
    }
}
