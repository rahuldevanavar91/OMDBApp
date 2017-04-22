package android.com.omdb.activity;

import android.com.omdb.R;
import android.com.omdb.model.MovieResponse;
import android.com.omdb.network.Request;
import android.com.omdb.network.VolleyResponseListener;
import android.com.omdb.network.VolleySingleton;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Rahul D on 4/22/17.
 */

public class ResultDetailActivity extends AppCompatActivity implements VolleyResponseListener {

    private ImageView mPosterImage;
    private TextView mTitle;
    private TextView mReleaseYear;
    private TextView mCast;
    private TextView mPlot;
    private ProgressBar mProgress;

    private Request mRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_detail_result);
        getWidgets();
        requestForMovieDetail();
    }

    private void requestForMovieDetail() {
        if (mRequest == null) {
            mRequest = new Request();
        }
        mProgress.setVisibility(View.VISIBLE);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("i", getIntent().getStringExtra(getString(R.string.movie_id)));
        mRequest.getRequest(getBaseContext(), Request.OMDB_URL, hashMap, this);
    }

    private void getWidgets() {
        mPosterImage = (ImageView) findViewById(R.id.movie_image);
        mTitle = (TextView) findViewById(R.id.movie_title);
        mReleaseYear = (TextView) findViewById(R.id.year);
        mCast = (TextView) findViewById(R.id.cast);
        mPlot = (TextView) findViewById(R.id.plot);
        mProgress = (ProgressBar) findViewById(R.id.progress_bar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getIntent().getStringExtra(getString(R.string.title)));
        }
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {
        mProgress.setVisibility(View.GONE);
        MovieResponse movieResponse = new Gson().fromJson(jsonObject.toString(), MovieResponse.class);
        if (movieResponse != null && movieResponse.getResponse().equalsIgnoreCase("True")) {
            updateUI(movieResponse);
        } else {
            Toast.makeText(getBaseContext(), movieResponse.getError(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onError(VolleyError volleyError) {
        mProgress.setVisibility(View.GONE);
        Toast.makeText(getBaseContext(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
    }

    private void updateUI(MovieResponse movieResponse) {
        if (movieResponse.getPoster() != null) {
            mPosterImage.setVisibility(View.VISIBLE);

            VolleySingleton.getInstance(getBaseContext()).getImageLoader().get(movieResponse.getPoster(),
                    new ImageLoader.ImageListener() {
                        @Override
                        public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                            if (response.getBitmap() != null) {
                                mPosterImage.setImageBitmap(response.getBitmap());
                            }
                        }

                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
        } else {
            mPosterImage.setVisibility(View.GONE);
        }

        mTitle.setText("Title : " + movieResponse.getTitle());
        mReleaseYear.setText("Year : " + movieResponse.getYear());
        mCast.setText("Cast : " + movieResponse.getActors());
        mPlot.setText("Plot : " + movieResponse.getPlot());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }
}
