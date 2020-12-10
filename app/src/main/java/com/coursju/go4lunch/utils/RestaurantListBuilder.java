package com.coursju.go4lunch.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.coursju.go4lunch.base.BaseFragment;
import com.coursju.go4lunch.modele.Restaurant;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class RestaurantListBuilder {

    private String TAG = "--RestaurantListBuilder";

    private Context mContext;
    private List<Restaurant> mRestaurantList;
    private Callback mCallback;
    private String googleKey = "AIzaSyBM42q3bmSdlAnPGzGesADPLjRVD6KPLbk";
    private int k;
    private int listSize;
    private int cpt;

    public RestaurantListBuilder(Context context, Callback callback) {
        mContext = context;
        mCallback = callback;
        mRestaurantList = new ArrayList<>();
        listSize = 0;
        cpt = 0;
    }

    public void buildRestaurantsList(){
        listSize = 0;
        cpt = 1;
        mRestaurantList.clear();
        getPlaceIDList();
    }

    /**
     * build a List<String> of place id from current position
     */
    private void getPlaceIDList(){
        String url ="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" +
                BaseFragment.currentLocation.getLatitude()+","+BaseFragment.currentLocation.getLongitude() +
                "&radius=" +
                BaseFragment.radius +
                "&types=restaurant&sensor=true" +
                "&key=A" +
                "IzaSyBM42q3bmSdlAnPGzGesADPLjRVD6KPLbk";

        RequestQueue queue = Volley.newRequestQueue(mContext);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<String> iDList = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("results");
                            listSize = jsonArray.length();

                            for (int i = 0; i< jsonArray.length(); i++){
                                JSONObject jsonObjt = (JSONObject) jsonArray.get(i);
                                iDList.add(jsonObjt.getString("place_id"));
                               }
                            getRestaurantDetailsIteration(iDList);
                            Log.i(TAG,"iDList ok, size= "+listSize);

                        }catch (Exception e){}
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                       // textView.setText("That didn't work!");

                    }
                });
        queue.add(jsonObjectRequest);
    }

    /**
     * make a iteration of the place id list to build Restaurant class details
     * @param iDList the list from getPlaceIDList method
     */
    private void getRestaurantDetailsIteration(List<String> iDList){
        Log.i(TAG,"iteration");
        k=0;

        for (String id : iDList){
            getRestaurantDetails(id);
        }
    }

    /**
     * build the Restaurant class details
     * @param id string place id from getRestaurantDetailsIteration method
     */
    private void getRestaurantDetails(String id){
        String url ="https://maps.googleapis.com/maps/api/place/details/json?" +
                "place_id=" +
                id +
                "&fields=opening_hours,website,geometry/location,formatted_address,name,photo,formatted_phone_number" +
                "&key=" +
                "AIzaSyBM42q3bmSdlAnPGzGesADPLjRVD6KPLbk";

        RequestQueue queue = Volley.newRequestQueue(mContext);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject =  response.getJSONObject("result");

                            //get name
                            String name = (jsonObject.getString("name") == null)? null: jsonObject.getString("name");

                            //get address
                            String address = (jsonObject.getString("formatted_address") == null)? null: jsonObject.getString("formatted_address");

                            //get photo reference
                            String photoReference = null;
                            try {
                                photoReference = jsonObject.getJSONArray("photos").getJSONObject(0).getString("photo_reference");
                            }catch (Exception e){Log.d(TAG, e+": photoReference didn't work!");}

                            //get website
                            String website = null;
                            try {
                                 website = jsonObject.getString("website");
                            }catch (Exception e){Log.d(TAG, e+": website didn't work!");}

                            //get phone numbers
                            String phone = null;
                            try {
                                phone = (jsonObject.getString("formatted_phone_number") == null)? null: jsonObject.getString("formatted_phone_number");
                            }catch (Exception e){Log.d(TAG, e+": phone didn't work!");}

                            //get location
                            double lat = jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                            double lng = jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                            LatLng latLng = new LatLng(lat,lng);

                            //get distance
                            Location loc = new Location("");
                            loc.setLatitude(lat);
                            loc.setLongitude(lng);
                            String distance =  String.valueOf((int)BaseFragment.currentLocation.distanceTo(loc));

                            //get opening hours
                            List<String> openingHours = new ArrayList<>();
                            try {
                                if (!jsonObject.getJSONObject("opening_hours").isNull("weekday_text")){
                                    JSONArray arr = jsonObject.getJSONObject("opening_hours").getJSONArray("weekday_text");
                                    for (int i =0; i< arr.length(); i++){
                                        openingHours.add(arr.getString(i));
                                    }
                                }
                            }catch (Exception e){Log.d(TAG, e+": openingHours didn't work!");}

                            //get is open
                            Boolean isOpen = null;
                            try {
                                isOpen = (jsonObject.getJSONObject("opening_hours").isNull("open_now")) ? null : jsonObject.getJSONObject("opening_hours").getBoolean("open_now");
                            }catch (Exception e){Log.d(TAG, e+": isOpen didn't work!");}

                            mRestaurantList.add(new Restaurant(name,address,photoReference,website,phone,id,latLng,distance,openingHours,isOpen));
                            int numberInList = mRestaurantList.size()-1;
                            Log.i(TAG,/*name+" into: "+ k++ +" : "+*/mRestaurantList.get(numberInList).toString());

                            //downloading bitmap relates to Restaurant
                            if (mRestaurantList.get(numberInList).getRestaurantPhotoReference() != null){
                                getBitmapFromUrl(mRestaurantList.get(numberInList).getRestaurantPhotoReference(), numberInList);
                            }else{listSize--;}

                        }catch (Exception e){Log.d(TAG, "getRestaurantDetails: onResponse didn't work!");}
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "getRestaurantDetails didn't work!");
                    }
                });
        queue.add(jsonObjectRequest);
    }

    /**
     * Used to download the bipmap image in background
     * @param photoReference the photo_reference to download
     */
    private void getBitmapFromUrl(String photoReference, int numberInList){
        String url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" +
                photoReference+
                "&key=" +
                "AIzaSyBM42q3bmSdlAnPGzGesADPLjRVD6KPLbk";

        RequestQueue queue = Volley.newRequestQueue(mContext);

        ImageRequest imageRequest = new ImageRequest( url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        mRestaurantList.get(numberInList).setBitmap(response);
                        Log.i(TAG,"bitmap: "+numberInList+"  listSize= "+listSize+" cpt size= "+cpt+" restolist size: "+mRestaurantList.size()+"\n");
                        cpt++;

                        if (cpt == listSize){
                            mCallback.onFinish(mRestaurantList);
                            Log.i(TAG,"equality "+mRestaurantList.size());
                        }
                    }
                },
                400,
                400,
                Bitmap.Config.ARGB_8888,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, photoReference + " getBitmapFromUrl didn't work!");
                    }
                });
        queue.add(imageRequest);
    }

}
