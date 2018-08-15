package com.jacobarau.mincast.subscription

import android.arch.persistence.room.TypeConverter
import org.threeten.bp.Instant

class Converters {
    companion object {
        // Used via Reflection by Room
        @Suppress("unused")
        @JvmStatic
        @TypeConverter
        fun fromTimestamp(value: Long?): Instant? {
            if (value == null) return null
            return Instant.ofEpochMilli(value)
        }

        // Used via Reflection by Room
        @Suppress("unused")
        @JvmStatic
        @TypeConverter
        fun instantToTimestamp(instant: Instant?): Long? {
            if (instant == null) return null
            return instant.toEpochMilli()
        }
    }
}