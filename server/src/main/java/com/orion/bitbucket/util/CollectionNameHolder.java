package com.orion.bitbucket.util;

// this class sets and gets collection name for changeable collection names.
public class CollectionNameHolder {
    private static final ThreadLocal<String> collectionNameThreadLocal = new ThreadLocal<>();
    public static String get(){
        String collectionName = collectionNameThreadLocal.get();
        if(collectionName == null){
            collectionName ="pull-requests";
            collectionNameThreadLocal.set(collectionName);
        }
        return collectionName;
    }
    public static void set(String collectionName){
        collectionNameThreadLocal.set(collectionName);
    }
    public static void reset(){
        collectionNameThreadLocal.remove();
    }
}


