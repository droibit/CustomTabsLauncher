package com.droibit.android.customtabs.launcher;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * To be used as a fallback to open the Uri when Custom Tabs is not available.
 */
public interface CustomTabsFallback {

    /**
     *
     * @param activity The Activity that wants to open the Uri.
     * @param uri The uri to be opened by the fallback.
     */
    void openUri(@NonNull Activity activity, @NonNull Uri uri);
}