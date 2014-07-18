package org.motechproject.sample.domain;

import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Unique;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Author {

    @Field(required = true)
    @Unique
    private String name;

    @Field
    @Cascade(persist = true, update = true, delete = true)
    private Bio bio;

    @Field
    @Cascade(persist = true, update = true, delete = true)
    private List<Book> books = new ArrayList<>();

    @Field
    @Cascade(persist = true, update = true, delete = true)
    private List<NomDePlume> nomsDePlume = new ArrayList<>();

    public Author(String name) {
        this.name = name;
    }

    public Author(String name, Bio bio, List<Book> books, List<NomDePlume> nomsDePlume) {
        this.name = name;
        this.bio = bio;
        if (null != books) {
            this.books = books;
        }
        if (null != nomsDePlume) {
            this.nomsDePlume = nomsDePlume;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bio getBio() {
        return bio;
    }

    public void setBio(Bio bio) {
        this.bio = bio;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        if (null == books) {
            this.books = new ArrayList<>();
        } else {
            this.books = books;
        }
    }

    public List<NomDePlume> getNomsDePlume() {
        return nomsDePlume;
    }

    public void setNomsDePlume(List<NomDePlume> nomsDePlume) {
        if (null == nomsDePlume) {
            this.nomsDePlume = new ArrayList<>();
        } else {
            this.nomsDePlume = nomsDePlume;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Author)) return false;

        Author author = (Author) o;

        if (bio != null ? !bio.equals(author.bio) : author.bio != null) return false;
        if (books != null ? !books.equals(author.books) : author.books != null) return false;
        if (!name.equals(author.name)) return false;
        if (nomsDePlume != null ? !nomsDePlume.equals(author.nomsDePlume) : author.nomsDePlume != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (bio != null ? bio.hashCode() : 0);
        result = 31 * result + (books != null ? books.hashCode() : 0);
        result = 31 * result + (nomsDePlume != null ? nomsDePlume.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Author{" +
                "name='" + name + '\'' +
                ", bio=" + bio +
                ", books=" + books +
                ", nomsDePlume=" + nomsDePlume +
                '}';
    }
}