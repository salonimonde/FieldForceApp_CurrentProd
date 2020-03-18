package com.fieldforce.services;

import com.fieldforce.db.DatabaseManager;
import com.fieldforce.models.NotificationCard;
import com.fieldforce.utility.AppConstants;
import com.fieldforce.utility.AppPreferences;
import com.fieldforce.utility.CommonUtility;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFireBaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        NotificationCard notificationCard = new NotificationCard();
        notificationCard.message = remoteMessage.getNotification().getBody();
        notificationCard.title = remoteMessage.getNotification().getTitle();
        notificationCard.date = CommonUtility.getCurrentDate();
        notificationCard.isRead = "false";
        notificationCard.userId = AppPreferences.getInstance(this).getString(AppConstants.EMP_ID,"");
        DatabaseManager.saveNotification(this, notificationCard);
    }
}
