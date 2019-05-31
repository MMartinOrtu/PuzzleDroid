package com.example.puzzledroid;

import android.graphics.Bitmap;

public class ImageData {
    private Bitmap bitmap;
    private int sequenceNumber;

    public ImageData(Bitmap bitmap, int sequenceNumber) {
        this.bitmap = bitmap;
        this.sequenceNumber = sequenceNumber;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
}

