package com.gmail.jiangyang5157.java_core.time;

/**
 * @author Yang
 * @since 6/27/2016.
 */
public class FrameRate {
    private final static String TAG = "[FrameRate]";

    private final long fps; // frame per second
    private final long npf; // nano per frame
    private long npfRealTime;

    private long lastTime;

    public FrameRate(long fps) {
        if (fps < 0) {
            throw new IllegalArgumentException("Fps cannot less than 0.");
        }
        this.fps = fps;
        this.npf = fps == 0 ? 0 : TimeUtils.NANO_IN_SECOND / fps;
    }

    public boolean newFrame() {
        boolean ret = false;

        long thisTime = System.nanoTime();
        long elapsedTime = thisTime - lastTime;
        if (elapsedTime >= npf) {
            ret = true;
            npfRealTime = elapsedTime;
            lastTime = thisTime;
        }

        return ret;
    }

    public long getFpsRealTime() {
        return Math.round(TimeUtils.NANO_IN_SECOND / (double) npfRealTime);
    }

    public long getFps() {
        return fps;
    }

    public static void main(String[] args) {
        FrameRate frameRate = new FrameRate(60);
        while (true) {
            if (frameRate.newFrame()) {
                System.out.println(TAG + " Real time fps = " + frameRate.getFpsRealTime());
            }
        }
    }
}
