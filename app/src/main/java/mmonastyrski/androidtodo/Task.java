package mmonastyrski.androidtodo;

//TODO make this parcelable
public class Task {
    private int _id;
    private String _description;
    private boolean _done;
    
    Task(int _id, String _description, boolean _done) {
        this._id = _id;
        this._description = _description;
        this._done = _done;
    }
    
    public Task(String _description, boolean _done) {
        this._description = _description;
        this._done=_done;
    }
    
    Task(String _description) {
        this._description = _description;
        this._done=false;
    }
    
    Task(){
        _id=0;
        _description="";
        _done=false;
    }
    
    int get_id() {
        return _id;
    }
    
    void set_id(int _id) {
        this._id = _id;
    }
    
    String get_description() {
        return _description;
    }
    
    void set_description(String _description) {
        this._description = _description;
    }
    
    boolean is_done() {
        return _done;
    }
    
    void set_done(boolean _isDone) {
        this._done = _isDone;
    }
}
