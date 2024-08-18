package com.nayab.splitwise.split;

import com.nayab.splitwise.User;

public class ExactSplit extends Split {

    public ExactSplit(User user, double amount) {
        super(user);
        this.amount = amount;
    }

}
