package com.example.zilvinastomkevicius.georentate.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.zilvinastomkevicius.georentate.Entities.AppInfo;
import com.example.zilvinastomkevicius.georentate.GlobalObjects.AppInfoObj;
import com.example.zilvinastomkevicius.georentate.GlobalObjects.MapObj;
import com.example.zilvinastomkevicius.georentate.R;

import org.w3c.dom.Text;

/**
 * A fragment for displaying and altering App settings
 */
public class SettingsFragment extends Fragment {

    //Changing icons
    private ImageView mExpandPrivacyPolicyIc;
    private ImageView mExpandAboutIc;
    private ImageView mExpandConditionsIc;

    private TextView mVersionTextView;

    private TextView mExpandableLayoutTextView;

    //Default layouts
    private RelativeLayout mPrivacyPolicyRel;
    private RelativeLayout mAboutRel;
    private RelativeLayout mConditionsRel;
    private RelativeLayout mFbRel;
    private RelativeLayout mWebRel;

    //Expandable layouts
    private RelativeLayout mExpandableLayoutPP;
    private RelativeLayout mExpandableLayoutAbout;
    private RelativeLayout mExpandableLayoutCond;

    private static boolean isPrivacyExpanded = false;
    private static boolean isAboutExpanded = false;
    private static boolean isConditionsExpanded = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        mExpandPrivacyPolicyIc = view.findViewById(R.id.expand_more_PP);
        mExpandAboutIc = view.findViewById(R.id.expand_more_about);
        mExpandConditionsIc = view.findViewById(R.id.expand_more_conditions);

        mPrivacyPolicyRel = view.findViewById(R.id.privacy_policy_rel);
        mAboutRel = view.findViewById(R.id.about_rel);
        mConditionsRel = view.findViewById(R.id.conditions_rel);
        mFbRel = view.findViewById(R.id.fb_rel);
        mWebRel = view.findViewById(R.id.web_rel);

        mVersionTextView = view.findViewById(R.id.settings_version);
        mVersionTextView.setText(AppInfoObj.APP_INFO.Version);

        mExpandableLayoutPP = view.findViewById(R.id.expandable_container_PP);
        mExpandableLayoutAbout = view.findViewById(R.id.expandable_container_about);
        mExpandableLayoutCond = view.findViewById(R.id.expandable_container_conditions);

        mPrivacyPolicyRel.setOnClickListener(new View.OnClickListener() {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View expandView = inflater.inflate(R.layout.expandable_layout_settings, null);

            @Override
            public void onClick(View v) {
                if(!isPrivacyExpanded) {
                    onPrivacyPolicyExpand(expandView);
                }
                else if(isPrivacyExpanded) {
                    onPrivacyPolicyCollapse(expandView);
                }
            }
        });

        mAboutRel.setOnClickListener(new View.OnClickListener() {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View expandView = inflater.inflate(R.layout.expandable_layout_settings, null);

            @Override
            public void onClick(View v) {
                if(!isAboutExpanded) {
                    onAboutExpand(expandView);
                } else if(isAboutExpanded) {
                    onAboutCollapse(expandView);
                }
            }
        });

        mConditionsRel.setOnClickListener(new View.OnClickListener() {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View expandView = inflater.inflate(R.layout.expandable_layout_settings, null);

            @Override
            public void onClick(View v) {
                if(!isConditionsExpanded) {
                    onConditionsExpand(expandView);
                } else if(isConditionsExpanded) {
                    onConConditionsCollapse(expandView);
                }
            }
        });

        mFbRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/tomkeviciu.zilvinelis")));
            }
        });

        mWebRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * Expandable view management
     * @param view
     */

    public void onPrivacyPolicyExpand(View view) {
        mExpandableLayoutTextView = view.findViewById(R.id.expandable_layout_settings_text);
        mExpandableLayoutTextView.setText(AppInfoObj.APP_INFO.PrivacyPolicy);
        mExpandPrivacyPolicyIc.setImageResource(R.drawable.ic_expand_less_yellow_24dp);
        isPrivacyExpanded = true;

        mExpandableLayoutPP.addView(view);
    }

    public void onPrivacyPolicyCollapse(View view) {
        mExpandPrivacyPolicyIc.setImageResource(R.drawable.ic_expand_more_yellow_24dp);
        isPrivacyExpanded = false;

        mExpandableLayoutPP.removeView(view);
    }

    public void onAboutExpand(View view) {
        mExpandableLayoutTextView = view.findViewById(R.id.expandable_layout_settings_text);
        mExpandableLayoutTextView.setText(AppInfoObj.APP_INFO.About);
        mExpandAboutIc.setImageResource(R.drawable.ic_expand_less_yellow_24dp);
        isAboutExpanded = true;

        mExpandableLayoutAbout.addView(view);
    }

    public void onAboutCollapse(View view) {
        mExpandAboutIc.setImageResource(R.drawable.ic_expand_more_yellow_24dp);
        isAboutExpanded = false;

        mExpandableLayoutAbout.removeView(view);
    }

    public void onConditionsExpand(View view) {
        mExpandableLayoutTextView = view.findViewById(R.id.expandable_layout_settings_text);
        mExpandableLayoutTextView.setText(AppInfoObj.APP_INFO.Conditions);
        mExpandConditionsIc.setImageResource(R.drawable.ic_expand_less_yellow_24dp);
        isConditionsExpanded = true;

        mExpandableLayoutCond.addView(view);
    }

    public void onConConditionsCollapse(View view) {
        mExpandConditionsIc.setImageResource(R.drawable.ic_expand_more_yellow_24dp);
        isConditionsExpanded = false;

        mExpandableLayoutCond.removeView(view);
    }
}
