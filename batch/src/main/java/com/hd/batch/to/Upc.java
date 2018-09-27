package com.hd.batch.to;

public class Upc {

    private String nameId;
    private String upc;
    private String productDescription;

    public Upc(String nameId, String upc, String productDescription) {
        this.nameId = nameId;
        this.upc = upc;
        this.productDescription = productDescription;
    }

    public String getNameId() {
        return nameId;
    }

    public void setNameId(String nameId) {
        this.nameId = nameId;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    @Override
    public String toString() {
        return "Upc{" +
                "nameId='" + nameId + '\'' +
                ", upc='" + upc + '\'' +
                ", productDescription='" + productDescription + '\'' +
                '}';
    }
}
