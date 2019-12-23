package Mohamed.mad.markmycar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "PINED_LOCATIONS";
    private static final String COLM_1 = "Title";
    private static final String COLM_2 = "Date";
    private static final String COLM_3 = "Latitude";
    private static final String COLM_4 = "Longitude";
    private static final String COLM_5 = "Status";
    public static final String OPEN = "OPEN";
    public static final String CLOSED = "CLOSED";

    public DatabaseHelper(Context context){
        super(context,TABLE_NAME,null,1);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF  EXISTS " + TABLE_NAME);
        Log.i("TAG", "Droping table sinve the database exists");
        onCreate(sqLiteDatabase);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "" +
                "( ID INTEGER PRIMARY KEY AUTOINCREMENT , " + COLM_1 +" TEXT,"
                + COLM_2 +" TEXT,"+ COLM_3 +" TEXT,"+ COLM_4 +" TEXT,"+COLM_5 + " TEXT)";
        Log.i("TAG","Database is being intilized");

        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    public boolean insert(String tile, String date, String longitude, String latitude, String status){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Log.i("TAG", "Inserting data to database");
        ContentValues values = new ContentValues();
        values.put(COLM_1, tile);
        values.put(COLM_2, date);
        values.put(COLM_3, longitude);
        values.put(COLM_4, latitude);
        values.put(COLM_5,status);
          // Insert the new row, returning the primary key value of the new row
         long checkResult = sqLiteDatabase.insert(TABLE_NAME,null,values);
         if(checkResult == -1) {
            Log.i("TAG", "Data was not inserted!");
             return false;
         }
         else {
             Log.i("TAG", "Data was inserted sucessfuly.");
             return true;
         }
    }

    //return all data
    public Cursor getData(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME ;
        Cursor data = sqLiteDatabase.rawQuery(query,null);

        return data;
    }
    public void deleteData(String title){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
         sqLiteDatabase.delete(TABLE_NAME, "Title = ?", new String[] {title});
    }
    public void deleteAllRows(){

        Log.i("TAG","Table is erased");
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME;
        sqLiteDatabase.execSQL(query);


    }



}
