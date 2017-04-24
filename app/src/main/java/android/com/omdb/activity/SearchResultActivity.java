package android.com.omdb.activity;

import android.com.omdb.R;
import android.com.omdb.adapter.SearchAdapter;
import android.com.omdb.model.SearchItem;
import android.com.omdb.model.SearchResponse;
import android.com.omdb.network.Request;
import android.com.omdb.network.VolleyResponseListener;
import android.com.omdb.util.OnItemClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.com.omdb.R.id.toolbar;

/**
 * Created by Rahul D on 4/24/17.
 */

public class SearchResultActivity extends BaseActivity implements VolleyResponseListener, OnItemClickListener {
    private RecyclerView mSearchRecyclerView;
    private ProgressBar mProgressBar;
    private SearchAdapter mSearchAdapter;
    private int mPageNumber = 1;
    private Request mRequest;
    private ArrayList<SearchItem> mSearchList;
    private TextView mMessageText;
    private String mSearchText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mSearchText = getIntent().getStringExtra(getString(R.string.search_term));
        getWidgets();
        requestForSearch();
    }

    private void getWidgets() {
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mMessageText = (TextView) findViewById(R.id.message_text);
        mSearchRecyclerView = (RecyclerView) findViewById(R.id.search_list);
        mSearchRecyclerView.setLayoutManager(getLayoutManager());
        Toolbar toolBar = (Toolbar) findViewById(toolbar);
        setSupportActionBar(toolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back_arrow);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Search Result");
            actionBar.setSubtitle(mSearchText);
            toolBar.setTitleTextColor(ContextCompat.getColor(getBaseContext(), R.color.white));
            toolBar.setSubtitleTextColor(ContextCompat.getColor(getBaseContext(), R.color.white));
        }
    }

    private GridLayoutManager getLayoutManager() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getBaseContext(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mSearchList.get(position).getViewType() == SearchAdapter.VIEW_TYPE_MORE_LOADING) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });
        return gridLayoutManager;
    }

    private void requestForSearch() {
        if (mRequest == null) {
            mRequest = new Request();
        }
        if (mSearchList == null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("s", mSearchText);
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
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        } else {
            mSearchRecyclerView.setVisibility(View.GONE);
            mMessageText.setText(message);
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
            mSearchRecyclerView.setVisibility(View.VISIBLE);
            mMessageText.setVisibility(View.GONE);
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
