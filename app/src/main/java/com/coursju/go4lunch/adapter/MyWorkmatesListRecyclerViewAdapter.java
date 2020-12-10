package com.coursju.go4lunch.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.coursju.go4lunch.R;
import com.coursju.go4lunch.modele.Workmate;

import java.util.List;


public class MyWorkmatesListRecyclerViewAdapter extends RecyclerView.Adapter<MyWorkmatesListRecyclerViewAdapter.ViewHolder> {
    private String TAG = "MyWorkmatesListRecyclerViewAdapter";

    private final List<Workmate> mWorkmates;
    private final Activity mActivity;

    public MyWorkmatesListRecyclerViewAdapter(List<Workmate> workmates, Activity activity) {
        mWorkmates = workmates;
        mActivity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_workmates_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String wName = mWorkmates.get(position).getWorkmateName();
        String wResto = mWorkmates.get(position).getYourLunch().getName();

        if (mWorkmates.get(position).getYourLunch().getID()==null){
            holder.mWorkmateListText.setText(wName+" "+mActivity.getString(R.string.hasnt_decided));
            holder.mWorkmateListText.setTextColor(mActivity.getResources().getColor(R.color.alpha_grey));
            holder.mWorkmateListText.setTypeface(holder.mWorkmateListText.getTypeface(), Typeface.ITALIC);
            }else{
            holder.mWorkmateListText.setText(wName+" "+mActivity.getString(R.string.is_eating)+" "+wResto);

        }

        if (mWorkmates.get(position).getWorkmatePicture() != null) {
            Glide.with(mActivity)
                    .load(mWorkmates.get(position).getWorkmatePicture())
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.mWorkmateListPicture);
        }
    }

    @Override
    public int getItemCount() {
        return mWorkmates.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mWorkmateListText;
        public final ImageView mWorkmateListPicture;

        public ViewHolder(View view) {
            super(view);
            mWorkmateListText = (TextView) view.findViewById(R.id.workmate_list_text);
            mWorkmateListPicture = (ImageView) view.findViewById(R.id.workmate_list_picture);
        }

        @Override
        public String toString() {
            return super.toString();// + " '" + mContentView.getText() + "'";
        }
    }
}