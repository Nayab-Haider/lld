package com.nayab.splitwise.service;

import com.nayab.splitwise.User;
import com.nayab.splitwise.split.EqualSplit;
import com.nayab.splitwise.split.ExactSplit;
import com.nayab.splitwise.split.PercentSplit;
import com.nayab.splitwise.split.Split;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SplitService {
    public static List<Split> createEqualSplit(List<User> users) {
        return users.stream().map(EqualSplit::new).collect(Collectors.toList());
    }

    public static List<Split> createExactSplit(Map<User, Double> balances) {
        return balances.entrySet().stream().map(entry -> new ExactSplit(entry.getKey(), entry.getValue())).collect(Collectors.toList());
    }

    public static List<Split> createPercentageSplit(Map<User, Float> percentage) {
        return percentage.entrySet().stream().map(entry -> new PercentSplit(entry.getKey(), entry.getValue())).collect(Collectors.toList());
    }
}
