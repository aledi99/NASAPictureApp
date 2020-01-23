package android.salesianostriana.com.nasaapodbase;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.salesianostriana.com.api.NasaApi;
import android.salesianostriana.com.api.NasaPicture;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.salesianostriana.com.nasaapodbase.ui.main.SectionsPagerAdapter;
import android.widget.Toast;

import org.joda.time.LocalDate;

public class BaseActivity extends AppCompatActivity implements ISeleccionarFechaListener {
    NasaApi nasaApi = new NasaApi("tH7fKpbpVD9ciN9BMQKqeSc4sDNDIqdDsrBYt0Qg");
    NasaPicture nasaPicture;
    int mDay, mMonth, mYear;
    LocalDate dateSelected;
    LocalDate today = LocalDate.now();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

    }

    public void mostrarCalendario(View view) {
        DialogFragment newFragment = DialogoFechaFragment.newInstance(this);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onFechaSeleccionada(int year, int month, int day) {
        mDay = day;
        mMonth = month + 1;
        mYear = year;

        dateSelected = new LocalDate(mYear, mMonth, mDay);

        if(dateSelected.isAfter(today)) {
            Toast.makeText(this, "La fecha es mayor a la actual.", Toast.LENGTH_SHORT).show();
        } else {
            new LoadImage().execute();
        }

    }

    public class LoadImage extends AsyncTask<Void, Void, NasaPicture> {

        @Override
        protected NasaPicture doInBackground(Void... voids) {
            nasaPicture = nasaApi.getPicOfAnyDate(dateSelected.toString());

            return nasaPicture;
        }

        @Override
        protected void onPostExecute(NasaPicture nasaPicture) {
            Intent i = new Intent(BaseActivity.this, CalendarDetailActivity.class);
            i.putExtra("url", nasaPicture.getUrl());
            i.putExtra("title", nasaPicture.getTitle());
            i.putExtra("explanation", nasaPicture.getExplanation());
            i.putExtra("date", nasaPicture.getDate());
            startActivity(i);
        }
    }
}