package com.coursju.go4lunch.controler;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coursju.go4lunch.R;
import com.coursju.go4lunch.adapter.MyRestaurantListRecyclerViewAdapter;
import com.coursju.go4lunch.base.BaseFragment;
import com.coursju.go4lunch.modele.Restaurant;

import java.util.List;

public class RestaurantListFragment extends BaseFragment {

    private final String TAG = "RestaurantsListFragment--";
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        inputSearchIsEmpty();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            if (go4LunchViewModel.getRestaurantsList() != null) showRestaurants(go4LunchViewModel.getRestaurantsList());

        }
        return view;
    }

    @Override
    protected void showRestaurants(List<Restaurant> restoList){
        recyclerView.setAdapter(new MyRestaurantListRecyclerViewAdapter(restoList, getContext(), go4LunchViewModel.getWorkmateList()));
    }

}