package mmonastyrski.androidtodo;

import android.app.Fragment;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import android.view.View.OnClickListener;

public class MainActivity extends AppCompatActivity implements FragmentList.UpdateTaskListener {
    
    private DBManager dbManager;
    private Button rightButton;
    private Button leftButton;
    
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
        RelativeLayout toolbar = (RelativeLayout) findViewById(R.id.toolbar);
    
        rightButton = new Button(this);
        RelativeLayout.LayoutParams addButtonParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        addButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        addButtonParams.addRule(RelativeLayout.ALIGN_PARENT_END);
    
        leftButton = new Button(this);
        RelativeLayout.LayoutParams removeButtonParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        removeButtonParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        removeButtonParams.addRule(RelativeLayout.ALIGN_PARENT_START);
    
    
        toolbar.addView(rightButton, addButtonParams);
        toolbar.addView(leftButton, removeButtonParams);
    }
    
    private void addButtons(Fragment f){
        if(f instanceof FragmentList){
            leftButton.setText("R");
            leftButton.setOnClickListener(onRemoveListener);
            rightButton.setText("A");
            rightButton.setOnClickListener(onGoToAddTaskListener);
        }
        if(f instanceof FragmentAddTask){
            leftButton.setText("B");
            leftButton.setOnClickListener(onGoBackListener);
            rightButton.setText("C");
            rightButton.setOnClickListener(onCreateTaskListener);
        }
    }
    
    private void changeFragment(Fragment f){
        addButtons(f);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, f, "currentFragment");
        transaction.commit();
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
        args.putString("description", task.get_description());
        args.putInt("id", task.get_id());
        args.putBoolean("isDone", task.is_done());
        fragmentAddTask.setArguments(args);
        changeFragment(fragmentAddTask);
        
        updateWidget();
    }
    
    //called after data chas been changed
    //to reflect changes in the widget
    private void updateWidget(){
        Context context = getApplicationContext();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisWidget = new ComponentName(context, TaskWidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.task_view);
    }
}
