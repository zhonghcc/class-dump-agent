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
        this.add("handling.world.MapleParty");

    }};
    static final int MAX_USERS=40;
    public byte[] transform(ClassLoader loader, final String className, final Class<?> classBeingRedefined, ProtectionDomain protectionDomain, final byte[] classfileBuffer) throws IllegalClassFormatException {

        String classNamePot = className.replace('/','.');

        if(!classNames.contains(className.split("/")[0])&&
                !classNames.contains(classNamePot)){
            return null;
        }

//        startDump(className,classBeingRedefined,classfileBuffer);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startDump(className,classBeingRedefined,classfileBuffer);
            }
        }).start();

        return null;
    }

    public void startDump(String className,Class<?> classBeingRedefined, byte[] classfileBuffer){

        String filePath = "/tmp/" + className.replace('.', '/') + ".class";
        String classNamePot = className.replace('/','.');

        try {


            File file = new File(filePath);
            File fileParent = file.getParentFile();
            if(!fileParent.exists()){
                fileParent.mkdirs();
            }

            Class cc = Class.forName(classNamePot);
            Field[] fields = Class.forName(classNamePot).getDeclaredFields();
            Method[] methods = cc.getDeclaredMethods();
            Object instance = null;
            for(Field field : fields){
                if(Modifier.isStatic(field.getModifiers())){
                    field.setAccessible(true);
                    Object f = field.get(Object.class);
                    System.out.println("class="+classNamePot+" field=" + field.getName()+
                                ":" +field.getType().getName()+ " static:" + " value=" + f);

                }
                if(field.getName().equals("服务端启动模式")){
                    field.setAccessible(true);

                    field.set(Object.class,2);
                    instance = field.get(Object.class);
                    System.out.println(field.getName() +" value="+instance);
                }
                if(field.getName().equals("容纳人数")){
                    field.setAccessible(true);

                    field.set(Object.class,MAX_USERS);
                    final Field fl = field;
                    instance = field.get(Object.class);
                    System.out.println(field.getName() +" value="+instance);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                int trys=6;
                                while (trys>0) {
                                    Thread.sleep(10000);
                                    Object f = fl.get(Object.class);
                                    System.out.println(fl.getName() + " value=" + f);
                                    fl.set(Object.class,MAX_USERS);
                                    System.out.println(fl.getName() + " value=" + MAX_USERS);
                                    trys--;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();
                }

            }


        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }




}
