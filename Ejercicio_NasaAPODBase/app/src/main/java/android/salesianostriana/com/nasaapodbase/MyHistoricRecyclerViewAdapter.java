package android.salesianostriana.com.nasaapodbase;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.salesianostriana.com.api.NasaPicture;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import android.salesianostriana.com.nasaapodbase.dummy.DummyContent.DummyItem;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link IPhotosListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyHistoricRecyclerViewAdapter extends RecyclerView.Adapter<MyHistoricRecyclerViewAdapter.ViewHolder> {
    APIError apiError = new APIError();
    Context ctx;
    int layoutPlantilla;
    List<NasaPicture> listData;

    public MyHistoricRecyclerViewAdapter(@NonNull Context context, int resource, @NonNull List<NasaPicture> objects) {
        this.ctx = context;
        this.layoutPlantilla = resource;
        this.listData = objects;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_historic, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = listData.get(position);
        holder.tvDate.setText(holder.mItem.getDate());
        Glide.with(ctx).load(holder.mItem.getUrl()).error(Glide.with(ctx).load(apiError.getUrlError())).into(holder.ivPhoto);

        holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ctx, DetailActivity.class);
                i.putExtra("url", holder.mItem.getUrl());
                i.putExtra("title", holder.mItem.getTitle());
                i.putExtra("explanation", holder.mItem.getExplanation());
                i.putExtra("date", holder.mItem.getDate());
                ctx.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView ivPhoto;
        public final TextView tvDate;
        public NasaPicture mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ivPhoto = view.findViewById(R.id.imageViewPhoto);
            tvDate = view.findViewById(R.id.textViewDate);

        }
    }
}
