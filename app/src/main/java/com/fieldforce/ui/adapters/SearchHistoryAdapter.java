package com.fieldforce.ui.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldforce.R;
import com.fieldforce.models.HistoryModel;
import com.fieldforce.utility.App;
import com.fieldforce.utility.CommonUtility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.SearchHistoryHolder> {

    private Context mContext;
    private String screenName = "";
    private ArrayList<HistoryModel> historyModelArrayList = new ArrayList<>();
    private Typeface mRegularBold, mRegularMedium, mRegular;


    public SearchHistoryAdapter(Context context, String screenName, ArrayList<HistoryModel> historyModelArrayList) {
        this.mContext = context;
        this.screenName = screenName;
        this.historyModelArrayList = historyModelArrayList;
    }

    public SearchHistoryAdapter(Context context, String screenName) {
        this.mContext = context;
        this.screenName = screenName;
    }
    @NonNull
    @Override
    public SearchHistoryHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_history, null);
        SearchHistoryHolder viewHolder = new SearchHistoryHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHistoryHolder searchHistoryHolder, int position) {
        CommonUtility.setAnimation(searchHistoryHolder.itemView, position, -1, mContext);
        if (screenName.equals(CommonUtility.getString(mContext, R.string.enquiry))) {
            searchHistoryHolder.relativeModule.setBackgroundColor(CommonUtility.getColor(mContext, R.color.colorMenuCream));
            searchHistoryHolder.txtYear.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuCream));
            searchHistoryHolder.txtOpenTask.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuCream));
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.site_verification))) {
            searchHistoryHolder.relativeModule.setBackgroundColor(CommonUtility.getColor(mContext, R.color.colorMenuBlue));
            searchHistoryHolder.txtYear.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuBlue));
            searchHistoryHolder.txtOpenTask.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuBlue));
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.installation))) {
            searchHistoryHolder.relativeModule.setBackgroundColor(CommonUtility.getColor(mContext, R.color.colorMenuGrey));
            searchHistoryHolder.txtYear.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuGrey));
            searchHistoryHolder.txtOpenTask.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuGrey));
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.convert))) {
            searchHistoryHolder.relativeModule.setBackgroundColor(CommonUtility.getColor(mContext, R.color.colorMenuOrange));
            searchHistoryHolder.txtYear.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuOrange));
            searchHistoryHolder.txtOpenTask.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuOrange));
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.asset_indexing))) {
            searchHistoryHolder.relativeModule.setBackgroundColor(CommonUtility.getColor(mContext, R.color.colorMenuCream));
            searchHistoryHolder.txtYear.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuCream));
            searchHistoryHolder.txtOpenTask.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuCream));
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.services))) {
            searchHistoryHolder.relativeModule.setBackgroundColor(CommonUtility.getColor(mContext, R.color.colorMenuCream));
            searchHistoryHolder.txtYear.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuCream));
            searchHistoryHolder.txtOpenTask.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuCream));
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.complaint))) {
            searchHistoryHolder.relativeModule.setBackgroundColor(CommonUtility.getColor(mContext, R.color.colorMenuBlue));
            searchHistoryHolder.txtYear.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuBlue));
            searchHistoryHolder.txtOpenTask.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuBlue));
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.preventive))) {
            searchHistoryHolder.relativeModule.setBackgroundColor(CommonUtility.getColor(mContext, R.color.colorMenuGrey));
            searchHistoryHolder.txtYear.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuGrey));
            searchHistoryHolder.txtOpenTask.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuGrey));
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.breakdown))) {
            searchHistoryHolder.relativeModule.setBackgroundColor(CommonUtility.getColor(mContext, R.color.colorMenuOrange));
            searchHistoryHolder.txtYear.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuOrange));
            searchHistoryHolder.txtOpenTask.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuOrange));
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.commissioning))) {
            searchHistoryHolder.relativeModule.setBackgroundColor(CommonUtility.getColor(mContext, R.color.colorMenuCream));
            searchHistoryHolder.txtYear.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuCream));
            searchHistoryHolder.txtOpenTask.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuCream));
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.decommissioning))) {
            searchHistoryHolder.relativeModule.setBackgroundColor(CommonUtility.getColor(mContext, R.color.colorMenuBlue));
            searchHistoryHolder.txtYear.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuBlue));
            searchHistoryHolder.txtOpenTask.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuBlue));
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.recovery))) {
            searchHistoryHolder.relativeModule.setBackgroundColor(CommonUtility.getColor(mContext, R.color.colorMenuGrey));
            searchHistoryHolder.txtYear.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuGrey));
            searchHistoryHolder.txtOpenTask.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuGrey));
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.all))) {
            String screen = historyModelArrayList.get(position).screen;
            if (screen.equals(CommonUtility.getString(mContext, R.string.enquiry))) {
                searchHistoryHolder.relativeModule.setBackgroundColor(CommonUtility.getColor(mContext, R.color.colorMenuCream));
                searchHistoryHolder.txtYear.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuCream));
                searchHistoryHolder.txtOpenTask.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuCream));
            } else if (screen.equals(CommonUtility.getString(mContext, R.string.verification))) {
                searchHistoryHolder.relativeModule.setBackgroundColor(CommonUtility.getColor(mContext, R.color.colorMenuBlue));
                searchHistoryHolder.txtYear.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuBlue));
                searchHistoryHolder.txtOpenTask.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuBlue));
            } else if (screen.equals(CommonUtility.getString(mContext, R.string.installation))) {
                searchHistoryHolder.relativeModule.setBackgroundColor(CommonUtility.getColor(mContext, R.color.colorMenuGrey));
                searchHistoryHolder.txtYear.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuGrey));
                searchHistoryHolder.txtOpenTask.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuGrey));
            } else if (screen.equals(CommonUtility.getString(mContext, R.string.convert))) {
                searchHistoryHolder.relativeModule.setBackgroundColor(CommonUtility.getColor(mContext, R.color.colorMenuOrange));
                searchHistoryHolder.txtYear.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuOrange));
                searchHistoryHolder.txtOpenTask.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuOrange));
            } else if (screen.equals(CommonUtility.getString(mContext, R.string.services))) {
                searchHistoryHolder.relativeModule.setBackgroundColor(CommonUtility.getColor(mContext, R.color.colorMenuCream));
                searchHistoryHolder.txtYear.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuCream));
                searchHistoryHolder.txtOpenTask.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuCream));
            } else if (screen.equals(CommonUtility.getString(mContext, R.string.complaint))) {
                searchHistoryHolder.relativeModule.setBackgroundColor(CommonUtility.getColor(mContext, R.color.colorMenuBlue));
                searchHistoryHolder.txtYear.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuBlue));
                searchHistoryHolder.txtOpenTask.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuBlue));
            }

        }

        try {
            DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
            String inputDateStr = historyModelArrayList.get(position).getDate();
            Date date = inputFormat.parse(inputDateStr);
            String outputDateStr = outputFormat.format(date);

            String lastIndex = outputDateStr.substring(0, outputDateStr.length() - 5);
            lastIndex = lastIndex.replace(" ", "\n");

            searchHistoryHolder.txtDayMonth.setText(lastIndex);
            searchHistoryHolder.txtYear.setText(outputDateStr.substring(outputDateStr.length() - 4, outputDateStr.length()));

        } catch (Exception e) {
        }

        String openCount = historyModelArrayList.get(position).getOpen();
        String completedCount = historyModelArrayList.get(position).getCompleted();
