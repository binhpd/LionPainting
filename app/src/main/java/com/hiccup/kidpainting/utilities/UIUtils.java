package com.hiccup.kidpainting.utilities;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Environment;
import androidx.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by hiccup on 26/08/2015.
 */
public class UIUtils {

    public static float convertDpToPx(Context context, int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    @NonNull
    public static File writeStringToFile(String content) throws IOException {
        File myFile = new File(Environment.getExternalStorageDirectory() + "/export.txt");
        myFile.createNewFile();

        FileOutputStream fOut = new FileOutputStream(myFile);
        OutputStreamWriter myOutWriter =
                new OutputStreamWriter(fOut);
        myOutWriter.append(content);
        myOutWriter.close();
        fOut.close();
        return myFile;
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }


}
