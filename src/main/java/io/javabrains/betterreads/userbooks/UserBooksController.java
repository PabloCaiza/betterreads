package io.javabrains.betterreads.userbooks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;

@Controller
public class UserBooksController {
    @Autowired
    private  UserBooksRepository userBooksRepository;
    @PostMapping("/addUserBook")
    public ModelAndView addBookForUser(@AuthenticationPrincipal OAuth2User principal,
                                       @RequestBody MultiValueMap<String,String> formData){

        if (principal == null || principal.getAttribute("login")==null) {
            return null;
        }
        UserBooksPrimaryKey userBooksPrimaryKey=new UserBooksPrimaryKey();
        userBooksPrimaryKey.setUserId(principal.getAttribute("login"));
        String bookId = formData.getFirst("bookId");
        userBooksPrimaryKey.setBookId(bookId);

        UserBooks userBooks=new UserBooks();
        userBooks.setKey(userBooksPrimaryKey);
        userBooks.setStartedDate(LocalDate.parse(formData.getFirst("startDate")));
        userBooks.setCompletedDate(LocalDate.parse(formData.getFirst("completedDate")));
        userBooks.setRating(Integer.parseInt(formData.getFirst("rating")));
        userBooks.setReadingStatus(formData.getFirst("readingStatus"));

        userBooksRepository.save(userBooks);

        return new ModelAndView("redirect:/books/"+bookId);
    }
}