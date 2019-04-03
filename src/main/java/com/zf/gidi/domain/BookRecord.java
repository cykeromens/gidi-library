package com.zf.gidi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import com.zf.gidi.domain.enumeration.BookRecordStatus;

/**
 * A BookRecord.
 */
@Entity
@Table(name = "book_record")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "bookrecord")
public class BookRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "intention", nullable = false)
    private String intention;

    @Column(name = "borrowed_at")
    private LocalDate borrowedAt;

    @NotNull
    @Column(name = "return_date", nullable = false)
    private LocalDate returnDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookRecordStatus status;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private User user;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private Book book;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIntention() {
        return intention;
    }

    public BookRecord intention(String intention) {
        this.intention = intention;
        return this;
    }

    public void setIntention(String intention) {
        this.intention = intention;
    }

    public LocalDate getBorrowedAt() {
        return borrowedAt;
    }

    public BookRecord borrowedAt(LocalDate borrowedAt) {
        this.borrowedAt = borrowedAt;
        return this;
    }

    public void setBorrowedAt(LocalDate borrowedAt) {
        this.borrowedAt = borrowedAt;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public BookRecord returnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
        return this;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public BookRecordStatus getStatus() {
        return status;
    }

    public BookRecord status(BookRecordStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(BookRecordStatus status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public BookRecord user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public BookRecord book(Book book) {
        this.book = book;
        return this;
    }

    public void setBook(Book book) {
        this.book = book;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BookRecord bookRecord = (BookRecord) o;
        if (bookRecord.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), bookRecord.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BookRecord{" +
            "id=" + getId() +
            ", intention='" + getIntention() + "'" +
            ", borrowedAt='" + getBorrowedAt() + "'" +
            ", returnDate='" + getReturnDate() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
