package mmonastyrski.androidtodo;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

public class TaskWidgetService extends RemoteViewsService {
    
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new TaskRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

class TaskRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private List<Task> Tasks = new ArrayList<>();
    private Context mContext;
    private DBManager dbManager;
    
    TaskRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
    }
    
    public void onCreate() {
        // In onCreate() you setup any connections / cursors to your data source. Heavy lifting,
        // for example downloading or creating content etc, should be deferred to onDataSetChanged()
        // or getViewAt(). Taking more than 20 seconds in this call will result in an ANR.
        dbManager = new DBManager(mContext.getApplicationContext(), null, null, 1);
    }
    
    public void onDestroy() {
        // In onDestroy() you should tear down anything that was setup for your data source,
        // eg. cursors, connections, etc.
        Tasks.clear();
        dbManager.close();
    }
    
    public int getCount() {
        return Tasks.size();
    }
    
    public RemoteViews getViewAt(int position) {
        Task task = Tasks.get(position);
        SpannableString taskDescription = new SpannableString(task.get_description());
        if(Tasks.get(position).is_done()) {
            taskDescription.setSpan(new StrikethroughSpan(), 0, task.get_description().length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        // position will always range from 0 to getCount() - 1.
        // We construct a remote views item based on our widget item xml file, and set the
        // text based on the position.
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.task_widget_item);
        remoteViews.setTextViewText(R.id.widget_text, taskDescription);
    
        //set onclick item in ListView
        Bundle extras = new Bundle();
        
        //okay so for some weird reason when I put parcelable
        //object into extras it is null so i have to set
        //every task property manually
        extras.putString(TaskWidgetProvider.TASK_DESCRIPTION, task.get_description());
        extras.putBoolean(TaskWidgetProvider.TASK_DONE, task.is_done());
        extras.putInt(TaskWidgetProvider.TASK_ID, task.get_id());
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        remoteViews.setOnClickFillInIntent(R.id.task_widget_item, fillInIntent);
        
        // Return the remote views object.
        return remoteViews;
    }
    
    public RemoteViews getLoadingView() {
        // You can create a custom loading view (for instance when getViewAt() is slow.) If you
        // return null here, you will get the default loading view.
        return null;
    }
    
    public int getViewTypeCount() {
        return 1;
    }
    
    public long getItemId(int position) {
        return position;
    }
    
    public boolean hasStableIds() {
        return true;
    }
    
    public void onDataSetChanged() {
        // This is triggered when you call AppWidgetManager notifyAppWidgetViewDataChanged
        // on the collection view corresponding to this factory. You can do heaving lifting in
        // here, synchronously. For example, if you need to process an image, fetch something
        // from the network, etc., it is ok to do it here, synchronously. The widget will remain
        // in its current state while work is being done here, so you don't need to worry about
        // locking up the widget.
        Tasks=dbManager.getDB();
    }
}

