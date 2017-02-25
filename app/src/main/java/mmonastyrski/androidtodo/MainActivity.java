package mmonastyrski.androidtodo;

import android.app.Fragment;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.view.View.OnClickListener;

public class MainActivity extends AppCompatActivity {
    
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
                    addFragmentAddTaskButtons();
                    changeFragment(new FragmentAddTask());
                    break;
                }
                case "FragmentList":{
                    addFragmentListButtons();
                    changeFragment(new FragmentList());
                    break;
                }
                default:{
                    addFragmentListButtons();
                    changeFragment(new FragmentList());
                    break;
                }
            }
        }
        else {
            addFragmentListButtons();
            changeFragment(new FragmentList());
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
    
    private void addFragmentAddTaskButtons(){
        leftButton.setText("B");
        leftButton.setOnClickListener(onGoBackListener);
        rightButton.setText("C");
        rightButton.setOnClickListener(onCreateTaskListener);
    }
    
    private void addFragmentListButtons(){
        leftButton.setText("R");
        leftButton.setOnClickListener(onRemoveListener);
        rightButton.setText("A");
        rightButton.setOnClickListener(onGoToAddTaskListener);
    }
    
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Fragment fragment = getFragmentManager().findFragmentById(R.id.fragment);
        if(fragment instanceof FragmentList){
            savedInstanceState.putString("fragment", "FragmentList");
        }
        else{
            EditText newTask = (EditText) findViewById(R.id.newTask);
            String description = newTask.getText().toString();
            savedInstanceState.putString("description", description);
            savedInstanceState.putString("fragment", "FragmentAddTask");
        }
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState!=null && savedInstanceState.getString("fragment").equals("FragmentAddTask")) {
            EditText newTask = (EditText) findViewById(R.id.newTask);
            String description = savedInstanceState.getString("description");
            newTask.setText(description);
        }
    }
    
    private void changeFragment(Fragment f){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, f);
        transaction.commit();
    }
    
    private OnClickListener onGoToAddTaskListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            addFragmentAddTaskButtons();
            changeFragment(new FragmentAddTask());
        }
    };
    
    private OnClickListener onCreateTaskListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            EditText newTask = (EditText)findViewById(R.id.newTask);
            String description = newTask.getText().toString().trim();
            //only adds task if description was added
            if(!description.equals("")) {
                dbManager.addTask(new Task(description));
                addFragmentListButtons();
            changeFragment(new FragmentList());
        }
        }
    };
    
    private OnClickListener onGoBackListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            addFragmentListButtons();
            changeFragment(new FragmentList());
        }
    };
    
    private OnClickListener onRemoveListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            dbManager.deleteTasks();
            changeFragment(new FragmentList());
        }
    };
}
