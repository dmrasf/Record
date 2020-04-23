package com.dmrasf.record.home.item_day;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.dmrasf.record.R;

import java.text.SimpleDateFormat;
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
        imageView.setImageBitmap(currentDay.getBitmap());

        TextView title = (TextView) itemDetailView.findViewById(R.id.item_day_text_view);

        // 过长的话 裁切显示
        String text = currentDay.getText();
        String m = text;
        if (text != null && text.length() > 10) {
            m = text.substring(0, 8) + " ······";
        }
        title.setText(m);

        TextView date = (TextView) itemDetailView.findViewById(R.id.item_day_date_text_view);
        @SuppressLint("SimpleDateFormat") String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentDay.getDate());
        date.setText(time);

        return itemDetailView;
    }
}