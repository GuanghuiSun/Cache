package com.cacheCore.support.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 读写文件工具类
 *
 * @author sgh
 * @date 2022/10/14 16:00
 */
public class FileUtils {

    /**
     * 读文件
     *
     * @param path 文件路径
     * @return json行
     */
    public static List<String> readAll(String path) {
        List<String> lines = new ArrayList<>();
        if (path == null || path.length() <= 0) return lines;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path)), StandardCharsets.UTF_8.newDecoder()));
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            return lines;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lines;
    }

    /**
     * 追加写文件
     *
     * @param path    文件路径
     * @param content 内容
     */
    public static void writeAll(String path, List<String> content) {
        if (path == null || path.length() <= 0) return;
        BufferedWriter bw = null;

        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(path), true), StandardCharsets.UTF_8.newEncoder()));

            for (String s : content) {
                bw.append(s);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null)
                    bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
