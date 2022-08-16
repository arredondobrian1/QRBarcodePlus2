package com.test.qrbarcodeplus;

public class barcodeSingleton {

    private static barcodeSingleton instance = null;
    protected barcodeSingleton(){

    }
    public static barcodeSingleton getInstance() {
        if(instance == null){
            instance = new barcodeSingleton();
    }
        return instance;
    }

    private String barcode = "";


    public String getBarcodeValue() {
        return barcode;
    }

    public void setBarcodeValue(String value) {
        this.barcode = value;
    }

}