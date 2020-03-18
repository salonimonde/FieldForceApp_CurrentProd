package com.fieldforce.ui.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fieldforce.R;
import com.fieldforce.db.DatabaseManager;
import com.fieldforce.interfaces.ItemTouchHelperAdapter;
import com.fieldforce.models.NotificationCard;
import com.fieldforce.utility.App;
import com.fieldforce.utility.CommonUtility;

import java.util.ArrayList;
import java.util.Collections;

public class NotificationCardAdapter extends RecyclerView.Adapter<NotificationCardAdapter.NotificationCardHolder>
        implements ItemTouchHelperAdapter {

    public Context mContext;
    private ArrayList<NotificationCard> mNotificationCard;

    public NotificationCardAdapter(Context context, ArrayList<NotificationCard> NotificationCards) {
        this.mContext = context;
        this.mNotificationCard = NotificationCards;
    }

    @Override
    public NotificationCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_notification_card, null);
        NotificationCardHolder viewHolder = new NotificationCardHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final NotificationCardHolder holder, final int position) {
        CommonUtility.setAnimation(holder.itemView, position, -1, mContext);
        final NotificationCard item = mNotificationCard.get(position);
        holder.txtModuleName.setText(String.valueOf(item.title));
        holder.txtMessage.setText(String.valueOf(item.message) + "\n" + String.valueOf(item.date));
    }

    @Override
    public int getItemCount() {
        if (mNotificationCard != null && mNotificationCard.size() > 0)
            return mNotificationCard.size();
        else
            return 0;
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mNotificationCard, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        DatabaseManager.deleteAccount(mContext, mNotificationCard.get(position).message);
        mNotificationCard.remove(position);
        notifyItemRemoved(position);
    }

    public class NotificationCardHolder extends RecyclerView.ViewHolder {

        private TextView txtModuleName, txtMessage;

        public NotificationCardHolder(View itemView) {
            super(itemView);
            Typeface regular = App.getMontserratRegularFont();
            Typeface bold = App.getMontserratBoldFont();

            txtModuleName = itemView.findViewById(R.id.txt_module_name);
            txtModuleName.setTypeface(regular);
            txtMessage = itemView.findViewById(R.id.txt_message);
            txtMessage.setTypeface(bold);
        }
    }
}



