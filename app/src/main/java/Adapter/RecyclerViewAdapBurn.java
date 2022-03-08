package Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.k9_pxz.R;

import java.util.ArrayList;

import Interface.RecyclerViewClickInterface;
import Model.ModelBurn;
import Model.ModelScan;

public class RecyclerViewAdapBurn extends RecyclerView.Adapter<RecyclerViewAdapBurn.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapBurn";
    RecyclerViewClickInterface recyclerViewClickInterface;
    ArrayList<ModelBurn> modelBurnsArrayList;

    public RecyclerViewAdapBurn(RecyclerViewClickInterface recyclerViewClickInterface) {
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }

    public RecyclerViewAdapBurn() {
    }

    public RecyclerViewAdapBurn(RecyclerViewClickInterface recyclerViewClickInterface, ArrayList<ModelBurn> modelBurnsArrayList) {
        this.recyclerViewClickInterface = recyclerViewClickInterface;
        this.modelBurnsArrayList = modelBurnsArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapBurn.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_burning, parent, false);
        RecyclerViewAdapBurn.ViewHolder holder = new RecyclerViewAdapBurn.ViewHolder(view);
        if (holder != null) {
            Log.d(TAG, "onCreateViewHolder RecyclerViewAdapBurn: Ready!");
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapBurn.ViewHolder holder, int position) {
        ModelBurn modelBurn= modelBurnsArrayList.get(position);
        holder.tvModName.setText(modelBurn.getModName());
        holder.tvModFreq.setText(modelBurn.getModFreq());
        holder.tvModTime.setText(modelBurn.getModTime());
        holder.tvModInt.setText(modelBurn.getModInt());
        holder.tvModCycles.setText(modelBurn.getModCycles());
        holder.tvModStatus.setText(modelBurn.getModStatus());
    }

    @Override
    public int getItemCount() {
        return modelBurnsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvModName;
        TextView tvModFreq;
        TextView tvModTime;
        TextView tvModInt;
        TextView tvModCycles;
        TextView tvModStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvModName=itemView.findViewById(R.id.tvBurnModule);
            tvModFreq=itemView.findViewById(R.id.tvBurnFreq);
            tvModTime=itemView.findViewById(R.id.tvBurnTime);
            tvModInt=itemView.findViewById(R.id.tvBurnInt);
            tvModCycles=itemView.findViewById(R.id.tvBurnCycle);
            tvModStatus=itemView.findViewById(R.id.tvBurnStatus);
        }

    }
}
