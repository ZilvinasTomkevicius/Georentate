package com.example.zilvinastomkevicius.georentate.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zilvinastomkevicius.georentate.Entities.Checkpoint;
import com.example.zilvinastomkevicius.georentate.Entities.UserCheckpoint;
import com.example.zilvinastomkevicius.georentate.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CheckpointListRecyclerView extends RecyclerView.Adapter<CheckpointListRecyclerView.ViewHolder> {

    private ArrayList<Checkpoint> mCheckpointArrayList;
    private ArrayList<UserCheckpoint> mUserCheckpointArrayList;
    private Context mContext;

    public CheckpointListRecyclerView(Context context, ArrayList<Checkpoint> checkpointArrayList, ArrayList<UserCheckpoint> userCheckpointArrayList) {
        mCheckpointArrayList = checkpointArrayList;
        mUserCheckpointArrayList = userCheckpointArrayList;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkpoint_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Checkpoint checkpoint = mCheckpointArrayList.get(position);
        holder.mCheckpointName.setText(checkpoint.getName());
        holder.mCheckpointPoints.setText(Float.toString(checkpoint.getPoints()) + " taškai");

        for(UserCheckpoint userCheckpoint : mUserCheckpointArrayList) {
            if(checkpoint.getId() == userCheckpoint.getCheckpointID()) {
                if(userCheckpoint.isCompleted()) {
                    holder.mCheckpointCompletedTextView.setText("");
                    holder.mCheckpointCompletedTextView.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
                    holder.mCheckpointCompleted.setColorFilter(mContext.getResources().getColor(R.color.colorPrimary));
                    break;
                } else if(!userCheckpoint.isCompleted()) {
                    holder.mCheckpointCompletedTextView.setText("");
                    holder.mCheckpointCompletedTextView.setTextColor(mContext.getResources().getColor(R.color.colorBlack));
                    holder.mCheckpointCompleted.setColorFilter(mContext.getResources().getColor(R.color.secondaryText));
                    break;
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mCheckpointArrayList == null ? 0 : mCheckpointArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mCheckpointName;
        private ImageView mCheckpointCompleted;
        private TextView mCheckpointPoints;
        private TextView mCheckpointCompletedTextView;

        public ViewHolder(View view) {
            super(view);

            mCheckpointName = view.findViewById(R.id.checkpoint_item_name);
            mCheckpointCompleted = view.findViewById(R.id.checkpoint_item_completed);
            mCheckpointPoints = view.findViewById(R.id.checkpoint_item_points);
            mCheckpointCompletedTextView = view.findViewById(R.id.checkpoint_item_completed_textView);
        }
    }
}
