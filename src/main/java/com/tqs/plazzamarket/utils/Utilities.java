package com.tqs.plazzamarket.utils;

public class Utilities {
    private Utilities() {
        
    }

    public static int hashCodeFinal(int hashCode, long temp) {
        final int prime = 31;
        return prime * hashCode + (int) (temp ^ (temp >>> 32));
    }

    public static int isEqualsBase(Object self, Object obj) {
        if (self == obj)
            return 1;
        if (self == null)
            return -1;
        if (self.getClass() != obj.getClass())
            return -1;
        return 1;
    }
}