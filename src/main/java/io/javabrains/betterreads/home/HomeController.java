package io.javabrains.betterreads.home;


import io.javabrains.betterreads.user.BooksByUser;
import io.javabrains.betterreads.user.BooksByUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {
    private final String COVER_IMAGE_ROOT = "http://covers.openlibrary.org/b/id/";

    @Autowired
    private BooksByUserRepository booksByUserRepository;

    @GetMapping("/")
    public String home(@AuthenticationPrincipal OAuth2User principal, Model model) {

        if (principal == null || principal.getAttribute("login") == null) {
            return "index";
        }
        String userId = principal.getAttribute("login");
        Slice<BooksByUser> booksByUserSlice = booksByUserRepository.findAllById(userId, CassandraPageRequest.of(0, 50));
        List<BooksByUser> booksByUser = booksByUserSlice.getContent();
        booksByUser= booksByUser.stream()
                .distinct()
                .map(booksByUser1 -> {
                    String coverImageUrl = "/images/no-image.png";
                    if (booksByUser1.getCoverIds() != null && booksByUser1.getCoverIds().size() > 0) {
                        coverImageUrl = COVER_IMAGE_ROOT + booksByUser1.getCoverIds().get(0) + "-M.jpg";

                    }
                    booksByUser1.setCoverUrl(coverImageUrl);
                    return booksByUser1;
                })
                .collect(Collectors.toList());

        model.addAttribute("books", booksByUser);
        return "home";

    }

}
