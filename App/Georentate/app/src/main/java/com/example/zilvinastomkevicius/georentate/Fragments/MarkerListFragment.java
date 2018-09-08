package com.example.zilvinastomkevicius.georentate.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.support.v7.widget.Toolbar;

import com.example.zilvinastomkevicius.georentate.APIClients.CheckpointClients;
import com.example.zilvinastomkevicius.georentate.Adapters.CheckpointListRecyclerView;
import com.example.zilvinastomkevicius.georentate.Entities.Checkpoint;
import com.example.zilvinastomkevicius.georentate.GlobalObjects.CheckpointObj;
import com.example.zilvinastomkevicius.georentate.GlobalObjects.UserObj;
import com.example.zilvinastomkevicius.georentate.R;

import java.util.Collections;

public class MarkerListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private CheckpointListRecyclerView mCheckpointListRecyclerView;
    private CheckpointClients checkpointClients;

    private ProgressBar progressBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_marker_list, container, false);

        progressBar = view.findViewById(R.id.markerList_progressBar);
        mRecyclerView = view.findViewById(R.id.myMarkerList_recyclerView);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setMarkerListRecyclerView();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser) {
        //    setToolbarName();
            setMarkerListRecyclerView();
        }
    }

    public void setMarkerListRecyclerView() {
        sortMarkerListByID();
        mCheckpointListRecyclerView = new CheckpointListRecyclerView(getContext(), CheckpointObj.checkpointArrayList, CheckpointObj.userCheckpointArrayList);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mCheckpointListRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void setToolbarName() {
        Toolbar toolbar = getActivity().findViewById(R.id.mainToolbar);
        toolbar.setTitle("Checkpoint list");
    }

    public void sortMarkerListByID() {
        Collections.sort(CheckpointObj.checkpointArrayList, (c1, c2) -> c1.ID - c2.ID);
    }
}
