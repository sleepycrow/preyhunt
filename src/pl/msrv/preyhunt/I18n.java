package pl.msrv.preyhunt;

import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Level;

public class I18n {

    private static final String MESSAGES = "messages";
    private static Locale locale;
    private static ResourceBundle bundle;
    private static Map<String, MessageFormat> messageFormatCache;

    public I18n(final String lang){
        messageFormatCache = new HashMap<>();
        locale = new Locale(lang);
        bundle = ResourceBundle.getBundle(MESSAGES, locale);
    }

    public static String tl(final String key, final Object... objects){
        String string;
        try {
            string = bundle.getString(key);
        }catch (MissingResourceException e){
            Preyhunt.log.log(Level.SEVERE, "No translation found for key " + key);
            return "ERROR: No message could be found for key " + key;
        }catch (NullPointerException e){
            Preyhunt.log.log(Level.SEVERE, "I18n not initialized.");
            return "ERROR: I18n not initalized. Key: " + key;
        }

        return String.format(string, objects);
    }

}
