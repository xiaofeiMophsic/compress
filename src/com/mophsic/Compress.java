package com.mophsic;

import com.tinify.Result;
import com.tinify.Source;
import com.tinify.Tinify;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author xiaofei
 * @date 2016/12/15
 */
public class Compress {

    private static final String API_KEY = "your apikey";
    private static final long THRESHOLD = 100 * 1024; // 100k

    public static void main(String[] args) throws IOException {

        if (args == null || args.length < 2) {
            System.out.println("请输入有效的图片路径！");
            return ;
        }

        String path = args[0];      //源路径
        String distDir = args[1];   // 目标路径

        System.out.println("原图片路径->" + path);
        System.out.println("压缩后图片路径->" + distDir);

        try {
            Tinify.setKey(API_KEY);
            /*
            if (!Tinify.validate()){
                System.out.println("apikey 验证失败！");
            }*/
        } catch(java.lang.Exception e) {
            e.printStackTrace();
        }

        if (path != null && !path.isEmpty()) {
            File file = new File(path);
            if (file.isDirectory()) {
                File[] images = file.listFiles(pathname -> {
                    return isPicture(pathname) && pathname.length() > THRESHOLD;   //图片大于100K才进行压缩
                });

                if (images == null || images.length <= 0) {
                    System.out.println("此路径下没有满足条件的图片！");
                    return;
                }

                for (File f : images) {
                    compress(f, distDir);
                }
            } else {
                if (isPicture(file) && file.length() > THRESHOLD) {
                    compress(file, distDir);
                }
            }
        } else {
            System.out.println("请输入有效的路径！");
        }

    }

    private static void compress(File src, String dist) throws IOException {
        String imageName = src.getName();

        if (!dist.endsWith("\\") || !dist.endsWith("/")) {
            dist += File.separator;
        }

        String srcString = src.getAbsolutePath();
        String distString = dist + imageName;
        System.out.println("压缩" + src.getName() + "中。。");
        Source source = Tinify.fromFile(srcString);
        Result result = source.result();
        System.out.println("文件压缩后大小->" + result.size());
        byte[] bytes = result.toBuffer();
        //source.toFile(distString);
        File distFile = new File(dist, imageName);
        FileOutputStream fileOutputStream = new FileOutputStream(distFile);
        fileOutputStream.write(bytes);
        fileOutputStream.close();
    }

    private static boolean isPicture(File pathname) {
        return pathname.getName().endsWith(".jpg") ||
                pathname.getName().endsWith(".jpeg") ||
                pathname.getName().endsWith(".png");
    }
}
