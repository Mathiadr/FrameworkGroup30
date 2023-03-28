package no.hiof.framework30.brunost.util;

// Source: GamesWithGabe, 27.09.21 - https://www.youtube.com/playlist?list=PLtrSb4XxIVbp8AKuEAlwNXDxr99e3woGE
public class Time {
    public static float timeStarted = System.nanoTime();

    public static float getTime() {
        return (float)((System.nanoTime() - timeStarted) * 1E-9);
    }
}
