package mmonastyrski.androidtodo;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Created by mateusz on 2/18/17.
 */
public class TaskAdapter extends ArrayAdapter<Task> {
    
    public TaskAdapter(Context context, Task[] tasks) {
        super(context, R.layout.task, tasks);
    }
    
    static class ViewHolder{
        TextView textView;
        CheckBox checkBox;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        Task task = getItem(position);
        
        if(view==null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.task, parent, false);
            
            holder = new ViewHolder();
            
            holder.textView = (TextView) view.findViewById(R.id.taskDescription);
            holder.checkBox = (CheckBox) view.findViewById(R.id.isDone);
            
            view.setTag(holder);
        }
        else {
            holder=(ViewHolder) view.getTag();
        }
        holder.textView.setText(task.get_description());
        holder.checkBox.setChecked(task.is_done());
        if(holder.checkBox.isChecked()){
            holder.textView.setPaintFlags(holder.textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        
        return view;
    }
    
    public void update(){
    }
    
    
}
