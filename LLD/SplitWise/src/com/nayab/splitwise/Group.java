package com.nayab.splitwise;

import com.nayab.splitwise.split.Split;

import java.util.List;

public class Group {
    private String id;
    private String name;
    private List<User> members;

    public Group(String id, String name, List<User> members) {
        this.id = id;
        this.name = name;
        this.members = members;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public boolean validateGroupSplit(List<Split> splits) {
        return splits.stream().allMatch(split -> members.contains(split.getUser()));
    }
}
