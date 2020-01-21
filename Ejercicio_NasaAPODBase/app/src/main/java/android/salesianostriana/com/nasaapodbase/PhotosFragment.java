package android.salesianostriana.com.nasaapodbase;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.salesianostriana.com.api.NasaApi;
import android.salesianostriana.com.api.NasaPicture;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.GridView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class PhotosFragment extends Fragment {
    Context mContext;
    GridView gvHistoric;
    NasaApi nasaApi = new NasaApi("tH7fKpbpVD9ciN9BMQKqeSc4sDNDIqdDsrBYt0Qg");
    List<NasaPicture> photos = new ArrayList<NasaPicture>();
    LocalDate today = LocalDate.now();
    LocalDate timeBefore = today.minusDays(30);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_photos, container, false);

       gvHistoric = view.findViewById(R.id.gvHistoric);

       new GetHistorics().execute();

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public class GetHistorics extends AsyncTask<Void, Void, List<NasaPicture>> {


        @Override
        protected List<NasaPicture> doInBackground(Void... voids) {
            photos = nasaApi.getPicOfDateInterval(timeBefore.toString(), today.toString());
            return photos;
        }

        @Override
        protected void onPostExecute(List<NasaPicture> nasaPictures) {
            List<NasaPicture> s = nasaPictures;
            Collections.sort(s, Collections.<NasaPicture>reverseOrder());
            PhotosAdapter photosAdapter = new PhotosAdapter(mContext, R.layout.list_historic, s);
            gvHistoric.setAdapter(photosAdapter);
        }
    }
}
