package com.androide.algebrago.centro.patterns;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import java.util.Locale;

public class configManager {
    private static final String PREFS_NAME = "AlgebraGoPrefs";
    private static final String KEY_LANG = "idioma";
    private static final String KEY_THEME = "tema_oscuro";

    private SharedPreferences prefs;

    public configManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    // --- LÓGICA DE IDIOMA ---
    public void setLocale(Context context, String lang) {
        prefs.edit().putString(KEY_LANG, lang).apply();
        updateResource(context, lang);
    }

    public void updateResource(Context context, String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    public String getLang() { return prefs.getString(KEY_LANG, "es"); }

    // --- LÓGICA DE TEMA ---
    public void setDarkMode(boolean isDark) {
        prefs.edit().putBoolean(KEY_THEME, isDark).apply();
    }

    public boolean isDarkMode() { return prefs.getBoolean(KEY_THEME, false); }
}