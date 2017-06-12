package com.tachyon5.kstart.ble;

import com.orhanobut.logger.Logger;
import com.tachyon5.kstart.utils.DigitalTrans;
import com.tachyon5.kstart.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Font {
    public static byte[] Bytes_Read_from_HZK16_ByOffset(int offset, byte[] chs) {
        byte[] newChs = new byte[32];
        //根据内码找出汉字在HZK16中的偏移位置
        try {
            FileInputStream fin = new FileInputStream("//data/data//com.magispec.milkdefender//files//HZK16");
            fin.skip(offset);
            fin.read(chs, 0, 32);
            newChs = Utils.hexStringToByte(Utils.bytesToHexString(chs));
            for (int i = 0; i < chs.length; i++) {
                Logger.d(chs[i] + "chs[" + i + "]");
            }
            fin.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return newChs;
    }

    public static byte[] FontCreate(int[] a) {
        byte[] chs = new byte[32];
        int CH1 = a[0];
        int CH2 = a[1];
        int offset = ((CH1 - 0xa1) * 94 + (CH2 - 0xa1)) * 32;
        return Bytes_Read_from_HZK16_ByOffset(offset, chs);
    }

    public static byte[] FontLibraryGeneration(String s) {
        //String []a=s.split(regularExpression)
        ByteArrayOutputStream a = new ByteArrayOutputStream();
        String[] ss = s.split("\\*");
        List list = Arrays.asList(ss);
        Set set = new HashSet(list);
        String[] rid = (String[]) set.toArray(new String[0]);
        System.out.print("------------测试字库点阵输出---------font-------------" + rid.length);
        for (int i = 0; i < rid.length; i++) {
            if (ss[i].length() == 4) {
                int[] nameInt = Utils.twoHexStringTOBytes(ss[i]);
                byte[] m = FontCreate(nameInt);
                byte[] nameByte = new byte[nameInt.length];
                for (int j = 0; j < nameByte.length; j++) {
                    nameByte[j] = new Integer(nameInt[j]).byteValue();
                }
                try {
                    a.write(nameByte);
                    a.write(m);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                Logger.d("------------测试字库点阵输出----------------------");
            }
        }
        try {
            a.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return a.toByteArray();
    }

    public static byte[] FontLibraryGenerationName(String s) {
        //String []a=s.split(regularExpression)
        ByteArrayOutputStream a = new ByteArrayOutputStream();

        String[] ss = s.split("\\*");
        for (int i = 0; i < ss.length; i++) {
            if (ss[i].length() == 4) {
                int[] nameInt = Utils.twoHexStringTOBytes(ss[i]);
                byte[] nameByte = new byte[nameInt.length];
                for (int j = 0; j < nameByte.length; j++) {
                    nameByte[j] = new Integer(nameInt[j]).byteValue();
                }
                try {
                    a.write(nameByte);

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                try {
                    a.write(DigitalTrans.hexStringToString(ss[i], 2).getBytes());
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        try {
            a.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte[] value = a.toByteArray();
        byte[] value1 = new byte[value.length + 1];
        for (int i = 0; i < value.length; i++) {
            value1[i] = value[i];
        }
        value1[value.length] = 0x00;
        return value1;
    }
}



