package android.salesianostriana.com.nasaapodbase;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.salesianostriana.com.api.NasaApi;
import android.salesianostriana.com.api.NasaPicture;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.salesianostriana.com.nasaapodbase.dummy.DummyContent;
import android.salesianostriana.com.nasaapodbase.dummy.DummyContent.DummyItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

    int count1 = 0;
    int count2 = 1;
    int mColumnCount = 2;
    boolean isScrolling = false;
    int currentItems, totalItems, scrollOutItems;


    MyHistoricRecyclerViewAdapter photosAdapter;
    LinearLayoutManager manager;
    RecyclerView recyclerView;
    IPhotosListener mListener;
    NasaApi nasaApi = new NasaApi("tH7fKpbpVD9ciN9BMQKqeSc4sDNDIqdDsrBYt0Qg");
    List<NasaPicture> photos = new ArrayList<NasaPicture>();
    LocalDate today = LocalDate.now();
    LocalDate timeBefore = today.minusMonths(count2);
    List<NasaPicture> s = new ArrayList<NasaPicture>();
    ProgressDialog progressDialog;
    Context context;

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
        view.setBackgroundColor(getResources().getColor(R.color.colorAccent));


        if (view instanceof RecyclerView) {
             context = view.getContext();

            recyclerView = (RecyclerView) view;

            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }


            //Constancia de intentar dar a entender al usuario desde este tab que habia un error de
            //la API.
            //tvtError.setVisibility(View.GONE);
            //ivError.setVisibility(View.GONE);

            manager = new GridLayoutManager(context, mColumnCount);

            new GetHistorics().execute();

        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
            Collections.reverse(nasaPictures);
            s.addAll(nasaPictures);


                photosAdapter = new MyHistoricRecyclerViewAdapter(mContext, R.layout.recycler_historic, s);
                recyclerView.setAdapter(photosAdapter);
                recyclerView.setLayoutManager(manager);

                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                            isScrolling = true;
                        }
                    }

                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        currentItems = manager.getChildCount();
                        totalItems = manager.getItemCount();
                        scrollOutItems = manager.findFirstVisibleItemPosition();

                        if(isScrolling && (currentItems + scrollOutItems == totalItems)) {
                            isScrolling = false;
                            count1 = count1++;
                            count2 = count2++;

                            today = timeBefore.minusMonths(count1).minusDays(1);
                            timeBefore = timeBefore.minusMonths(count2);
                            progressDialog = ProgressDialog.show(context, "Aviso","Cargando...", true);
                            progressDialog.getWindow().setBackgroundDrawableResource(R.color.text_color);


                            new LoadMore().execute();
                    }
                    }
                });





        }
    }

    public class LoadMore extends AsyncTask<Void, Void, List<NasaPicture>> {

        @Override
        protected List<NasaPicture> doInBackground(Void... voids) {
            photos = nasaApi.getPicOfDateInterval(timeBefore.toString(), today.toString());
            return photos;
        }

        @Override
        protected void onPostExecute(List<NasaPicture> nasaPictures) {
            Collections.reverse(nasaPictures);
            s.addAll(nasaPictures);
            photosAdapter.notifyDataSetChanged();
            progressDialog.dismiss();
        }
    }
}
