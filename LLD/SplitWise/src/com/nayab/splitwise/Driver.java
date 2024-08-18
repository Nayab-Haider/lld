package com.nayab.splitwise;

import com.nayab.splitwise.split.EqualSplit;
import com.nayab.splitwise.split.ExactSplit;
import com.nayab.splitwise.split.PercentSplit;
import com.nayab.splitwise.split.Split;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.nayab.splitwise.ExpenseType.EQUAL;

public class Driver {
    private static Splitwise splitwise = new Splitwise();

    public static void main(String[] args) {
        addUsers();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.nextLine();
            String[] commands = command.split(" ");
            String commandType = commands[0];
            switch (commandType) {
                case "SHOW":
                    if (commands.length == 1) {
                        splitwise.showBalances();
                    } else {
                        splitwise.showBalance(commands[1]);
                    }
                    break;
                case "EXPENSE":
                    addExpense(commands);
                    break;
                case "GROUP":
                    String groupName = commands[1];
                    int membersCount = Integer.parseInt(commands[2]);
                    List<String> membersId = new ArrayList<>();
                    for (int i = 0; i < membersCount; i++) {
                        membersId.add(commands[3 + i]);
                    }
                    splitwise.createGroup(groupName, membersId);
                    break;
                case "GRP_BAL":
                    splitwise.showGroupBalances(commands[1]);
                    break;
                case "GRP_EXPENSE":
                    addExpense(commands);
                    break;
                default:
                    return;
            }
        }
    }


    private static void addExpense(String[] commands) {
        String paidBy = commands[1];
        double amount = Double.parseDouble(commands[2]);
        int noOfUsers = Integer.parseInt(commands[3]);
        ExpenseType expenseType = ExpenseType.valueOf(commands[4 + noOfUsers]);
        List<Split> splits = new ArrayList<>();
        String groupName = null;
        switch (expenseType) {
            case EQUAL:
                for (int i = 0; i < noOfUsers; i++) {
                    splits.add(new EqualSplit(splitwise.userMap.get(commands[4 + i])));
                }
                if (commands.length == 4 + noOfUsers + 2) {
                    groupName = commands[4 + noOfUsers + 1];
                }
                splitwise.addExpense(EQUAL, amount, groupName, paidBy, splits, null);
                break;
            case EXACT:
                for (int i = 0; i < noOfUsers; i++) {
                    splits.add(new ExactSplit(splitwise.userMap.get(commands[4 + i]), Double.parseDouble(commands[5 + noOfUsers + i])));
                }
                if (commands.length == 4 + 2 * noOfUsers + 2) {
                    groupName = commands[4 + 2 * noOfUsers + 1];
                }
                splitwise.addExpense(ExpenseType.EXACT, amount, groupName, paidBy, splits, null);
                break;
            case PERCENT:
                for (int i = 0; i < noOfUsers; i++) {
                    splits.add(new PercentSplit(splitwise.userMap.get(commands[4 + i]), Double.parseDouble(commands[5 + noOfUsers + i])));
                }
                if (commands.length == 4 + 2 * noOfUsers + 2) {
                    groupName = commands[4 + 2 * noOfUsers + 1];
                }
                splitwise.addExpense(ExpenseType.PERCENT, amount, groupName, paidBy, splits, null);
                break;
        }
    }

    private static void addUsers() {
        splitwise.addUser(new User("u1", "Nayab", "nayab@nayab.com", "1111111111"));
        splitwise.addUser(new User("u2", "Ashutosh", "ashutosh@nayab.com", "2222222222"));
        splitwise.addUser(new User("u3", "Manoj", "manoj@nayab.com", "3333333333"));
        splitwise.addUser(new User("u4", "Harshit", "harshit@nayab.com", "4444444444"));
    }
}
