package com.nayab.splitwise.service;

import com.nayab.splitwise.ExpenseMetadata;
import com.nayab.splitwise.ExpenseType;
import com.nayab.splitwise.User;
import com.nayab.splitwise.expense.EqualExpense;
import com.nayab.splitwise.expense.ExactExpense;
import com.nayab.splitwise.expense.Expense;
import com.nayab.splitwise.expense.PercentExpense;
import com.nayab.splitwise.split.PercentSplit;
import com.nayab.splitwise.split.Split;

import java.util.List;

public class ExpenseService {
    public static Expense createExpense(ExpenseType expenseType, String groupId, double amount, User paidBy, List<Split> splits, ExpenseMetadata expenseMetadata) {
        switch (expenseType) {
            case EXACT:
                return new ExactExpense(groupId, amount, paidBy, splits, expenseMetadata);
            case PERCENT:
                splits.forEach(split -> {
                    PercentSplit percentSplit = (PercentSplit) split;
                    split.setAmount((amount * percentSplit.getPercent()) / 100.0);
                });
                return new PercentExpense(groupId, amount, paidBy, splits, expenseMetadata);
            case EQUAL:
                int totalSplits = splits.size();
                double splitAmount = ((double) Math.round(amount * 100 / totalSplits)) / 100.0;
                splits.forEach(split -> split.setAmount(splitAmount));
                return new EqualExpense(groupId, amount, paidBy, splits, expenseMetadata);
            default:
                return null;
        }
    }
}
