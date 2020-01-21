package android.salesianostriana.com.nasaapodbase;

import android.content.Context;
import android.salesianostriana.com.api.NasaPicture;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.List;

public class PhotosAdapter extends ArrayAdapter<NasaPicture> {
    Context ctx;
    int layoutPlantilla;
    List<NasaPicture> listData;

    public PhotosAdapter(@NonNull Context context, int resource, @NonNull List<NasaPicture> objects) {
        super(context, resource, objects);
        this.ctx = context;
        this.layoutPlantilla = resource;
        this.listData = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = LayoutInflater.from(ctx).inflate(layoutPlantilla, parent, false);

        ImageView ivPhoto = v.findViewById(R.id.imageViewPhoto);
        TextView tvDate = v.findViewById(R.id.textViewDate);

        NasaPicture dataSelected = listData.get(position);

        String date = dataSelected.getDate();
        String url = dataSelected.getUrl();

        tvDate.setText(date);

        Glide.with(ctx).load(url).into(ivPhoto);


        return v;
    }
}
