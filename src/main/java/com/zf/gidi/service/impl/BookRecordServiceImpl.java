package com.zf.gidi.service.impl;

import com.zf.gidi.service.BookRecordService;
import com.zf.gidi.domain.BookRecord;
import com.zf.gidi.repository.BookRecordRepository;
import com.zf.gidi.repository.search.BookRecordSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing BookRecord.
 */
@Service
@Transactional
public class BookRecordServiceImpl implements BookRecordService {

    private final Logger log = LoggerFactory.getLogger(BookRecordServiceImpl.class);

    private final BookRecordRepository bookRecordRepository;

    private final BookRecordSearchRepository bookRecordSearchRepository;

    public BookRecordServiceImpl(BookRecordRepository bookRecordRepository, BookRecordSearchRepository bookRecordSearchRepository) {
        this.bookRecordRepository = bookRecordRepository;
        this.bookRecordSearchRepository = bookRecordSearchRepository;
    }

    /**
     * Save a bookRecord.
     *
     * @param bookRecord the entity to save
     * @return the persisted entity
     */
    @Override
    public BookRecord save(BookRecord bookRecord) {
        log.debug("Request to save BookRecord : {}", bookRecord);
        BookRecord result = bookRecordRepository.save(bookRecord);
        bookRecordSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the bookRecords.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BookRecord> findAll(Pageable pageable) {
        log.debug("Request to get all BookRecords");
        return bookRecordRepository.findAll(pageable);
    }


    /**
     * Get one bookRecord by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<BookRecord> findOne(Long id) {
        log.debug("Request to get BookRecord : {}", id);
        return bookRecordRepository.findById(id);
    }

    /**
     * Delete the bookRecord by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete BookRecord : {}", id);
        bookRecordRepository.deleteById(id);
        bookRecordSearchRepository.deleteById(id);
    }

    /**
     * Search for the bookRecord corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BookRecord> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of BookRecords for query {}", query);
        return bookRecordSearchRepository.search(queryStringQuery(query), pageable);    }
}
