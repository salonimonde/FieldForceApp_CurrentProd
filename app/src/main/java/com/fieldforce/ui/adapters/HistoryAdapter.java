package com.fieldforce.ui.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldforce.R;
import com.fieldforce.models.HistoryModel;
import com.fieldforce.utility.App;
import com.fieldforce.utility.AppConstants;
import com.fieldforce.utility.CommonUtility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryHolder> {

    private Context mContext;
    private String screenName = "";
    private ArrayList<HistoryModel> historyModelArrayList = new ArrayList<>();
    private Typeface mRegularBold, mRegularMedium, mRegular;

    public HistoryAdapter(Context context, String screenName, ArrayList<HistoryModel> historyModelArrayList) {
        this.mContext = context;
        this.screenName = screenName;
        this.historyModelArrayList = historyModelArrayList;
    }

    public HistoryAdapter() {
    }

    @Override
    public HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_history, null);
        HistoryHolder viewHolder = new HistoryHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HistoryHolder holder, int position) {
//        CommonUtility.setAnimation(holder.itemView, position, -1, mContext);
        if (screenName.equals(CommonUtility.getString(mContext, R.string.enquiry))) {
            holder.relativeModule.setBackgroundColor(CommonUtility.getColor(mContext, R.color.colorMenuCream));
            holder.txtYear.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuCream));
            holder.txtOpenTask.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuCream));
            if (historyModelArrayList.get(position).getStatus().equals(AppConstants.CARD_STATUS_COMPLETED)){
                holder.imageCheck.setVisibility(View.GONE);
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.relativeModule.getLayoutParams();
                params.leftMargin = 40;

            } else {
                holder.imageCheck.setVisibility(View.VISIBLE);
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.site_verification))) {
            holder.relativeModule.setBackgroundColor(CommonUtility.getColor(mContext, R.color.colorMenuBlue));
            holder.txtYear.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuBlue));
            holder.txtOpenTask.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuBlue));
            if (historyModelArrayList.get(position).getStatus().equals(AppConstants.CARD_STATUS_CLOSED)){
                holder.imageCheck.setVisibility(View.VISIBLE);
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.installation))) {
            holder.relativeModule.setBackgroundColor(CommonUtility.getColor(mContext, R.color.colorMenuGrey));
            holder.txtYear.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuGrey));
            holder.txtOpenTask.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuGrey));
            if (historyModelArrayList.get(position).getStatus().equals(AppConstants.CARD_STATUS_CLOSED)){
                holder.imageCheck.setVisibility(View.VISIBLE);
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.convert))) {
            holder.relativeModule.setBackgroundColor(CommonUtility.getColor(mContext, R.color.colorMenuOrange));
            holder.txtYear.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuOrange));
            holder.txtOpenTask.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuOrange));
            if (historyModelArrayList.get(position).getStatus().equals(AppConstants.CARD_STATUS_CLOSED)){
                holder.imageCheck.setVisibility(View.VISIBLE);
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.asset_indexing))) {
            holder.relativeModule.setBackgroundColor(CommonUtility.getColor(mContext, R.color.colorMenuCream));
            holder.txtYear.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuCream));
            holder.txtOpenTask.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuCream));
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.services))) {
            holder.relativeModule.setBackgroundColor(CommonUtility.getColor(mContext, R.color.colorMenuCream));
            holder.txtYear.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuCream));
            holder.txtOpenTask.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuCream));
            if (historyModelArrayList.get(position).getStatus().equals(AppConstants.CARD_STATUS_CLOSED)){
                holder.imageCheck.setVisibility(View.VISIBLE);
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.complaint))) {
            holder.relativeModule.setBackgroundColor(CommonUtility.getColor(mContext, R.color.colorMenuBlue));
            holder.txtYear.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuBlue));
            holder.txtOpenTask.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuBlue));
            if (historyModelArrayList.get(position).getStatus().equals(AppConstants.CARD_STATUS_CLOSED)){
                holder.imageCheck.setVisibility(View.VISIBLE);
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.preventive))) {
            holder.relativeModule.setBackgroundColor(CommonUtility.getColor(mContext, R.color.colorMenuGrey));
            holder.txtYear.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuGrey));
            holder.txtOpenTask.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuGrey));
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.breakdown))) {
            holder.relativeModule.setBackgroundColor(CommonUtility.getColor(mContext, R.color.colorMenuOrange));
            holder.txtYear.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuOrange));
            holder.txtOpenTask.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuOrange));
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.commissioning))) {
            holder.relativeModule.setBackgroundColor(CommonUtility.getColor(mContext, R.color.colorMenuCream));
            holder.txtYear.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuCream));
            holder.txtOpenTask.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuCream));
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.decommissioning))) {
            holder.relativeModule.setBackgroundColor(CommonUtility.getColor(mContext, R.color.colorMenuBlue));
            holder.txtYear.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuBlue));
            holder.txtOpenTask.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuBlue));
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.recovery))) {
            holder.relativeModule.setBackgroundColor(CommonUtility.getColor(mContext, R.color.colorMenuGrey));
            holder.txtYear.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuGrey));
            holder.txtOpenTask.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuGrey));
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.all))) {
            String screen = historyModelArrayList.get(position).screen;
            if (screen.equals(CommonUtility.getString(mContext, R.string.enquiry))) {
                holder.relativeModule.setBackgroundColor(CommonUtility.getColor(mContext, R.color.colorMenuCream));
                holder.txtYear.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuCream));
                holder.txtOpenTask.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuCream));
                if (historyModelArrayList.get(position).getStatus().equals(AppConstants.CARD_STATUS_COMPLETED)){
                    holder.imageCheck.setVisibility(View.GONE);
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.relativeModule.getLayoutParams();
                    params.leftMargin = 40;

                } else {
                    holder.imageCheck.setVisibility(View.VISIBLE);
                }
            } else if (screen.equals(CommonUtility.getString(mContext, R.string.verification))) {
                holder.relativeModule.setBackgroundColor(CommonUtility.getColor(mContext, R.color.colorMenuBlue));
                holder.txtYear.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuBlue));
                holder.txtOpenTask.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuBlue));

                if (historyModelArrayList.get(position).getStatus().equals(AppConstants.CARD_STATUS_CLOSED)){
                    holder.imageCheck.setVisibility(View.VISIBLE);
                }
            } else if (screen.equals(CommonUtility.getString(mContext, R.string.installation))) {
                holder.relativeModule.setBackgroundColor(CommonUtility.getColor(mContext, R.color.colorMenuGrey));
                holder.txtYear.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuGrey));
                holder.txtOpenTask.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuGrey));
                if (historyModelArrayList.get(position).getStatus().equals(AppConstants.CARD_STATUS_CLOSED)){
                    holder.imageCheck.setVisibility(View.VISIBLE);
                }
            } else if (screen.equals(CommonUtility.getString(mContext, R.string.convert))) {
                holder.relativeModule.setBackgroundColor(CommonUtility.getColor(mContext, R.color.colorMenuOrange));
                holder.txtYear.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuOrange));
                holder.txtOpenTask.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuOrange));
                if (historyModelArrayList.get(position).getStatus().equals(AppConstants.CARD_STATUS_CLOSED)){
                    holder.imageCheck.setVisibility(View.VISIBLE);
                }
            } else if (screen.equals(CommonUtility.getString(mContext, R.string.services))) {
                holder.relativeModule.setBackgroundColor(CommonUtility.getColor(mContext, R.color.colorMenuCream));
                holder.txtYear.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuCream));
                holder.txtOpenTask.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuCream));
                if (historyModelArrayList.get(position).getStatus().equals(AppConstants.CARD_STATUS_CLOSED)){
                    holder.imageCheck.setVisibility(View.VISIBLE);
                }
            } else if (screen.equals(CommonUtility.getString(mContext, R.string.complaint))) {
                holder.relativeModule.setBackgroundColor(CommonUtility.getColor(mContext, R.color.colorMenuBlue));
                holder.txtYear.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuBlue));
                holder.txtOpenTask.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuBlue));
                if (historyModelArrayList.get(position).getStatus().equals(AppConstants.CARD_STATUS_CLOSED)){
                    holder.imageCheck.setVisibility(View.VISIBLE);
                }
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

            holder.txtDayMonth.setText(lastIndex);
            holder.txtYear.setText(outputDateStr.substring(outputDateStr.length() - 4, outputDateStr.length()));

        } catch (Exception e) {
        }

        String openCount = historyModelArrayList.get(position).getOpen();
        String completedCount = historyModelArrayList.get(position).getCompleted();
