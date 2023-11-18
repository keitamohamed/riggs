package com.keita.riggs.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class ChartMonthlyTarget {
    private String month;
    private int  numRoomBook;
    private double amount;

    public ChartMonthlyTarget(String month, int numRoomBook, double amount) {
        this.month = month;
        this.numRoomBook = numRoomBook;
        this.amount = amount;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getNumRoomBook() {
        return numRoomBook;
    }

    public void setNumRoomBook(int numRoomBook) {
        this.numRoomBook = numRoomBook;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
