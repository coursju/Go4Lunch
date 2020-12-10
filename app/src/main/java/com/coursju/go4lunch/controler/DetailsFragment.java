package com.coursju.go4lunch.controler;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.coursju.go4lunch.R;
import com.coursju.go4lunch.api.WorkmateHelper;
import com.coursju.go4lunch.modele.Restaurant;
import com.coursju.go4lunch.modele.Workmate;
import com.coursju.go4lunch.utils.Constants;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DetailsFragment extends Fragment {


    private static final String TAG = "DetailsFragment";

    private ImageView detailsImageView;
    private FloatingActionButton detailsFloatingButton;
    private Button detailsCallButton;
    private Button detailsLikeButton;
    private Button detailsWebsiteButton;
    private TextView detailsRestoName;
    private TextView detailsRestoAddress;
    private ImageView detailsStar1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Boolean bol = Constants.DETAILS_RESTAURANT == null;
        Log.i(TAG, Constants.DETAILS_RESTAURANT.getName());
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        linkView(view);
        updateView();
        configureButtonListener();
        super.onViewCreated(view, savedInstanceState);
    }

    private void linkView(View view){
        detailsImageView = view.findViewById(R.id.details_imageView);
        detailsFloatingButton = view.findViewById(R.id.details_floatingActionButton);
        detailsCallButton = view.findViewById(R.id.details_call_button);
        detailsLikeButton = view.findViewById(R.id.details_like_button);
        detailsWebsiteButton = view.findViewById(R.id.details_website_button);
        detailsRestoName = view.findViewById(R.id.details_resto_name);
        detailsRestoAddress = view.findViewById(R.id.details_resto_address);
        detailsStar1 = view.findViewById(R.id.details_star_1);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void updateView(){
        if (Constants.DETAILS_RESTAURANT != null) {
            detailsRestoName.setText(Constants.DETAILS_RESTAURANT.getName());
            detailsRestoAddress.setText(Constants.DETAILS_RESTAURANT.getAddress());
            if (Constants.DETAILS_RESTAURANT.getBitmap()==null
                && Constants.DETAILS_RESTAURANT.getRestaurantPhotoReference() != null){
                fetchBitmap();
            }else{
                detailsImageView.setImageBitmap(Constants.DETAILS_RESTAURANT.getBitmap());
            }

            if (Constants.DETAILS_RESTAURANT.getID().equals(Constants.CURRENT_WORKMATE.getYourLunch().getID())){
                detailsFloatingButton.setForegroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorGreen)));
            }
        }
    }

    private void configureButtonListener(){
        detailsFloatingButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (!Constants.DETAILS_RESTAURANT.getID().equals(Constants.CURRENT_WORKMATE.getYourLunch().getID())) {
                    WorkmateHelper.updateRestaurant(Constants.DETAILS_RESTAURANT);
                    detailsFloatingButton.setForegroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorGreen)));
                }else{
                    WorkmateHelper.updateRestaurant(new Restaurant());
                    detailsFloatingButton.setForegroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                }
            }
        });

    }

    private void fetchBitmap(){
        String url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" +
                Constants.DETAILS_RESTAURANT.getRestaurantPhotoReference()+
                "&key=" +
                "AIzaSyBM42q3bmSdlAnPGzGesADPLjRVD6KPLbk";

        RequestQueue queue = Volley.newRequestQueue(getContext());

        ImageRequest imageRequest = new ImageRequest( url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        Constants.DETAILS_RESTAURANT.setBitmap(response);
                        Constants.CURRENT_WORKMATE.getYourLunch().setBitmap(response);
                        detailsImageView.setImageBitmap(Constants.DETAILS_RESTAURANT.getBitmap());
                    }
                },
                400,
                400,
                Bitmap.Config.ARGB_8888,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, " fetchBitmap didn't work!");
                    }
                });
        queue.add(imageRequest);

    }
}
