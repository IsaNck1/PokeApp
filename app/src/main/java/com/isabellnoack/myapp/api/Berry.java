package com.isabellnoack.myapp.api;

import java.util.ArrayList;

public class Berry {
    public String name = "Unknown";
    public int size;
    public int smoothness;
    public ArrayList<BerryFlavor> flavors = new ArrayList<>();

    @Override
    public String toString() {
        return "Berry{" +
                "name='" + name + '\'' +
                ", size=" + size +
                ", smoothness=" + smoothness +
                ", flavors=" + flavors +
                '}';
    }
}
