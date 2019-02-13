package com.gradproj.medassistant.medicalassistant.AlcoholLevelReading;

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
public class AlcoholLevelReading {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<AlcoholLevelReadingItem> ITEMS = new ArrayList<AlcoholLevelReadingItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, AlcoholLevelReadingItem> ITEM_MAP = new HashMap<String, AlcoholLevelReadingItem>();

    private static DBAdapter mDBAdapter;

    private static final Cursor result;

    static {

        try {
            mDBAdapter = new DBAdapter(getApplicationUsingReflection().getApplicationContext());
            mDBAdapter.open();
        } catch (Exception e) {
            e.printStackTrace();
        }

        result = mDBAdapter.fetchAllSensorReadings(SensorType.ALCOHOL);


        if (result.moveToFirst()) {
            do {
                String id = result.getString(result.getColumnIndex(DBAdapter.KEY_ROW_ID));
                String reading = result.getString(result.getColumnIndex(DBAdapter.KEY_READING_VALUE));
                String date = result.getString(result.getColumnIndex(DBAdapter.KEY_READING_DATE));
                addItem(new AlcoholLevelReadingItem(id,reading,date));
                // do what ever you want here
            } while (result.moveToNext());
        }

    }

    private static void addItem(AlcoholLevelReadingItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }




    /**
     * A dummy item representing a piece of content.
     */
    public static class AlcoholLevelReadingItem {
        public final String id;
        public final String content;
        public final String details;

        public AlcoholLevelReadingItem(String id, String content, String details) {
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
