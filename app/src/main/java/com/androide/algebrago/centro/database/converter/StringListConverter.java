package com.androide.algebrago.centro.database.converter;

import androidx.room.TypeConverter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Convierte List<String> ↔ String JSON para que Room pueda persistirlo
 * en una sola columna TEXT de SQLite.
 *
 * Room no soporta tipos de colección nativamente; este TypeConverter
 * actúa como el puente entre el modelo de dominio y la capa de persistencia.
 *
 * Se registra en AppDatabase mediante @TypeConverters(StringListConverter.class).
 */
public class StringListConverter {

    /**
     * Convierte una lista de Strings a su representación JSON.
     * Ejemplo: ["x=1,y=2", "x=3"] → "[\"x=1,y=2\",\"x=3\"]"
     */
    @TypeConverter
    public static String fromList(List<String> list) {
        if (list == null || list.isEmpty()) return "[]";
        JSONArray array = new JSONArray();
        for (String item : list) array.put(item);
        return array.toString();
    }

    /**
     * Convierte un String JSON a una lista de Strings.
     * Retorna lista vacía si el JSON es nulo, vacío o malformado.
     */
    @TypeConverter
    public static List<String> toList(String json) {
        List<String> result = new ArrayList<>();
        if (json == null || json.isEmpty()) return result;
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                result.add(array.getString(i));
            }
        } catch (JSONException e) {
            // JSON malformado: retornamos lista vacía para no crashear
        }
        return result;
    }
}
