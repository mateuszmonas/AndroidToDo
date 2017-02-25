package mmonastyrski.androidtodo;

/**
 * Created by mateusz on 2/18/17.
 */
public class Task {
    private int _id;
    private String _description;
    private boolean _done;
    
    public Task(int _id, String _description, boolean _done) {
        this._id = _id;
        this._description = _description;
        this._done = _done;
    }
    
    public Task(String _description, boolean _done) {
        this._description = _description;
        this._done=_done;
    }
    
    public Task(String _description) {
        this._description = _description;
        this._done=false;
    }
    
    public int get_id() {
        return _id;
    }
    
    public void set_id(int _id) {
        this._id = _id;
    }
    
    public String get_description() {
        return _description;
    }
    
    public void set_description(String _description) {
        this._description = _description;
    }
    
    public boolean is_done() {
        return _done;
    }
    
    public void set_done(boolean _isDone) {
        this._done = _isDone;
    }
}
