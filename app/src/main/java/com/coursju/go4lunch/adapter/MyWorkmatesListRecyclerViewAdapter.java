package com.coursju.go4lunch.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.coursju.go4lunch.controler.DetailsActivity;
import com.coursju.go4lunch.modele.Workmate;
import com.coursju.go4lunch.utils.Constants;

import java.util.List;


public class MyWorkmatesListRecyclerViewAdapter extends RecyclerView.Adapter<MyWorkmatesListRecyclerViewAdapter.ViewHolder> {
    private String TAG = "MyWorkmatesListRecyclerViewAdapter";

    private final List<Workmate> mWorkmates;
    private final Activity mActivity;
    private Context mContext;

    public MyWorkmatesListRecyclerViewAdapter(List<Workmate> workmates, Activity activity, Context context) {
        mWorkmates = workmates;
        mActivity = activity;
        mContext = context;
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
            if (mWorkmates.get(position).getWorkmateName().equals(Constants.CURRENT_WORKMATE.getWorkmateName())){
                holder.mWorkmateListText.setText(mActivity.getString(R.string.you_havent_decided));
            }else {
                holder.mWorkmateListText.setText(wName + " " + mActivity.getString(R.string.hasnt_decided));
            }
            holder.mWorkmateListText.setTextColor(mActivity.getResources().getColor(R.color.alpha_grey));
            holder.mWorkmateListText.setTypeface(holder.mWorkmateListText.getTypeface(), Typeface.ITALIC);
            }else{
            if (mWorkmates.get(position).getWorkmateName().equals(Constants.CURRENT_WORKMATE.getWorkmateName())){
                holder.mWorkmateListText.setText(mActivity.getString(R.string.you_are_eating) + " " + wResto);
            }else {
                holder.mWorkmateListText.setText(wName + " " + mActivity.getString(R.string.is_eating) + " " + wResto);
            }
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

    public class ViewHolder extends RecyclerView.ViewHolder
                             implements View.OnClickListener{
        public final TextView mWorkmateListText;
        public final ImageView mWorkmateListPicture;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            mWorkmateListText = (TextView) view.findViewById(R.id.workmate_list_text);
            mWorkmateListPicture = (ImageView) view.findViewById(R.id.workmate_list_picture);
        }

        @Override
        public String toString() {
            return super.toString();// + " '" + mContentView.getText() + "'";
        }

        @Override
        public void onClick(View v) {
            int mPosition = getLayoutPosition();
            if (mWorkmates.get(mPosition).getYourLunch().getID() != null){
                Log.i(TAG,"item clicked!");
                Constants.DETAILS_RESTAURANT = mWorkmates.get(mPosition).getYourLunch();
                Intent intent = new Intent(mContext, DetailsActivity.class);
                mContext.startActivity(intent);
            }
        }
    }
}