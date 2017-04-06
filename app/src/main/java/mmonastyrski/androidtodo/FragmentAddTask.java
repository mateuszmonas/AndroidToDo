package mmonastyrski.androidtodo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class FragmentAddTask extends Fragment {
        
    Task task;
    
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_task, container, false);
    
        task = new Task();
    
        EditText newTask = (EditText) view.findViewById(R.id.newTask);
        
        if(getArguments()!=null){
            task = getArguments().getParcelable("task");
        }
        
        newTask.setText(task.get_description());
        
        return view;
    }
    
}
