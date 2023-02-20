package Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
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

        final ConstraintLayout.LayoutParams layoutparams = (ConstraintLayout.LayoutParams) holder.imageView.getLayoutParams();

        switch (position) {
            case 0:
                setMargin(layoutparams, holder, 17, 20, 0, 0);//0,25,40,10
                break;
            case 1:
                setMargin(layoutparams, holder, 17, 30, 0, 0);
                break;
            case 2:
                setMargin(layoutparams, holder, 17, 50, 0, 0);
                break;
            case 3: setMargin(layoutparams, holder, 17, 65, 0, 0);
                break;
            case 4:
                setMargin(layoutparams, holder, 17, 72, 0, 0);
                break;
            case 5:
                setMargin(layoutparams, holder, 0, 0, 0, 5);
                break;
        }
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

    private void setMargin(ConstraintLayout.LayoutParams params, RecyclerViewAdapticonB.ViewHolder holder, int left, int top, int right, int bot) {
        params.setMargins(left, top, right, bot);
        holder.imageView.setLayoutParams(params);
    }
}
