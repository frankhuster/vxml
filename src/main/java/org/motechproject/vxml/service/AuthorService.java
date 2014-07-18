package org.motechproject.vxml.service;

import org.motechproject.vxml.domain.Author;
import org.motechproject.vxml.domain.Book;

import java.util.List;

/**
 * Service interface for CRUD on simple repository authors.
 */
public interface AuthorService {

    void create(String name, List<Book> books);

    void add(Author author);

    Author findAuthorByName(String authorName);

    List<Author> getAuthors();

    void delete(Author author);

    void update(Author author);
}
