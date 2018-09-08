package com.example.zilvinastomkevicius.georentate.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.support.v7.widget.Toolbar;

import com.example.zilvinastomkevicius.georentate.GlobalObjects.MapObj;
import com.example.zilvinastomkevicius.georentate.R;

public class SettingsFragment extends Fragment {

    private Spinner zoomSpinner;
    private Spinner checkpointSpinner;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        zoomSpinner = view.findViewById(R.id.zoom_spinner);
        checkpointSpinner = view.findViewById(R.id.checkpoint_spinner);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String[] zoomTypes = {"City", "Closer city", "Streets", "Closer streets", "Buildings", "World", "Continent"};
        setSpinner(zoomTypes, zoomSpinner);

        String[] checkpointTypes = {"Nearest checkpoint", "Destination", "Checked"};
        setSpinner(checkpointTypes, checkpointSpinner);

        zoomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("World")) {
                    MapObj.DEFAULT_ZOOM = 1f;
                }

                else if(parent.getItemAtPosition(position).equals("Continents")) {
                    MapObj.DEFAULT_ZOOM = 5f;
                }

                else if(parent.getItemAtPosition(position).equals("City")) {
                    MapObj.DEFAULT_ZOOM = 10f;
                }

                else if(parent.getItemAtPosition(position).equals("Closer city")) {
                    MapObj.DEFAULT_ZOOM = 12.5f;
                }

                else if(parent.getItemAtPosition(position).equals("Streets")) {
                    MapObj.DEFAULT_ZOOM = 15f;
                }

                else if(parent.getItemAtPosition(position).equals("Closer streets")) {
                    MapObj.DEFAULT_ZOOM = 17.5f;
                }

                else if(parent.getItemAtPosition(position).equals("Buildings")) {
                    MapObj.DEFAULT_ZOOM = 20f;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        checkpointSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("Nearest checkpoint")) {
                    MapObj.CHECKPOINT_LOCATE = "Nearest checkpoint";
                }

                else if(parent.getItemAtPosition(position).equals("Destination")) {
                    MapObj.CHECKPOINT_LOCATE = "Destination";
                }

                else if(parent.getItemAtPosition(position).equals("Checked")) {
                    MapObj.CHECKPOINT_LOCATE = "Checked";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser) {
    //        setToolbarName();
        }
    }

    public void setSpinner(String[] content, Spinner spinner) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, content);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void setToolbarName() {
        Toolbar toolbar = getActivity().findViewById(R.id.mainToolbar);
        toolbar.setTitle("Settings");
    }
}
