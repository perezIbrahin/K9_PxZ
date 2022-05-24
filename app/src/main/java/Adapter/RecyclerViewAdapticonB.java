package Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.k9_pxz.R;

import java.util.ArrayList;

import Model.ModelBtn;
import Util.Default_values;
import Util.TextSize;

public class RecyclerViewAdapticonB extends RecyclerView.Adapter<RecyclerViewAdapticonB.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapticonB";
    private ArrayList<ModelBtn> modelArrayList;

    public RecyclerViewAdapticonB() {
    }

    public RecyclerViewAdapticonB(ArrayList<ModelBtn> modelArrayList) {
        this.modelArrayList = modelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_transducer, parent, false);
        RecyclerViewAdapticonB.ViewHolder holder = new RecyclerViewAdapticonB.ViewHolder(view);
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
        holder.imageView.setImageDrawable(modelBtn.getMenuDrawable());
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.ivTransduccer);
        }
    }
}
