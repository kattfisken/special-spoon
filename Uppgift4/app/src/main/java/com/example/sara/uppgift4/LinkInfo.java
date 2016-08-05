package com.example.sara.uppgift4;

import java.io.Serializable;

public class LinkInfo implements Serializable {

        public String linkType;
        public String data;
        public String title;

        public static  final String YOUTUBE_LINK = "youtube_video";
        public static  final String WEB_PAGE_LINK = "web_page";


        public LinkInfo (String linkType_input,String tu,String i){
            data = tu;
            title = i;
            linkType = linkType_input;
        }


    public String getUrl() {
        String s;
        //todo förklara skillnad på == och equals
        if (linkType.equals(YOUTUBE_LINK)) {
            s = "https://www.youtube.com/watch?v="+data;
        } else  {
            s = data;
        }
        return s;
    }

}
