package com.example.schedulemydiet.viewholders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.schedulemydiet.R;

public class LabelsViewHolder extends RecyclerView.ViewHolder {

    public TextView title1;
    public TextView title2;
    public LinearLayout layout;


    public LabelsViewHolder(@NonNull View itemView) {
        super(itemView);
        title1 = itemView.findViewById(R.id.id_label_list_adapter_label1);
        title2 = itemView.findViewById(R.id.id_label_list_adapter_label2);
        layout = itemView.findViewById(R.id.id_label_list_adapter_linearLayout);
    }
}