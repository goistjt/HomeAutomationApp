package com.jgoist.homeautomationapp.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.jgoist.homeautomationapp.R
import com.jgoist.homeautomationapp.controllers.ArloController
import org.jetbrains.anko.doAsyncResult

class ArloWidgetProvider : AppWidgetProvider() {
    private val ACTION_CLICK = "ACTION_CLICK"

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray?) {
        val thisWidget = ComponentName(context, javaClass)
        val allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)
        val arloController = ArloController(context)
        val basestationMode = doAsyncResult { arloController.getBasestationMode().displayName }
        allWidgetIds.forEach {
            val remoteViews = RemoteViews(context.packageName, R.layout.arlo_button_layout)
            remoteViews.setTextViewText(R.id.arlo_button_text, basestationMode.get())

            val intent = Intent(context, javaClass)
            intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)

            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            remoteViews.setOnClickPendingIntent(R.id.arlo_button_text, pendingIntent)
            appWidgetManager.updateAppWidget(it, remoteViews)
        }
    }
}