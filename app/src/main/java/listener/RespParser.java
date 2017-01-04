package listener;

import org.json.JSONException;

/**
 * Created by Administrator on 2016/9/28.
 */

public interface RespParser<T> {
    public T parseResponse(String result)throws JSONException;
}
