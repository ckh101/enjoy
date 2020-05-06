package com.hbnet.fastsh.web.cache;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;



@Service
public class CacheParser3 {
	
	public static long time;
	
	private static final ObjectMapper mapper;
	private static final ObjectMapper mapper2;

     static{
         mapper = new ObjectMapper();
         mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
         mapper2 = new ObjectMapper();
         mapper2.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
     }
	
	public <T> T parse(String line, TypeReference<T> typeReference) {
		if(typeReference == null){
			return null;
		}
				
		T t = null;
		
		if(line==null || line.trim().length()==0){
			return t;
		}
		
		try {
			t = (T)mapper.readValue(line, typeReference);
			return t;
		} catch (JsonParseException e1) {
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		return t;

	}
	
	public String unparse(Object o) {
	    if (o == null){
            return null;
        }
        String t = null;
        try {
            t = mapper.writeValueAsString(o);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t;
	}
	
	public String unparseForIntf(Object o) {//System.out.println("in unparse Mehtod :"+(System.currentTimeMillis() - time));
        if (o == null){
            return null;
        }
        String t = null;
        
        //JSONObject json = new JSONObject(); 
        //ObjectMapper mapper = new ObjectMapper(); //System.out.println("new JSONObject :"+(System.currentTimeMillis() - time));
        try {
            t = mapper2.writeValueAsString(o);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t;
        
    }
	
}
