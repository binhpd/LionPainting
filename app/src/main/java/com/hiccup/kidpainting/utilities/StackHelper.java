package com.hiccup.kidpainting.utilities;

/**
 * Created by ${binhpd} on 7/21/2016.
 */
public class StackHelper {

    private static int stackSize;

    private int stack[];

    public StackHelper(int w, int h) {
        this.h = h;
        this.w = w;
        stackSize = w * h;
        stack = new int[stackSize];
    }

    int stackPointer;
    int h, w;

    public boolean pop(int xy[]) {
        if (stackPointer > 0) {
            int p = stack[stackPointer];
            xy [0] = p / w;
            xy [1] = p % w;
            stackPointer--;
            return true;
        }
        return false;
    }


    public boolean push(int[] xy) {
        if (stackPointer < stackSize-1) {
            stackPointer++;
            stack[stackPointer] = w *xy[0]+ xy[1];
            return true;
        }
        return false;
    }

    public void clear() {
        while(pop(new int[2]));
    }
}
