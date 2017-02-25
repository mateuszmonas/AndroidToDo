package mmonastyrski.androidtodo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class FragmentAddTask extends Fragment {
        
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_task, container, false);
                
        
        //if(savedInstanceState!=null) {
        //    String description = savedInstanceState.getString("description");
        //    newTask.setText(description);
        //}
        
        
        return view;
    }
    
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        EditText newTask = (EditText)getActivity().findViewById(R.id.newTask);
        String description = newTask.getText().toString();
        savedInstanceState.putString("description", description);
    }
    
    
}
