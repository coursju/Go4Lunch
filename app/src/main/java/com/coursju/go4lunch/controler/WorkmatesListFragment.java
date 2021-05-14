package com.coursju.go4lunch.controler;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coursju.go4lunch.R;
import com.coursju.go4lunch.adapter.MyWorkmatesListRecyclerViewAdapter;
import com.coursju.go4lunch.base.BaseFragment;

public class WorkmatesListFragment extends BaseFragment {

    private String TAG = "WorkmatesListFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workmates_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new MyWorkmatesListRecyclerViewAdapter(go4LunchViewModel.getWorkmateList(), getActivity(), getContext()));
        }
        return view;
    }
}