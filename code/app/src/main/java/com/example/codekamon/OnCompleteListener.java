package com.example.codekamon;

/**
 * This interface is used to handle the result of DB operation.
 *
 * Citations :
 * https://github.com/CMPUT301F22T02/well-fed/blob/main/app/src/main/java/com/xffffff/wellfed/common/OnCompleteListener.java
 * @param <T>
 */
public interface OnCompleteListener <T>{
    /**
     * Called when a DB operation is completed
     *
     * @param item    the object
     * @param success true if the operation was successful,
     *                false otherwise.
     */
    void onComplete(T item, boolean success);
}
