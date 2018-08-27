package com.unique.domain.outlookie.core;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

public class Circle {
    public static ShapeDrawable draw (int width, int color) {
        ShapeDrawable oval = new ShapeDrawable (new OvalShape());
        oval.setIntrinsicHeight(width);
        oval.setIntrinsicWidth(width);
        oval.getPaint().setColor(color);
        return oval;
    }
}
