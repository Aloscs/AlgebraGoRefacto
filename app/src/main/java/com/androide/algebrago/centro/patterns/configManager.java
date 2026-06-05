package com.androide.algebrago.centro.patterns;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import java.util.Locale;

/**
 * Gestor de configuración de la aplicación.
 *
 * Almacena y recupera las preferencias del usuario (idioma y tema) usando
 * {@link SharedPreferences}. Aplica el idioma activo al contexto de la
 * aplicación en tiempo de ejecución.
 *
 * Patrón aplicado: Manager de configuración centralizado, accedido desde
 * {@link com.androide.algebrago.feature.home.MainActivity}.
 */
public class configManager {
    private static final String PREFS_NAME = "AlgebraGoPrefs";
    private static final String KEY_LANG = "idioma";
    private static final String KEY_THEME = "tema_oscuro";

    private SharedPreferences prefs;

    /**
     * Crea una nueva instancia del gestor de configuración.
     *
     * @param context contexto de la aplicación o actividad.
     */
    public configManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    // --- LÓGICA DE IDIOMA ---

    /**
     * Persiste el código de idioma y aplica el nuevo locale al contexto.
     *
     * @param context contexto de la actividad que invoca el cambio.
     * @param lang    código ISO 639-1 del idioma (ej. "es", "en").
     */
    public void setLocale(Context context, String lang) {
        prefs.edit().putString(KEY_LANG, lang).apply();
        updateResource(context, lang);
    }

    /**
     * Aplica el locale indicado a los recursos del contexto.
     * Debe llamarse al iniciar la aplicación para respetar la preferencia guardada.
     *
     * @param context contexto sobre el que se actualiza la configuración.
     * @param lang    código ISO 639-1 del idioma (ej. "es", "en").
     */
    public void updateResource(Context context, String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    /**
     * Retorna el código de idioma guardado en preferencias.
     * Si no se ha guardado ninguno, devuelve "es" (español) por defecto.
     *
     * @return código ISO 639-1 del idioma activo.
     */
    public String getLang() { return prefs.getString(KEY_LANG, "es"); }

    // --- LÓGICA DE TEMA ---

    /**
     * Persiste la preferencia de tema oscuro.
     *
     * @param isDark {@code true} para activar el tema oscuro; {@code false} para el claro.
     */
    public void setDarkMode(boolean isDark) {
        prefs.edit().putBoolean(KEY_THEME, isDark).apply();
    }

    /**
     * Indica si el tema oscuro está activo.
     *
     * @return {@code true} si el tema oscuro está activado; {@code false} en caso contrario.
     */
    public boolean isDarkMode() { return prefs.getBoolean(KEY_THEME, false); }
}