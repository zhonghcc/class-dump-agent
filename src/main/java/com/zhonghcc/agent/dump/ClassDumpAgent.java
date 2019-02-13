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
//        this.add("constants.ServerConstants");
//        this.add("constants.GameConstants");
//        this.add("gui.ZEVMS");
//        this.add("a.本地数据库");
//        this.add("abc.Game");
//        this.add("server.ServerProperties");
//        this.add("handling.login.LoginServer");
//        this.add("handling.login.LoginWorker");
//        this.add("handling.login.handler.CharLoginHandler");
//        this.add("handling.login.Balloon");
//        this.add("a");
//        this.add("abc");
//        this.add("aVer.2");
//        this.add("client");
//
//        this.add("constants");
//        this.add("data");
//        this.add("database");
//        this.add("database1");
//        this.add("download");
//        this.add("folder");
//        this.add("fumo");
//        this.add("gui");
//        this.add("handling");
//        this.add("properties");
//        this.add("provider");
//        this.add("pvp");
//        this.add("quest");
//        this.add("scripting");
//        this.add("server");
//        this.add("tools");
//        this.add("zevms");
        this.add("handling.world.MapleParty");

    }};
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
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startDump(className,classBeingRedefined,classfileBuffer);
            }
        }).start();

        return null;
    }

    public synchronized void startDump(String className,Class<?> classBeingRedefined, byte[] classfileBuffer){

        String filePath = "/tmp/" + className.replace('.', '/') + ".class";
        String classNamePot = className.replace('/','.');
//        System.out.println("dump "+classNamePot + " to "+filePath + " size="+classfileBuffer.length);
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
//            System.out.println("CLASS: "+classNamePot);
//            System.out.println("field-------");
            Object instance = null;
            for(Field field : fields){
//                System.out.println(field.getName() + ":" +field.getType().getName()+ " static:" + Modifier.isStatic(field.getModifiers()));

                if(Modifier.isStatic(field.getModifiers())){
                    field.setAccessible(true);
                    Object f = field.get(Object.class);
//                    if (f.toString().equals("1")) {
                        System.out.println("class="+classNamePot+" field=" + field.getName()+
                                ":" +field.getType().getName()+ " static:" + " value=" + f);
//                        field.set(Object.class,4);
//                    }

                }
                if(field.getName().equals("服务端启动模式")){
                    field.setAccessible(true);

                    field.set(Object.class,1);
                    instance = field.get(Object.class);
                    System.out.println(field.getName() +" value="+instance);
                }
                if(field.getName().equals("容纳人数")){
                    field.setAccessible(true);

                    field.set(Object.class,40);
                    final Field fl = field;
                    instance = field.get(Object.class);
                    System.out.println(field.getName() +" value="+instance);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(10000);
                                Object f = fl.get(Object.class);
                                System.out.println(fl.getName() +" value="+f);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();
                }


//                if(field.getName().equals("instance")){
//                    field.setAccessible(true);
//                    final Object loginInstance = field.get(Object.class);
//                    System.out.println(loginInstance);
//                    final Field userLimitField = cc.getDeclaredField("userLimit");
//                    userLimitField.setAccessible(true);
//
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                Thread.sleep(10000);
//
//                                Object f = userLimitField.get(loginInstance);
//                                System.out.println(userLimitField.getName() + " value=" + f);
//
//                                userLimitField.set(loginInstance, 40);
//                                f = userLimitField.get(loginInstance);
//                                System.out.println(userLimitField.getName() + " value=" + f);
//                            }catch (Exception e){
//                                System.out.println(e.getMessage());
//                                e.printStackTrace();
//                            }
//                        }
//                    }).start();



                }

//                if(field.getName().equals("显示人数")||field.getName().equals("显示人数2")){
//                    field.setAccessible(true);
//                    Object f = field.get(instance);
//                    System.out.println(f);
//                }
//                if(field.getName().equals("props")){
//                    field.setAccessible(true);
//                    Object f = field.get(Object.class);
//                    Properties props = (Properties)f;
//                    props.list(System.out);
//                }
//                if(field.getName().equals("单机人数")){
//                    field.setAccessible(true);
//                    Object f = field.get(Object.class);
//                    field.set(Object.class,4);
//                    instance = field.get(Object.class);
//                    System.out.println(field.getName() +" value="+instance);
//                }
//                if(field.getName().equals("调试")||field.getName().equals("调试输出")){
//                    field.setAccessible(true);
//                    Object f = field.get(Object.class);
//                    field.set(Object.class,"开");
//                    instance = field.get(Object.class);
//                    System.out.println(field.getName() +" value="+instance);
//                }
//            }
//            System.out.println("method-------");
//            for(Method method: methods){
//                System.out.println(method.getName());
//                if(method.getName().equals("getInstance")){
////                    Method getInstance = cc.getClass().getMethod("getInstance",null);
////                    Object instance = getInstance.invoke(null);
////                    System.out.println(instance);
//                }
//            }

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
//            System.out.println("dump "+ className + " complete");

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }




}
