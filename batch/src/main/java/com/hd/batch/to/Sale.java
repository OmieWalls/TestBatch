package com.hd.batch.to;

import com.google.cloud.bigquery.FieldValueList;
import com.hd.batch.util.Util;
import org.joda.time.DateTime;

import java.util.Objects;

public class Sale {

    private String storeNumber;
    private DateTime salesTsLocal;
    private String upcCode;
    private String skuNumber;
    private String unitSales;
    private String currRetailAmount;
    private String posTransTypeCode;
    private String posTransId;
    private String registerNumber;

    public Sale() {
        this.storeNumber = null;
        this.salesTsLocal = null;
        this.upcCode = null;
        this.skuNumber = null;
        this.unitSales = null;
        this.currRetailAmount = null;
        this.posTransTypeCode = null;
        this.posTransId = null;
        this.registerNumber = null;
    }

    public Sale(String storeNumber, DateTime salesTsLocal, String upcCode, String skuNumber,
                 String unitSales, String currRetailAmount, String posTransTypeCode,
                 String posTransId, String registerNumber) {
        this.storeNumber = storeNumber;
        this.salesTsLocal = salesTsLocal;
        this.upcCode = upcCode;
        this.skuNumber = skuNumber;
        this.unitSales = unitSales;
        this.currRetailAmount = currRetailAmount;
        this.posTransTypeCode = posTransTypeCode;
        this.posTransId = posTransId;
        this.registerNumber = registerNumber;
    }

    public Sale(FieldValueList tableResultRow) {

        this.storeNumber = tableResultRow.get(0).getValue() != null ?
                tableResultRow.get(0).getValue().toString() : null;

        this.salesTsLocal = tableResultRow.get(1).getValue() != null ?
                Util.formatDateTimeFromString(tableResultRow.get(1).getValue().toString()) : null;

        this.upcCode = tableResultRow.get(2).getValue() != null ?
                tableResultRow.get(2).getValue().toString() : null;

        this.skuNumber = tableResultRow.get(3).getValue() != null ?
                tableResultRow.get(3).getValue().toString() : null;

        this.unitSales = tableResultRow.get(4).getValue() != null ?
                tableResultRow.get(4).getValue().toString() : null;

        this.currRetailAmount = tableResultRow.get(5).getValue() != null ?
                tableResultRow.get(5).getValue().toString() : null;

        this.posTransTypeCode = tableResultRow.get(6).getValue() != null ?
                tableResultRow.get(6).getValue().toString() : null;

        this.posTransId = tableResultRow.get(7).getValue() != null ?
                tableResultRow.get(7).getValue().toString() : null;

        this.registerNumber = tableResultRow.get(8).getValue() != null ?
                tableResultRow.get(8).getValue().toString() : null;
    }

    public String getStoreNumber() {
        return storeNumber;
    }

    public void setStoreNumber(String storeNumber) {
        this.storeNumber = storeNumber;
    }

    public DateTime getSalesTsLocal() {
        return salesTsLocal;
    }

    public void setSalesTsLocal(DateTime salesTsLocal) {
        this.salesTsLocal = salesTsLocal;
    }

    public String getUpcCode() {
        return upcCode;
    }

    public void setUpcCode(String upcCode) {
        this.upcCode = upcCode;
    }

    public String getSkuNumber() {
        return skuNumber;
    }

    public void setSkuNumber(String skuNumber) {
        this.skuNumber = skuNumber;
    }

    public String getUnitSales() {
        return unitSales;
    }

    public void setUnitSales(String unitSales) {
        this.unitSales = unitSales;
    }

    public String getCurrRetailAmount() {
        return currRetailAmount;
    }

    public void setCurrRetailAmount(String currRetailAmount) {
        this.currRetailAmount = currRetailAmount;
    }

    public String getPosTransTypeCode() {
        return posTransTypeCode;
    }

    public void setPosTransTypeCode(String posTransTypeCode) {
        this.posTransTypeCode = posTransTypeCode;
    }

    public String getPosTransId() {
        return posTransId;
    }

    public void setPosTransId(String posTransId) {
        this.posTransId = posTransId;
    }

    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sale sale = (Sale) o;
        return Objects.equals(storeNumber, sale.storeNumber) &&
                Objects.equals(salesTsLocal, sale.salesTsLocal) &&
                Objects.equals(upcCode, sale.upcCode) &&
                Objects.equals(skuNumber, sale.skuNumber) &&
                Objects.equals(unitSales, sale.unitSales) &&
                Objects.equals(currRetailAmount, sale.currRetailAmount) &&
                Objects.equals(posTransTypeCode, sale.posTransTypeCode) &&
                Objects.equals(posTransId, sale.posTransId) &&
                Objects.equals(registerNumber, sale.registerNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeNumber, salesTsLocal, upcCode, skuNumber, unitSales, currRetailAmount, posTransTypeCode, posTransId, registerNumber);
    }

    @Override
    public String toString() {
        return "Event{" +
                "storeNumber='" + storeNumber + '\'' +
                ", salesTsLocal='" + salesTsLocal + '\'' +
                ", upcCode='" + upcCode + '\'' +
                ", skuNumber='" + skuNumber + '\'' +
                ", unitSales='" + unitSales + '\'' +
                ", currRetailAmount='" + currRetailAmount + '\'' +
                ", posTransTypeCode='" + posTransTypeCode + '\'' +
                ", posTransId='" + posTransId + '\'' +
                ", registerNumber='" + registerNumber + '\'' +
                '}';
    }

}
