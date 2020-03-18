package com.fieldforce.ui.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fieldforce.R;
import com.fieldforce.models.CheckListModel;
import com.fieldforce.models.Consumer;
import com.fieldforce.models.SopModel;
import com.fieldforce.ui.activities.DetailsActivity;
import com.fieldforce.utility.App;
import com.fieldforce.utility.CommonUtility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static com.fieldforce.ui.adapters.SopAdapter.hashMapSubSop;
import static com.fieldforce.ui.adapters.SopAdapter.hashMapSubSopValue;
import static com.fieldforce.ui.adapters.SopAdapter.selectedRemark;
import static com.fieldforce.ui.adapters.SopAdapter.hashMapSopRemark;
import static com.fieldforce.ui.adapters.SopAdapter.selectedRemarkName;
import static com.fieldforce.ui.adapters.SopAdapter.sopRemark;

public class SubSopAdapter extends RecyclerView.Adapter<SubSopAdapter.SubSopHolder> {


    private Context mContext;
    private ArrayList<SopModel> sop_list;
    private ArrayList<CheckListModel> remarkList;
    private HashMap hashMapRemark = new HashMap<String, String>();
    private ArrayList<String> cityArray;
    private List<String> cityList;


    public SubSopAdapter(Context context, ArrayList<SopModel> sopList, ArrayList<CheckListModel> remarkList) {
        this.mContext = context;
        this.sop_list = sopList;
        this.remarkList = remarkList;
    }


