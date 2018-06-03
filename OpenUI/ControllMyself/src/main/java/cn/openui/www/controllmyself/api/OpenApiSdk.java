package cn.openui.www.controllmyself.api;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.asm.ClassReader;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

import cn.openui.www.controllmyself.api.RequestEntries;
import cn.openui.www.controllmyself.util.TextUtils;
import okhttp3.*;

public class OpenApiSdk {

	private String HOST = "http://www.openui.cn/cp";

	private OkHttpClient client = new OkHttpClient();

	public void setHost(String host){
		this.HOST = host;
	}
	
	public String doGet(RequestEntries entry){
		Request request = new Request.Builder().url(HOST+"/"+entry.getUrl()).build();
		String result = "";
		try {
			Response response = client.newCall(request).execute();
			if(!response.isSuccessful()){
				throw new IOException("服务器端故障:" + response);
			}
			//Headers responsheader = response.headers();
			//result = response.body().toString();
			result = response.body().string();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "{\"status\":404,\"msg\":\"timeout\"}";
		}
		
		return result;
	}

	public String doPost(RequestEntries entry){


		FormBody.Builder builder = new FormBody.Builder();

		if(!TextUtils.isEmpty(entry.getPostDataText()))
			builder.add("data",entry.getPostDataText());

		Iterator<Map.Entry<String,String>> entrys = entry.getPostData().entrySet().iterator();
		while(entrys.hasNext()){
			Map.Entry<String,String> postdata = entrys.next();
			builder.add(postdata.getKey(),postdata.getValue());
		}

		RequestBody formBody = builder.build();

		String result = "";
		try {
			Request request = new Request.Builder().url(HOST+"/"+entry.getUrl()).post(formBody).build();
			Response response = client.newCall(request).execute();
			if(!response.isSuccessful()){
				throw new IOException("服务器端故障:" + response);
			}
			//Headers responsheader = response.headers();
			//result = response.body().toString();
			result = response.body().string();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "{\"status\":404,\"msg\":\"timeout\"}";
		}

		return result;
	}

	public <E extends OpenAPIModel> E parseResponse(String response, Class<E> clazz){

		E instance = null;
		if(!TextUtils.isEmpty(response)){
			try {

				instance = (E) clazz.newInstance();

				if(response.contains("wrong")){

					instance.setStatus(406);
					instance.setMsg(response);
					return instance;
				}

				JSONObject object = JSON.parseObject(response);

				instance.setStatus(Integer.valueOf(object.get("status").toString()));
				instance.setMsg(object.get("msg").toString());

				Method[] methpods = clazz.getMethods();

				if(object.containsKey("data")){
					String jsonData = object.get("data").toString();
					JSONObject data = JSON.parseObject(jsonData);
				}



			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

		}
		return instance;
	}


	public interface CallBack{
		public void onSuccess(String msg);
		public void onFail(String msg);
	}

}
