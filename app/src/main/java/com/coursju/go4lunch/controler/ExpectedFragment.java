package com.coursju.go4lunch.controler;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coursju.go4lunch.R;
import com.coursju.go4lunch.adapter.MyExpectedRecyclerViewAdapter;
import com.coursju.go4lunch.api.ExpectedHelper;
import com.coursju.go4lunch.modele.Expected;
import com.coursju.go4lunch.utils.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ExpectedFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expected_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            //TODO remove ponctuations on restaurants names
            CollectionReference ref = ExpectedHelper.getExpectedRestaurant(Constants.DETAILS_RESTAURANT.getName());

            ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List<Expected> expectedList = queryDocumentSnapshots.toObjects(Expected.class);
                    List<Expected> expectedListCopy = new ArrayList<>(expectedList);
                    for (Expected expected: expectedListCopy){
                        if (expected.getUid().equals(Constants.CURRENT_WORKMATE.getUid())){
                            expectedList.remove(expected);
                        }
                    }
                    recyclerView.setAdapter(new MyExpectedRecyclerViewAdapter(expectedList, getActivity()));
                }
            });
        }
        return view;
    }
}