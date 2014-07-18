package org.motechproject.sample.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

@Entity
public class Bio {
    @Field
    private String text;

    @Field
    private Author author;

    public Bio(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bio)) return false;

        Bio bio = (Bio) o;

        if (author != null ? !author.getName().equals(bio.author.getName()) : bio.author != null) return false;
        if (text != null ? !text.equals(bio.text) : bio.text != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = text != null ? text.hashCode() : 0;
        result = 31 * result + (author != null ? author.getName().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Bio{" +
                "text='" + text + '\'' +
                ", author=" + author.getName() +
                '}';
    }
}
