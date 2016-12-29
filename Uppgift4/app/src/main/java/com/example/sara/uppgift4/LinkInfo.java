package com.example.sara.uppgift4;

import java.io.Serializable;

/**
 * A container class for holding the information about cat links. Nothing in the implementation is
 * specific about cats though...
 */
class LinkInfo implements Serializable{

    String linkType;
    String data;
    String title;

    static final String YOUTUBE_LINK = "youtube_video";
    static final String WEB_PAGE_LINK = "web_page";


    /**
     * Default constructor. Simply stores all the data in a container object.
     *
     * @param linkTypeInput Must be either the constant LinkInfo.YOUTUBE_LINK or
     *                      LinkInfo.WEB_PAGE_LINK
     * @param dataInput     A Youtube video ID or a url to a web page.
     * @param titleInput    The title of the link.
     */
    LinkInfo(String linkTypeInput, String dataInput, String titleInput) {
        data = dataInput;
        title = titleInput;
        linkType = linkTypeInput;
    }


    /**
     * Formatting funtion for getting an URL to a resource.
     *
     * @return an URL, starting with http(s)://
     */
    String getUrl() {

        if (linkType.equals(YOUTUBE_LINK)) {
            return "https://www.youtube.com/watch?v=" + data;
        } else {
            return data;
        }
    }

}
