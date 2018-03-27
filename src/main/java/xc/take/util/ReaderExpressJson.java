package xc.take.util;


import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
 /*
  需求，获取到json文件里面的json数据。然后读取出来用于展示。
 * 1. 按其中一个字段A-Z来排序。
 * 2. 输入之后模糊匹配查询
 * 3. 
 * 方法1 ：   模糊匹配查询的方法，（首字母查询，中文name查询）    返回一个List集合。list里面是匹配到的数据
 * 方法2 : 所有方法排方式 A-Z
 */

public class ReaderExpressJson {
	private static JsonParser  parser;
	private static JSONArray array;
	private static  Map<String, List<Express>>  sortMap;
	
	//联想数据库连接池加载驱动。静态代码块在装载的时候执行，且只执行一次
	static{
		 try {
			 InputStream input = ReaderExpressJson.class.getClassLoader().getResourceAsStream("/xc/take/util/text2.json");
			 parser = new JsonParser();
			  JsonElement parse = parser.parse(new InputStreamReader(input, "utf-8"));
				Gson gson = new Gson();
				String json = gson.toJson(parse);
			 array = JSON.parseArray(json);
			 //第一次加载的时候，就已经执行并排序
			 sortMap= ReaderExpressJson.sortAll();
			 
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		
	}
	
	//获取所有数据的方法
	public static Map<String, List<Express>> getAll(){
		
		return sortMap;
	}
	
	// 查询方法
	public static List<Express>  getLike(String  key){
		if(key==null||key==""){
			return null;
		}
		
		//根据关键字查询。  从我所有的Map里面查询。
		List<Express> newList = new ArrayList<Express>();
		//首先判断是否存在于map的key里面
		if(sortMap.containsKey(key)){
			//如果存在 ，返回此Map的value
			return sortMap.get(key);
		}else {
		  //如果不存在,是否存在于name字段里面,首先遍历这个map
		  for (List<Express> list : sortMap.values()) {
			for (Express express : list) {
				if(express.getName().indexOf(key)>=0){
				//存在，则添加到新的List集合里面
				newList.add(express);
				}
			}
		  }
		}
		return newList;
	}
	
	

	
	//获取数据并排序
	private static Map<String, List<Express>> sortAll(){
		List<Express> list= null;
		Express express=null;
		Map<String, List<Express>>  expressMap = new TreeMap<String , List<Express>>();
		//判空
		if(array==null||array.size()<=0){
			return null;
		}
		for (int i = 0; i < array.size(); i++) {
			JSONObject object = array.getJSONObject(i);
			//首先第一步，查看键是否已经存在于map里面。
			if(!expressMap.containsKey(object.getString("firstLetter"))){
				
				//如果不存在，就new对象，创建新的List集合。
				 list= new ArrayList<Express>();
				 express = new Express();
				 express.setName(object.getString("name") );
				 express.setFirstLetter(object.getString("firstLetter"));
				 express.setCode(object.getString("code"));
				 list.add(express);
				 expressMap.put(express.getFirstLetter(), list);
			}else{
				//如果已经存在，那么创建对象，不创建list
				 express = new Express();
				 express.setName(object.getString("name"));
				 express.setFirstLetter(object.getString("firstLetter"));
				 express.setCode(object.getString("code"));
				expressMap.get(object.getString("firstLetter")).add(express);
			}
		}	
		return expressMap;
	}
	

	
}



