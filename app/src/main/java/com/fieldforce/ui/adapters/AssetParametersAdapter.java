package com.fieldforce.ui.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fieldforce.R;
import com.fieldforce.utility.App;

import java.util.ArrayList;
import java.util.HashMap;

public class AssetParametersAdapter extends RecyclerView.Adapter<AssetParametersAdapter.AssetParametersHolder> {

    private Context mContext;
    private ArrayList<String> assets_parametersList;
    public static HashMap<String, CharSequence> hashMap = new HashMap<>();
    private String screen;

    private LinearLayoutManager layoutManager;

    public AssetParametersAdapter(Context context, ArrayList<String> assestsParametersList, String screen) {
        this.mContext = context;
        this.assets_parametersList = assestsParametersList;
        this.screen = screen;
    }

    @Override
    public AssetParametersHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_work, null);
        AssetParametersHolder viewHolder = new AssetParametersHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final AssetParametersHolder holder, final int position) {

        if (!assets_parametersList.get(position).equals("Consumer consent form")) {
            holder.txtParameters.setText(assets_parametersList.get(position));
        } else {
            holder.txtParameters.setText(assets_parametersList.get(position));
            holder.linearDropdown.setVisibility(View.GONE);
        }


        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                hashMap.put(holder.txtParameters.getText().toString(), String.valueOf(isChecked));
                if (assets_parametersList.get(position).equals("Consumer consent form")) {
                    if (isChecked) {
                        holder.linearDropdown.setVisibility(View.GONE); // TODO change to visible when changing for remark dropdown
                    } else {
                        holder.linearDropdown.setVisibility(View.GONE);
                    }
                        /*if (subSopHolder.linearDropdown.getVisibility() == View.VISIBLE) {
                            if (!selectedRemarkName.equals("Select Remark*")) {
                                hashMapSopRemark.put(sop_list.get(i).sopId, selectedRemark);
                            }
                            sopRemark.put(selectedRemark, selectedRemarkName);
                        }*/
                }
            }

        });

        if (!hashMap.values().equals(holder.txtParameters.getText().toString())) {
            hashMap.put(holder.txtParameters.getText().toString(), "false");
        }
    }

    @Override
    public int getItemCount() {
        if (assets_parametersList != null && assets_parametersList.size() > 0) {
            return assets_parametersList.size();
        } else {
            return 0;
        }
    }

    public class AssetParametersHolder extends RecyclerView.ViewHolder {

        private CheckBox checkBox;
        private TextView txtParameters;
        private RecyclerView recyclerViewSubSop;
        private Typeface mFontRegular;
        private LinearLayout linearDropdown;
        private Spinner spinnerRemark;

        public AssetParametersHolder(View itemView) {
            super(itemView);

            mFontRegular = App.getMontserratRegularFont();

            checkBox = itemView.findViewById(R.id.chk_box);
            txtParameters = itemView.findViewById(R.id.txt_parameter);
            txtParameters.setTypeface(mFontRegular);


            linearDropdown = itemView.findViewById(R.id.linear_dropdown);
            spinnerRemark = itemView.findViewById(R.id.spinner_remark);

//            recyclerViewSubSop = itemView.findViewById(R.id.recycler_view_sub_sop);

        }
    }
}
