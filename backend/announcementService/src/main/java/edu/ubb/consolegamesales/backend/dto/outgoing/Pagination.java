package edu.ubb.consolegamesales.backend.dto.outgoing;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Pagination {
    private int page;
    private int limit;
    private long totalCount;
    private int totalPages;

    public Pagination(int page, int limit, long totalCount, int totalPages) {
        this.page = page;
        this.limit = limit;
        this.totalCount = totalCount;
        this.totalPages = totalPages;
    }
}
