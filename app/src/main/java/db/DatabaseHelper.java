package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import bean.Article;
import bean.ArticleDetail;

/**
 * Created by Administrator on 2016/9/21.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TABLE_ARTICLE="article";
    private static final String TABLE_ARTICLE_CONTENT="article_content";
    private static final String CREATE_ARTICLES_TABLE_SQL = "CREATE TABLE articles (  "
            + " post_id INTEGER PRIMARY KEY UNIQUE, "
            + " author VARCHAR(30) NOT NULL ,"
            + " title VARCHAR(50) NOT NULL,"
            + " category INTEGER ,"
            + " publish_time VARCHAR(50) "
            + " )";
    private static final String CREATE_ARTICLE_CONTENT_TABLE_SQL = "CREATE TABLE article_content (  "
            + " post_id INTEGER PRIMARY KEY UNIQUE, "
            + " content TEXT NOT NULL "
            + " )";
    static final String DB_NAME="devtf.db";
    static  final int DB_VERSION = 1;
    private SQLiteDatabase database;
    private static DatabaseHelper databaseHelper;
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        database=getWritableDatabase();
    }
    public static void init(Context context){
        if (databaseHelper==null)
        databaseHelper=new DatabaseHelper(context);
    }
    public static DatabaseHelper getInstance() {
        if (databaseHelper == null) {
            throw new NullPointerException("sDatabaseHelper is null,please call init method first.");
        }
        return databaseHelper;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
          db.execSQL(TABLE_ARTICLE);
          db.execSQL(TABLE_ARTICLE_CONTENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void saveArticles(List<Article> articles){
        for(Article article:articles){
            database.insertWithOnConflict(TABLE_ARTICLE,null,ArticleToContentValues(article)
                    ,SQLiteDatabase.CONFLICT_REPLACE);
        }
    }
    private ContentValues ArticleToContentValues(Article item){
        ContentValues values=new ContentValues();
        values.put("post_id",item.post_id);
        values.put("author",item.author);
        values.put("title",item.title);
        values.put("category",item.category);
        values.put("publish_time",item.publishTime);
        return  values;
    }
    public List<Article> getArticles(){
        Cursor cursor=database.rawQuery("select * from "+TABLE_ARTICLE,null);
        List<Article> result=parseArticles(cursor);
        cursor.close();
        return  result;
    }
    private List<Article> parseArticles(Cursor cursor){
        List<Article> articles=new ArrayList<>();
        while (cursor.moveToNext()){
            Article item=new Article();
            item.post_id=cursor.getString(0);
            item.author=cursor.getString(1);
            item.title=cursor.getString(2);
            item.category=cursor.getInt(3);
            item.publishTime=cursor.getString(4);
            articles.add(item);
        }
        return  articles;
    }
    public void saveArticleDetail(ArticleDetail detail){

        database.insertWithOnConflict(TABLE_ARTICLE_CONTENT,null,contentToContentValues(detail)
                ,SQLiteDatabase.CONFLICT_REPLACE);
    }
    public ArticleDetail getArticleDetail(String postid){
        Cursor cursor=database.rawQuery("select * from "+TABLE_ARTICLE_CONTENT
                +"where post_id="+postid,null);
        ArticleDetail detail=new ArticleDetail(parseArticleContent(cursor),postid);
        return detail;
    }
    private String parseArticleContent(Cursor cursor){
        return  cursor.moveToNext()?cursor.getString(1):"";
    }
    private ContentValues contentToContentValues(ArticleDetail detail){
        ContentValues values=new ContentValues();
        values.put("post_id",detail.getPost_id());
        values.put("content",detail.getContent());
        return  values;
    }
}
