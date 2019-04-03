package com.zf.gidi.service.dto;

import java.io.Serializable;
import java.util.Objects;
import com.zf.gidi.domain.enumeration.BookStatus;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the Book entity. This class is used in BookResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /books?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BookCriteria implements Serializable {
    /**
     * Class for filtering BookStatus
     */
    public static class BookStatusFilter extends Filter<BookStatus> {
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter description;

    private StringFilter isbn;

    private StringFilter author;

    private StringFilter summary;

    private LocalDateFilter datePublished;

    private BookStatusFilter status;

    public BookCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getIsbn() {
        return isbn;
    }

    public void setIsbn(StringFilter isbn) {
        this.isbn = isbn;
    }

    public StringFilter getAuthor() {
        return author;
    }

    public void setAuthor(StringFilter author) {
        this.author = author;
    }

    public StringFilter getSummary() {
        return summary;
    }

    public void setSummary(StringFilter summary) {
        this.summary = summary;
    }

    public LocalDateFilter getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(LocalDateFilter datePublished) {
        this.datePublished = datePublished;
    }

    public BookStatusFilter getStatus() {
        return status;
    }

    public void setStatus(BookStatusFilter status) {
        this.status = status;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BookCriteria that = (BookCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(description, that.description) &&
            Objects.equals(isbn, that.isbn) &&
            Objects.equals(author, that.author) &&
            Objects.equals(summary, that.summary) &&
            Objects.equals(datePublished, that.datePublished) &&
            Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        title,
        description,
        isbn,
        author,
        summary,
        datePublished,
        status
        );
    }

    @Override
    public String toString() {
        return "BookCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (isbn != null ? "isbn=" + isbn + ", " : "") +
                (author != null ? "author=" + author + ", " : "") +
                (summary != null ? "summary=" + summary + ", " : "") +
                (datePublished != null ? "datePublished=" + datePublished + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
            "}";
    }

}
