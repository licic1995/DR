package com.example.drpet.DataProcessPackage;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


/**
 * Created by lichengchen on 16/5/30.
 */
public class FileHelper {
    private String filename;

    /***
     * 用文件名来实例化
     * @param FileName 用户名
     */
    public FileHelper(String FileName){
        filename = FileName;
    }

    /***
     * 写入数据
     * @param context 实例化上下文
     * @param content 写入内容
     */
    public void f_write(Context context, String content){
        FileOutputStream fileOutputStream = null;
        BufferedWriter bufferedWriter = null;
        try {
            fileOutputStream = context.openFileOutput(filename, Context.MODE_APPEND);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            bufferedWriter.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(bufferedWriter != null){
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /***
     * 读取内容
     * @param context 上下文
     * @return 返回字符串格式的数据内容
     */
    public String f_read(Context context){
        FileInputStream fileInputStream = null;
        BufferedReader bufferedReader = null;
        StringBuilder content = new StringBuilder();
        try {
            fileInputStream = context.openFileInput(filename);
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            String line="";
            while((line = bufferedReader.readLine()) != null){
                content.append(line);
            }
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }
}

