package com.dmrasf.record.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.dmrasf.record.R;

import java.util.ArrayList;

public class ItemDayAdapter extends ArrayAdapter<Day> {

    public ItemDayAdapter(@NonNull Context context, ArrayList<Day> days) {
        super(context, 0, days);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemDetailView = convertView;

        if (itemDetailView == null) {
            itemDetailView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_day, parent, false);
        }

        Day currentDay = getItem(position);

        ImageView imageView = (ImageView) itemDetailView.findViewById(R.id.item_day_image_view);
        imageView.setImageResource(currentDay.getDayImage());

        TextView title = (TextView) itemDetailView.findViewById(R.id.item_day_text_view);
        title.setText(currentDay.getTitle());

        TextView date = (TextView) itemDetailView.findViewById(R.id.item_day_date_text_view);
        date.setText(String.valueOf(currentDay.getDate()));

        return itemDetailView;
    }
}