package ru.club.decorators;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import ru.club.adapters.PageAsList;
import ru.club.models.Club;
import ru.club.models.User;
import ru.club.transfer.club.ClubDto;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class PageMutable<T> {
    private List<T> innerList;
    private Integer totalPages;
    private Long totalElements;

    public PageMutable() {
        this.innerList = new ArrayList<>();
    }

    public void createFromPage(Page<T> pages) {
        for (T page : pages) {
            this.innerList.add(page);
        }

        this.totalElements = pages.getTotalElements();
        this.totalPages = pages.getTotalPages();
    }
}
