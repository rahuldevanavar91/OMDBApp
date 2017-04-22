package android.com.omdb.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rahul D on 4/22/17.
 */

public class SearchItem {
    private int viewType;
    @SerializedName("Title")
    String title;
    @SerializedName("Year")
    String year;

    @SerializedName("imdbID")
    String imdbId;
    @SerializedName("Type")
    String type;
    @SerializedName("Poster")
    String poster;

    public String getTitle() {
        return title;
    }

    public String getImdbId() {
        return imdbId;
    }

    public String getPoster() {
        return poster;
    }

    public String getType() {
        return type;
    }

    public String getYear() {
        return year;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
