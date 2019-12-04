package cyclonefrontend;

import java.util.HashMap;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class CycloneProperties {
    private static final String PROPERTIES_BUNDLE = "cyclonefrontend.clnfe";
    private static final String CPA_FILE_PATH = "CPAFilePath";
    private static final String MMD_OUTPUT_DIR = "MMDOutputDir";
    private static final String MSG_OUTPUT_DIR = "MsgOutputDir";
    private static final String DFT_DATA_DIR = "DftDataDir";

    private static HashMap CLNFeProperties = new HashMap();

    public CycloneProperties() {
    }

    private static ResourceBundle getProperties(Locale locale) {
        ResourceBundle bundle = (ResourceBundle)CLNFeProperties.get(locale);
        if (bundle == null) {
            bundle = ResourceBundle.getBundle(PROPERTIES_BUNDLE, locale);
            CLNFeProperties.put(locale, bundle);
        }
        return bundle;
    }

    public static String getCPAFilePath() {
        try {
            return getProperties(Locale.getDefault()).getString(CPA_FILE_PATH);
        } catch (MissingResourceException ex) {
            // If it is not specified, return the (hardcoded) default
            return null;
        }
    }

    public static String getMMDOutputDir() {
        try {
            return getProperties(Locale.getDefault()).getString(MMD_OUTPUT_DIR);
        } catch (MissingResourceException ex) {
            // If it is not specified, return the (hardcoded) default
            return null;
        }
    }

    public static String getMsgOutputDir() {
        try {
            return getProperties(Locale.getDefault()).getString(MSG_OUTPUT_DIR);
        } catch (MissingResourceException ex) {
            // If it is not specified, return the (hardcoded) default
            return null;
        }
    }

    public static String getDftDataDir() {
        try {
            return getProperties(Locale.getDefault()).getString(DFT_DATA_DIR);
        } catch (MissingResourceException ex) {
            // If it is not specified, return the (hardcoded) default
            return ".";
        }
    }
}
