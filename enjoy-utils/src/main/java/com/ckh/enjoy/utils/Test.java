package com.ckh.enjoy.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Test {
    static int j = 0;

    public static void main(String[] args) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("key", "1f313f4ee819f4955aa6feb64fc3cadd");
        ExecutorService pool = Executors.newFixedThreadPool(5);
        CountDownLatch latch = new CountDownLatch(5);
        String orderJsonStr = readJsonFile("D:\\programfile\\nginx-1.15.2\\html\\yptm_order.json");
        JSONArray orderJsonArray = JSONArray.parseArray(orderJsonStr);

        List<JSONObject> list =  orderJsonArray.toJavaList(JSONObject.class);
        FileWriter writer = new FileWriter("E:\\data\\ddt_order.txt");
        list.forEach(obj->{
            String product_id = obj.getString("product_id");
            String cn = obj.getString("cn");
            String account_id = obj.getString("account_id");
            String openid = obj.getString("openid");
            String product_name = obj.getString("product_name").trim();
            String name = obj.getString("address").trim().split(" ")[0];
            String address = obj.getString("address").trim().split(" ")[1].replaceAll("-","");
            String paid_price = obj.getString("paid_price");
            String mobile = obj.getString("mobile");
            String paied_time = obj.getString("paied_time");
            if(!name.equals("测试")){
                try {
                    params.put("address", address);
                    String jstr = OkHttpUtils.getInstance().get("https://restapi.amap.com/v3/geocode/geo", params);
                    if(jstr != null){
                        JSONObject json = JSONObject.parseObject(jstr);
                        if(json.getString("info").equals("OK")&&json.getIntValue("count") > 0){
                            JSONObject geo = json.getJSONArray("geocodes").getJSONObject(0);
                            String province = geo.getString("province");
                            String city = geo.getString("city");
                            String district = geo.getString("district");
                            String[] location = geo.getString("location").split(",");
                            writer.write(openid+"\001"+product_id+"\001"+cn+"\001"+account_id+"\001"+product_name+"\001"+name+"\001"+address+"\001"+province+"\001"+city+"\001"+district+"\001"+location[0]+"\001"+location[1]+"\001"+paid_price+"\001"+mobile+"\001"+paied_time+"\n");
                        }
                    }
                    Thread.sleep(200);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        writer.flush();
        writer.close();
    }

    public static String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);

            Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
