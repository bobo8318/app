package cn.openui.www.mytime.util;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OpenApiSdk {

	private String HOST = "http://www.openui.cn/api";

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
		builder.add("data",entry.getPostData());
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


}
