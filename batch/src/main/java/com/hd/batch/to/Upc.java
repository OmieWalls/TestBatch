package com.hd.batch.to;

import java.util.Objects;

public class Upc {

    private String nameId;
    private String upc;
    private String productDescription;

    public Upc() {
        this.nameId = null;
        this.upc = null;
        this.productDescription = null;
    }

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Upc upc1 = (Upc) o;
        return Objects.equals(nameId, upc1.nameId) &&
                Objects.equals(upc, upc1.upc) &&
                Objects.equals(productDescription, upc1.productDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameId, upc, productDescription);
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
