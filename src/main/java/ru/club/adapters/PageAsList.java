package ru.club.adapters;

import lombok.Data;

import java.util.ArrayList;

@Data
public class PageAsList<T> extends ArrayList<T> {
    private Long totalElements;
    private Integer totalPages;

    @Override
    public String toString() {
        String toReturn = super.toString();
        toReturn = toReturn.substring(0, toReturn.length() - 1);
        toReturn += ", totalElements=" + this.totalElements + ", totalPages=" + this.totalPages + "]";

        return toReturn;
    }
}
