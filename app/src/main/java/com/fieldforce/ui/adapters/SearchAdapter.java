package com.fieldforce.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldforce.R;
import com.fieldforce.models.RegistrationModel;
import com.fieldforce.models.TodayModel;
import com.fieldforce.ui.activities.DetailsActivity;
import com.fieldforce.ui.activities.DetailsActivity2;
import com.fieldforce.ui.activities.RegistrationFormActivity;
import com.fieldforce.utility.App;
import com.fieldforce.utility.AppConstants;
import com.fieldforce.utility.CommonUtility;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchHolder> {

    private Context mContext;
    private String screenName = "";
    private ArrayList<TodayModel> mTodayModelCards = new ArrayList<>();
    private ArrayList<RegistrationModel> mRegistrationModelCards = new ArrayList<>();

    public SearchAdapter(Context context, String screenName, ArrayList<TodayModel> mTodayModelCards) {
        this.mContext = context;
        this.screenName = screenName;
        this.mTodayModelCards = mTodayModelCards;
    }
    public SearchAdapter(Context context,ArrayList<RegistrationModel> mRegistrationModelCards, String screenName) {
        this.mContext = context;
        this.screenName = screenName;
        this.mRegistrationModelCards = mRegistrationModelCards;
    }

    public SearchAdapter(Context context, String screenName) {
        this.mContext = context;
        this.screenName = screenName;
    }

    @Override
    public SearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_open, null);
        SearchHolder viewHolder = new SearchHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SearchHolder holder, final int position) {
        CommonUtility.setAnimation(holder.itemView, position, -1, mContext);

        if (screenName.equals(CommonUtility.getString(mContext, R.string.enquiry))) {
            holder.imgModule.setImageResource(R.drawable.ic_action_all);
            holder.txtJobIdDate.setText(mContext.getString(R.string.id) + " #" + mTodayModelCards.get(position).getEnquiryNumber()
                    + " | " + mTodayModelCards.get(position).getDueDate());
            holder.txtJobName.setText(mTodayModelCards.get(position).getConsumerName());
            holder.txtJobName.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuCream));
            holder.txtJobAddress.setText(mTodayModelCards.get(position).getArea());

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, RegistrationFormActivity.class);
                    intent.putExtra(AppConstants.COMING_FROM, mContext.getString(R.string.edit_nsc));
                    intent.putExtra(AppConstants.JOB_ID, mTodayModelCards.get(position).getJob_id());
                    intent.putExtra(AppConstants.MODEL, mTodayModelCards.get(position));
                    mContext.startActivity(intent);
                }
            });
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.site_verification))) {
            holder.imgModule.setImageResource(R.drawable.ic_action_site_verification);
            holder.txtJobIdDate.setText(mContext.getString(R.string.id) + " #" + mTodayModelCards.get(position).getRequestId()
                    + " | " + mTodayModelCards.get(position).getDueDate());
            holder.txtJobName.setText(mTodayModelCards.get(position).getConsumerName());
            holder.txtJobName.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuBlue));
            holder.txtJobAddress.setText(mTodayModelCards.get(position).getAddress());

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, DetailsActivity.class);
                    intent.putExtra(AppConstants.COMING_FROM, screenName);
                    intent.putExtra(AppConstants.JOB_ID, mTodayModelCards.get(position).getSite_verification_id());
                    intent.putExtra(AppConstants.MODEL, mTodayModelCards.get(position));
                    mContext.startActivity(intent);
                }
            });
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.installation))) {
            holder.imgModule.setImageResource(R.drawable.ic_action_meter_installation);
            holder.txtJobIdDate.setText(mContext.getString(R.string.id) + " #" + mTodayModelCards.get(position).getRequestId()
                    + " | " + mTodayModelCards.get(position).getDueDate());
            holder.txtJobName.setText(mTodayModelCards.get(position).getConsumerName());
            holder.txtJobName.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuGrey));
            holder.txtJobAddress.setText(mTodayModelCards.get(position).getAddress());

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, DetailsActivity.class);
                    intent.putExtra(AppConstants.COMING_FROM, screenName);
                    intent.putExtra(AppConstants.JOB_ID, mTodayModelCards.get(position).getMeterInstallationId());
                    intent.putExtra(AppConstants.MODEL, mTodayModelCards.get(position));
                    mContext.startActivity(intent);
                }
            });
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.convert))) {
            holder.imgModule.setImageResource(R.drawable.ic_action_convert);
            holder.txtJobIdDate.setText(mContext.getString(R.string.id) + " #" + mTodayModelCards.get(position).getJob_id()
                    + " | " + mTodayModelCards.get(position).getDueDate());
            holder.txtJobName.setText(mTodayModelCards.get(position).getConsumerName());
            holder.txtJobName.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuOrange));
            holder.txtJobAddress.setText(mTodayModelCards.get(position).getArea());

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, DetailsActivity.class);
                    intent.putExtra(AppConstants.COMING_FROM, screenName);
                    intent.putExtra(AppConstants.JOB_ID, mTodayModelCards.get(position).getConversionId());
                    intent.putExtra(AppConstants.MODEL, mTodayModelCards.get(position));
                    mContext.startActivity(intent);
                }
            });
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.services))) {
            holder.imgModule.setImageResource(R.drawable.ic_action_services);
            holder.txtJobIdDate.setText(mContext.getString(R.string.id) + " #" + mTodayModelCards.get(position).getServiceNumber()
                    + " | " + mTodayModelCards.get(position).getDueDate());
            holder.txtJobName.setText(mTodayModelCards.get(position).getConsumerName());
            holder.txtJobName.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuCream));
            holder.txtJobAddress.setText(mTodayModelCards.get(position).getAddress());

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, DetailsActivity2.class);
                    intent.putExtra(AppConstants.COMING_FROM, screenName);
                    intent.putExtra(AppConstants.JOB_ID, mTodayModelCards.get(position).getService_id());
                    intent.putExtra(AppConstants.MODEL, mTodayModelCards.get(position));
                    mContext.startActivity(intent);

                }
            });
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.complaint))) {
            holder.imgModule.setImageResource(R.drawable.ic_action_complaints);
            holder.txtJobIdDate.setText(mContext.getString(R.string.id) + " #" + mTodayModelCards.get(position).getComplaintNumber()
                    + " | " + mTodayModelCards.get(position).getDueDate());
            holder.txtJobName.setText(mTodayModelCards.get(position).getConsumerName());
            holder.txtJobName.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuBlue));
            holder.txtJobAddress.setText(mTodayModelCards.get(position).getAddress());

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, DetailsActivity2.class);
                    intent.putExtra(AppConstants.COMING_FROM, screenName);
                    intent.putExtra(AppConstants.JOB_ID, mTodayModelCards.get(position).getComplaintId());
                    intent.putExtra(AppConstants.MODEL, mTodayModelCards.get(position));
                    mContext.startActivity(intent);
                }
            });
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.asset_indexing))) {
            holder.imgModule.setImageResource(R.drawable.ic_action_asset_indexing);
            holder.txtJobIdDate.setText(mContext.getString(R.string.id) + " #" + mTodayModelCards.get(position).getAssetMakeNo()
                    + " | " + mTodayModelCards.get(position).getAssignedDate());
            holder.txtJobName.setText(mTodayModelCards.get(position).getAssetName());
            holder.txtJobAddress.setText(mTodayModelCards.get(position).getLocation());

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, DetailsActivity.class);
                    intent.putExtra(AppConstants.COMING_FROM, screenName);
                    intent.putExtra(AppConstants.JOB_ID, mTodayModelCards.get(position).getComplaintId());
                    intent.putExtra(AppConstants.MODEL, mTodayModelCards.get(position));
                    mContext.startActivity(intent);
                }
            });
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.preventive))) {
            holder.imgModule.setImageResource(R.drawable.ic_action_preventive);
            holder.txtJobIdDate.setText(mContext.getString(R.string.id) + " #" + mTodayModelCards.get(position).getPreventive_id()
                    + " | " + mTodayModelCards.get(position).getAssignedDate());
            holder.txtJobName.setText(mTodayModelCards.get(position).getJob_name());
            holder.txtJobAddress.setText(mTodayModelCards.get(position).getArea());

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, DetailsActivity2.class);
                    intent.putExtra(AppConstants.COMING_FROM, screenName);
                    intent.putExtra("id", mTodayModelCards.get(position).getPreventive_id());
                    mContext.startActivity(intent);
                }
            });
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.breakdown))) {
            holder.imgModule.setImageResource(R.drawable.ic_action_breakdown);
            holder.txtJobIdDate.setText(mContext.getString(R.string.id) + " #" + mTodayModelCards.get(position).getAssetmakeNo()
                    + " | " + mTodayModelCards.get(position).getAssignedDate());
            holder.txtJobName.setText(mTodayModelCards.get(position).getJob_name());
            holder.txtJobAddress.setText(mTodayModelCards.get(position).getArea());

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, DetailsActivity2.class);
                    intent.putExtra(AppConstants.COMING_FROM, screenName);
                    intent.putExtra("id", mTodayModelCards.get(position).getBreakdown_id());
                    mContext.startActivity(intent);
                }
            });
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.commissioning))) {
            holder.imgModule.setImageResource(R.drawable.ic_action_commissioning);
            holder.txtJobIdDate.setText(mContext.getString(R.string.id) + " #" + mTodayModelCards.get(position).getJob_id() +
                    " | " + mTodayModelCards.get(position).getAssignedDate());
            holder.txtJobName.setText(mTodayModelCards.get(position).getJob_name());
            holder.txtJobAddress.setText(mTodayModelCards.get(position).getLocation());

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, DetailsActivity2.class);
                    intent.putExtra(AppConstants.COMING_FROM, screenName);
                    intent.putExtra("id", mTodayModelCards.get(position).getCommission_id());
                    mContext.startActivity(intent);
                }
            });
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.decommissioning))) {
            holder.imgModule.setImageResource(R.drawable.ic_action_decommissioning);
            holder.txtJobIdDate.setText(mContext.getString(R.string.id) + " #" + mTodayModelCards.get(position).getDecommission_id() +
                    " | " + mTodayModelCards.get(position).getAssignedDate());
            holder.txtJobName.setText(mTodayModelCards.get(position).getJob_name());
            holder.txtJobAddress.setText(mTodayModelCards.get(position).getArea());

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, DetailsActivity2.class);
                    intent.putExtra(AppConstants.COMING_FROM, screenName);
                    intent.putExtra("id", mTodayModelCards.get(position).getDecommission_id());
                    mContext.startActivity(intent);
                }
            });
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.recovery))) {
            /*holder.imgModule.setImageResource(R.drawable.ic_action_all);
            holder.txtJobIdDate.setText(mTodayModelCards.get(position).getServiceNumber());
            holder.txtJobName.setText(mTodayModelCards.get(position).getServiceType());
            holder.txtJobAddress.setText(mTodayModelCards.get(position).getConsumerName());
            holder.txtJobStatus.setText(mTodayModelCards.get(position).getConsumer_contact_no());

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, DetailsActivity2.class);
                    intent.putExtra(AppConstants.COMING_FROM, screenName);
                    intent.putExtra("id", mTodayModelCards.get(position).getService_id());
                    mContext.startActivity(intent);
                }
            });*/
        }
        else if (screenName.equals(CommonUtility.getString(mContext, R.string.rejected_registrations))) {
            holder.imgModule.setImageResource(R.drawable.ic_action_all);
            holder.txtJobIdDate.setText("");
            holder.txtJobName.setText(mRegistrationModelCards.get(position).name);
            holder.txtJobIdDate.setText(mContext.getString(R.string.id) + " #" + mRegistrationModelCards.get(position).registrationNo);
            holder.txtJobName.setTextColor(CommonUtility.getColor(mContext, R.color.colorMenuCream));
            holder.txtJobAddress.setText(mRegistrationModelCards.get(position).areaName);

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, RegistrationFormActivity.class);
                    intent.putExtra(AppConstants.COMING_FROM, mContext.getString(R.string.rejected_nsc));
                    intent.putExtra(AppConstants.JOB_ID, mRegistrationModelCards.get(position).nscId);
                    intent.putExtra(AppConstants.MODEL, mRegistrationModelCards.get(position));
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mTodayModelCards != null && mTodayModelCards.size() > 0) {
            return mTodayModelCards.size();
        } else if (mRegistrationModelCards != null && mRegistrationModelCards.size() > 0) {
            return mRegistrationModelCards.size();
        }else {
            return 0;
        }
    }

    public class SearchHolder extends RecyclerView.ViewHolder {

        private LinearLayout linearLayout;
        private ImageView imgModule;
        private TextView txtJobIdDate, txtJobName, txtJobAddress, txtJobStatus;
        private Typeface mRegularBold, mRegularItalic, mRegular;

        public SearchHolder(View itemView) {
            super(itemView);

            mRegularBold = App.getMontserratMediumFont();
            mRegularItalic = App.getMontserratBoldFont();
            mRegular = App.getMontserratRegularFont();

            linearLayout = itemView.findViewById(R.id.linear_layout);
            imgModule = itemView.findViewById(R.id.img_module);

            txtJobIdDate = itemView.findViewById(R.id.txt_job_id_date);
            txtJobName = itemView.findViewById(R.id.txt_job_name);
            txtJobAddress = itemView.findViewById(R.id.txt_job_address);
            txtJobStatus = itemView.findViewById(R.id.txt_job_status);

            txtJobIdDate.setTypeface(mRegular);
            txtJobName.setTypeface(mRegular);
            txtJobAddress.setTypeface(mRegular);
            txtJobStatus.setTypeface(mRegular);
        }
    }
}