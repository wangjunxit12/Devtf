package listener;

/**
 * Created by Administrator on 2016/9/28.
 */

public interface DataListener<T> {
    void onComplete(T result);
}
