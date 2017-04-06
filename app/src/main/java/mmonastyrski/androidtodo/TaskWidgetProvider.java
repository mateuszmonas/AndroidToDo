package mmonastyrski.androidtodo;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class TaskWidgetProvider extends AppWidgetProvider {
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
        super.onReceive(context, intent);
    }
    
    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId){
        // Here we setup the intent which points to the TaskViewService which will
        // provide the views for this collection.
        Intent intent = new Intent(context, TaskWidgetService.class);
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        rv.setRemoteAdapter(R.id.task_view, intent);
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
