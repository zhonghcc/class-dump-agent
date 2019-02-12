package com.zhonghcc.agent.dump;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class ClassDumpAgent implements ClassFileTransformer {

    static Set<String> classNames = new HashSet<String>(){{
        this.add("constants.ServerConstants");
        this.add("gui.ZEVMS");
        this.add("server.ServerProperties");
    }};
    public byte[] transform(ClassLoader loader, final String className, final Class<?> classBeingRedefined, ProtectionDomain protectionDomain, final byte[] classfileBuffer) throws IllegalClassFormatException {

        String classNamePot = className.replace('/','.');
        if(!classNames.contains(classNamePot)){
            return null;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                startDump(className,classBeingRedefined,classfileBuffer);
            }
        }).start();

        return null;
    }

    public void startDump(String className,Class<?> classBeingRedefined, byte[] classfileBuffer){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String filePath = "/tmp/" + className.replace('.', '/') + ".class";
        String classNamePot = className.replace('/','.');
        System.out.println("dump "+classNamePot + " to "+filePath + " size="+classfileBuffer.length);
        try {


            File file = new File(filePath);
            File fileParent = file.getParentFile();
            if(!fileParent.exists()){
                fileParent.mkdirs();
            }
//            ClassPool pool = ClassPool.getDefault();
            Class cc = Class.forName(classNamePot);
            Field[] fields = Class.forName(classNamePot).getDeclaredFields();
            Method[] methods = cc.getDeclaredMethods();
            System.out.println("CLASS: "+classNamePot);
            System.out.println("field-------");
            Object instance = null;
            for(Field field : fields){
                System.out.println(field.getName() + ":" +field.getType().getName()+ "static:" + Modifier.isStatic(field.getModifiers()));

                if(field.getName().equals("instance")){
                    field.setAccessible(true);
                    instance = field.get(Object.class);
                    System.out.println(instance);
                }
                if(field.getName().equals("显示人数")){
                    field.setAccessible(true);
                    Object f = field.get(instance);
                    System.out.println(f);
                }
                if(field.getName().equals("props")){
                    field.setAccessible(true);
                    Object f = field.get(Object.class);
                    Properties props = (Properties)f;
                    props.list(System.out);
                }
            }
            System.out.println("method-------");
            for(Method method: methods){
                System.out.println(method.getName());
                if(method.getName().equals("getInstance")){
//                    Method getInstance = cc.getClass().getMethod("getInstance",null);
//                    Object instance = getInstance.invoke(null);
//                    System.out.println(instance);
                }
            }

//            byte[] cryptedByte = cc.toBytecode();
//
//            OutputStream out = new FileOutputStream(filePath);
//            InputStream is = new ByteArrayInputStream(cryptedByte);
//            byte[] buff = new byte[1024];
//            int len = 0;
//            while((len=is.read(buff))!=-1){
//                out.write(buff, 0, len);
//            }
//            is.close();
//            out.close();
            System.out.println("dump "+ className + " complete");

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }




}
