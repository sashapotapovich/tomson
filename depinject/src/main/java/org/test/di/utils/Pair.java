package org.test.di.utils;

import java.io.Serializable;

public class Pair<L, R> implements Serializable {
    
    private static final long serialVersionUID = 2231632513145237208L;
    
    private L left;
    private R right;

    public Pair(L left, R right){
        this.left = left;
        this.right = right;
    }

    public L getLeft() {
        return left;
    }

    public R getRight() {
        return right;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "left=" + left +
                ", right=" + right +
                '}';
    }
}
