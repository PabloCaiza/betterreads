package io.javabrains.betterreads.search;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SearchController {

    private final WebClient webClient;
    private final String COVER_IMAGE_ROOT = "http://covers.openlibrary.org/b/id/";

    public SearchController(WebClient.Builder webClBuilder) {
        this.webClient = webClBuilder
                .baseUrl("http://openlibrary.org/search.json")
                .exchangeStrategies(ExchangeStrategies.builder().codecs(clientCodecConfigurer -> {
                    clientCodecConfigurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024);
                }).build())
                .build();
    }

    @GetMapping(value = "/search")
    public String getSearchResults(@RequestParam String query, Model model) {
        Mono<SearchResult> foo = webClient.get()
                .uri("?q={query}", query)
                .retrieve().bodyToMono(SearchResult.class);
        SearchResult result = foo.block();
        List<SearchResulBook> books = result.getDocs()
                .stream()
                .limit(10)
                .map(searchResulBook -> {
                    searchResulBook.setKey(
                            searchResulBook.getKey().replace("/works/", ""));
                    String coverId =searchResulBook.getCover_i();
                    if(StringUtils.hasText(coverId)){
                        coverId=COVER_IMAGE_ROOT+coverId+"-M.jpg";
                    }else{
                        coverId="/images/no-image.png";
                    }
                    searchResulBook.setCover_i(coverId);
                    return searchResulBook;

                })
                .collect(Collectors.toList());
        model.addAttribute("searchResults", books);
        return "search";
    }
}
