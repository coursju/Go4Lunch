package com.coursju.go4lunch.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.coursju.go4lunch.R;
import com.coursju.go4lunch.controler.DetailsActivity;
import com.coursju.go4lunch.modele.Restaurant;
import com.coursju.go4lunch.modele.Workmate;
import com.coursju.go4lunch.utils.Constants;
import com.coursju.go4lunch.viewmodel.Go4LunchViewModel;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MyRestaurantListRecyclerViewAdapter extends RecyclerView.Adapter<MyRestaurantListRecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RestaurantListRecycler";
    private final List<Restaurant> mValues;
    private final Context mContext;
    private List<Workmate> mWorkmateList;
    private Map<String, Map<String, Object>> favoritesMap;

    public MyRestaurantListRecyclerViewAdapter(Context context, List<Restaurant> restoList, Go4LunchViewModel go4LunchViewModel) {
        this.favoritesMap = Constants.FAVORITES_MAP;
        mValues = restoList;
        mContext = context;
        mWorkmateList = go4LunchViewModel.getWorkmateList();
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

        int day = LocalDate.now().getDayOfWeek().getValue();
        if (mValues.get(position).getOpeningHours() != null){
            if (mValues.get(position).getOpeningHours().size() > 0){
                String[] tab = mValues.get(position).getOpeningHours().get(day).split("day: ");
                holder.mOpeningHours.setText(tab[1]);
            }
        }
        holder.mStar.setRating(getFavorites(mValues.get(position).getName()));
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
        public final RatingBar mStar;
        public final ImageView itemRestoOverview;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            mName = (TextView) view.findViewById(R.id.item_resto_name);
            mAddress = (TextView) view.findViewById(R.id.item_resto_address);
            mOpeningHours = (TextView) view.findViewById(R.id.item_resto_openinghours);
            mDistance = (TextView) view.findViewById(R.id.item_resto_distance);
            mWorkmatesNumber = (TextView) view.findViewById(R.id.item_resto_workmates);
            mStar = (RatingBar) view.findViewById(R.id.item_resto_star);
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

    private Float getFavorites(String restaurantName){
        if (favoritesMap.containsKey(restaurantName)){
            Collection<Object> rate = favoritesMap.get(restaurantName).values();
            if (rate.size() != 0) {
                long cpt = 0;
                for (Object obj : rate) {
                    Boolean boolObj = (Boolean) obj;
                    cpt += (boolObj) ? 1 : 0;
                }
                return cpt / rate.size() * 3f;
            }else {
                return 0f;
            }
        }else{
            return 0f;
        }
    }
}