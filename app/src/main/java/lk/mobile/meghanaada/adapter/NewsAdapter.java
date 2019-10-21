package lk.mobile.meghanaada.adapter;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import lk.mobile.meghanaada.R;
import lk.mobile.meghanaada.model.News;

public class NewsAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final String TAG = "NewsAdapter";
    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_NORMAL = 1;

    private Callback mCallback;
    private List<News> mNewsList;

    public NewsAdapter(List<News> newsList) {
        mNewsList = newsList;
    }

    public NewsAdapter(ArrayList<News> newsList) {
        mNewsList = newsList;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new EmptyViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mNewsList != null && mNewsList.size() > 0) {
            return VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public int getItemCount() {
        if (mNewsList != null && mNewsList.size() > 0) {
            return mNewsList.size();
        } else {
            return 1;
        }
    }

    public void addItems(List<News> newsList) {
        mNewsList.addAll(newsList);
        notifyDataSetChanged();
    }

    public interface Callback {
        void onEmptyViewRetryClick();
    }

    public class ViewHolder extends BaseViewHolder {

        TextView titleTextView;

        TextView newsTextView;

        TextView infoTextView;


        public ViewHolder(View itemView) {
            super(itemView);

            infoTextView = itemView.findViewById(R.id.newsInfo);
            newsTextView = itemView.findViewById(R.id.newsTitle);
            titleTextView = itemView.findViewById(R.id.title);
        }

        protected void clear() {
            titleTextView.setText("");
            newsTextView.setText("");
            infoTextView.setText("");
        }

        public void onBind(int position) {
            super.onBind(position);

            final News mNews = mNewsList.get(position);


            if (mNews.getTitle() != null) {
                titleTextView.setText(mNews.getTitle());
            }

            if (mNews.getSubTitle() != null) {
                newsTextView.setText(mNews.getSubTitle());
            }

            if (mNews.getInfo() != null) {
                infoTextView.setText(mNews.getInfo());
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mNews.getImageUrl() != null) {
                        try {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent.setData(Uri.parse(mNews.getImageUrl()));
                            itemView.getContext().startActivity(intent);
                        } catch (Exception e) {
                            Log.e(TAG, "onClick: Image url is not correct");
                        }
                    }
                }
            });


        }
    }

    public class EmptyViewHolder extends BaseViewHolder {

        TextView messageTextView;
        TextView buttonRetry;

        EmptyViewHolder(View itemView) {
            super(itemView);

            messageTextView = itemView.findViewById(R.id.tv_message);
            buttonRetry = itemView.findViewById(R.id.buttonRetry);

            buttonRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onEmptyViewRetryClick();
                }
            });
        }

        @Override
        protected void clear() {

        }

    }
}
