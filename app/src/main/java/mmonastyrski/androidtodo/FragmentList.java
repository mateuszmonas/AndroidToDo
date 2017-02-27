package mmonastyrski.androidtodo;

import android.app.Fragment;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;


public class FragmentList extends Fragment {
    
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        
        final DBManager dbManager = new DBManager(getActivity(), null, null, 1);
        
        ArrayList<Task> tasks = dbManager.getDB();
        
        final ListAdapter tasksAdapter = new TaskAdapter(getActivity(), tasks);
        final ListView tasksListView = (ListView) view.findViewById(R.id.tasksListView);
        tasksListView.setAdapter(tasksAdapter);
        
        tasksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Task task = (Task) adapterView.getItemAtPosition(i);
                task.set_done(!task.is_done());
                
                CheckBox isDone = (CheckBox)view.findViewById(R.id.isDone);
                TextView taskDescription = ((TextView)view.findViewById(R.id.taskDescription));
                
                
                isDone.setChecked(task.is_done());
                if(isDone.isChecked()) {
                    taskDescription.setPaintFlags(taskDescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
                else {
                    taskDescription.setPaintFlags(taskDescription.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);
                }
                dbManager.updateTask(task);
            }
        });
        return view;
    }
    
}
