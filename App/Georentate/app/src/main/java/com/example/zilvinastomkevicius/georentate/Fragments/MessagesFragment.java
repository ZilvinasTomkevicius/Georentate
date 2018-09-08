package com.example.zilvinastomkevicius.georentate.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;

import com.example.zilvinastomkevicius.georentate.R;

public class MessagesFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser) {
     //       setToolbarName();
        }
    }

    public void setToolbarName() {
        Toolbar toolbar = getActivity().findViewById(R.id.mainToolbar);
        toolbar.setTitle("Messages");
    }
}
