package bean;

/**
 * Created by Administrator on 2016/9/14.
 */
public class MenuItem {
    public MenuItem( String name,int resId) {
        ResId = resId;
        this.name = name;
    }

    public int getResId() {
        return ResId;
    }

    public void setResId(int resId) {
        ResId = resId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private int ResId;
    private String name;

}
