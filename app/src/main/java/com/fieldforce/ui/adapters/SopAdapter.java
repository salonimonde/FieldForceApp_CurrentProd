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
import android.widget.TextView;

import com.fieldforce.R;
import com.fieldforce.models.CheckListModel;
import com.fieldforce.utility.App;

import java.util.ArrayList;
import java.util.HashMap;

public class SopAdapter extends RecyclerView.Adapter<SopAdapter.SopHolder> {

    private Context mContext;
    private ArrayList<CheckListModel> configuration;
    private ArrayList<CheckListModel> remarkList;
    public static HashMap<String, String> hashMap = new HashMap<>();
    public static HashMap<String, String> hashMapSubSop = new HashMap<>();
    public static HashMap<String, String> hashMapSubSopValue = new HashMap<>();
    public static HashMap<String, String> hashMapSopRemark = new HashMap<>();
    public static String selectedRemark = "";
    public static String selectedRemarkName = "";
    public static HashMap<String, String> sopRemark = new HashMap<>();
    public SubSopAdapter subSopAdapter;
    private Boolean isChanged = false;
    private Boolean isConfigChecked;


    public SopAdapter(Context context, ArrayList<CheckListModel> config, ArrayList<CheckListModel> remarkList) {
        this.mContext = context;
        this.configuration = config;
        this.remarkList = remarkList;
    }

    public SopAdapter(Boolean isChanged){
        this.isChanged = isChanged;
        notifyDataSetChanged();
    }

    @Override
    public SopHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_work, null);
        SopHolder viewHolder = new SopHolder(view);
        return viewHolder;
    }

    public void onBindViewHolder(final SopHolder sopHolder, final int i) {

         isConfigChecked = Boolean.parseBoolean(configuration.get(i).mainFlag);



        subSopAdapter = new SubSopAdapter(mContext, configuration.get(i).subConfiguration, remarkList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        sopHolder.recyclerViewSubSop.setLayoutManager(layoutManager);
        subSopAdapter.notifyDataSetChanged();

        sopHolder.txtParameters.setText(configuration.get(i).configuration);

        sopHolder.checkBox.setVisibility(View.GONE);
        sopHolder.recyclerViewSubSop.setAdapter(subSopAdapter);
    }

    @Override
    public int getItemCount() {

        if (configuration != null && configuration.size() > 0) {
            return configuration.size();
        } else
            return 0;

    }

    public class SopHolder extends RecyclerView.ViewHolder {

        private CheckBox checkBox;
        private TextView txtParameters;
        private RecyclerView recyclerViewSubSop;


        public SopHolder(View itemView) {
            super(itemView);

            Typeface mFontRegular = App.getMontserratRegularFont();

            checkBox = itemView.findViewById(R.id.chk_box);
            txtParameters = itemView.findViewById(R.id.txt_parameter);
            txtParameters.setTypeface(mFontRegular);

            recyclerViewSubSop = itemView.findViewById(R.id.recycler_view_sub_sop);
        }
    }
}
