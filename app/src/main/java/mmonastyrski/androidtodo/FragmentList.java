package mmonastyrski.androidtodo;

import android.app.Fragment;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;


public class FragmentList extends Fragment {
    
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        
        
        Task[] tasks = new Task[5];
        tasks[0] = new Task("aaa");
        tasks[1] = new Task("bbb");
        tasks[2] = new Task("ccc");
        tasks[3] = new Task("ddd");
        tasks[4] = new Task("eee");
        
        final ListAdapter tasksAdapter = new TaskAdapter(getActivity(), tasks);
        final ListView tasksListView = (ListView) view.findViewById(R.id.tasksListView);
        tasksListView.setAdapter(tasksAdapter);
        
        tasksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Task task = (Task) adapterView.getItemAtPosition(i);
                task.set_done(!task.is_done());
                
                CheckBox checkBox = (CheckBox)view.findViewById(R.id.isDone);
                TextView textView = ((TextView)view.findViewById(R.id.taskDescription));
                
                checkBox.setChecked(task.is_done());
                if(checkBox.isChecked()) {
                    textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
                else {
                    textView.setPaintFlags(textView.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);
                }
            }
        });
        return view;
    }
    
}
