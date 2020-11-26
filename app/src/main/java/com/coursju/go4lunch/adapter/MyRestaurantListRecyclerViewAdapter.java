package com.coursju.go4lunch.adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coursju.go4lunch.R;
import com.coursju.go4lunch.base.BaseFragment;
import com.coursju.go4lunch.controler.RestaurantListFragment;
import com.coursju.go4lunch.controler.dummy.DummyContent.DummyItem;
import com.coursju.go4lunch.modele.Restaurant;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static com.coursju.go4lunch.base.BaseFragment.mPlacesClient;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyRestaurantListRecyclerViewAdapter extends RecyclerView.Adapter<MyRestaurantListRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RestaurantListRecycler";
    private final List<Restaurant> mValues;
    Bitmap bitmap;


    public MyRestaurantListRecyclerViewAdapter(List<Restaurant> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_restaurant_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getName());
        holder.mContentView.setText(mValues.get(position).getAddress());

        if (mValues.get(position).getRestaurantPicture() != null) {
            Log.i("--bindview--", "into");
            getBitmapFromMetadata(mValues.get(position).getRestaurantPicture(), holder);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final ImageView itemRestoOverview;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_resto_name);
            mContentView = (TextView) view.findViewById(R.id.item_resto_address);
            itemRestoOverview = (ImageView) view.findViewById(R.id.item_resto_overview);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    public void getBitmapFromMetadata(PhotoMetadata photoMetadata, final ViewHolder holder){
        // Create a FetchPhotoRequest.

        final FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                //.setMaxWidth(300) // Optional.
                //.setMaxHeight(300) // Optional.
                .build();
        BaseFragment.mPlacesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
            bitmap =  fetchPhotoResponse.getBitmap();
            holder.itemRestoOverview.setImageBitmap(bitmap);
            Log.i("--fetch bitmap--", " "+bitmap);

        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                final ApiException apiException = (ApiException) exception;
                Log.e("--fetch", "Place not found: " + exception.getMessage());
                final int statusCode = apiException.getStatusCode();
                // TODO: Handle error with given status code.
            }
        });
    }
}