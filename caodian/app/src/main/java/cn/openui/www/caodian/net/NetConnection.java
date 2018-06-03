package cn.openui.www.caodian.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.os.AsyncTask;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.megvii.cloud.http.HttpRequest;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLException;

import cn.openui.www.caodian.config.Config;

public class NetConnection extends AsyncTask<Integer, Integer, String> {

	private HttpMethod method;
	private String url;
	private SuccessCallback success;
	private FailCallback fail;
	private String postData;
	private  HashMap<String, String> map;
	private  HashMap<String, byte[]> fileMap;

	
	public NetConnection(String url, HttpMethod method, final SuccessCallback success,final FailCallback fail, String...params){
		this.method = method;
		this.url = url;
		this.success = success;
		this.fail = fail;

		StringBuffer sb = new StringBuffer();
		for(int i=0;i<params.length;i++){
			sb.append("/").append(params[i]);
		}
		postData = sb.toString();
	}

	public NetConnection(String url, final SuccessCallback success, final FailCallback fail, HashMap<String, String> map, HashMap<String, byte[]> fileMap) throws Exception {

		this.method = HttpMethod.MULTIPOST;
		this.url = url;
		this.success = success;
		this.fail = fail;

		this.map = map;
		this.fileMap = fileMap;
	}
	private void postData(DataOutputStream obos) throws Exception {

		Iterator iter = map.entrySet().iterator();
		while(iter.hasNext()){
			Map.Entry<String, String> entry = (Map.Entry) iter.next();
			String key = entry.getKey();
			String value = entry.getValue();
			obos.writeBytes(Config.BOUNDARYSTR);
			obos.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"\r\n");
			obos.writeBytes("\r\n");
			obos.writeBytes(value + "\r\n");

		}

		if(fileMap != null && fileMap.size() > 0){
			Iterator fileIter = fileMap.entrySet().iterator();
			while(fileIter.hasNext()){
				Map.Entry<String, byte[]> fileEntry = (Map.Entry<String, byte[]>) fileIter.next();
				obos.writeBytes( Config.BOUNDARYSTR);
				obos.writeBytes("Content-Disposition: form-data; name=\"" + fileEntry.getKey() + "\"; filename=\"" + encode(" ") + "\"\r\n");
				obos.writeBytes("\r\n");
				obos.write(fileEntry.getValue());
				obos.writeBytes("\r\n");
			}
		}
		obos.writeBytes(Config.BOUNDARYSTR);
		obos.writeBytes("\r\n");


	}

	@Override
	protected String doInBackground(Integer... params) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		BufferedWriter bw = null;
		InputStream ins = null;
		int code;
		try {
			HttpURLConnection uc;
			switch(this.method){
			case POST:
				uc = (HttpURLConnection) new URL(this.url).openConnection();
				uc.setDoInput(true);
				uc.setDoOutput(true);
				uc.setRequestProperty("accept", "*/*");
				uc.setRequestProperty("connection", "Keep-Alive");
				uc.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
				bw = new BufferedWriter(new OutputStreamWriter(uc.getOutputStream()));
				bw.write(postData);
				break;
			case MULTIPOST:
				DectectHelper helper = new DectectHelper();
				return helper.DetectFace(this.map,this.fileMap);
				//HttpRequest.post()
			default:
				Log.v("debug", this.url+postData);
				uc = (HttpURLConnection) new URL(this.url+postData).openConnection();
				code = uc.getResponseCode();
				if(code==200) ins = uc.getInputStream();
				break;
			}

			StringBuffer result = new StringBuffer();
			if(ins!=null) {

				BufferedReader br = new BufferedReader(new InputStreamReader(ins, Config.CHARSET));

				String line = null;
				while ((line = br.readLine()) != null) {
					result.append(line);
				}
			}
			return result.toString();
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		this.success.onSuccess(result);
	}
	private static String encode(String value) throws Exception{
		return URLEncoder.encode(value, "UTF-8");
	}

	public static interface SuccessCallback{
		void onSuccess(String result);
	}
	
	public static interface FailCallback{
		void onFail();
	}
}
