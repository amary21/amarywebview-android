package com.amary.amarywebview.utils

import android.content.Context
import android.graphics.Typeface
import androidx.collection.SimpleArrayMap

/**
 * Created by Leonardo on 11/14/15.
 *//*
    Each call to Typeface.createFromAsset will load a new instance of the typeface into memory,
    and this memory is not consistently get garbage collected
    http://code.google.com/p/android/issues/detail?id=9904
    (It states released but even on Lollipop you can see the typefaces accumulate even after
    multiple GC passes)
    You can detect this by running:
    adb shell dumpsys meminfo com.your.packagenage
    You will see output like:
     Asset Allocations
        zip:/data/app/com.your.packagenage-1.apk:/assets/Roboto-Medium.ttf: 125K
        zip:/data/app/com.your.packagenage-1.apk:/assets/Roboto-Medium.ttf: 125K
        zip:/data/app/com.your.packagenage-1.apk:/assets/Roboto-Medium.ttf: 125K
        zip:/data/app/com.your.packagenage-1.apk:/assets/Roboto-Regular.ttf: 123K
        zip:/data/app/com.your.packagenage-1.apk:/assets/Roboto-Medium.ttf: 125K
*/
object TypefaceUtil {
  private val cache = SimpleArrayMap<String, Typeface>()

  operator fun get(c: Context, name: String): Typeface? {
    synchronized(cache) {
      return if (!cache.containsKey(name)) {
        try {
          val t = Typeface.createFromAsset(c.assets, String.format("fonts/%s", name))
          cache.put(name, t)
          t
        } catch (e: RuntimeException) {
          null
        }
      } else cache[name]
    }
  }
}