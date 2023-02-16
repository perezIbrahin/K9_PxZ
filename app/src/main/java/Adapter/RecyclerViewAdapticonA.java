package Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.k9_pxz.R;

import java.util.ArrayList;

import Model.ModelBtn;
import Util.Default_values;
import Util.TextSize;

public class RecyclerViewAdapticonA extends RecyclerView.Adapter<RecyclerViewAdapticonA.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapticonA";
    private ArrayList<ModelBtn> modelArrayList;

    public RecyclerViewAdapticonA() {
    }

    public RecyclerViewAdapticonA(ArrayList<ModelBtn> modelArrayList) {
        this.modelArrayList = modelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_transducer, parent, false);
        RecyclerViewAdapticonA.ViewHolder holder = new RecyclerViewAdapticonA.ViewHolder(view);
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

        //
        final ConstraintLayout.LayoutParams layoutparams = (ConstraintLayout.LayoutParams) holder.imageView.getLayoutParams();

        switch (position) {
            case 0:
                setMargin(layoutparams, holder, 0, 20, 20, 0);//0,25,40,10
                break;
            case 1:
                setMargin(layoutparams, holder, 0, 30, 20, 0);
                break;
            case 2:
                setMargin(layoutparams, holder, 0, 50, 20, 0);
                break;
            case 3: setMargin(layoutparams, holder, 0, 65, 20, 0);
                break;
            case 4:
                setMargin(layoutparams, holder, 0, 72, 20, 0);
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
            imageView = itemView.findViewById(R.id.ivTransduccer);
            //imageView.setOnClickListener(this);
        }
    }

    private void setMargin(ConstraintLayout.LayoutParams params, ViewHolder holder, int left, int top, int right, int bot) {
        params.setMargins(left, top, right, bot);
        holder.imageView.setLayoutParams(params);
    }
}
