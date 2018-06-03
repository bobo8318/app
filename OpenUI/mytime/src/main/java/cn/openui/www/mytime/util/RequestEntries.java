package cn.openui.www.mytime.util;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class RequestEntries {

	private static String METHOD = "method";
	private static String USER = "user";
	private static String TIME = "time";
	private static String INFO = "info";
	private static String KEY = "key";
	private static String TOKEN = "token";
	private static String VERSION = "v";
	//固定请求内容
	private String key;
	private String token;

	private String postData;

	private String method;
	
	//可变参数
	private Map<String,String> params;

	public void clear(){
		this.params.clear();
	}
	
	public RequestEntries(String key, String token){
		params = new HashMap<String,String>();
		
		this.key = key;
		this.token = token;
	}
	/**
	 * 根据实体数据 生成请求参数字符串
	 * @return
	 */
	public String getUrl(){
		StringBuffer sb = new StringBuffer();
		sb.append(method).append("/");
		//将所有键值对进行排序 生成sha1 token
		//加入key
		params.put(KEY, this.key);
		List<Entry<String,String>> sortlist = new ArrayList<Entry<String,String>>(params.entrySet());
		for(Entry<String,String> entry:params.entrySet()){
			sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
		}
		
		
		Collections.sort(sortlist, new Comparator<Entry<String,String>>(){
			@Override
			public int compare(Entry<String, String> o1,Entry<String, String> o2) {
				// TODO Auto-generated method stub
				return (o1.getKey().toString()).compareToIgnoreCase(o2.getKey().toString());
			}
			
		});
		String shatoken = getSha1Token(sortlist);
		
		sb.append(TOKEN).append("=").append(shatoken);
		
		return sb.toString();
	}
	
	/**
	 * 使用sha1 token
	 * @return
	 */
	public  String getSha1Token(List<Entry<String,String>> sortlist){
		
		StringBuffer sb = new StringBuffer();
		
		for(Entry<String,String> keyvalue:sortlist){
			sb.append(keyvalue.getKey()).append(keyvalue.getValue());
		}
		sb.append(this.token);
		return new String(Hex.encodeHex(DigestUtils.sha1(sb.toString())));
	}
	public void addParam(String key, String value){
		params.put(key, value);
	}


    public String getPostData() {
        return postData;
    }

    public void setPostData(String postdata){
		this.postData = postdata;
	}

	public void setMethod(String method) {
		this.method = method;
	}
}
