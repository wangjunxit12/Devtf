package com.example.devtf.devtf;

import android.os.AsyncTask;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import listener.DataListener;
import listener.RespParser;

/**
 * Created by Administrator on 2016/9/28.
 */

public final class HttpFlinger {
    private HttpFlinger(){

    }
   public static <T> void get(final String reqUrl,final RespParser<T> parser,
       final DataListener<T> listener   ){
       new AsyncTask<Void,Void,T>(){

           @Override
           protected T doInBackground(Void... params) {
               HttpURLConnection urlConnection=null;
               try{

                   urlConnection= (HttpURLConnection) new URL(reqUrl).openConnection();
                   urlConnection.connect();
                   BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(
                           urlConnection.getInputStream()
                   ));
                   StringBuilder sb=new StringBuilder();
                   String line=null;
                   while((line=bufferedReader.readLine())!=null){
                       sb.append(line).append("\n");
                   }
                   String result=sb.toString();
                   return parser.parseResponse(result);//将json解析为文章列表
               } catch (MalformedURLException e) {
                   e.printStackTrace();
               } catch (JSONException e) {
                   e.printStackTrace();
               }catch (IOException e){
                  e.printStackTrace();
               }  finally{
                   if(urlConnection!=null)
                   urlConnection.disconnect();
               }
               return null;
           }

           @Override
           protected void onPostExecute(T t) {
               if(listener!=null){
                   listener.onComplete(t);
               }
           }
       }.execute();
   }
}
