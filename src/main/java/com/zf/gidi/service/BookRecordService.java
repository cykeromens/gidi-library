package com.zf.gidi.service;

import com.zf.gidi.domain.BookRecord;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing BookRecord.
 */
public interface BookRecordService {

    /**
     * Save a bookRecord.
     *
     * @param bookRecord the entity to save
     * @return the persisted entity
     */
    BookRecord save(BookRecord bookRecord);

    /**
     * Get all the bookRecords.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<BookRecord> findAll(Pageable pageable);


    /**
     * Get the "id" bookRecord.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<BookRecord> findOne(Long id);

    /**
     * Delete the "id" bookRecord.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the bookRecord corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<BookRecord> search(String query, Pageable pageable);
}
