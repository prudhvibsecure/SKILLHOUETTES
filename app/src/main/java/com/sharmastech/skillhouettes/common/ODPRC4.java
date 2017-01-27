package com.sharmastech.skillhouettes.common;

import com.sharmastech.skillhouettes.utils.TraceUtils;

public class ODPRC4 {

    private final int N = 256;

    private int[] sbox;

    private String password;



    public ODPRC4(String passKey)

    {
        this.password = passKey;
    }



    private String EnDeCrypt(String text)

    {

        RC4Initialize();

        int i = 0, j = 0, k = 0;

        StringBuilder cipher = new StringBuilder();

        for (int a = 0; a < text.length(); a++)

        {

            i = (i + 1) % N;

            j = (j + sbox[i]) % N;

            int tempSwap = sbox[i];

            sbox[i] = sbox[j];

            sbox[j] = tempSwap;


            k = sbox[(sbox[i] + sbox[j]) % N];

            int cipherBy = ((int)text.charAt(a)) ^ k;  //xor operation

            cipher.append(Character.toChars(cipherBy));

        }

        return cipher.toString();

    }



    private static String StrToHexStr(String str)

    {
    	
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < str.length(); i++)

        {

            //int v = Character.ToInt32(str[i]);
        	Character c = str.charAt(i);
        	int v = (int)c.charValue();

        	
        	sb.append(String.format("%02x", v));
        	
            //sb.append(String.format("{0:X2}", v));

        }

        return sb.toString();

    }


    private static String HexStrToStr(String hexStr)
    {

        StringBuilder sb = new StringBuilder();

        try {

            for (int i = 0; i < hexStr.length(); i+=2) {
                String str = hexStr.substring(i, i+2);
                sb.append((char) Integer.parseInt(str, 16));
            }

        } catch (Exception e) {
            TraceUtils.logException(e);
        }

        /*for (int i = 0; i < hexStr.length(); i += 2)

        {

            int n = Integer.parseInt(hexStr.charAt(i)+""*//*substring(i)*//*, 16);
            sb.append(Character.toChars(n));
            // int n = Convert.ToInt32(hexStr.substring(i, 2), 16);
            //byte b = Byte.parseByte(hexStr, 16);
            //int n = Integer.parseInt(hexStr.substring(i, 2));//Convert.ToInt32(hexStr.substring(i, 2), 16);
            //String st = new String(b);
            //sb.append(new String(b, "UTF-8"));

            *//*int n = Convert.ToInt32(hexStr.substring(i, 2), 16);
            sb.append(Character.toChars(n));*//*

        }*/

        return sb.toString();

    }



    public String encrypt(String text)

    {

        return StrToHexStr(EnDeCrypt(text));

    }



    public String Decrypt(String text)

    {
        return EnDeCrypt(ODPRC4.HexStrToStr(text));
    }



    private void RC4Initialize()

    {

        sbox = new int[N];

        int[] key = new int[N];

        int n = password.length();

        for (int a = 0; a < N; a++)

        {

            key[a] = (int)password.charAt(a % n);

            sbox[a] = a;

        }



        int b = 0;

        for (int a = 0; a < N; a++)

        {

            b = (b + sbox[a] + key[a]) % N;

            int tempSwap = sbox[a];

            sbox[a] = sbox[b];

            sbox[b] = tempSwap;

        }

    }

}


