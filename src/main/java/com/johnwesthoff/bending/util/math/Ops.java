package com.johnwesthoff.bending.util.math;

public class Ops {
    public static double lengthdir_x(final double R, final double T) {
        return (R * (Math.cos(T * Math.PI / 180)));
    }

    public static double lengthdir_y(final double R, final double T) {
        return (-R * (Math.sin(T * Math.PI / 180)));
    }

    public static double pointDir(final double x1, final double y1, final double x2, final double y2) {
        return Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));
    }

    public static double pointDis(final double x1, final double y1, final double x2, final double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
}
