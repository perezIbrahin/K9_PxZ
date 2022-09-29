package Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.k9_pxz.R;

import java.util.ArrayList;

import Interface.RecyclerViewClickInterface;
import Model.ModelBtn;
import Util.Default_values;
import Util.RecyclerLocations;
import Util.TextSize;


public class RecyclerViewAdaptBtnI extends RecyclerView.Adapter<RecyclerViewAdaptBtnI.ViewHolder> {
    private static final String TAG = "RecyclerViewAdaptBtnI";

    RecyclerViewClickInterface recyclerViewClickInterface;
    private ArrayList<ModelBtn> modelArrayList;

    public RecyclerViewAdaptBtnI() {
    }

    public RecyclerViewAdaptBtnI(RecyclerViewClickInterface recyclerViewClickInterface, ArrayList<ModelBtn> modelArrayList) {
        this.recyclerViewClickInterface = recyclerViewClickInterface;
        this.modelArrayList = modelArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewAdaptBtnI.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_btn, parent, false);
        RecyclerViewAdaptBtnI.ViewHolder holder = new RecyclerViewAdaptBtnI.ViewHolder(view);
        if (holder != null) {
            Log.d(TAG, "onCreateViewHolder: Ready!");
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdaptBtnI.ViewHolder holder, int position) {
        Default_values default_values = new Default_values();
        TextSize textSize = new TextSize();

        ModelBtn modelBtn = modelArrayList.get(position);
        Log.d(TAG, "onBindViewHolder: " + modelArrayList.get(position).getBtnName());
        holder.button.setText(modelBtn.getBtnName());
        holder.button.setCompoundDrawablesRelativeWithIntrinsicBounds(modelBtn.getMenuDrawable(), null, null, null);
        if (modelBtn.getBtnStatus().equalsIgnoreCase(default_values.DEF_STATUS_CHECKED)) {
            holder.button.setTextSize(textSize.TEXT_SIZE_30);
        } else {
            holder.button.setTextSize(textSize.TEXT_SIZE_30);
        }
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.btnF);
            button.setOnClickListener(this);
            //itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            RecyclerLocations recyclerLocations = new RecyclerLocations();
            int pos = getAdapterPosition();
            recyclerViewClickInterface.onItemPostSelect(pos, recyclerLocations.LOCATION_VIB_INT);
        }

    }

}
