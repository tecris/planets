package org.terra.bs.entities;

// Generated Apr 25, 2014 12:34:44 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * BookAuthorId generated by hbm2java
 */
@Embeddable
public class BookAuthorId implements java.io.Serializable {

    private int authorId;
    private int bookId;

    public BookAuthorId() {
    }

    public BookAuthorId(int authorId, int bookId) {
        this.authorId = authorId;
        this.bookId = bookId;
    }

    @Column(name = "AUTHOR_ID", nullable = false)
    public int getAuthorId() {
        return this.authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    @Column(name = "BOOK_ID", nullable = false)
    public int getBookId() {
        return this.bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null)
            return false;
        if (!(other instanceof BookAuthorId))
            return false;
        BookAuthorId castOther = (BookAuthorId) other;

        return (this.getAuthorId() == castOther.getAuthorId()) && (this.getBookId() == castOther.getBookId());
    }

    @Override
    public int hashCode() {
        int result = 17;

        result = 37 * result + this.getAuthorId();
        result = 37 * result + this.getBookId();
        return result;
    }

}
