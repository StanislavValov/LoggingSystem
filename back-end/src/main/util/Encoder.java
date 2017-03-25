package main.util;

import java.util.Arrays;
import java.util.Base64;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Created by Stan on 7.1.2017 г..
 */
public class Encoder {

    public String convertToString(byte[] bytes){
        if (bytes.length != 0){
            return Arrays.toString(Base64.getUrlEncoder().encode(bytes));
        }

        return "";
    }

    public byte [] convertToArray(String value){
        if (!isNullOrEmpty(value)){
            return Base64.getUrlDecoder().decode(value);
        }

        return new byte[]{};
    }
}
