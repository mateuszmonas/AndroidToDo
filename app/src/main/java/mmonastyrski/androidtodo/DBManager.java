package mmonastyrski.androidtodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBManager extends SQLiteOpenHelper {
    
    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="tasks.db";
    private static final String TABLE_TASKS="tasks";
    private static final String COLUMN_ID="_id";
    private static final String COLUMN_DESCRIPTION="_description";
    private static final String COLUMN_DONE="_done";
    
    DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_TASKS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,  " + COLUMN_DESCRIPTION + " TEXT, " + COLUMN_DONE + " BOOLEAN);";
        sqLiteDatabase.execSQL(query);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(sqLiteDatabase);
    }
    
    public void drop(){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(sqLiteDatabase);
    }
    
    void addTask(Task task){
        ContentValues values = new ContentValues();
        values.put(COLUMN_DESCRIPTION, task.get_description());
        values.put(COLUMN_DONE, task.is_done()?1:0);
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.insert(TABLE_TASKS, null, values);
        sqLiteDatabase.close();
    }
    
    void deleteTasks(){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TASKS + " WHERE 1";
        
        Cursor c = sqLiteDatabase.rawQuery(query, null);
    
        while (c.moveToNext()){
            if(c.getString(c.getColumnIndex(COLUMN_DESCRIPTION))!=null){
                if((c.getInt(c.getColumnIndex(COLUMN_DONE))==1)) {
                    int id = c.getInt(c.getColumnIndex(COLUMN_ID));
                    sqLiteDatabase.delete(TABLE_TASKS, "_id=?", new String[]{Integer.toString(id)});
                }
            }
        }
        c.close();
        sqLiteDatabase.close();
    }
    
    void updateTask(Task task){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DESCRIPTION, task.get_description());
        values.put(COLUMN_DONE, task.is_done()?1:0);
        sqLiteDatabase.update(TABLE_TASKS, values, COLUMN_ID + "=?", new String[]{Integer.toString(task.get_id())});
        sqLiteDatabase.close();
    }
    
    ArrayList<Task> getDB(){
        ArrayList<Task> tasks = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TASKS + " WHERE 1";
        
        Cursor c = sqLiteDatabase.rawQuery(query, null);
        
        while (c.moveToNext()){
            if(c.getString(c.getColumnIndex(COLUMN_DESCRIPTION))!=null){
                tasks.add(new Task(
                        c.getInt(c.getColumnIndex(COLUMN_ID)),
                        c.getString(c.getColumnIndex(COLUMN_DESCRIPTION)),
                        (c.getInt(c.getColumnIndex(COLUMN_DONE))==1)
                ));
            }
        }
        sqLiteDatabase.close();
        c.close();
        return tasks;
    }
    
}
