package android.salesianostriana.com.nasaapodbase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class CalendarDetailActivity extends AppCompatActivity {
    APIError apiError = new APIError();
    ImageView ivPhoto;
    TextView tvtTitle;
    TextView tvtExplanation;
    TextView tvtDate;

    String url, title, explanation, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_detail);

        ivPhoto = findViewById(R.id.imageViewPhoto);
        tvtTitle = findViewById(R.id.textViewTitle);
        tvtExplanation = findViewById(R.id.textViewDescr);
        tvtDate = findViewById(R.id.textViewDate);

        url = getIntent().getExtras().getString("url");
        title = getIntent().getExtras().getString("title");
        explanation = getIntent().getExtras().getString("explanation");
        date = getIntent().getExtras().getString("date");

        tvtTitle.setText(title);
        tvtDate.setText(date);
        tvtExplanation.setText(explanation);
        tvtExplanation.setMovementMethod(new ScrollingMovementMethod());

        Glide.with(this).load(url).error(Glide.with(this).load(apiError.getUrlError())).into(ivPhoto);

    }
}
