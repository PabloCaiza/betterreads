package io.javabrains.betterreads.search;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SearchResulBook  {
    private String key;
    private String title;
    private List<String> auhor_name;
    private String cover_i;
    private int first_publish_year;


}
