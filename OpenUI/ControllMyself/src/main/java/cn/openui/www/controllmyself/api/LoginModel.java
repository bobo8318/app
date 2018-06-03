package cn.openui.www.controllmyself.api;

import cn.openui.www.controllmyself.api.OpenAPIModel;

/**
 * Created by My on 2018/2/6.
 */
public class LoginModel extends OpenAPIModel{

    private String token;
    private long validatTime;
    private String email;
    private String username;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getValidatTime() {
        return validatTime;
    }

    public void setValidatTime(long validatTime) {
        this.validatTime = validatTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
