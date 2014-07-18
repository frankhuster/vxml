package org.motechproject.sample.service.impl;

import org.motechproject.sample.domain.Author;
import org.motechproject.sample.domain.Book;
import org.motechproject.sample.repository.AuthorDataService;
import org.motechproject.sample.service.AuthorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the {@link org.motechproject.sample.service.AuthorService} interface. Uses
 * {@link org.motechproject.sample.repository.AuthorDataService} in order to retrieve and persist authors.
 */
@Service("authorService")
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorDataService authorDataService;

    @Override
    public void create(String name, List<Book> books) {
        authorDataService.create(
                new Author(name)
        );
    }

    @Override
    public void add(Author author) {
        authorDataService.create(author);
    }

    @Override
    public Author findAuthorByName(String authorName) {
        Author author = authorDataService.findByName(authorName);
        if (null == author) {
            return null;
        }
        return author;
    }

    @Override
    public List<Author> getAuthors() {
        return authorDataService.retrieveAll();
    }

    @Override
    public void update(Author author) {
        authorDataService.update(author);
    }

    @Override
    public void delete(Author author) {
        authorDataService.delete(author);
    }
}
