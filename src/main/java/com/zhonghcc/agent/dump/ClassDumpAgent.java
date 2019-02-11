package com.zhonghcc.agent.dump;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class ClassDumpAgent implements ClassFileTransformer {

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        System.out.println("dump "+className);
        try {
            FileUtils.writeByteArrayToFile(new File("/tmp/" + className.replace('.', '/') + ".class"),
                    classfileBuffer);
        } catch (IOException e) {
            // Quite
        }
        return null;
    }


}
