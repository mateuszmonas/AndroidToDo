package mmonastyrski.androidtodo;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

public class TaskWidgetProvider extends AppWidgetProvider {
    private static final String UPDATE_TASK = "mmonastyrski.androidtodo.UPDATE_TASK";
    static final String BUNDLE = "mmonastyrski.androidtodo.BUNDLE";
    static final String TASK = "mmonastyrski.androidtodo.TASK";
    
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
    
        if(intent.getAction().equals(UPDATE_TASK)){
            final DBManager dbManager = new DBManager(context.getApplicationContext(), null, null, 1);
    
            //it turns out that i have to pull out bundle first
            //and access task directly from the bundle
            Bundle bundle = intent.getBundleExtra(BUNDLE);
            Task task = bundle.getParcelable(TASK);
            task.set_done(!task.is_done());
            
            if(task.get_id()!=-1) {
                dbManager.updateTask(task);
            }
            //update widget when data changes
            ComponentName thisWidget = new ComponentName(context, TaskWidgetProvider.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.task_view);
        }
        super.onReceive(context, intent);
    }
    
    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId){
        // Here we setup the intent which points to the TaskViewService which will
        // provide the views for this collection.
        Intent intent = new Intent(context, TaskWidgetService.class);
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        rv.setRemoteAdapter(R.id.task_view, intent);
        //set onclick for individual items in task_view
        Intent taskIntent = new Intent(context, TaskWidgetProvider.class);
        taskIntent.setAction(TaskWidgetProvider.UPDATE_TASK);
        taskIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent taskPendingIntent = PendingIntent.getBroadcast(context, 0, taskIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setPendingIntentTemplate(R.id.task_view, taskPendingIntent);
        
        Intent launchIntent = new Intent(context, MainActivity.class);
        PendingIntent launchPendingIntent = PendingIntent.getActivity(context, 0, launchIntent, 0);
        rv.setOnClickPendingIntent(R.id.widget_title, launchPendingIntent);
        
        // The empty view is displayed when the collection has no items. It should be a sibling
        // of the collection view.
        rv.setEmptyView(R.id.task_view, R.id.empty_view);
        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }
    
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // update each of the widgets with the remote adapter
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
