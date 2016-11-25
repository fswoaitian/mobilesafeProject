package com.itheima.mobilesafe.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/11/22 0022.
 */
public class StreamUtil {
    public static String streamToString(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while((len = is.read(buffer)) != -1) {
            baos.write(buffer, 0, len);
        }
        String string = baos.toString();
        baos.close();
        is.close();
        return string;
    }
}