/*
        if (openCount.length() == 1)
            searchHistoryHolder.txtOpenTask.setText("0" + openCount);
        else
            searchHistoryHolder.txtOpenTask.setText(openCount);

        if (completedCount.length() == 1)
            searchHistoryHolder.txtCompletedTask.setText("0" + completedCount);
        else
            searchHistoryHolder.txtCompletedTask.setText(completedCount);*/

        searchHistoryHolder.lblOpenTask.setText("ID # " + historyModelArrayList.get(position).getNscId() + " | " +
                historyModelArrayList.get(position).getScreen());
//        searchHistoryHolder.lblCompletedTask.setText(historyModelArrayList.get(position).getScreen());
        searchHistoryHolder.txtOpenTask.setText(historyModelArrayList.get(position).getName());
        searchHistoryHolder.txtCompletedTask.setText(historyModelArrayList.get(position).getArea());

    }

    @Override
    public int getItemCount() {
        if (historyModelArrayList != null && historyModelArrayList.size() > 0) {
            return historyModelArrayList.size();
        } else {
            return 0;
        }
    }

    public class SearchHistoryHolder extends RecyclerView.ViewHolder {


        private RelativeLayout relativeModule;
        private TextView txtDayMonth, txtYear, lblOpenTask, lblCompletedTask, txtOpenTask, txtCompletedTask;
        private Typeface mFontBold, mFontMedium, mFontRegular;

        public SearchHistoryHolder(View itemView) {
            super(itemView);

            mFontBold = App.getMontserratBoldFont();
            mFontMedium = App.getMontserratMediumFont();
            mFontRegular = App.getMontserratRegularFont();

            relativeModule = itemView.findViewById(R.id.relative_module);
            lblOpenTask = itemView.findViewById(R.id.lbl_open_task);
//            lblCompletedTask = itemView.findViewById(R.id.lbl_completed_task);

            txtDayMonth = itemView.findViewById(R.id.txt_day_month);
            txtYear = itemView.findViewById(R.id.txt_year);
            txtOpenTask = itemView.findViewById(R.id.txt_open_task);
            txtCompletedTask = itemView.findViewById(R.id.txt_completed_task);

            lblOpenTask.setTypeface(mFontRegular);
//            lblCompletedTask.setTypeface(mFontRegular);

            txtDayMonth.setTypeface(mFontRegular);
            txtYear.setTypeface(mFontRegular);
            txtOpenTask.setTypeface(mRegularBold);
            txtCompletedTask.setTypeface(mFontRegular);

        }
    }
}

