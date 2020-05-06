package com.hbnet.fastsh.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

//令牌生产器  
@SuppressWarnings("restriction")
public class TokenProcessor {  
  private TokenProcessor(){}  
  private static TokenProcessor instance = new TokenProcessor();  
  public static TokenProcessor getInstance(){  
      return instance;  
  }  
  public String generateTokeCode(){  
      String value = System.currentTimeMillis()+new Random().nextInt()+"";  
      //获取数据指纹，指纹是唯一的  
      try {  
          MessageDigest md = MessageDigest.getInstance("md5");  
          byte[] b = md.digest(value.getBytes());//产生数据的指纹
          return Base64.getEncoder().encodeToString(b);//制定一个编码
      } catch (NoSuchAlgorithmException e) {  
          e.printStackTrace();  
      }   
      return null;  
  }  
}  