    @NonNull
    @Override
    public SubSopHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_sop, null);
        SubSopHolder viewHolder = new SubSopHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final SubSopHolder subSopHolder, final int i) {


        hashMapRemark.clear();
        hashMapSopRemark.clear();
        sopRemark.clear();
        hashMapRemark.put("0", "Select Remark*");
        /*for (CheckListModel remarkModel : remarkList)
            hashMapRemark.put(remarkModel.remarkId, remarkModel.remark);*/



        subSopHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (sop_list.get(i).sop.equals("Consumer consent form")) {
                        subSopHolder.linearDropdown.setVisibility(View.VISIBLE);
                        setRemarkSpinner(subSopHolder, i);
                        /*if (subSopHolder.linearDropdown.getVisibility() == View.VISIBLE) {
                            if (!selectedRemarkName.equals("Select Remark*")) {
                                hashMapSopRemark.put(sop_list.get(i).sopId, selectedRemark);
                            }
                            sopRemark.put(selectedRemark, selectedRemarkName);
                        }*/
                    }
                    hashMapSubSop.put(sop_list.get(i).sopId, sop_list.get(i).sop);
                    hashMapSubSopValue.put(sop_list.get(i).sopId, "true");
                } else {
                    if (sop_list.get(i).sop.equals("Consumer consent form")) {
                        hashMapSopRemark.remove(sop_list.get(i).sopId);
                        sopRemark.remove(sop_list.get(i).sopId);
                        subSopHolder.linearDropdown.setVisibility(View.GONE);
                        setRemarkSpinner(subSopHolder, i);
                    }
                    hashMapSubSop.remove(sop_list.get(i).sopId);
                    hashMapSubSopValue.put(sop_list.get(i).sopId, "false");
                }
            }
        });


        if (sop_list.get(i).subFlag.equals("true")) {
            subSopHolder.checkBox.setChecked(true);
            subSopHolder.checkBox.setEnabled(false);
            hashMapSubSop.put(sop_list.get(i).sopId, sop_list.get(i).sop);
            hashMapSubSopValue.put(sop_list.get(i).sopId, "true");
        } else if (sop_list.get(i).subFlag.equals("false")) {
            subSopHolder.checkBox.setChecked(false);
            hashMapSubSop.remove(sop_list.get(i).sopId);
            hashMapSubSopValue.put(sop_list.get(i).sopId, "false");
        }
        if (!sop_list.get(i).sop.equals("Consumer consent form")) {
            subSopHolder.txtParameters.setText(sop_list.get(i).sop);
        } else {
            subSopHolder.txtParameters.setVisibility(View.GONE);
            subSopHolder.checkBox.setVisibility(View.GONE);
        }


    }


    @Override
    public int getItemCount() {

        if (sop_list.size() != 0 && sop_list.size() > 0) {
            return sop_list.size();
        } else {
            return 0;
        }
    }

    public class SubSopHolder extends RecyclerView.ViewHolder {

        private CheckBox checkBox;
        private TextView txtParameters;
        private Typeface mFontRegular;
        private LinearLayout linearDropdown;
        private Spinner spinnerRemark;

        public SubSopHolder(View itemView) {
            super(itemView);

            mFontRegular = App.getMontserratRegularFont();

            checkBox = itemView.findViewById(R.id.chk_box);
            txtParameters = itemView.findViewById(R.id.txt_parameter);
            txtParameters.setTypeface(mFontRegular);


            linearDropdown = itemView.findViewById(R.id.linear_dropdown);
            spinnerRemark = itemView.findViewById(R.id.spinner_remark);
        }
    }

    private TreeMap<String, String> sortByKey(HashMap<String, String> map) {
        // TreeMap to store values of HashMap
        TreeMap<String, String> sorted = new TreeMap<>();
        // Copy all data from hashMap into TreeMap
        sorted.putAll(map);
        return sorted;
    }

    private List getKeysFromValue(Map hm, Object value) {
        Set ref = hm.keySet();
        Iterator it = ref.iterator();
        List list = new ArrayList();

        while (it.hasNext()) {
            Object o = it.next();
            if (hm.get(o).equals(value)) {
                list.add(o);
            }
        }
        return list;
    }

    private void setRemarkSpinner(SubSopHolder subSopHolder, int i) {
        Collection<String> valueSet = null;
        cityArray = new ArrayList<>();
        if (hashMapRemark != null && hashMapRemark.size() > 0) {
            for (int j = 0; j < hashMapRemark.size(); j++) {
                valueSet = sortByKey(hashMapRemark).values();
                cityArray = new ArrayList<>(valueSet);

            }
        } else cityArray.add("Select Remark*");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, cityArray) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(subSopHolder.mFontRegular);
                ((TextView) v).setTextColor(CommonUtility.getColor(mContext, R.color.colorProfileEditText));
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(subSopHolder.mFontRegular);
                ((TextView) v).setTextColor(CommonUtility.getColor(mContext, R.color.colorBlack));
                return v;
            }
        };
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subSopHolder.spinnerRemark.setAdapter(dataAdapter);
        subSopHolder.spinnerRemark.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (hashMapRemark != null && hashMapRemark.size() > 0) {
                    cityList = getKeysFromValue(hashMapRemark, subSopHolder.spinnerRemark.getSelectedItem().toString());
                    selectedRemark = cityList.get(0);
                    selectedRemarkName = subSopHolder.spinnerRemark.getSelectedItem().toString();
                    if (!selectedRemarkName.equals("Select Remark*")) {
                        if (!hashMapSopRemark.containsValue(selectedRemark)) {
                            hashMapSopRemark.put(sop_list.get(i).sopId, selectedRemark);
                            if (!sopRemark.containsKey(sop_list.get(i).sopId)) {
                                sopRemark.put(sop_list.get(i).sopId, selectedRemarkName);
                            } else {
                                 if (!sopRemark.containsValue(selectedRemarkName)){
                                     sopRemark.put(sop_list.get(i).sopId, selectedRemarkName);
                                 }
                            }
                        }
                    } else {
                        if (!sopRemark.containsKey(sop_list.get(i).sopId)) {
                            sopRemark.put(sop_list.get(i).sopId, selectedRemarkName);
                        }  else {
                            if (!sopRemark.containsValue(selectedRemarkName)){
                                sopRemark.put(sop_list.get(i).sopId, selectedRemarkName);
                            }
                        }
                        if (!hashMapSopRemark.containsValue(selectedRemark)){
                            hashMapSopRemark.put(sop_list.get(i).sopId, selectedRemark);
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

    }
}
