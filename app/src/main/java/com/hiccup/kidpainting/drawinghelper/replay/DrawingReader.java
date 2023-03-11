package com.hiccup.kidpainting.drawinghelper.replay;

import android.content.Context;
import android.view.MotionEvent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.hiccup.kidpainting.drawinghelper.DrawingPoint;
import com.hiccup.kidpainting.drawinghelper.EntityModel;
import com.hiccup.kidpainting.drawinghelper.TimedPoint;

/**
 * Class to read data struct from json file
 * Created by lion on 6/7/16.
 */
public class DrawingReader {

    public DrawingReader() {
    }

    public EntityModel readObjectDataFromFile(Context context, String fileName) {
        EntityModel stickerModel = null;
        try {
            JSONArray dataSticker = loadJsonFromAsset(context, fileName);
            if (dataSticker == null) {
                return null;
            }
            ArrayList<TimedPoint> stickerPoints = new ArrayList<>();
            JSONArray path;
            TimedPoint timedPoint;

            // area bound path
            float left, top, right, bottom;
            left = top = right = bottom = 0;

            for (int i = 0; i < dataSticker.length(); i++) {
                path = dataSticker.getJSONArray(i);
                if (i == 0) {
                    JSONObject jo_inside = path.getJSONObject(0);
                    float x = Float.parseFloat(jo_inside.getString("x"));
                    float y = Float.parseFloat(jo_inside.getString("y"));
                    left = right = x;
                    top = bottom = y;
                }
                for (int j = 0; j < path.length(); j++) {
                    JSONObject jo_inside = path.getJSONObject(j);
                    float x = Float.parseFloat(jo_inside.getString("x"));
                    float y = Float.parseFloat(jo_inside.getString("y"));
                    long time = Long.parseLong(jo_inside.getString("time"));

                    //Add your values in your `ArrayList` as below:
                    DrawingPoint nhoPoint;
                    if (j == 0) {
                        nhoPoint = new DrawingPoint(0, MotionEvent.ACTION_DOWN, x, y);
                    } else {
                        if (x < left) {
                            left = x;
                        } else if (x > right) {
                            right = x;
                        }

                        if (y < top) {
                            top = y;
                        } else if (y > bottom) {
                            bottom = y;
                        }

                        if (j == path.length() - 1) {
                            nhoPoint = new DrawingPoint(0, MotionEvent.ACTION_UP, x, y);
                        } else {
                            nhoPoint = new DrawingPoint(0, MotionEvent.ACTION_MOVE, x, y);
                        }
                    }

                    timedPoint = new TimedPoint(nhoPoint, time);
                    stickerPoints.add(timedPoint);
                }
            }

            stickerModel = new EntityModel(stickerPoints);
            return stickerModel;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return stickerModel;
    }

    private JSONArray loadJsonFromAsset(Context context, String fileName) {
        JSONArray jsonArray = null;
        try {

            InputStream is = context.getAssets().open(fileName);
            int sizeAvailable = is.available();
            byte[] buffer = new byte[sizeAvailable];
            is.read(buffer);
            is.close();
            String json;
            json = new String(buffer, "UTF-8");
            jsonArray = new JSONArray(json);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }
}
