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
            int id = getArguments().getInt("id");
            String description = getArguments().getString("description");
            boolean isDone = getArguments().getBoolean("isDone");
            task.set_id(id);
            task.set_description(description);
            task.set_done(isDone);
        }
        
        newTask.setText(task.get_description());
        
        return view;
    }
    
}
