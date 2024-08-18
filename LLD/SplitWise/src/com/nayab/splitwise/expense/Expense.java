package com.nayab.splitwise.expense;

import com.nayab.splitwise.ExpenseMetadata;
import com.nayab.splitwise.User;
import com.nayab.splitwise.split.Split;

import java.util.List;

public abstract class Expense {
    private String id;
    private String groupId;
    private double amount;
    private User paidBy;
    private List<Split> splits;
    private ExpenseMetadata metadata;

    public Expense(String groupId, double amount, User paidBy, List<Split> splits, ExpenseMetadata metadata) {
        this.groupId = groupId;
        this.amount = amount;
        this.paidBy = paidBy;
        this.splits = splits;
        this.metadata = metadata;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public User getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(User paidBy) {
        this.paidBy = paidBy;
    }

    public List<Split> getSplits() {
        return splits;
    }

    public void setSplits(List<Split> splits) {
        this.splits = splits;
    }

    public ExpenseMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(ExpenseMetadata metadata) {
        this.metadata = metadata;
    }

    public abstract boolean validate();
}
