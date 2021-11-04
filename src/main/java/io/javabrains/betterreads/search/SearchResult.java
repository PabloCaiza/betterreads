package io.javabrains.betterreads.search;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SearchResult {
    private int numFound;
    private List<SearchResulBook> docs;


}
