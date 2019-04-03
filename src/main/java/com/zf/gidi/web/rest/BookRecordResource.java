package com.zf.gidi.web.rest;

import com.zf.gidi.domain.BookRecord;
import com.zf.gidi.service.BookRecordService;
import com.zf.gidi.web.rest.errors.BadRequestAlertException;
import com.zf.gidi.web.rest.util.HeaderUtil;
import com.zf.gidi.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing BookRecord.
 */
@RestController
@RequestMapping("/api")
public class BookRecordResource {

    private final Logger log = LoggerFactory.getLogger(BookRecordResource.class);

    private static final String ENTITY_NAME = "bookRecord";

    private final BookRecordService bookRecordService;

    public BookRecordResource(BookRecordService bookRecordService) {
        this.bookRecordService = bookRecordService;
    }

    /**
     * POST  /book-records : Create a new bookRecord.
     *
     * @param bookRecord the bookRecord to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bookRecord, or with status 400 (Bad Request) if the bookRecord has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/book-records")
    public ResponseEntity<BookRecord> createBookRecord(@Valid @RequestBody BookRecord bookRecord) throws URISyntaxException {
        log.debug("REST request to save BookRecord : {}", bookRecord);
        if (bookRecord.getId() != null) {
            throw new BadRequestAlertException("A new bookRecord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BookRecord result = bookRecordService.save(bookRecord);
        return ResponseEntity.created(new URI("/api/book-records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /book-records : Updates an existing bookRecord.
     *
     * @param bookRecord the bookRecord to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bookRecord,
     * or with status 400 (Bad Request) if the bookRecord is not valid,
     * or with status 500 (Internal Server Error) if the bookRecord couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/book-records")
    public ResponseEntity<BookRecord> updateBookRecord(@Valid @RequestBody BookRecord bookRecord) throws URISyntaxException {
        log.debug("REST request to update BookRecord : {}", bookRecord);
        if (bookRecord.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BookRecord result = bookRecordService.save(bookRecord);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bookRecord.getId().toString()))
            .body(result);
    }

    /**
     * GET  /book-records : get all the bookRecords.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bookRecords in body
     */
    @GetMapping("/book-records")
    public ResponseEntity<List<BookRecord>> getAllBookRecords(Pageable pageable) {
        log.debug("REST request to get a page of BookRecords");
        Page<BookRecord> page = bookRecordService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/book-records");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /book-records/:id : get the "id" bookRecord.
     *
     * @param id the id of the bookRecord to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bookRecord, or with status 404 (Not Found)
     */
    @GetMapping("/book-records/{id}")
    public ResponseEntity<BookRecord> getBookRecord(@PathVariable Long id) {
        log.debug("REST request to get BookRecord : {}", id);
        Optional<BookRecord> bookRecord = bookRecordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bookRecord);
    }

    /**
     * DELETE  /book-records/:id : delete the "id" bookRecord.
     *
     * @param id the id of the bookRecord to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/book-records/{id}")
    public ResponseEntity<Void> deleteBookRecord(@PathVariable Long id) {
        log.debug("REST request to delete BookRecord : {}", id);
        bookRecordService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/book-records?query=:query : search for the bookRecord corresponding
     * to the query.
     *
     * @param query the query of the bookRecord search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/book-records")
    public ResponseEntity<List<BookRecord>> searchBookRecords(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of BookRecords for query {}", query);
        Page<BookRecord> page = bookRecordService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/book-records");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
