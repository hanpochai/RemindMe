package com.example.mariaconcepciondaod.remindme;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class SQLiteHelper extends SQLiteOpenHelper {


    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);

    }

    public void insertData(String title, String desc, byte[] image){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "Insert into notes values (NULL, ?, ?, ?, ?, ?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1,title);
        statement.bindString(2,desc);
        statement.bindBlob(3,image);
        statement.bindString(4,"Unlocked");
        statement.bindString(5,"None");
        statement.executeInsert();

    }
    public int getPassCount() {
        String countQuery = "SELECT  * FROM  defaultpassword";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public String getPass() {
        String countQuery = "SELECT  * FROM  defaultpassword";
         String pass="";
        Cursor c = getData(countQuery);
        while (c.moveToNext()) {
           pass = c.getString(0);

        }

        return pass;
    }

    public void insertDataForDefaultPassword(String password){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "Insert into defaultPassword values (?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1,password);

        statement.executeInsert();

    }


    public void updatePassData(int ids,String type, String pass){
        SQLiteDatabase database = getWritableDatabase();

        String sql = "UPDATE notes SET type = ?, pass= ?  where Id = ?";

        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1, type);
        statement.bindString(2,pass);

        statement.bindDouble(3,(double)ids);

        statement.execute();
        database.close();




    }


    public void deletePassData(int ids){
        SQLiteDatabase database = getWritableDatabase();

        String sql = "UPDATE notes SET type = ?, pass= ?  where Id = ?";

        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1,"Unlocked");
        statement.bindString(2,"None");

        statement.bindDouble(3,(double)ids);

        statement.execute();
        database.close();




    }

public void updateData(int ids, String titles, String descs, byte[] images){
        SQLiteDatabase database = getWritableDatabase();

        String sql = "UPDATE notes SET title = ?, description= ? , image = ? where Id = ?";

        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1, titles);
        statement.bindString(2,descs);
        statement.bindBlob(3,images);
        statement.bindDouble(4,(double)ids);

        statement.execute();
        database.close();




}
public void deleteData(int id){

        SQLiteDatabase database = getWritableDatabase();
        String sql="Delete from notes where id = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1,(double)id);
        statement.execute();
        database.close();



}



    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql,null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
