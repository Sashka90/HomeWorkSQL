package tables;

import db.IDBExecutor;

public class StudentTable extends AbsTable{

    public StudentTable(IDBExecutor idbExecutor){
        super("student", idbExecutor);
    }
}
