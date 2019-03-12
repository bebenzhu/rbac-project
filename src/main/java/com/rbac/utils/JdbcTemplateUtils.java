package com.rbac.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rbac.system.spring.ContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class JdbcTemplateUtils {
    private static NamedParameterJdbcTemplate namedParameterJdbcTemplate = ContextHolder.getBean("namedParameterJdbcTemplate");

    /**
     * 单条数据查询
     * @param sql
     * @param param
     * @return
     */
    public static Map<String,Object> queryForMap(String sql, Map<String,Object> param){
        List<Map<String,Object>> result = namedParameterJdbcTemplate.queryForList(sql,param);
        if(result==null||result.size()!=1){
            return null;
        }else{
            return result.get(0);
        }
    }

    public static List<Map<String,Object>> queryForList(String sql, Map<String,Object> param){
        return namedParameterJdbcTemplate.queryForList(sql,param);
    }


    public static void insertForList(String sql, Map<String,Object>[] paramMaps){
		namedParameterJdbcTemplate.batchUpdate(sql, paramMaps);
	}



	public static String getString(Object obj){
    	return obj==null?"":obj.toString();
    }
    
    public static double getDouble(Object obj){
    	if(obj==null) return 0.00;
    	else if(obj instanceof BigDecimal) return new BigDecimal(obj.toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    	else if(obj instanceof Double) return NumberUtils.round2Decimal(Double.parseDouble(obj.toString()));
    	else if(obj instanceof Float) return NumberUtils.round2Decimal(Double.parseDouble(obj.toString()));
    	else if(obj instanceof String) {
    		try{
    			return NumberUtils.round2Decimal(Double.parseDouble(obj.toString()));
    		}catch(NumberFormatException e){
    			return 0.00;
    		}
    	}
    	else return 0.00;
    }
    
    public static int getInteger(Object obj){
    	if(obj==null) return 0;
    	else if(obj instanceof Integer) return Integer.parseInt(obj.toString());
    	else if(obj instanceof Long) return Integer.parseInt(obj.toString());
    	else if(obj instanceof BigDecimal) return new BigDecimal(obj.toString()).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
    	else if(obj instanceof String) {
    		try{
    			return Integer.parseInt(obj.toString());
    		}catch(NumberFormatException e){
    			return 0;
    		}
    	}
    	else return 0;
    }
    
    /**
     * 获取精度为小数点后4位
     * @param obj
     * @return
     */
    public static double getDouble2(Object obj){
    	if(obj==null) return 0.00;
    	else if(obj instanceof BigDecimal) return new BigDecimal(obj.toString()).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
    	else if(obj instanceof Double) return Double.parseDouble(obj.toString());
    	else if(obj instanceof String) {
    		try{
    			return Double.parseDouble(obj.toString());
    		}catch(NumberFormatException e){
    			return 0.00;
    		}
    	}
    	else return 0.00;
    }
    
    
    /**
     * object对象转map
     * @param obj
     * @return
     */
    public static Map<String,Object> objToMap(Object obj){
    	Map<String,Object> result = new HashMap<String,Object>();
    	if(obj==null)return result;
    	try {
	        Field[] fields = obj.getClass().getDeclaredFields();  
	        for (int i = 0; i < fields.length; i++) {
	        	Field field = fields[i];
	        	field.setAccessible(true);
	            String name = field.getName();
	            Object value = field.get(obj);
	            result.put(name, value);
	        }  
    	} catch (IllegalArgumentException e) {
    		e.printStackTrace();
    	} catch (IllegalAccessException e) {
    		e.printStackTrace();
    	}
		return result;
    }
    
    /**
     * 创建Excel
     * @param data
     * @param outFilePath
     */
	public static void createExcel(List<Map<String,Object>> data,String outFilePath,List<String> headList){
  		try { 	
  			if(data==null)return;
  			if(StringUtils.isBlank(outFilePath))return;
  			if(headList==null)return;
  			//创建Excel文件
  			OutputStream out = new FileOutputStream(new File(outFilePath));
  			// 声明一个工作薄
  			XSSFWorkbook workbook = new XSSFWorkbook();
  			// 生成一个表格
  			XSSFSheet sheet = workbook.createSheet();
  			
  			// 产生表格标题行
  			XSSFRow headRow = sheet.createRow(0);
  			int headLength=0;
  			for(String header:headList){
  				XSSFCell cell = headRow.createCell(headLength);
  				XSSFRichTextString text = new XSSFRichTextString(header);
  				cell.setCellValue(text);
  				headLength++;
  			}
  			//进行值填充
  			int rowCount = 1;
  			for (int i = 0; i < data.size(); i++, rowCount++) {
  				XSSFRow dataRow = sheet.createRow(rowCount);
  				Map<String,Object> rowData = data.get(i);
  				for(int j=0;j<=headLength;j++){
  					if(headRow.getCell(j)==null)continue;
  					String key = headRow.getCell(j).getStringCellValue();
  					Object value = rowData.get(key);
  					XSSFCell cell = dataRow.createCell(j);
  					if (value instanceof BigDecimal) {
  						cell.setCellValue(getDouble(value));
  					} else if (value instanceof Integer){
  						cell.setCellValue(getInteger(value));
  					} else {
  						cell.setCellValue(getString(value));
  					}
  				}
  			}
  			workbook.write(out);
  			workbook.close();
  			out.close();
  		} catch (FileNotFoundException e) {
  			e.printStackTrace();
  		} catch (IOException io){
  			io.printStackTrace();
  		}
  	}
  	
    
}
