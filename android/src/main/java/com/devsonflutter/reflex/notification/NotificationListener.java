/*

                Copyright (c) 2022 DevsOnFlutter (Devs On Flutter)
                            All rights reserved.

The plugin is governed by the BSD-3-clause License. Please see the LICENSE file
for more details.

*/

package com.devsonflutter.reflex.notification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import androidx.annotation.RequiresApi;

import com.devsonflutter.reflex.ReflexPlugin;
import com.devsonflutter.reflex.notification.autoReply.AutoReply;

import io.flutter.Log;

/* Notification Listener */
@RequiresApi(api = VERSION_CODES.JELLY_BEAN_MR2)
@SuppressLint("OverrideAbstract")
public class NotificationListener extends NotificationListenerService {

    @RequiresApi(api = VERSION_CODES.N)
    @Override
    public void onNotificationPosted(StatusBarNotification notification) {
        Log.d("[Reflex]","Notification Received!");

        // Package name as title
        String packageName = notification.getPackageName();
        Log.d("[Reflex]","Notification From: " + packageName);

        // Extra Payload
        Bundle extras = notification.getNotification().extras;
        Log.d("[Reflex]","Extras: " + extras.toString());

        Intent intent = new Intent(NotificationUtils.NOTIFICATION_INTENT);
        intent.putExtra(NotificationUtils.NOTIFICATION_PACKAGE_NAME, packageName);

        CharSequence title = extras.getCharSequence(Notification.EXTRA_TITLE);
        CharSequence text = extras.getCharSequence(Notification.EXTRA_TEXT);

        intent.putExtra(NotificationUtils.NOTIFICATION_TITLE, title.toString());
        intent.putExtra(NotificationUtils.NOTIFICATION_MESSAGE, text.toString());

        sendBroadcast(intent);

        new AutoReply(ReflexPlugin.context).sendReply(notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }
}

