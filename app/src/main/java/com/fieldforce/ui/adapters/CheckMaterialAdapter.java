package com.fieldforce.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.fieldforce.R;
import com.fieldforce.models.TodayModel;

import java.util.ArrayList;
import java.util.HashMap;

public class CheckMaterialAdapter extends RecyclerView.Adapter<CheckMaterialAdapter.CheckMaterialHolder> {

    private Context mContext;
    private ArrayList<TodayModel> assets_parametersList;
    public static HashMap<String, CharSequence> materialList = new HashMap<>();

    public CheckMaterialAdapter(Context context, ArrayList<TodayModel> assestsParametersList) {
        this.mContext = context;
        this.assets_parametersList = assestsParametersList;
    }

    @Override
    public CheckMaterialHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_material_list_layout, null);
        CheckMaterialHolder viewHolder = new CheckMaterialHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CheckMaterialHolder holder, final int position) {


        holder.txtMaterialName.setText(assets_parametersList.get(position).getMaterial_name());
        holder.txtCount.setText(assets_parametersList.get(position).getMaterial_count());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {

                } else {


                }
            }

        });


    }

    @Override
    public int getItemCount() {
        if (assets_parametersList != null && assets_parametersList.size() > 0) {
            return assets_parametersList.size();
        } else {

            return 0;
        }

    }

    public class CheckMaterialHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBox;
        private TextView txtMaterialName, txtCount;

        public CheckMaterialHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.card_check_material);
            txtMaterialName = itemView.findViewById(R.id.txt_material_name);
            txtCount = itemView.findViewById(R.id.txt_count);
        }
    }
}
