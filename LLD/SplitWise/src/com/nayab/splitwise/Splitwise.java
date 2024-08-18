package com.nayab.splitwise;

import com.nayab.splitwise.expense.Expense;
import com.nayab.splitwise.service.ExpenseService;
import com.nayab.splitwise.split.Split;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


public class Splitwise {
    List<Expense> expenses;
    Map<String, User> userMap;
    Map<String, Map<String, Double>> balanceSheet;
    List<Group> groups;

    public Splitwise() {
        expenses = new ArrayList<>();
        userMap = new HashMap<>();
        balanceSheet = new HashMap<>();
        groups = new ArrayList<>();
    }

    public void addUser(User user) {
        userMap.put(user.getId(), user);
        balanceSheet.put(user.getId(), new HashMap<>());
    }

    public void createGroup(String groupName, List<String> membersId) {
        groups.add(new Group(UUID.randomUUID().toString(), groupName, membersId.stream().map(member -> userMap.get(member)).collect(Collectors.toList())));
    }

    public void addExpense(ExpenseType expenseType, double amount, String groupName, String paidBy, List<Split> splits, ExpenseMetadata expenseMetadata) {
        Optional<Group> groupOptional = groups.stream().filter(group -> group.getName().equals(groupName)).findAny();
        if (groupOptional.isPresent() && !validateGroupExpense(groupOptional, splits)) {
            System.out.println("Invalid group members");
            return;
        }
        Expense expense = ExpenseService.createExpense(expenseType, groupOptional.map(Group::getId).orElse(null), amount, userMap.get(paidBy), splits, expenseMetadata);
        expenses.add(expense);
        expense.getSplits().forEach(split -> {
            String paidTo = split.getUser().getId();
            Map<String, Double> balances = balanceSheet.get(paidBy);
            balances.put(paidTo, balances.getOrDefault(paidTo, 0d) + split.getAmount());

            balances = balanceSheet.get(paidTo);
            balances.put(paidBy, balances.getOrDefault(paidBy, 0d) - split.getAmount());
        });
    }

    private boolean validateGroupExpense(Optional<Group> groupOptional, List<Split> splits) {
        return groupOptional.map(group -> group.validateGroupSplit(splits)).orElse(false);
    }

    public void showBalance(String userId) {
        boolean isPresent = balanceSheet.get(userId).entrySet().stream()
                .filter(entry -> entry.getValue() > 0)
                .peek(entry -> printBalance(userId, entry.getKey(), entry.getValue()))
                .findAny()
                .isPresent();

        if (!isPresent) {
            System.out.println("No balances");
        }
    }

    public void showBalances() {
        boolean hasBalance = balanceSheet.entrySet().stream()
                .flatMap(allBalances -> allBalances.getValue().entrySet().stream()
                        .filter(userBalance -> userBalance.getValue() > 0)
                        .map(userBalance -> new AbstractMap.SimpleEntry<>(allBalances.getKey(), userBalance)))
                .peek(entry -> printBalance(entry.getKey(), entry.getValue().getKey(), entry.getValue().getValue()))
                .findAny()
                .isPresent();

        if (!hasBalance) {
            System.out.println("No balances");
        }
    }

    public void showGroupBalances(String groupName) {
        groups.stream()
                .filter(group -> group.getName().equals(groupName)).findFirst()
                .map(group -> expenses.stream()
                        .filter(expense -> Objects.nonNull(expense.getGroupId()))
                        .filter(expense -> expense.getGroupId().equals(group.getId()))
                        .flatMap(expense -> expense.getSplits().stream()
                                .filter(split -> !split.getUser().equals(expense.getPaidBy()))
                                .map(split -> new AbstractMap.SimpleEntry<>(
                                        expense.getPaidBy(),
                                        new AbstractMap.SimpleEntry<>(split.getUser(), split.getAmount())
                                ))
                        )
                        .collect(Collectors.groupingBy(
                                Map.Entry::getKey,                                // Group by lender (PaidBy)
                                Collectors.toMap(
                                        entry -> entry.getValue().getKey(),            // Borrower (User)
                                        entry -> entry.getValue().getValue(),          // Amount
                                        Double::sum,                                   // Merge function to sum amounts
                                        HashMap::new                                   // Create new HashMap for the inner map
                                )
                        ))
                ).orElseGet(HashMap::new)
                .forEach((paidBy, balance) -> balance.forEach((owedBy, amount) -> printBalance(paidBy.getId(), owedBy.getId(), amount)));
    }

    private void printBalance(String user1, String user2, double amount) {
        String user1Name = userMap.get(user1).getName();
        String user2Name = userMap.get(user2).getName();
        if (amount < 0) {
            System.out.println(user1Name + " owes " + user2Name + ": " + Math.abs(amount));
        } else if (amount > 0) {
            System.out.println(user2Name + " owes " + user1Name + ": " + Math.abs(amount));
        }
    }
}
