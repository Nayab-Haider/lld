package com.nayab.splitwise.expense;

import com.nayab.splitwise.ExpenseMetadata;
import com.nayab.splitwise.User;
import com.nayab.splitwise.split.ExactSplit;
import com.nayab.splitwise.split.Split;

import java.util.List;

public class ExactExpense extends Expense {
    public ExactExpense(String groupId, double amount, User paidBy, List<Split> splits, ExpenseMetadata expenseMetadata) {
        super(groupId, amount, paidBy, splits, expenseMetadata);
    }

    @Override
    public boolean validate() {
        for (Split split : getSplits()) {
            if (!(split instanceof ExactSplit)) {
                return false;
            }
        }

        double totalAmount = getAmount();
        double sumSplitAmount = 0;
        for (Split split : getSplits()) {
            ExactSplit exactSplit = (ExactSplit) split;
            sumSplitAmount += exactSplit.getAmount();
        }

        if (totalAmount != sumSplitAmount) {
            return false;
        }

        return true;
    }
}
