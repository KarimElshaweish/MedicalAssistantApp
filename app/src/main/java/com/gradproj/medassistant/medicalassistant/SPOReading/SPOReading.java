package com.gradproj.medassistant.medicalassistant.SPOReading;

import android.app.Application;
import android.database.Cursor;

import com.gradproj.medassistant.medicalassistant.Enums.SensorType;
import com.gradproj.medassistant.medicalassistant.database.DBAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class SPOReading {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<SPOReadingItem> ITEMS = new ArrayList<SPOReadingItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, SPOReadingItem> ITEM_MAP = new HashMap<String, SPOReadingItem>();

    private static DBAdapter mDBAdapter;

    private static final Cursor result;

    static {

        try {
            mDBAdapter = new DBAdapter(getApplicationUsingReflection().getApplicationContext());
            mDBAdapter.open();
        } catch (Exception e) {
            e.printStackTrace();
        }

        result = mDBAdapter.fetchAllSensorReadings(SensorType.SPO2_SENSOR);


        if (result.moveToFirst()) {
            do {
                String id = result.getString(result.getColumnIndex(DBAdapter.KEY_ROW_ID));
                String reading = result.getString(result.getColumnIndex(DBAdapter.KEY_READING_VALUE));
                String date = result.getString(result.getColumnIndex(DBAdapter.KEY_READING_DATE));
                addItem(new SPOReadingItem(id,reading,date));
                // do what ever you want here
            } while (result.moveToNext());
        }

    }

    private static void addItem(SPOReadingItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }



    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class SPOReadingItem {
        public final String id;
        public final String content;
        public final String details;

        public SPOReadingItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }

    public static Application getApplicationUsingReflection() throws Exception {
        return (Application) Class.forName("android.app.ActivityThread")
                .getMethod("currentApplication").invoke(null, (Object[]) null);
    }
}
