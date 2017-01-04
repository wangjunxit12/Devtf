package bean;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import listener.RespParser;

/**
 * Created by Administrator on 2016/9/28.
 */

public class ArticleParser implements RespParser<List<Article>>{
    private static SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
    @Override
    public List<Article> parseResponse(String result) throws JSONException {

        JSONArray array=new JSONArray(result);
        List<Article> articleList=new LinkedList<>();
        int count=array.length();
        for(int i=0;i<count;i++){
            JSONObject object=array.optJSONObject(i);
            articleList.add( parseItem( object));
        }
        return  articleList;
    }
    private Article parseItem(JSONObject object){
        Article article=new Article();
        article.title=object.optString("title");
        article.author=object.optString("author");
        article.post_id=object.optString("post_id");
        article.publishTime=formatDate(dateFormat,object.optString("date"));
        String category=object.optString("category");
        article.category= TextUtils.isEmpty(category)?0:Integer.valueOf(category);
        return  article;
    }
    private static String formatDate(SimpleDateFormat dateFormat,String date){
        try{
            Date datee=dateFormat.parse(date);
            return dateFormat.format(datee);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
