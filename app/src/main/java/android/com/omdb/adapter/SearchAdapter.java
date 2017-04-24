package android.com.omdb.adapter;

import android.com.omdb.R;
import android.com.omdb.activity.SearchResultActivity;
import android.com.omdb.model.SearchItem;
import android.com.omdb.network.VolleySingleton;
import android.com.omdb.util.OnItemClickListener;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Rahul D on 4/22/17.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    public static final int VIEW_TYPE_MORE_LOADING = 1;
    private static final int SEARCH_ITEM = 0;
    private final Context mContext;
    private OnItemClickListener mClickListener;
    private ArrayList<SearchItem> mSearchItems;
    private int mLastMoreRequestPos;

    public SearchAdapter(Context context, ArrayList<SearchItem> items) {
        mContext = context;
        mSearchItems = items;
        mClickListener = (OnItemClickListener) context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case SEARCH_ITEM:
                return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.search_list_item, parent, false));
            case VIEW_TYPE_MORE_LOADING:
                return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.more_loading_layout, parent, false));
        }
        return null;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (holder.getItemViewType() == SEARCH_ITEM) {
            final SearchItem item = mSearchItems.get(position);
            holder.moviePoster.setTag(item.getPoster());
            holder.moviePoster.setImageResource(R.color.smokey_white);
            VolleySingleton.getInstance(mContext).getImageLoader().get(item.getPoster(), new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    String tag = holder.moviePoster.getTag() != null ? (String) holder.moviePoster.getTag() : null;
                    if (tag != null && tag.equalsIgnoreCase(response.getRequestUrl()) && response.getBitmap() != null) {
                        holder.moviePoster.setImageBitmap(response.getBitmap());
                    } else {
                        holder.moviePoster.setImageResource(R.color.smokey_white);
                    }
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    holder.moviePoster.setImageResource(R.color.smokey_white);
                }
            });
            holder.movieName.setText(item.getTitle());
            holder.movieYear.setText("Relase Year : " + item.getYear());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.setOnItemClick(v, holder.getAdapterPosition());
                }
            });
        } else {
            if (mLastMoreRequestPos != position) {
                ((SearchResultActivity) mContext).requestForMoreItems();
                mLastMoreRequestPos = position;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mSearchItems != null ? mSearchItems.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return mSearchItems.get(position).getViewType();
    }


    public void upateList(ArrayList<SearchItem> list) {
        mSearchItems = list;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView moviePoster;
        private TextView movieName;
        private TextView movieYear;

        public ViewHolder(View itemView) {
            super(itemView);
            moviePoster = (ImageView) itemView.findViewById(R.id.movie_image);
            movieName = (TextView) itemView.findViewById(R.id.movie_title);
            movieYear = (TextView) itemView.findViewById(R.id.year);
        }
    }
}
