package mmonastyrski.androidtodo;

import android.app.Fragment;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.view.View.OnClickListener;

public class MainActivity extends AppCompatActivity implements FragmentList.UpdateTaskListener {
    
    private DBManager dbManager;
    private ImageButton rightButton;
    private ImageButton leftButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        dbManager = new DBManager(this, null, null, 1);
        
        createButtons();
        
        if(savedInstanceState!=null){
            switch (savedInstanceState.getString("fragment")){
                case "FragmentAddTask":{
                    changeFragment(new FragmentAddTask());
                    break;
                }
                case "FragmentList":{
                    changeFragment(new FragmentList());
                    break;
                }
                default:{
                    changeFragment(new FragmentList());
                    break;
                }
            }
        }
        else {
            changeFragment(new FragmentList());
        }
    }
    
    @Override
    protected void onRestart() {
        //this fragment is used to refresh fragment after app comes to foreground
        //otherwise the changes to list in widget are not reflected in app
        Fragment fragment = getFragmentManager().findFragmentById(R.id.fragment);
        if(fragment instanceof FragmentList) {
            changeFragment(new FragmentList());
        }
        super.onRestart();
    }
    
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Fragment fragment = getFragmentManager().findFragmentById(R.id.fragment);
        if(fragment instanceof FragmentList){
            savedInstanceState.putString("fragment", "FragmentList");
        }
        else{
            savedInstanceState.putString("fragment", "FragmentAddTask");
        }
    }
    
    private void createButtons(){
        //convert 50 dp to pixels so buttons fit right in the actionbar
        final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        int pixels = (int) (50 * scale + 0.5f);
        RelativeLayout toolbar = (RelativeLayout) findViewById(R.id.toolbar);
    
        rightButton = new ImageButton(this);
        RelativeLayout.LayoutParams addButtonParams = new RelativeLayout.LayoutParams(
                pixels,
                pixels
        );
        addButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
    
        leftButton = new ImageButton(this);
        RelativeLayout.LayoutParams removeButtonParams = new RelativeLayout.LayoutParams(
                pixels,
                pixels
        );
        removeButtonParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            addButtonParams.addRule(RelativeLayout.ALIGN_PARENT_END);
            removeButtonParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        }
        
        toolbar.addView(rightButton, addButtonParams);
        toolbar.addView(leftButton, removeButtonParams);
    }
    
    private void addButtons(Fragment f){
        if(f instanceof FragmentList){
            //set remove tasks button
            leftButton.setImageResource(R.drawable.ic_delete_forever_black_24dp);
            leftButton.setBackgroundColor(Color.TRANSPARENT);
            leftButton.setOnClickListener(onRemoveListener);
            //set add new task button
            rightButton.setImageResource(R.drawable.ic_add_circle_black_24dp);
            rightButton.setBackgroundColor(Color.TRANSPARENT);
            rightButton.setOnClickListener(onGoToAddTaskListener);
        }
        if(f instanceof FragmentAddTask){
            //set go back button
            leftButton.setImageResource(R.drawable.ic_cancel_black_24dp);
            leftButton.setBackgroundColor(Color.TRANSPARENT);
            leftButton.setOnClickListener(onGoBackListener);
            //set create task button
            rightButton.setImageResource(R.drawable.ic_check_circle_black_24dp);
            rightButton.setBackgroundColor(Color.TRANSPARENT);
            rightButton.setOnClickListener(onCreateTaskListener);
        }
    }
    
    private void changeFragment(Fragment f){
        if(getCurrentFocus()!=null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
        addButtons(f);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, f, "currentFragment");
        transaction.commit();
    }
    
    //if fragment does not equal fragment_list it goes back to fragment_list
    //otherwise it closes the app
    @Override
    public void onBackPressed() {
        final Fragment fragment = getFragmentManager().findFragmentById(R.id.fragment);
        if(!(fragment instanceof FragmentList)){
            changeFragment(new FragmentList());
        }
        else {
            super.onBackPressed();
        }
    }
    
    private OnClickListener onGoToAddTaskListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            changeFragment(new FragmentAddTask());
        }
    };
    
    private OnClickListener onCreateTaskListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            //get current fragment so we can access currently edited task
            FragmentAddTask fragmentAddTask = (FragmentAddTask) getFragmentManager().findFragmentByTag("currentFragment");
            Task task = fragmentAddTask.task;
            TextView newTask = (TextView)findViewById(R.id.newTask);
            String description = newTask.getText().toString().trim();
            //only adds task if description was added
            if(!description.equals("")) {
                task.set_description(description);
                //if task id is already set, task will be updated
                if(task.get_id()!=0){
                    dbManager.updateTask(task);
                }
                //if id is not set, new task will be created
                else {
                    dbManager.addTask(new Task(task.get_description()));
                }
                changeFragment(new FragmentList());
            }
            updateWidget();
        }
    };
    
    private OnClickListener onGoBackListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            changeFragment(new FragmentList());
        }
    };
    
    private OnClickListener onRemoveListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            dbManager.deleteTasks();
            changeFragment(new FragmentList());
            updateWidget();
        }
    };
    
    //changes activity to FragmentAddTask
    //is used to update task description after it has already been made
    @Override
    public void updateTask(Task task) {
        
        FragmentAddTask fragmentAddTask = new FragmentAddTask();
        Bundle args = new Bundle();
        args.putParcelable("task", task);
        fragmentAddTask.setArguments(args);
        changeFragment(fragmentAddTask);
        
        updateWidget();
    }
    
    //called after data chas been changed
    //to reflect changes in the widget
    public void updateWidget(){
        Context context = getApplicationContext();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisWidget = new ComponentName(context, TaskWidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.task_view);
    }
}
