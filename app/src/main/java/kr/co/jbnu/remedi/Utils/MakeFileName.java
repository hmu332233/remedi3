package kr.co.jbnu.remedi.Utils;

import java.util.Date;

/**
 * Created by seokhyeon on 2016-08-24.
 */
public class MakeFileName {
    private static MakeFileName makeFileName;

    public String makefilename(String image_name){
        String result ="";
        Date date = new Date();
        String year = String.valueOf(date.getYear());
        String month = String.valueOf(date.getDay());
        String day = String.valueOf(date.getDay());
        String hour = String.valueOf(date.getHours());
        String min = String.valueOf(date.getMinutes());

        String random = randomRange(100000000,1000000000);

        result+=year+month+day+hour+min+random+image_name;

        return result;
    }

    private String randomRange(int n1, int n2) {
        int value = (int) (Math.random() * (n2 - n1 + 1)) + n1;
        return String.valueOf(value);
    }


    public static synchronized MakeFileName getInstance () {
        if (makeFileName==null){
            makeFileName = new MakeFileName();
        }
        return makeFileName;
    }
}
