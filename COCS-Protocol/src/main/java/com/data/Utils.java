package com.data;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class Utils {


    public static long getCheckSum(Data data) {
        byte[] bytes = data.getMessage().getBytes(StandardCharsets.UTF_8);
        Checksum checksum = new CRC32();
        checksum.update(bytes, 0, bytes.length);
        return checksum.getValue();
    }

    public static boolean isDataCorrupted(Data data) {
        Long obtainedChecksum = getCheckSum(data);
        if (obtainedChecksum == data.getHeader().getCheckSum()) {
            return false;
        }
        return true;
    }

    public static byte[] dataToByteArray(Object obj) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(2048);
        ObjectOutputStream os = null;
        try {
            os = new ObjectOutputStream(new BufferedOutputStream(byteArrayOutputStream));
            os.flush();
            os.writeObject(obj);
            os.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}