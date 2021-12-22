 package utils.collector.protocol.jt808;


import lombok.extern.slf4j.Slf4j;

 @Slf4j
 public class ClassUtils
 {
   public static Object getBean(String className)
   {
     Class<?> clazz = null;
     try
     {
       clazz = Class.forName(className);
     }
     catch (Exception ex)
     {
       log.info("getBean异常"+ex.getMessage());
     }
     if (clazz != null)
     {
       try
       {
         return clazz.newInstance();
       }
       catch (Exception ex) {
    	  log.info("getBean异常"+ex.getMessage());
       }
     }
     return null;
   }
 }

