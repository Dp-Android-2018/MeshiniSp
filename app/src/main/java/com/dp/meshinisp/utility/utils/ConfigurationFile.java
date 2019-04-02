package com.dp.meshinisp.utility.utils;

import android.text.format.DateFormat;

import java.util.Date;

public class ConfigurationFile {

    public static class UrlConstants {

        public static final String BASE_URL = "http://151.106.52.109:2018";
    }

    public static class Constants {

        public static final String API_KEY = "27180383-4918-4b94-9e24-27e37ec19c94";
        public static final String CONTENT_TYPE = "application/json";
        public static final String ACCEPT = "application/json";
        public static final String BEARER_STRING = "Bearer ";
        public static String ACCEPT_LANGUAGE = "en";
        public static String AUTHORIZATION = "token";


        public static String REGISTER1DATA = "REGISTER1Data";
        public static int DEFAULT_INTEGER_VALUE = 0;
        public static int WAIT_VALUE = 2000;

        public static final String ACCEPT_LANGUAGE_ARABIC = "ar";
        public static final String ACCEPT_LANGUAGE_ENGLISH = "en";
        public static final String DEFAULT_LANGUAGE_STRING = "0";
        public static final String ENGLISH_LANGUAGE_STRING = "English";

        public static final String DATE_FORMAT = "yyyy-MM-dd";
        public static final String MALE_TYPE = "male";
        public static final String MALE_ARABIC_TYPE = "ذكر";
        public static final String FEMALE_TYPE = "female";
        public static final String COMPANY_LINK = "https://bdsmes.com/";


        public static final String MARKET_URL = "market://details?id=";
        public static final String PLAYSTORE_URL = "http://play.google.com/store/apps/details?id=";

        static final String name = DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString();
        public static final String SERVICEPROVIDER_DIRECTORY_NAME = name;

        public static final String TYPE = "TYPE";
        public static final String DATA = "DATA";
        public static final String COUNTRY_ID = "country id";
        public static final String DATE_FROM = "from";
        public static final String DATE_TO = "DATA";
        public static final String REQUEST_ID = "request id";
        public static final String REQUEST_Type = "request type";
        public static final String FROM_REQUESTS_TYPE = "request";
        public static final String TRIPS_TYPE_PAST = "past";
        public static final String TRIPS_TYPE_UPCOMING = "upcoming";
        public static final String OFFERS_TYPE_ACTIVITY = "offer activity";
        public static final String OFFER_PRICE = "offer price";
        public static final String PREREQUESTS_TYPE = "order type";
        public static final String ORDER_TYPE = "order type";
        public static final String OTHERS_TYPE = "others type";
        public static final String EMPLOYMENT_TYPE = "employment type";
        public static final String HOUSING_TYPE = "housing type";
        public static final String EDUCATION_TYPE = "education type";
        public static final String AFFAIRS_TYPE = "affairs type";
        public static final String HEALTH_TYPE = "health type";
        public static final String VIDEO_DETAILS = "VIDEO DETAILS";
        public static final String MEMBER_KEY = "MEMBER";
        public static final String PARLIAMENT_Key_VALUE = "3";
        public static final String DEPARTMENT_Key_VALUE = "5";
        public static final String ALDAIRA_Key_VALUE = "2";
        public static final String ARTICLES_Key_VALUE = "1";
        public static final String DEBUTY_COUNCIL_Key_VALUE = "4";
        public static final String USER_TYPE_VALUE = "USER";
        public static final String MOBILE_TYPE_VALUE = "MOBILE";

        public static final String PARLIAMENT_SUGGESTIONS_Key_VALUE = "6";
        public static final String PARLIAMENT_QUESTIONS_Key_VALUE = "7";
        public static final String DEPARTMENT_EVENTS_Key_VALUE = "1";
        public static final String PARLIAMENT_EVENTS_Key_VALUE = "2";
        public static final String YOUTUBE_API_KEY = "AIzaSyCF1b68xnxxOEUZ1chUSS868brvayX6dB0";
        public static final String MIME_TYPE = "text/html; charset=utf-8";
        public static final String ENCODING_TYPE = "UTF-8";

        //requests History
        public static final String REQUEST_HISTORY_TYPE = "REQUEST";
        public static final String REQUEST_HISTORY_ID = "id";
        public static final String EMPLOYMENT_HISTORY_TYPE = "EMPLOYMENT";
        public static final String HOUSING_HISTORY_TYPE = "HOUSING";
        public static final String EDUCATION_HISTORY_TYPE = "EDUCATION";
        public static final String AFFAIRS_HISTORY_TYPE = "AFFAIRS";
        public static final String HEALTH_HISTORY_TYPE = "HEALTH";
        public static final String OTHER_HISTORY_TYPE = "OTHER";

        //intents types
        public static final String ACTIVATION_TYPE = "Activation Type";
        public static final String LANGUAGE_TYPE = "language Type";
        public static final String FACEBOOK_NAME = "Facebook";
        public static final String TWITTER_NAME = "Twitter";
        public static final String INSTAGRAM_NAME = "Instagram";
        public static final String MAIL_NAME = "Mail";
        public static final String TOKN_VALUE = "Token";
        public static final String BROWSER_NAME = "Browser";
        public static final String FACEBOOK_PACKAGE_NAME = "com.facebook.katana";
        public static final String FACEBOOK_PACKAGE_NAME_URL = "fb://facewebmodal/f?href=";
        public static final String TWITTER_PACKAGE_NAME = "com.twitter.android";
        public static final String INSTAGRAM_PACKAGE_NAME = "com.instagram.android";
        public static final String GOOGLE_MAPS_PACKAGE_NAME = "com.google.android.apps.maps";
        public static final String GOOGLE_MAPS_URI_DATA = "geo:0,0?q=";
        public static final String INTENT_MAIL_TYPE = "message/rfc822";
        public static final String INTENT_TEXT_TYPE = "text/plain";

        /////////////////////////////////////////
        public static final int WAYS_ID = 1;
        public static final int PUBLIC_PLACES_ID = 2;
        public static final int SANITATION_ID = 3;
        public static final int ENVIRONMENT_ID = 4;
        public static final int LIVESTOCK_ID = 5;


        public static final int PAGE_ID = 0;
        public static final int SUCCESS_CODE_FROM = 200;
        public static final int SUCCESS_CODE_TO = 300;
        public static final int SUCCESS_CODE = 200;
        public static final int SUCCESS_CODE_SECOND = 201;
        public static final int SUCCESS_CODE_Third = 202;
        public static final int NOT_ACTIVATED_CODE = 417 ;
        public static final int LOGGED_IN_BEFORE_CODE = 401 ;
        public static final int WAIT_CODE = 429;
        public static final int INVALED_DATA_CODE = 422;
        public static final String DEFAULT_STRING_VALUE = "";


    }

    public static class SharedPrefConstants {
        public static final String SHARED_PREF_NAME = "TADAWAY_SHARED_PREF";
        public static final String NewDataToSave = "NewDataToSave";
    }

}
