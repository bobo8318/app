package cn.openui.www.controllmyself.api;

/**
 * Created by My on 2018/2/6.
 */
public class OpenAPIModel {

        private Integer status;
        private String msg;


        public void setStatus(Integer status) {
            this.status = status;
        }

        public void setMsg(String msg) {
            this.msg = msg;

        }

    public Integer getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }
}


