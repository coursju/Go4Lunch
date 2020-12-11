package com.coursju.go4lunch.adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.provider.SyncStateContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.coursju.go4lunch.R;
import com.coursju.go4lunch.controler.dummy.DummyContent.DummyItem;
import com.coursju.go4lunch.modele.Expected;
import com.coursju.go4lunch.utils.Constants;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyExpectedRecyclerViewAdapter extends RecyclerView.Adapter<MyExpectedRecyclerViewAdapter.ViewHolder> {

    private final List<Expected> mValues;
    private final Activity mActivity;

    public MyExpectedRecyclerViewAdapter(List<Expected> items, Activity activity) {
        mValues = items;
        mActivity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_expected, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mTextView.setText(mValues.get(position).getWorkmateName());

            if (mValues.get(position).getWorkmatePicture() != null) {
                Glide.with(mActivity)
                        .load(mValues.get(position).getWorkmatePicture())
                        .apply(RequestOptions.circleCropTransform())
                        .into(holder.mImageView);
            }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mTextView;
        public final ImageView mImageView;

        public ViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.expected_text);
            mImageView = (ImageView) view.findViewById(R.id.expected_image);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}