package com.hobbyjoin.ships;

import java.util.Locale;
import java.util.ResourceBundle;

public class Translator {
    private static Translator instance;
    private ResourceBundle translatorHandler;
    Locale defaultLocale = Locale.getDefault();
    private Translator() {
        translatorHandler =  ResourceBundle.getBundle("MessagesBundle", defaultLocale);
    }
    public String translate(String toTranslate){
        return translatorHandler.getString(toTranslate);
    }

    public static Translator getInstance() {
        if (instance == null) {
            instance = new Translator();
        }
        return instance;
    }

    public void setTranslatorHandler(String lang) {
        //TODO validation - 2 chars, and exists this lang
        Locale currentLocale = new Locale(lang);
        this.translatorHandler = ResourceBundle.getBundle("MessagesBundle", currentLocale);
    }
}
