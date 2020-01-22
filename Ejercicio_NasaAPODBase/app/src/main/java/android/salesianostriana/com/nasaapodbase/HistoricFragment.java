package android.salesianostriana.com.nasaapodbase;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.salesianostriana.com.api.NasaApi;
import android.salesianostriana.com.api.NasaPicture;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.salesianostriana.com.nasaapodbase.dummy.DummyContent;
import android.salesianostriana.com.nasaapodbase.dummy.DummyContent.DummyItem;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link IPhotosListener}
 * interface.
 */
public class HistoricFragment extends Fragment {
    APIError apiError = new APIError();
    Context mContext;
    int mColumnCount = 2;
    RecyclerView recyclerView;
    IPhotosListener mListener;
    GridView gvHistoric;
    TextView tvtError;
    ImageView ivError;
    NasaApi nasaApi = new NasaApi("tH7fKpbpVD9ciN9BMQKqeSc4sDNDIqdDsrBYt0Qg");
    List<NasaPicture> photos = new ArrayList<NasaPicture>();
    LocalDate today = LocalDate.now();
    LocalDate timeBefore = today.minusDays(30);

    public HistoricFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historic_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();

            recyclerView = (RecyclerView) view;

            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            tvtError = view.findViewById(R.id.textViewError);
            ivError = view.findViewById(R.id.imageViewError);

            //tvtError.setVisibility(View.GONE);
            //ivError.setVisibility(View.GONE);

            new GetHistorics().execute();

        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IPhotosListener) {
            mListener = (IPhotosListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " el Activity no implementa la interface IPhotosListener");
        }
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

            if(nasaPictures == null) {
                tvtError.setVisibility(View.VISIBLE);
                ivError.setVisibility(View.VISIBLE);

                tvtError.setText(apiError.getTextError());
                Glide.with(mContext).load(apiError.getUrlError()).into(ivError);
            } else {
                Collections.reverse(s);
                MyHistoricRecyclerViewAdapter photosAdapter = new MyHistoricRecyclerViewAdapter(mContext, R.layout.recycler_historic, s);
                recyclerView.setAdapter(photosAdapter);
            }

        }
    }
}
