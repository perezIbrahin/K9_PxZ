package Adapter;

import android.graphics.drawable.Drawable;
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

public class RecyclerViewAdaptBtnF extends RecyclerView.Adapter<RecyclerViewAdaptBtnF.ViewHolder>{
    private static final String TAG = "RecyclerViewAdaptBtnF";

    public RecyclerViewAdaptBtnF() {
    }

    public RecyclerViewAdaptBtnF(RecyclerViewClickInterface recyclerViewClickInterface, ArrayList<ModelBtn> modelArrayList) {
        this.recyclerViewClickInterface = recyclerViewClickInterface;
        this.modelArrayList = modelArrayList;
    }

    RecyclerViewClickInterface recyclerViewClickInterface;
    private ArrayList<ModelBtn> modelArrayList;
    @NonNull
    @Override
    public RecyclerViewAdaptBtnF.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_btn, parent, false);
        RecyclerViewAdaptBtnF.ViewHolder holder = new RecyclerViewAdaptBtnF.ViewHolder(view);
        if (holder != null) {
            Log.d(TAG, "onCreateViewHolder: Ready!");
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdaptBtnF.ViewHolder holder, int position) {
        Default_values default_values=new Default_values();
        TextSize textSize=new TextSize();

        ModelBtn modelBtnFreq= modelArrayList.get(position);
        Log.d(TAG, "onBindViewHolder: "+modelArrayList.get(position).getBtnName());
        holder.button.setText(modelBtnFreq.getBtnName());
        holder.button.setCompoundDrawablesRelativeWithIntrinsicBounds(modelBtnFreq.getMenuDrawable(), null, null, null);
        if(modelBtnFreq.getBtnStatus().equalsIgnoreCase(default_values.DEF_STATUS_CHECKED)){
            holder.button.setTextSize(textSize.TEXT_SIZE_30);
        }else{
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
            button=itemView.findViewById(R.id.btnF);
            button.setOnClickListener(this);
            //itemView.setOnClickListener(this);
            Log.d(TAG, "ViewHolder: ");
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick: ");
            RecyclerLocations recyclerLocations = new RecyclerLocations();
            int pos = getAdapterPosition();
            //Log.d(TAG, "onClick: pos:"+pos+". loc:"+recyclerLocations.LOCATION_VIB_FREQ);
            recyclerViewClickInterface.onItemPostSelect(pos, recyclerLocations.LOCATION_VIB_FREQ);
        }
    }

    private void btnCheck(ViewHolder viewHolder, Button button){
        if(viewHolder!=null) {
        }
    }
}
