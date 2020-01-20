package android.salesianostriana.com.nasaapodbase;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.salesianostriana.com.api.NasaApi;
import android.salesianostriana.com.api.NasaPicture;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


public class HomeFragment extends Fragment {
    ImageView ivPhoto;
    TextView tvtTitle;
    TextView tvtDescription;
    TextView tvtDate;
    NasaApi nasaApi = new NasaApi("tH7fKpbpVD9ciN9BMQKqeSc4sDNDIqdDsrBYt0Qg");
    NasaPicture photo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ivPhoto = view.findViewById(R.id.imageViewPhoto);
        tvtTitle = view.findViewById(R.id.textViewTitle);
        tvtDescription = view.findViewById(R.id.textViewDescr);
        tvtDate = view.findViewById(R.id.textViewDate);


        new PhotoToday().execute();

        return view;
    }

    public class PhotoToday extends AsyncTask<Void, Void, NasaPicture> {

        @Override
        protected NasaPicture doInBackground(Void... voids) {
            photo = nasaApi.getPicOfToday();

            return photo;
        }

        @Override
        protected void onPostExecute(NasaPicture nasaPicture) {
            tvtTitle.setText(nasaPicture.getTitle());
            tvtDescription.setText(nasaPicture.getExplanation());
            tvtDate.setText(nasaPicture.getDate());

            Glide.with(HomeFragment.this).load(nasaPicture.getUrl()).into(ivPhoto);


        }
    }
}
