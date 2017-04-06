package mmonastyrski.androidtodo;

import java.util.ArrayList;
import java.util.List;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
public class StackWidgetService extends RemoteViewsService {
    private static final String TAG = "StackWidgetService";
    
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d(TAG, "onGetViewFactory");
        return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private static final String TAG = "StackRemoteViewsFactory";
    private static final int mCount = 10;
    private List<WidgetItem> mWidgetItems = new ArrayList<>();
    private Context mContext;
    private int mAppWidgetId;
    
    public StackRemoteViewsFactory(Context context, Intent intent) {
        Log.d(TAG, "StackRemoteViewsFactory");
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }
    public void onCreate() {
        Log.d(TAG, "onCreate");
        // In onCreate() you setup any connections / cursors to your data source. Heavy lifting,
        // for example downloading or creating content etc, should be deferred to onDataSetChanged()
        // or getViewAt(). Taking more than 20 seconds in this call will result in an ANR.
        for (int i = 0; i < mCount; i++) {
            mWidgetItems.add(new WidgetItem("asd"));
        }
    }
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        // In onDestroy() you should tear down anything that was setup for your data source,
        // eg. cursors, connections, etc.
        mWidgetItems.clear();
    }
    public int getCount() {
        Log.d(TAG, "getCount");
        return mCount;
    }
    public RemoteViews getViewAt(int position) {
        Log.d(TAG, "getViewAt");
        // position will always range from 0 to getCount() - 1.
        // We construct a remote views item based on our widget item xml file, and set the
        // text based on the position.
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        rv.setTextViewText(R.id.widget_text, mWidgetItems.get(position).text);
        // Return the remote views object.
        return rv;
    }
    public RemoteViews getLoadingView() {
        Log.d(TAG, "getLoadingView");
        // You can create a custom loading view (for instance when getViewAt() is slow.) If you
        // return null here, you will get the default loading view.
        return null;
    }
    public int getViewTypeCount() {
        Log.d(TAG, "getViewTypeCount");
        return 1;
    }
    public long getItemId(int position) {
        Log.d(TAG, "getItemId");
        return position;
    }
    public boolean hasStableIds() {
        Log.d(TAG, "hasStableIds");
        return true;
    }
    public void onDataSetChanged() {
        Log.d(TAG, "onDataSetChanged");
        // This is triggered when you call AppWidgetManager notifyAppWidgetViewDataChanged
        // on the collection view corresponding to this factory. You can do heaving lifting in
        // here, synchronously. For example, if you need to process an image, fetch something
        // from the network, etc., it is ok to do it here, synchronously. The widget will remain
        // in its current state while work is being done here, so you don't need to worry about
        // locking up the widget.
    }
}

