package bean;

import org.json.JSONException;

import listener.RespParser;

/**
 * Created by Administrator on 2016/9/28.
 */

public class DefaultParser implements RespParser<String> {
    @Override
    public String parseResponse(String result) throws JSONException {
        return result;
    }
}
