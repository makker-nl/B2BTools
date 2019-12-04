package b2bfrontend;

import java.util.HashMap;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class B2BProperties {
    public static final String PROPERTIES_BUNDLE = "b2bfrontend.b2bfe";
    public static final String CONNECT_KEY = "connect";
    public static final String CONNECT1_KEY = "connect1";
    public static final String CONNECT2_KEY = "connect2";
    // This hashmap caches ResourceBundles, keyed by locale.
    private static HashMap B2BFeProperties = new HashMap();

    public B2BProperties() {
    }

    private static ResourceBundle getProperties(Locale locale) {
        ResourceBundle bundle = (ResourceBundle)B2BFeProperties.get(locale);
        if (bundle == null) {
            bundle = ResourceBundle.getBundle(PROPERTIES_BUNDLE, locale);
            B2BFeProperties.put(locale, bundle);
        }
        return bundle;
    }

    public static String getConnect1(Locale locale) {
        try {
            return getProperties(locale).getString(CONNECT1_KEY);
        } catch (MissingResourceException ex) {
            // If it is not specified, return the (hardcoded) default
            return null;
        }
    }
    public static String getConnect(Locale locale, int num) {
        try {
            return getProperties(locale).getString(CONNECT_KEY+num);
        } catch (MissingResourceException ex) {
            // If it is not specified, return the (hardcoded) default
            return null;
        }
    }
    public static String getProperty(Locale locale, String name) {
        try {
            return getProperties(locale).getString(name);
        } catch (MissingResourceException ex) {
            // If it is not specified, return the (hardcoded) default
            return null;
        }
    }
    public static String getConnect2(Locale locale) {
        try {
            return getProperties(locale).getString(CONNECT2_KEY);
        } catch (MissingResourceException ex) {
            // If it is not specified, return the (hardcoded) default
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(Locale.ENGLISH);
        System.out.println(Locale.getDefault());
        System.out.println(Locale.GERMAN);
        System.out.println("Connect 1");
        System.out.println(B2BProperties.getConnect1(Locale.ENGLISH));
        System.out.println(B2BProperties.getConnect1(Locale.getDefault()));
        System.out.println(B2BProperties.getConnect1(Locale.GERMAN));
        System.out.println("Connect 2");
        System.out.println(B2BProperties.getConnect2(Locale.ENGLISH));
        System.out.println(B2BProperties.getConnect2(Locale.getDefault()));
        System.out.println(B2BProperties.getConnect2(Locale.GERMAN));
        System.out.println("Connect 1, dynamic");
        System.out.println(B2BProperties.getConnect(Locale.ENGLISH, 1));
        System.out.println(B2BProperties.getConnect(Locale.getDefault(), 1));
        System.out.println(B2BProperties.getConnect(Locale.GERMAN, 1));
        System.out.println("Connect 2, dynamic");
        System.out.println(B2BProperties.getConnect(Locale.ENGLISH, 2));
        System.out.println(B2BProperties.getConnect(Locale.getDefault(), 2));
        System.out.println(B2BProperties.getConnect(Locale.GERMAN, 2));
        System.out.println("Connect 3, dynamic");
        System.out.println(B2BProperties.getConnect(Locale.ENGLISH, 3));
        System.out.println(B2BProperties.getConnect(Locale.getDefault(), 3));
        System.out.println(B2BProperties.getConnect(Locale.GERMAN, 3));
        if (B2BProperties.getConnect(Locale.getDefault(), 3)==null) {
            System.out.println("Null");
        }
        if ("".equals(B2BProperties.getConnect(Locale.getDefault(), 3))) {
            System.out.println("Leeg");
        }
    }
}
