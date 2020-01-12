package com.galihpw.weatherapps;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    private Context context;
    private List<Weather> list;

    public WeatherAdapter(Context context, List<Weather> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.weather_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Weather weather = list.get(position);

        holder.tvCuaca.setVisibility(View.GONE);
        holder.tvHari.setText(weather.getDate());
        holder.tvHari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.tvCuaca.getVisibility() == View.GONE) {
                    holder.tvCuaca.setVisibility(View.VISIBLE);
                }else{
                    holder.tvCuaca.setVisibility(View.GONE);
                }
            }
        });
        holder.tvCuaca.setText(String.valueOf(weather.getCuaca()));
        holder.tvSuhu.setText(String.valueOf(Math.round(weather.getTemp())) + "°C");

        Glide.with(context)
                .load("http://openweathermap.org/img/w/" + weather.getIcon() + ".png")
                .into(holder.ivIcon);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvHari, tvCuaca, tvSuhu;
        public ImageView ivIcon;

        public ViewHolder(View itemView) {
            super(itemView);

            tvHari = itemView.findViewById(R.id.tvHari);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            tvCuaca = itemView.findViewById(R.id.tvCuaca);
            tvSuhu = itemView.findViewById(R.id.tvSuhu);
        }
    }

}
