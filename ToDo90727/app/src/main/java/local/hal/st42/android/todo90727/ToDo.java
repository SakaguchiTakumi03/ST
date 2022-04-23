package local.hal.st42.android.todo90727;

public class ToDo {
    private long _id;
    private String _name;
    private long _deadline;
    private long _done;
    private String _note;

    public long getId(){return _id;}
    public void setId(long _id){this._id = _id;}

    public String getName(){return _name;}
    public void setName(String _name){this._name = _name;}

    public long getDeadline(){return _deadline;}
    public void setDeadline(long _deadline){this._deadline = _deadline;}

    public long getDone(){return _done;}
    public void setDone(long _id){this._done = _done;}

    public String getNote(){return _note;}
    public void setNote(String _note){this._note = _note;}

}
