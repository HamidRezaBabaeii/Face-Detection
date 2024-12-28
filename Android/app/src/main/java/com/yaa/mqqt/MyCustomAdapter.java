package com.yaa.mqqt;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

public class MyCustomAdapter extends BaseAdapter implements ListAdapter {
    private List<String> list;
    private Context context;
    private int resource;

    public MyCustomAdapter(Context context, int resource, List<String> list) {
        this.list = list;
        this.context = context;
        this.resource = resource;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resource, null);
        }
        String data = getItem(position);

        if (data != null && data.length() > 1) {
            Log.d("mqtt", data);
            String[] incommingData = data.split(",");

            String state = incommingData[2].trim();
            int value = Integer.parseInt(state);
            if (value == 200) {
                view.setBackground(context.getDrawable(R.drawable.theme_green));
            } else if (value == 400) {
                view.setBackground(context.getDrawable(R.drawable.theme_red));
            } else if (value == 500) {
                view.setBackground(context.getDrawable(R.drawable.theme_gray));
            }

            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            TextView textName = (TextView) view.findViewById(R.id.name);
            TextView textMessage = (TextView) view.findViewById(R.id.message);

            String name = incommingData[0].toLowerCase().trim();
            switch (name){
                case "hello": imageView.setImageResource(R.drawable.ic_launcher_foreground); break;
                case "hamid": imageView.setImageResource(R.drawable.hamid); break;
                case "tomcruise": imageView.setImageResource(R.drawable.tom); break;
                case "meganfox": imageView.setImageResource(R.drawable.megan); break;
                case "willsmith": imageView.setImageResource(R.drawable.will); break;
            }
            textName.setText(incommingData[0]);
            textMessage.setText(incommingData[1]);
        }
        return view;
    }
}