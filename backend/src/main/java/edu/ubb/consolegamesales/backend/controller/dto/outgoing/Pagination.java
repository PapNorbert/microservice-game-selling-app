package edu.ubb.consolegamesales.backend.controller.dto.outgoing;

import lombok.Data;

@Data
public class Pagination {
    private int page;
    private int limit;
    private int totalCount;
    private int totalPages;

    public Pagination(int page, int limit, int totalCount) {
        this.page = page;
        this.limit = limit;
        this.totalCount = totalCount;
        this.totalPages = (int) Math.ceil((double) totalCount / (double) limit);
    }
}
