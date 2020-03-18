package com.fieldforce.ui.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.fieldforce.R;
import com.fieldforce.utility.App;

import java.util.ArrayList;
import java.util.HashMap;

public class AssetReadingParametersAdapter extends RecyclerView.Adapter<AssetReadingParametersAdapter.AssetReadingParametersHolder> {

    private Context mContext;
    private ArrayList<String> readingParameterList;
    public static ArrayList<String> edtReadingParamsList = new ArrayList<>();
    public static HashMap<String, CharSequence> hashMap = new HashMap<>();

    public AssetReadingParametersAdapter(Context context, ArrayList<String> assestsReadingParametersList) {
        this.mContext = context;
        this.readingParameterList = assestsReadingParametersList;
    }

    @Override
    public AssetReadingParametersHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_reading_parameter_layout, null);
        AssetReadingParametersHolder viewHolder = new AssetReadingParametersHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final AssetReadingParametersHolder holder, final int position) {
        holder.edtReadingParameters.setHint(readingParameterList.get(position));
        edtReadingParamsList.add(holder.edtReadingParameters.getText().toString());

        holder.edtReadingParameters.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() > 0) {
                    hashMap.put(readingParameterList.get(position), charSequence);
                } else {
                    hashMap.remove(readingParameterList.get(position));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @Override
    public int getItemCount() {
        if (readingParameterList != null && readingParameterList.size() > 0) {
            return readingParameterList.size();
        } else {
            return 0;
        }

    }

    public class AssetReadingParametersHolder extends RecyclerView.ViewHolder {
        private EditText edtReadingParameters;

        public AssetReadingParametersHolder(View itemView) {
            super(itemView);
            Typeface mFontRegular = App.getMontserratRegularFont();

            edtReadingParameters = itemView.findViewById(R.id.edt_reading);
            edtReadingParameters.setTypeface(mFontRegular);
        }
    }
}