/*
        if (openCount.length() == 1)
            holder.txtOpenTask.setText("0" + openCount);
        else
            holder.txtOpenTask.setText(openCount);

        if (completedCount.length() == 1)
            holder.txtCompletedTask.setText("0" + completedCount);
        else
            holder.txtCompletedTask.setText(completedCount);*/

        holder.lblOpenTask.setText("ID # " + historyModelArrayList.get(position).getNscId() + " | " +
                historyModelArrayList.get(position).getScreen());
//        holder.lblCompletedTask.setText(historyModelArrayList.get(position).getScreen());
        holder.txtOpenTask.setText(historyModelArrayList.get(position).getName());
        holder.txtCompletedTask.setText(historyModelArrayList.get(position).getArea());
    }

    @Override
    public int getItemCount() {
        if (historyModelArrayList != null && historyModelArrayList.size() > 0) {
            return historyModelArrayList.size();
        } else {
            return 0;
        }
    }

    public class HistoryHolder extends RecyclerView.ViewHolder {

        private RelativeLayout relativeModule;
        private TextView txtDayMonth, txtYear, lblOpenTask, lblCompletedTask, txtOpenTask, txtCompletedTask;
        private Typeface mFontBold, mFontMedium, mFontRegular;
        private ImageView imageCheck;

        public HistoryHolder(View itemView) {
            super(itemView);

            imageCheck = itemView.findViewById(R.id.img_check);

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