package com.coursju.go4lunch.adapter;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coursju.go4lunch.R;
import com.coursju.go4lunch.controler.DetailsActivity;
import com.coursju.go4lunch.modele.Restaurant;
import com.coursju.go4lunch.modele.Workmate;
import com.coursju.go4lunch.utils.Constants;

import java.util.List;

public class MyRestaurantListRecyclerViewAdapter extends RecyclerView.Adapter<MyRestaurantListRecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RestaurantListRecycler";
    private final List<Restaurant> mValues;
    private final Context mContext;
    private List<Workmate> mWorkmateList;

    public MyRestaurantListRecyclerViewAdapter(List<Restaurant> items , Context context, List<Workmate> workmateList) {
        mValues = items;
        mContext = context;
        mWorkmateList = workmateList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_restaurant_list_item, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mName.setText(mValues.get(position).getName());
        holder.mAddress.setText(mValues.get(position).getAddress());
        holder.mDistance.setText(mValues.get(position).getDistance()+" m");
        int i = 0;
        for (Workmate workmate : mWorkmateList){
            if (workmate.getYourLunch().getName() != null){
                if (workmate.getYourLunch().getName().equals(mValues.get(position).getName())){
                    i++;
                }
            }
        }
        holder.mWorkmatesNumber.setText("("+i+")");
        holder.itemRestoOverview.setImageBitmap(mValues.get(position).getBitmap());

//        int day = LocalDate.now().getDayOfWeek().getValue();//.getDisplayName(TextStyle.NARROW_STANDALONE, Locale.ENGLISH);
//        if (mValues.get(position).getOpeningHours() != null){
//            Log.i(TAG,mValues.get(position).getOpeningHours().get(day)+" name: "+mValues.get(position).getName());
//
//        }


    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
                              implements View.OnClickListener{
        public final TextView mName;
        public final TextView mAddress;
        public final TextView mOpeningHours;
        public final TextView mDistance;
        public final TextView mWorkmatesNumber;
        public final ImageView mStar1;
        public final ImageView mStar2;
        public final ImageView mStar3;
        public final ImageView itemRestoOverview;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            mName = (TextView) view.findViewById(R.id.item_resto_name);
            mAddress = (TextView) view.findViewById(R.id.item_resto_address);
            mOpeningHours = (TextView) view.findViewById(R.id.item_resto_openinghours);
            mDistance = (TextView) view.findViewById(R.id.item_resto_distance);
            mWorkmatesNumber = (TextView) view.findViewById(R.id.item_resto_workmates);
            mStar1 = (ImageView) view.findViewById(R.id.item_resto_star1);
            mStar2 = (ImageView) view.findViewById(R.id.item_resto_star2);
            mStar3 = (ImageView) view.findViewById(R.id.item_resto_star3);
            itemRestoOverview = (ImageView) view.findViewById(R.id.item_resto_overview);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mName.getText() + "'";
        }

        @Override
        public void onClick(View v) {
            Log.i(TAG,"item clicked!");
            int mPosition = getLayoutPosition();
            Constants.DETAILS_RESTAURANT = mValues.get(mPosition);
            Intent intent = new Intent(mContext, DetailsActivity.class);
            mContext.startActivity(intent);

        }
    }
}