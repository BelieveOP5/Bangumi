package com.wen.bangumi.base;

import android.graphics.Bitmap;

import com.squareup.picasso.Transformation;

/**
 * 图像转换类
 * Created by BelieveOP5 on 2017/2/4.
 */

public class ImageTransformation implements Transformation {

    @Override
    public Bitmap transform(Bitmap source) {

        if (source == null)
            return null;

        Bitmap result = Bitmap.createScaledBitmap(
                source,
                source.getWidth() / 2,
                source.getHeight() / 2,
                false
        );

        source.recycle();

        return result;

    }

    @Override
    public String key() {
        return "ImageTransformation";
    }

}
