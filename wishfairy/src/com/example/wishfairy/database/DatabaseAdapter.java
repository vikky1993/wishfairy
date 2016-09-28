package com.example.wishfairy.database;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

/**
 * Adapts the database to deal with the front end.
 */
public class DatabaseAdapter {

	private static final String DB_NAME = "wishfairyDB";

	//Table name for categories table
	private static final String CategoryTable = "Category";
	//Table unique id for categories table and subcategories table
	public static final String COL_ID = "id";
	//Table username and password columns
	public static final String COL_CATEGORY = "name";
	
	//table name for subcategories
	private static final String SubCategoryTable = "SubCategory";
	//Table subcategoryname
	public static final String COL_SUBCATEGORY = "name";
	//Subcategory description
	public static final String COL_DESC = "desc";
	//Category ID (foreign key)
	public static final String COL_CAT_ID = "catid";
	//Resource ID for image
	public static final String COL_RES_ID = "resid";
	
	private static final String WishListTable = "Wishlist";
	
	private static final String DB_TABLE = "create table Wishlist (id integer primary key autoincrement, " 
			+ "title text not null, desc text not null, catid integer not null, subcatid integer not null);";

	private Context context;
	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;
	private static String DB_PATH;
	public final static String USER_TABLE = "USER_TABLE";
	private final static String DATABASE_CREATE_USER_TABLE = "create table if not exists " + USER_TABLE +"( username text primary key, password text not null, securityQuestion text not null, securityAnswer text not null)";


	/**
	 * The adapter constructor. 
	 * @param context
	 */
	public DatabaseAdapter(Context context) {
		this.context = context;
		DatabaseAdapter.DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
		dbHelper = new DatabaseHelper(context);
	}

	/**
	 * Creates the database helper and gets the database. 
	 * 
	 * @return
	 * @throws SQLException
	 */
	public DatabaseAdapter open() throws SQLException {
		
		return this;
	}

	  /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException{
    	Log.i("error message", "creating database");
    	boolean dbExist = checkDataBase();
    	Log.i("error message", "database checked");
 
    	if(!dbExist){
        	dbHelper.getWritableDatabase();
        	try {
    			copyDataBase();
    			Log.i("error message", "database copied");
    	        close();
    	        openDataBase();
    	        Log.i("error message", "database opened");
    	        database.execSQL(DB_TABLE);
    	        database.execSQL(DATABASE_CREATE_USER_TABLE);
    	        Log.i("error message", "table copied");
    			close();
    		} catch (IOException e) {
        		throw new Error("Error copying database");
        	}
    	}
    	else
    	{
    		Log.i("error message", "database exists already");
    	}
    }
 
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
 
    	Log.i("error message", "checking database");
    	SQLiteDatabase checkDB = null;
 
