package android.com.omdb.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Rahul D on 4/22/17.
 */

public class SearchResponse {
    @SerializedName("Response")
    private String response;

    @SerializedName("totalResults")
    private String totalResults;

    @SerializedName("Error")
    private String error;

    @SerializedName("Search")

    ArrayList<SearchItem> search;

    public ArrayList<SearchItem> getSearch() {
        return search;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public String getResponse() {
        return response;
    }

    public String getError() {
        return error;
    }
}
