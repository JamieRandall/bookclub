package ru.club.models;

public enum Role {
    ADMIN, USER;

    public static Role getRoleFromString(String stringRole) {
        for (Role role : values()) {
            if (role.toString().equals(stringRole))
                return role;
        }

        return null;
    }
}
