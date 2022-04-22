package app.com.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.boot.json.JsonParseException;
import org.springframework.util.ObjectUtils;

public class StringUtil {

	public static  <T> T convertJSONstringToEntity(String json, Class<T> type) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, com.fasterxml.jackson.core.JsonParseException, JsonMappingException, IOException {

		T instance = type.getConstructor().newInstance();

		ObjectMapper mapper = new ObjectMapper();

		instance = mapper.readValue(json, type);
		return instance;
	}
	
	/**
	 * JSONstring to Map
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public static Map<String,Object> convertJSONstringToMap(String json) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<String, Object>();        
        map = mapper.readValue(json, new TypeReference<Map<String, Object>>() {});        
        return map;
	}

	/**
	 * JSONstring to List
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public static List<Object> convertJSONstringToList(String json) throws Exception {

        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray = (JSONArray) jsonParser.parse(json);
        List<Object> list  = new ArrayList<Object>();
        for(int i=0; i<jsonArray.size(); i++){
            JSONObject jsonData = (JSONObject) jsonArray.get(i);            
            list.add(convertJSONstringToMap(jsonData.toString()));
        }        
        return list;
	}
	/**
	 * JSONObject를 Map<String, String> 형식으로 변환처리.
	 * @param jsonObj
	 * @return
	 */
	public static Map<String, Object> getMapFromJsonObject(JSONObject jsonObj){
	    Map<String, Object> map = null;	    
	    try {
	       map = new ObjectMapper().readValue(jsonObj.toString(), Map.class);
	    } catch (JsonParseException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    } catch (JsonMappingException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	    return map;
	}
	
	/**
	 * JSONstring to List
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> convertGridDataToList(String json) throws Exception {		
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();		
		if(!ObjectUtils.isEmpty(json)) {			
			JSONParser jsonParser = new JSONParser();
			Object jsonObject = jsonParser.parse(json);
			JSONArray changedDataArray = (JSONArray)jsonObject;	        
			for (int i = 0; i < changedDataArray.size(); i++) {
				JSONObject cRecord = (JSONObject)changedDataArray.get(i);
				String job = cRecord.get("job").toString();
				JSONObject data = (JSONObject)cRecord.get("data");
				Map<String, Object> map = getMapFromJsonObject(data);
				map.put("job", job);
				list.add(map);
			}
		}        
        return list;
	}

		/**
	 * JSONstring to List
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> convertGridDataToList(HttpServletRequest request) throws Exception {
	   	
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String json = request.getParameter("changedData");
		if(!ObjectUtils.isEmpty(json)) {		
			JSONParser jsonParser = new JSONParser();
			Object jsonObject = jsonParser.parse(json);
			JSONArray changedDataArray = (JSONArray)jsonObject;	        
			for (int i = 0; i < changedDataArray.size(); i++) {
				JSONObject cRecord = (JSONObject)changedDataArray.get(i);
				String job = cRecord.get("job").toString();
				JSONObject data = (JSONObject)cRecord.get("data");
				Map<String, Object> map = getMapFromJsonObject(data);				
				map.put("job", job);
				list.add(map);
			}
		}
        return list;
	}

	public static Map<String, Object> convertToMap(Object obj) throws IllegalAccessException, InstantiationException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		if (obj == null) {
			return Collections.emptyMap();
		}

		Map<String, Object> convertMap = new HashMap<>();

		Field[] fields = obj.getClass().getDeclaredFields();

		for (Field field : fields) {
			field.setAccessible(true);
			convertMap.put(field.getName(), field.get(obj));
		}
		return convertMap;
	}

	public static <T> T convertToValueObject(Map<String, Object> map, Class<T> type)
			throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, IllegalArgumentException, ParseException {
		if (type == null) {
			throw new NullPointerException("Class cannot be null");
		}

		T instance = type.getConstructor().newInstance();

		if (map == null || map.isEmpty()) {
			return instance;
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		for (Map.Entry<String, Object> entrySet : map.entrySet()) {
			Field[] fields = type.getDeclaredFields();

			for (Field field : fields) {
				field.setAccessible(true);

				String fieldName = field.getName();

				//boolean isSameType = entrySet.getValue().getClass().equals(field.getType());
				boolean isSameName = entrySet.getKey().equals(fieldName);
				
				if (isSameName) {
					if(map.get(fieldName) !=  null){
						if(field.getType().equals(String.class))
						field.set(instance, String.valueOf(map.get(fieldName)));
						else if(field.getType().equals(Long.class))
						field.set(instance, Long.parseLong(String.valueOf(map.get(fieldName))));
						else if(field.getType().equals(Float.class))
						field.set(instance, Float.parseFloat(String.valueOf(map.get(fieldName))));
						else if(field.getType().equals(Date.class))
						field.set(instance, dateFormat.parse((String) map.get(fieldName)));
						else field.set(instance, map.get(fieldName));
					}
				}
			}
		}
		return instance;
	}

	
	public static List<Map<String, Object>> convertToMaps(List<?> list)
			throws IllegalAccessException, InstantiationException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		if (list == null || list.isEmpty()) {
			return Collections.emptyList();
		}

		List<Map<String, Object>> convertList = new ArrayList<>();

		for (Object obj : list) {
			convertList.add(convertToMap(obj));
		}
		return convertList;
	}

	public static <T> List<T> convertToValueObjects(List<Map<String, Object>> list, Class<T> type)
			throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, IllegalArgumentException, ParseException {
		if (list == null || list.isEmpty()) {
			return Collections.emptyList();
		}

		List<T> convertList = new ArrayList<>();

		for (Map<String, Object> map : list) {
			convertList.add(convertToValueObject(map, type));
		}
		return convertList;
	}
	
}
