package com.fieldforce.ui.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.fieldforce.R;
import com.fieldforce.models.Consumer;
import com.fieldforce.utility.App;

import java.util.ArrayList;

public class DocumentIdAdapter extends RecyclerView.Adapter<DocumentIdAdapter.DocumentHolder> {

    private Context mContext;
    private ArrayList<Consumer> assetParametersList;
    public static ArrayList<String> checkParamsListId = new ArrayList<>();
    public static ArrayList<String> checkParamsListAddress = new ArrayList<>();
    private String type;
    private boolean isIdChange, isAddressChange;

    public DocumentIdAdapter(Context context, ArrayList<Consumer> arrayList, String type) {
        this.mContext = context;
        this.assetParametersList = arrayList;
        this.type = type;
        checkParamsListId.clear();
        checkParamsListAddress.clear();
        isIdChange = false;
        isAddressChange = false;
    }

    @Override
    public DocumentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_work, null);
        DocumentHolder viewHolder = new DocumentHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final DocumentHolder holder, final int position) {
        holder.txtParameter.setText(assetParametersList.get(position).getDocument());


        if (assetParametersList.get(position).getChecked() != null) {
            if (type.equalsIgnoreCase(mContext.getString(R.string.edit_id_proof))) {
                if (assetParametersList.get(position).getChecked().equalsIgnoreCase("true")) {
                    if (!checkParamsListId.contains(assetParametersList.get(position).getDocument()) && !isIdChange) {

                        holder.checkBox.setChecked(true);
                        holder.checkBox.setSelected(true);
                        checkParamsListId.add(assetParametersList.get(position).getDocument());
                    }
                }
            } else if (type.equalsIgnoreCase(mContext.getString(R.string.edit_add_proof))) {

                if (assetParametersList.get(position).getChecked().equalsIgnoreCase("true")) {
                    if (!checkParamsListAddress.contains(assetParametersList.get(position).getDocument()) && !isAddressChange) {
                        holder.checkBox.setChecked(true);
                        holder.checkBox.setSelected(true);
                        checkParamsListAddress.add(assetParametersList.get(position).getDocument());
                    }
                }
            }
        }

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (type.equalsIgnoreCase(mContext.getString(R.string.edit_id_proof))) {
                    if (isChecked == true) {
                        checkParamsListId.add(holder.txtParameter.getText().toString());
                    } else {
                        for (int i = 0; i < checkParamsListId.size(); i++) {
                            if (checkParamsListId.get(i).equalsIgnoreCase(assetParametersList.get(position).getDocument())) {
                                checkParamsListId.remove(i);
                                isIdChange = true;
                            }
                        }
                    }
                } else if (type.equalsIgnoreCase(mContext.getString(R.string.edit_add_proof))) {
                    if (isChecked == true) {
                        checkParamsListAddress.add(holder.txtParameter.getText().toString());
                    } else {
                        for (int i = 0; i < checkParamsListAddress.size(); i++) {
                            if (checkParamsListAddress.get(i).equalsIgnoreCase(assetParametersList.get(position).getDocument())) {
                                checkParamsListAddress.remove(i);
                                isAddressChange = true;
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (assetParametersList != null && assetParametersList.size() > 0) {
            return assetParametersList.size();
        } else {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class DocumentHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBox;
        private TextView txtParameter;

        public DocumentHolder(View itemView) {
            super(itemView);
            Typeface mFontRegular = App.getMontserratRegularFont();

            checkBox = itemView.findViewById(R.id.chk_box);
            txtParameter = itemView.findViewById(R.id.txt_parameter);
            txtParameter.setTypeface(mFontRegular);
        }
    }
}
