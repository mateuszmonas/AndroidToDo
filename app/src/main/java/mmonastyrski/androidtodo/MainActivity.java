package mmonastyrski.androidtodo;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.view.View.OnClickListener;

public class MainActivity extends AppCompatActivity {
    
    private Button rightButton;
    private Button leftButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    
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
                }
                default:{
                    addFragmentListButtons();
                    changeFragment(new FragmentList());
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
        rightButton.setOnClickListener(onAddListener);
    }
    
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.fragment);
        if(fragment instanceof FragmentList) savedInstanceState.putString("fragment", "FragmentList");
        else savedInstanceState.putString("fragment", "FragmentAddTask");
    }
    
    
    private void changeFragment(Fragment f){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, f);
        transaction.commit();
    }
    
    private OnClickListener onAddListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            addFragmentAddTaskButtons();
            changeFragment(new FragmentAddTask());
        }
    };
    
    private OnClickListener onCreateTaskListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            addFragmentListButtons();
            changeFragment(new FragmentList());
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
        }
    };
}
