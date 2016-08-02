package com.example.sara.uppgift4;

/**
 * Created by ludvig on 2016-08-02.
 */
public class LinkInfo {

        public String linkType;
        public String url;
        public String title;

        public static  final String YOUTUBE_LINK = "youtube_video";
        public static  final String WEB_PAGE_LINK = "web_page";


        public LinkInfo (String linkType_input,String tu,String i){
            url = tu;
            title = i;
            linkType = linkType_input;
        }

}
