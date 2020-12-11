package com.coursju.go4lunch.controler;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coursju.go4lunch.R;
import com.coursju.go4lunch.adapter.MyWorkmatesListRecyclerViewAdapter;
import com.coursju.go4lunch.api.WorkmateHelper;
import com.coursju.go4lunch.modele.Workmate;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class WorkmatesListFragment extends Fragment {

    private String TAG = "WorkmatesListFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workmates_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            WorkmateHelper.getUsersCollection().get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List<Workmate> workmateList = queryDocumentSnapshots.toObjects(Workmate.class);
                    Log.i(TAG,workmateList.get(0).getWorkmateName());
                    recyclerView.setAdapter(new MyWorkmatesListRecyclerViewAdapter(workmateList, getActivity(), getContext()));
                }
            });
        }
        return view;
    }
}