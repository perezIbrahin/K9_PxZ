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
import Model.ModelBtnFreq;
import Util.Default_values;
import Util.RecyclerLocations;
import Util.TextSize;

public class RecyclerViewAdaptBtnT extends RecyclerView.Adapter<RecyclerViewAdaptBtnT.ViewHolder>  {
    private static final String TAG = "RecyclerViewAdaptBtnT";
    RecyclerViewClickInterface recyclerViewClickInterface;

    public RecyclerViewAdaptBtnT() {
    }

    public RecyclerViewAdaptBtnT(RecyclerViewClickInterface recyclerViewClickInterface, ArrayList<ModelBtn> modelArrayList) {
        this.recyclerViewClickInterface = recyclerViewClickInterface;
        this.modelArrayList = modelArrayList;
    }

    private ArrayList<ModelBtn> modelArrayList;

    @NonNull
    @Override
    public RecyclerViewAdaptBtnT.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_btn, parent, false);
        RecyclerViewAdaptBtnT.ViewHolder holder = new RecyclerViewAdaptBtnT.ViewHolder(view);
        if (holder != null) {
            Log.d(TAG, "onCreateViewHolder: Ready!");
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdaptBtnT.ViewHolder holder, int position) {
        Default_values default_values = new Default_values();
        TextSize textSize = new TextSize();

        ModelBtn modelBtn = modelArrayList.get(position);
        Log.d(TAG, "onBindViewHolder: " + modelArrayList.get(position).getBtnName());
        holder.button.setText(modelBtn.getBtnName());
        holder.button.setCompoundDrawablesRelativeWithIntrinsicBounds(modelBtn.getMenuDrawable(), null, null, null);
        if (modelBtn.getBtnStatus().equalsIgnoreCase(default_values.DEF_STATUS_CHECKED)) {
            holder.button.setTextSize(textSize.TEXT_SIZE_30);
        } else {
            holder.button.setTextSize(textSize.TEXT_SIZE_20);
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
            Log.d(TAG, "onClick: timer");
            recyclerViewClickInterface.onItemPostSelect(pos, recyclerLocations.LOCATION_VIB_TIM);
        }
    }
}
