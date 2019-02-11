package com.zhonghcc.agent.dump;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class ClassDumpAgent implements ClassFileTransformer {

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {


//        String userHome = System.getProperties().getProperty("user.home");
        String separator = System.getProperty("file.separator");
        String filePath = "/tmp/" + className.replace('.', '/') + ".class";
        System.out.println("dump "+className + " to "+filePath + " size="+classfileBuffer.length);
        try {




//            FileUtils.writeByteArrayToFile(new File(filePath),classfileBuffer);
            File file = new File(filePath);
            File fileParent = file.getParentFile();
            if(!fileParent.exists()){
                fileParent.mkdirs();
            }
            OutputStream out = new FileOutputStream(filePath);
            InputStream is = new ByteArrayInputStream(classfileBuffer);
            byte[] buff = new byte[1024];
            int len = 0;
            while((len=is.read(buff))!=-1){
                out.write(buff, 0, len);
            }
            is.close();
            out.close();
            System.out.println("dump "+ className + " complete");
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


}
