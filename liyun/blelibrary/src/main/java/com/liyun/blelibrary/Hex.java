package com.liyun.blelibrary;

/**
 * @author chen.qinlei
 * @create 2016-04-16 16:04.
 */
public class Hex {

    final static char[] hexTable = "0123456789ABCDEF".toCharArray();

    public static String bytesToHexString(byte[] data)
    {
        char[] hexChars = new char[data.length * 2];
        for (int i = 0; i < data.length; i++)
        {
            int value = data[i]&0xFF;
            hexChars[2*i] = hexTable[value>>>4];
            hexChars[2*i+1] = hexTable[value&0x0F];
        }

        return new String(hexChars);
    }


    public static String bytesToHexString(byte[] data, int offset, int length) {
        byte[] array = new byte[length];
        System.arraycopy(data, offset, array, 0, length);
        return bytesToHexString(array);
    }


    private static byte hexCharToByte(char hex)
    {
        if ( ('0' <= hex) && (hex <= '9') )
        {
            return (byte)(hex - '0');
        }
        else if ( ('A' <= hex) && (hex <= 'F') )
        {
            return (byte)(hex - 'A' + 10);
        }
        else if ( ('a' <= hex) && (hex <= 'f') )
        {
            return (byte)(hex - 'a' + 10);
        }
        else
        {
            return (byte)0xFF;
        }
    }

    public static byte[] hexStringToBytes(String hex)
    {
        if (hex.length() < 1)
        {
            return null;
        }

        char[] hexChars = hex.toUpperCase().toCharArray();
        byte[] byteArray = new byte[hexChars.length/2 + 2];
        int byteCount = 0;

        int i = 0;
        while(i < hexChars.length)
        {
            byte high = hexCharToByte(hexChars[i++]);
            if (high == (byte)0xFF)
            {
                continue;
            }
            if (i == hexChars.length)
            {
                byteArray[byteCount] = high;
                byteCount ++;
                break;
            }

            byte low = hexCharToByte(hexChars[i++]);
            if (low == (byte)0xFF)
            {
                continue;
            }

            byteArray[byteCount] = (byte)( (high<<4) | (low) );
            byteCount ++;

        }


        byte[] bytes = new byte[byteCount];
        for (int j = 0; j < byteCount; j ++)
        {
            bytes[j] = byteArray[j];
        }

        return bytes;
    }
}
