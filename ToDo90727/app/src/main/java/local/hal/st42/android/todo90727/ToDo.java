package local.hal.st42.android.todo90727;

public class ToDo {
    private long _id;
    private String _name;
    private String _deadline;
    private long _done;//数値
    private String _note;

    public long getId(){return _id;}
    public void setId(){this._id = _id;}

    public String getName(){return _name;}
    public void setName(){this._name = _name;}

    public String getDeadline(){return _deadline;}
    public void setDeadline(){this._deadline = _deadline;}

    public long getDone(){return _done;}
    public void setDone(){this._done = _done;}

    public String getNote(){return _note;}
    public void setNote(){this._note = _note;}

}
