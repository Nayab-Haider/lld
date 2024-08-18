package com.nayab.splitwise.expense;

import com.nayab.splitwise.ExpenseMetadata;
import com.nayab.splitwise.User;
import com.nayab.splitwise.split.PercentSplit;
import com.nayab.splitwise.split.Split;

import java.util.List;

public class PercentExpense extends Expense {
    public PercentExpense(String groupId, double amount, User paidBy, List<Split> splits, ExpenseMetadata expenseMetadata) {
        super(groupId, amount, paidBy, splits, expenseMetadata);
    }

    @Override
    public boolean validate() {
        for (Split split : getSplits()) {
            if (!(split instanceof PercentSplit)) {
                return false;
            }
        }

        double totalPercent = 100;
        double sumSplitPercent = 0;
        for (Split split : getSplits()) {
            PercentSplit exactSplit = (PercentSplit) split;
            sumSplitPercent += exactSplit.getPercent();
        }

        if (totalPercent != sumSplitPercent) {
            return false;
        }

        return true;
    }
}
