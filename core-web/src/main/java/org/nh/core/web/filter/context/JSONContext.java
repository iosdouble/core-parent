//package org.nh.core.web.filter.context;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.gome.gscm.git.GitField;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonParser;
//
///**
// * Created by root on 3/5/18.
// */
//public class JSONContext {
//
//    public static <T> T toObject(String json, Class<T> t) {
//        return JSONObject.parseObject(json, t);
//    }
//
//    public static String toJson(Object obj) {
//        return JSONObject.toJSONString(obj);
//    }
//
//    public static JSONObject toJsonObject(String s) {
//        return JSONObject.parseObject(s);
//    }
//
//    public static JSONObject toJsonObject(Object o) {
//        return (JSONObject) JSONObject.toJSON(o);
//    }
//
//    public static String toOutput(JSON json) {
//        if (json == null)
//            return "";
//        String uglyJSONString = json.toString();
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        JsonParser jp = new JsonParser();
//        JsonElement je = jp.parse(uglyJSONString);
//        return gson.toJson(je);
//    }
//
//    public static String toOutput(String uglyJSONString) {
//        if (null == uglyJSONString || "".equals(uglyJSONString))
//            return "";
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        JsonParser jp = new JsonParser();
//        JsonElement je = jp.parse(JSONObject.parse(uglyJSONString).toString());
//        return gson.toJson(je);
//    }
//
//    //转换JSON
//    public String getJsonString(Object object){
//        if(null==object)
//            return null;
//        return JSON.toJSONString(object);
//    }
//
//    //转为字符串
//    public String resultString(Object object){
//        if(null==object)
//            return null;
//        return object.toString();
//    }
//
//    //初始化结果
//    public JSONObject initResult(){
//        JSONObject result = new JSONObject();
//        result.put(GitField.FLAG, GitField.Y);
//        return result;
//    }
//    //初始化结果
//    public JSONObject initResult(String message){
//    	JSONObject result = new JSONObject();
//    	result.put(GitField.FLAG, GitField.Y);
//    	result.put(GitField.MSG, message);
//    	return result;
//    }
//
//    //错误信息
//    public void errorResult(JSONObject result,String message){
//        result.put(GitField.FLAG, GitField.N);
//        result.put(GitField.MSG, message);
//    }
//
//    //系统异常
//    public void systemError(JSONObject result){
//        result.put(GitField.FLAG, GitField.N);
//        result.put(GitField.MSG, GitField.SYS_ERR);
//    }
//
//    //封装回调结果
//    public String CallResult(JSONObject result,String callback){
//        return callback + "(" + resultString(result) + ")";
//    }
//
//    //拼接字符串
//    public String contactString(String s){
//
//    	return s+"不能为空！";
//    }
//}
