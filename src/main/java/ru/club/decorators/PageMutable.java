package ru.club.decorators;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class PageMutable<T> implements Iterable<T>{
    private List<T> content;
    private Integer totalPages;
    private Long totalElements;
    private Sort sort;

    public PageMutable() {
        this.content = new ArrayList<>();
    }

    public void createFromPage(Page<T> pages) {
        for (T page : pages) {
            this.content.add(page);
        }


        this.sort = pages.getSort();
        this.totalElements = pages.getTotalElements();
        this.totalPages = pages.getTotalPages();
    }

    @Override
    public Iterator<T> iterator() {
        return this.content.iterator();
    }
}