    	try{
    		String myPath = DB_PATH + DB_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE| SQLiteDatabase.NO_LOCALIZED_COLLATORS);
    		
 
    	}catch(SQLiteException e){
 
    		//database does't exist yet.
 
    	}
 
    	if(checkDB != null){
    		checkDB.close();
    	}
 
    	return checkDB != null ? true : false;
    }
 
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{
 
    	//Open your local db as the input stream
    	InputStream myInput = context.getAssets().open(DB_NAME);
 
    	// Path to the just created empty db
    	String outFileName = DB_PATH + DB_NAME;
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
    }
 
    /**
     * Opens the database
     * @throws SQLException
     */
    public void openDataBase() throws SQLException{
    	//Open the database
        String myPath = DB_PATH + DB_NAME;
    	database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE | SQLiteDatabase.NO_LOCALIZED_COLLATORS);
    }
	

	/**
	 * Closes the database.
	 */
	public void close() {
		dbHelper.close();
	}


	/**
	 * Retrieves the details of all the categories stored in the category table.
	 * 
	 * @return cursor with the content values id and category name inside the category table
	 */
	public Cursor fetchAllCategories() {
		return database.query(CategoryTable, new String[] { COL_ID, COL_CATEGORY}, null, null, null, null, null);
	}

	/**
	 * Retrieves the details of a specific category, given a category id
	 * 
	 * @return cursor with the content value name inside the category table
	 */
	public Cursor fetchCategory(long id) {
		String queryString =
			    "SELECT name FROM Category " +
			    "WHERE id = ? ORDER BY id";
		String[] whereArgs = new String[]{String.valueOf(id+1)};
		Cursor myCursor = database.rawQuery(queryString, whereArgs);
		return myCursor;
	}
	
	/**
	 * Retrieves the details of all the subcategories stored in the subcategory table.
	 * 
	 * @return cursor with content values of id, subcategory name, description, category id, 
	 * resource id in the subcategory table according to the given category id
	 */
	public Cursor fetchAllSubCategories(long catid) {
		Cursor myCursor = database.query(SubCategoryTable, 
				new String[] { COL_ID, COL_SUBCATEGORY, COL_DESC, COL_CAT_ID, COL_RES_ID}, 
				"catid = ?", new String[]{String.valueOf(catid+1)}, null, null, null);

		if (myCursor != null) {
			myCursor.moveToFirst();
		}

		return myCursor;
	}

	/**
	 * Retrieves the details of a specific category, given a category id
	 * 
	 * @param id  is subcategory postion
	 * @param catid is the category id it belongs to
	 * @return cursor with content values id(0), subcategory name(1), description(2), category id(3), 
	 * resource id(4) in the subcategory table according to the given subcategory id and 
	 * the category id
	 */
	
	public Cursor fetchSubCategory(long id, long catid) {
		Cursor myCursor = database.query(SubCategoryTable, 
				new String[] { COL_ID, COL_SUBCATEGORY, COL_DESC, COL_CAT_ID, COL_RES_ID}, 
				"id = ? AND catid = ?", new String[]{String.valueOf(id+1), String.valueOf(catid+1)}, null, null, null);

		if (myCursor != null) {
			myCursor.moveToFirst();
		}
		return myCursor;
	}
	
	public void insertWish(String title, String desc, int catid, int subcatid)
	{
		ContentValues values = new ContentValues();
		values.put("title", title);
		values.put(COL_DESC, desc);
		values.put(COL_CAT_ID, catid);
		values.put("subcatid", subcatid);
	    long insertId = database.insert("Wishlist",null,  values);
	    if(insertId>-1)
	    {
	    	Toast.makeText(context, "New wish added", Toast.LENGTH_SHORT).show();
	    }
	}
	

	/**
	 * 
	 * @return cursor with values as id(0), title(1), description(2), category id(3), subcategory id(4)
	 */
	public Cursor fetchAllWishes()
	{
		Cursor myCursor = database.query(WishListTable, 
				new String[] { COL_ID, "title", COL_DESC, COL_CAT_ID, "subcatid" }, 
				null, null, null, null, null);

		if (myCursor != null) {
			myCursor.moveToFirst();
		}
		return myCursor;
	}
	
	/**
	 * @param id
	 * @return cursor with values as id(0), title(1), description(2), category id(3), subcategory id(4)
	 */
	public Cursor fetchWish(long id)
	{
		Cursor myCursor = database.query(WishListTable, 
				new String[] { COL_ID, "title", COL_DESC, COL_CAT_ID, "subcatid" }, 
				"id = ?", new String[]{String.valueOf(id+1)}, null, null, null);

		if (myCursor != null) {
			myCursor.moveToFirst();
		}

		return myCursor;
	}
	
	/**
	 * @param id of the category
	 * @return cursor with values as id(0), title(1), description(2), category id(3), subcategory id(4)
	 */
	public Cursor fetchWishesByCategoryID(long catid)
	{
		Cursor myCursor = database.query(WishListTable, 
				new String[] { COL_ID, "title", COL_DESC, COL_CAT_ID, "subcatid" }, 
				"catid = ?", new String[]{String.valueOf(catid+1)}, null, null, null);

		if (myCursor != null) {
			myCursor.moveToFirst();
		}

		return myCursor;
	}

	public void deleteWish(long id)
	{
		int flag = database.delete(WishListTable, "id = ?", new String[]{String.valueOf(id+1)});
		if(flag==1)
		{
			Toast.makeText(context, "deleted successfully", Toast.LENGTH_SHORT).show();
		}
		else
		{
			Toast.makeText(context, "deletion unsuccessful", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void updateWishIds()
	{
		ContentValues values = new ContentValues();
		Cursor c = this.fetchAllWishes();
		int i=0;
		do{
			i++;
			values.put("id", i);
			database.update(WishListTable, values, "id = ?", new String[] {String.valueOf(c.getLong(0))});
		}while(c.moveToNext());
	}
	
	public void updateWish(int id, String title, String desc, int catid, int subcatid) {
		// TODO Auto-generated method stub
		ContentValues values = new ContentValues();
		values.put("title", title);
		values.put(COL_DESC, desc);
		database.update(WishListTable, values, "id = ?", new String[] {String.valueOf(id)});
	}
	
	private static final String LOGIN_TABLE = "USER_TABLE";
	public static final String COL_USERNAME = "username";
	public static final String COL_PASSWORD = "password";
	public static final String COL_SECURITYQ = "securityQuestion";
	public static final String COL_SECURITYA = "securityAnswer";
	
	public long createUser(String username, String password, String securityQuestion, String securityAnswer) {
		ContentValues initialValues = createUserTableContentValues(username, password, securityQuestion, securityAnswer);
		return database.insert(LOGIN_TABLE, null, initialValues);
	}
	
	private ContentValues createUserTableContentValues(String username, String password, String securityQuestion, String securityAnswer) {
		ContentValues values = new ContentValues();
		values.put(COL_USERNAME, username);
		values.put(COL_PASSWORD, password);
		values.put(COL_SECURITYQ, securityQuestion);
		values.put(COL_SECURITYA, securityAnswer);
		return values;
	}

	public String returnPassword(String userName)
	{
		Cursor c = database.query("USER_TABLE", new String[] {"password"}, "username=?", new String[] {userName} , null, null, null);
		String passWord = "";
		if(c.moveToFirst())
		{
			passWord = c.getString(0);
		}
		c.close();
		return passWord;
	}
	
	public String returnQuestion(String userName)
	{
		Cursor c = database.query("USER_TABLE", new String[] {COL_SECURITYQ}, "username=?", new String[] {userName},null, null, null);
		String question = "";
		if(c.moveToFirst())
		{
			question = c.getString(0);
		}
		c.close();
		return question;
	}
	
	public String returnAnswer(String userName)
	{
		Cursor c = database.query("USER_TABLE", new String[] {"securityAnswer"}, "username=?", new String[] {userName}, null, null, null);
		String answer = "";
		if(c.moveToFirst())
		{
			answer = c.getString(0);
		}
		c.close();
		return answer;
	}
	
	public int updatePassword(String userName, String newPassword)
	{
		ContentValues c = new ContentValues();
		c.put(COL_PASSWORD, newPassword);
		return database.update(LOGIN_TABLE, c, "username=?", new String[] {userName});		
	}
	
	public boolean deleteUser(String userName)
	{
		return false;
	}

	
}
