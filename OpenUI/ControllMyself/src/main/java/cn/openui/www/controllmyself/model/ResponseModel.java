package cn.openui.www.controllmyself.model;

/**
 * Created by My on 2018/1/12.
 */
public class ResponseModel {

    private int status;
    private String msg;
    private String body;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
