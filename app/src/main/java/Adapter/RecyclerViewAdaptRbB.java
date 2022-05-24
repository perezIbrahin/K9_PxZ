package Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.k9_pxz.R;

import java.util.ArrayList;

import Interface.RecyclerViewClickInterface;
import Model.ModelBtn;
import Util.Default_values;
import Util.RecyclerLocations;
import Util.TextSize;

public class RecyclerViewAdaptRbB extends RecyclerView.Adapter<RecyclerViewAdaptRbB.ViewHolder> {
    private static final String TAG = "RecyclerViewAdaptRbB";

    RecyclerViewClickInterface recyclerViewClickInterface;
    private ArrayList<ModelBtn> modelArrayList;

    public RecyclerViewAdaptRbB() {
    }

    public RecyclerViewAdaptRbB(RecyclerViewClickInterface recyclerViewClickInterface, ArrayList<ModelBtn> modelArrayList) {
        this.recyclerViewClickInterface = recyclerViewClickInterface;
        this.modelArrayList = modelArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewAdaptRbB.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_radiobutton, parent, false);
        RecyclerViewAdaptRbB.ViewHolder holder = new RecyclerViewAdaptRbB.ViewHolder(view);
        if (holder != null) {
            Log.d(TAG, "onCreateViewHolder: Ready!");
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Default_values default_values = new Default_values();
        TextSize textSize = new TextSize();

        ModelBtn modelBtn = modelArrayList.get(position);
        Log.d(TAG, "onBindViewHolder: RbB name " + modelArrayList.get(position).getBtnName());
        holder.radioButton.setText(modelBtn.getBtnName());
        holder.radioButton.setButtonDrawable(modelBtn.getMenuDrawable());
    }


    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RadioButton radioButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton=itemView.findViewById(R.id.rb);
            radioButton.setOnClickListener(this);
            //itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            RecyclerLocations recyclerLocations = new RecyclerLocations();
            int pos = getAdapterPosition();
            recyclerViewClickInterface.onItemPostSelect(pos, recyclerLocations.LOCATION_RB_B);
        }
    }
}
