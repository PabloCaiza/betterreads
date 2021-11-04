package io.javabrains.betterreads.book;

import io.javabrains.betterreads.userbooks.UserBooks;
import io.javabrains.betterreads.userbooks.UserBooksPrimaryKey;
import io.javabrains.betterreads.userbooks.UserBooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/")
public class BookController {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserBooksRepository userBooksRepository;

    private final String COVER_IMAGE_ROOT="http://covers.openlibrary.org/b/id/";

    @GetMapping(value = "/books/{bookId}")
    public String getBook(@PathVariable String bookId, Model model, @AuthenticationPrincipal OAuth2User principal) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        Book bookById;
        if(optionalBook.isPresent()){
            Book book=optionalBook.get();
            String coverImageUrl="/images/no-image.png";
            if(book.getCoverIds()!=null && book.getCoverIds().size()>0) {
                coverImageUrl = COVER_IMAGE_ROOT + book.getCoverIds().get(0) + "-L.jpg";

            }
            model.addAttribute("book",book);
            model.addAttribute("coverImage",coverImageUrl);

            if(principal!=null && principal.getAttribute("login")!=null){
                model.addAttribute("loginId",principal.getAttribute("login"));
                UserBooksPrimaryKey userBooksPrimaryKey = new UserBooksPrimaryKey();
                userBooksPrimaryKey.setBookId(bookId);
                userBooksPrimaryKey.setUserId(principal.getAttribute("login"));
                Optional<UserBooks> userBooks = userBooksRepository.findById(userBooksPrimaryKey);
                if(userBooks.isPresent()){
                    model.addAttribute("userBooks",userBooks.get());
                }else{
                    model.addAttribute("userBooks",new UserBooks());
                }
            }
            return "book";
        }
        return  "book-not-found";

    }
}
