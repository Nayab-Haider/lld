package com.nayab.splitwise.expense;

import com.nayab.splitwise.ExpenseMetadata;
import com.nayab.splitwise.User;
import com.nayab.splitwise.split.EqualSplit;
import com.nayab.splitwise.split.Split;

import java.util.List;

public class EqualExpense extends Expense {
    public EqualExpense(String groupId, double amount, User paidBy, List<Split> splits, ExpenseMetadata expenseMetadata) {
        super(groupId, amount, paidBy, splits, expenseMetadata);
    }

    @Override
    public boolean validate() {
        for (Split split : getSplits()) {
            if (!(split instanceof EqualSplit)) {
                return false;
            }
        }

        return true;
    }
}
