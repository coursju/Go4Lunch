package com.coursju.go4lunch.adapter;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coursju.go4lunch.R;
import com.coursju.go4lunch.base.BaseFragment;
import com.coursju.go4lunch.modele.Restaurant;
import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class MyRestaurantListRecyclerViewAdapter extends RecyclerView.Adapter<MyRestaurantListRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RestaurantListRecycler";
    private final List<Restaurant> mValues;

    public MyRestaurantListRecyclerViewAdapter(List<Restaurant> items) {
        mValues = items;
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
        holder.mWorkmatesNumber.setText("(2)");//mValues.get(position).getExpectedWorkmates().size());
        holder.itemRestoOverview.setImageBitmap(mValues.get(position).getBitmap());

        int day = LocalDate.now().getDayOfWeek().getValue();//.getDisplayName(TextStyle.NARROW_STANDALONE, Locale.ENGLISH);
//        if (mValues.get(position).getOpeningHours() != null){
//            Log.i(TAG,mValues.get(position).getOpeningHours().get(day)+" name: "+mValues.get(position).getName());
//
//        }


    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
    }
}