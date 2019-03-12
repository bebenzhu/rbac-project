package com.rbac.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.util.UUID;

/**
 * 数字处理工具
 */
public class NumberUtils {

   
	/**
     * 小数保留2位精度
     * @param value 小数值
     * @return
     */
    public static double round2Decimal(double value){
        return round(value,2);
    }
	
    /**
     * 小数保留2位精度-非科学计数法输出
     * @param value
     * @return
     */
    public static String round2Decimal2(double value){
    	if(value != 0.00){
            java.text.DecimalFormat df = new java.text.DecimalFormat("#######0.00");
            return df.format(value);
        }else{
            return "0.00";
        }
    }
    
	/**
     * 小数保留精度(四舍五入)
     * @param value 小数值
     * @param scale 小数精度
     * @return
     */
    public static double round(double value,int scale){
        return BigDecimal.valueOf(value).setScale(scale, RoundingMode.HALF_UP).doubleValue();
    }
    
    public static String uuid(){
        UUID uuid = UUID.randomUUID();
        String uuidValue = uuid.toString();
        uuidValue = uuidValue.toUpperCase();
        return uuidValue;
    }
    
    /**
     * 任意整数转为36进制的字符串
     * @param i
     * @return
     */
    public static String convert36Radix(int i){
        return Integer.toString(i,36).toUpperCase();
    }
    /**
     * 任意整数转为36进制的字符串
     * @param i
     * @return
     */
    public static String convert36Radix(long i){
        return Long.toString(i,36).toUpperCase();
    }
    
    /**
     * 当前时间的纳秒值+4位随机数，以36进制表示
     * @return
     */
    public static String nanoTime36(){
    	return nanoTimeRandom36(4);
    }
    
    /**
     * 时间新纳秒值+随机数
     * @param randomCount 随机数个数
     * @return
     */
    public static String nanoTimeRandom36(int randomCount){
        StringBuffer sv = new StringBuffer(convert36Radix(System.nanoTime()));
        Random random = new Random();
        String rv = convert36Radix(random.nextInt(randomCount*36));
        return sv.append(rv).toString();
    }
    
    public static String byteToHexStr(byte b){
        return Integer.toHexString(b & 0xFF);
    }
    
    /**
     * 两整数相除向上取整
     * @param value1 被除数
     * @param value2 除数
     * @return
     */
    public static int round2Int4Div(int value1,int value2){
    	BigDecimal value3 = new BigDecimal(value1);
    	BigDecimal value4 = new BigDecimal(value2);
    	BigDecimal value = value3.divide(value4,2,BigDecimal.ROUND_DOWN);
    	return (int) Math.ceil(value.doubleValue());
    }
    
    /**
     * 加
     * @param num1
     * @param num2
     * @return
     */
    public static double add(double num1,double num2){
		BigDecimal d1 = new BigDecimal(num1);
		BigDecimal d2 = new BigDecimal(num2);
		return d1.add(d2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
    /**
     * 减
     * @param num1
     * @param num2
     * @return
     */
	public static double subtract(double num1,double num2){
		BigDecimal d1 = new BigDecimal(num1);
		BigDecimal d2 = new BigDecimal(num2);
		return d1.subtract(d2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	/**
	 * 乘
	 * @param num1
	 * @param num2
	 * @return
	 */
	public static double multiply(double num1,double num2){
		BigDecimal d1 = new BigDecimal(num1);
		BigDecimal d2 = new BigDecimal(num2);
		return d1.multiply(d2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	/**
	 * 除
	 * @param num1
	 * @param num2
	 * @return
	 */
	public static double divide(double num1,double num2){
		BigDecimal d1 = new BigDecimal(num1);
		BigDecimal d2 = new BigDecimal(num2);
		return d1.divide(d2, 2,BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	/**
	 * 两小数比较
	 * @param num1
	 * @param num2
	 * @return 
	 * num1>num2返回1
	 * num1<num2返回-1
	 * num1=num2返回0
	 */
	public static int compareTo(double num1,double num2){
		BigDecimal d1 = new BigDecimal(num1);
		BigDecimal d2 = new BigDecimal(num2);
		return d1.compareTo(d2);
	}
	
	/**
	 * 两整数比较
	 * @param num1
	 * @param num2
	 * @return num1>num2返回1
	 * @return num1<num2返回-1
	 * @return num1=num2返回0
	 */
	public static int compareTo(int num1,int num2){
		BigDecimal d1 = new BigDecimal(num1);
		BigDecimal d2 = new BigDecimal(num2);
		return d1.compareTo(d2);
	}
}
