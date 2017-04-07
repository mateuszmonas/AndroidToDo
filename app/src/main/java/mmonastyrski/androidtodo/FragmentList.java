package mmonastyrski.androidtodo;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import java.util.ArrayList;


public class FragmentList extends Fragment {
    private TaskAdapter tasksAdapter;
    private UpdateTaskListener activityCommander;
    private DBManager dbManager;
    private ArrayList<Task> tasks = new ArrayList<>();
    
    public interface UpdateTaskListener{
        void updateTask(Task task);
        void updateWidget();
    }
    
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            activityCommander = (UpdateTaskListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString());
        }
    }
    
    @Override
    public void onResume() {
        tasksAdapter.notifyDataSetChanged();
        super.onResume();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        
        dbManager = new DBManager(getActivity(), null, null, 1);
        
        tasks = dbManager.getDB();
        
        tasksAdapter = new TaskAdapter(getActivity(), tasks);
        ListView tasksListView = (ListView) view.findViewById(R.id.tasksListView);
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
                activityCommander.updateWidget();
            }
        });
        
        tasksListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                
                Task task = (Task) adapterView.getItemAtPosition(i);
                activityCommander.updateTask(task);
                
                return true;
            }
        });
        
        return view;
    }
    

}
