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
import Model.ModelScan;

public class RecyclerViewAdapScan extends RecyclerView.Adapter<RecyclerViewAdapScan.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapScan";

    public RecyclerViewAdapScan() {
    }

    public RecyclerViewAdapScan(RecyclerViewClickInterface recyclerViewClickInterface, ArrayList<ModelScan> modelScanArrayList) {
        this.recyclerViewClickInterface = recyclerViewClickInterface;
        this.modelScanArrayList = modelScanArrayList;
    }

    RecyclerViewClickInterface recyclerViewClickInterface;
    ArrayList<ModelScan> modelScanArrayList;


    @NonNull
    @Override
    public RecyclerViewAdapScan.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_scan, parent, false);
        RecyclerViewAdapScan.ViewHolder holder = new RecyclerViewAdapScan.ViewHolder(view);
        if (holder != null) {
            Log.d(TAG, "onCreateViewHolder: Ready!");
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapScan.ViewHolder holder, int position) {
        ModelScan modelScan=modelScanArrayList.get(position);
        holder.tvname.setText(modelScan.getDeviceName());
        holder.tvRssi.setText(modelScan.getDevRssi());
        holder.tvUUid.setText(modelScan.getDevUIID());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value=holder.tvname.getText().toString();
                recyclerViewClickInterface.onItemPostSelect(holder.getAdapterPosition(),value);
            }
        });


        Log.d(TAG, "onBindViewHolder: ");
    }

    @Override
    public int getItemCount() {
        return modelScanArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView tvname;
        TextView tvUUid;
        TextView tvRssi;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvname=itemView.findViewById(R.id.tvScanName);
            tvUUid=itemView.findViewById(R.id.tvScanUUID);
            tvRssi=itemView.findViewById(R.id.tvSvanRssi);
        }




    }
}
