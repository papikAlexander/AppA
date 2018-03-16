package com.alex.appa.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;


public class MyContentProvider extends ContentProvider{
    public MyContentProvider() {
    }

    static final String DB_NAME = "dbA";
    static final int DB_VERSION = 2;

    static final String LINKS_TABLE = "links";

    static final String LINK_ID = "_id";
    static final String LINK_LINK = "link";
    static final String LINK_STATUS = "status";
    static final String LINK_DATA = "data";

    static final String DB_CREATE = "create table " + LINKS_TABLE
            + "(" + LINK_ID + " integer primary key autoincrement, "
            + LINK_LINK + " text not null, " + LINK_STATUS + " integer, "
            + LINK_DATA + " text, UNIQUE(" + LINK_LINK + "));";

    static final String AUTHORITY = "com.alex.appa.providers.dbA";
    static final String LINK_PATH = "links";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + LINK_PATH);

    static final int URI_LINKS = 1;
    static final int URI_LINKS_ID = 2;

    static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + LINK_PATH;
    static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + LINK_PATH;

    private final static UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, LINK_PATH, URI_LINKS);
        uriMatcher.addURI(AUTHORITY, LINK_PATH + "/#", URI_LINKS_ID);
    }

    DBHelper dbHelper;
    SQLiteDatabase db;

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        switch (uriMatcher.match(uri)){
            case URI_LINKS:
                break;
            case URI_LINKS_ID:

                String id = uri.getLastPathSegment();

                if (TextUtils.isEmpty(selection)){
                    selection = LINK_ID + " = " + id;
                } else {
                    selection = selection + " AND " + LINK_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        db = dbHelper.getWritableDatabase();
        int count = db.delete(LINKS_TABLE, selection, selectionArgs);

        return count;
    }

    @Override
    public String getType(Uri uri) {

        switch (uriMatcher.match(uri)){
            case URI_LINKS:
                return CONTENT_TYPE;
            case URI_LINKS_ID:
                return CONTENT_ITEM_TYPE;
        }

        return null;

    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        if (uriMatcher.match(uri) != URI_LINKS)
            throw new IllegalArgumentException("Wrong URI: " + uri);

        db = dbHelper.getWritableDatabase();
        long rowID = db.insert(LINKS_TABLE, null, values);
        Uri resultUri = ContentUris.withAppendedId(CONTENT_URI, rowID);


        return resultUri;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        switch (uriMatcher.match(uri)){
            case URI_LINKS:
                if (TextUtils.isEmpty(sortOrder)){
                    sortOrder = LINK_DATA + " ASC";
                }
                break;
            case URI_LINKS_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)){
                    selection = LINK_ID + " = " + id;
                } else {
                    selection = selection + " AND " + LINK_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(LINKS_TABLE, projection, selection, selectionArgs,
                null, null, sortOrder);

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        switch (uriMatcher.match(uri)){
            case URI_LINKS:
                break;
            case URI_LINKS_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)){
                    selection = LINK_ID + " = " + id;
                } else {
                    selection = selection + " AND " + LINK_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int count = db.update(LINKS_TABLE, values, selection, selectionArgs);
        return count;
    }

    public class DBHelper extends SQLiteOpenHelper{

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);
            ContentValues cv = new ContentValues();
            for (int i = 1; i <= 3; i++) {
                cv.put(LINK_LINK, "name " + i);
                cv.put(LINK_DATA, "email " + i);
                cv.put(LINK_STATUS, i);
                db.insert(LINKS_TABLE, null, cv);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (newVersion > oldVersion) {
                db.execSQL("DROP TABLE IF EXISTS " + LINKS_TABLE);
                onCreate(db);
            }
        }
    }
}
