package android.com.omdb.activity;

import android.com.omdb.R;
import android.com.omdb.adapter.SearchAdapter;
import android.com.omdb.model.SearchItem;
import android.com.omdb.model.SearchResponse;
import android.com.omdb.network.Request;
import android.com.omdb.network.VolleyResponseListener;
import android.com.omdb.util.OnItemClickListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Rahul D on 4/22/17.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener, VolleyResponseListener, OnItemClickListener {


    private EditText mSearchEditText;
    private Request mRequest;
    private ArrayList<SearchItem> mSearchList;
    private RecyclerView mSearchRecyclerView;
    private TextView mMessageTextView;
    private ProgressBar mProgressBar;

    private SearchAdapter mSearchAdapter;
    private int mPageNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWidgets();
    }

    private void getWidgets() {
        mSearchEditText = (EditText) findViewById(R.id.query_edit_text);
        Button searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(this);
        mSearchRecyclerView = (RecyclerView) findViewById(R.id.search_list);
        mSearchRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        mMessageTextView = (TextView) findViewById(R.id.message);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        searchButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_button:
                if (mSearchEditText.getText() != null && !mSearchEditText.getText().toString().trim().isEmpty()) {
                    mSearchList = null;
                    mSearchRecyclerView.setVisibility(View.GONE);
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(mSearchEditText.getWindowToken(), 0);
                    mMessageTextView.setText("");
                    requestForSearch();
                } else {
                    Toast.makeText(getBaseContext(), "Please enter movie name", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void requestForSearch() {
        if (mRequest == null) {
            mRequest = new Request();
        }
        if (mSearchList == null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("s", mSearchEditText.getText().toString().trim());
        hashMap.put("page", String.valueOf(mPageNumber));
        mRequest.getRequest(getBaseContext(), Request.OMDB_URL, hashMap, this);
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {
        SearchResponse movieResponse = new Gson().fromJson(jsonObject.toString(), SearchResponse.class);
        mProgressBar.setVisibility(View.GONE);
        if (movieResponse != null && movieResponse.getResponse().equalsIgnoreCase("true")) {
            setAdapter(movieResponse.getSearch(), movieResponse.getTotalResults());
        } else {
            setEmptyResult(movieResponse.getError());
        }
    }


    @Override
    public void onError(VolleyError volleyError) {
        setEmptyResult(volleyError.getMessage());
    }


    private void setEmptyResult(String message) {
        mProgressBar.setVisibility(View.GONE);
        if (mSearchList != null && !mSearchList.isEmpty()) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        } else {
            mSearchRecyclerView.setVisibility(View.GONE);
            mMessageTextView.setText(message);
        }
    }


    private void setAdapter(ArrayList<SearchItem> search, String totalResults) {
        if (mSearchList != null) {
            if (mSearchList.get(mSearchList.size() - 1).getViewType() == SearchAdapter.VIEW_TYPE_MORE_LOADING) {
                mSearchList.remove(mSearchList.size() - 1);
            }
            mSearchList.addAll(search);
        } else {
            mSearchList = new ArrayList<>();
            mSearchList.addAll(search);
            mMessageTextView.setText("Total Result : " + totalResults);
            mSearchRecyclerView.setVisibility(View.VISIBLE);
        }
        if (Integer.parseInt(totalResults) > mSearchList.size()) {
            SearchItem viewMoreItem = new SearchItem();
            viewMoreItem.setViewType(SearchAdapter.VIEW_TYPE_MORE_LOADING);
            mSearchList.add(viewMoreItem);
        }
        if (mSearchAdapter != null) {
            mSearchAdapter.upateList(mSearchList);
        } else {
            mSearchAdapter = new SearchAdapter(this, mSearchList);
            mSearchRecyclerView.setAdapter(mSearchAdapter);
        }
    }

    @Override
    public void setOnItemClick(View view, int position) {
        Intent intent = new Intent(this, ResultDetailActivity.class);
        intent.putExtra(getString(R.string.movie_id), mSearchList.get(position).getImdbId());
        intent.putExtra(getString(R.string.title), mSearchList.get(position).getTitle());
        startActivity(intent);
    }

    public void requestForMoreItems() {
        mPageNumber++;
        requestForSearch();
    }
}
