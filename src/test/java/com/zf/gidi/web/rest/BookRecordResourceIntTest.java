package com.zf.gidi.web.rest;

import com.zf.gidi.GidiLibraryApp;

import com.zf.gidi.domain.BookRecord;
import com.zf.gidi.domain.User;
import com.zf.gidi.domain.Book;
import com.zf.gidi.repository.BookRecordRepository;
import com.zf.gidi.repository.search.BookRecordSearchRepository;
import com.zf.gidi.service.BookRecordService;
import com.zf.gidi.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;


import static com.zf.gidi.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.zf.gidi.domain.enumeration.BookRecordStatus;
/**
 * Test class for the BookRecordResource REST controller.
 *
 * @see BookRecordResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GidiLibraryApp.class)
public class BookRecordResourceIntTest {

    private static final String DEFAULT_INTENTION = "AAAAAAAAAA";
    private static final String UPDATED_INTENTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_BORROWED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BORROWED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_RETURN_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RETURN_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final BookRecordStatus DEFAULT_STATUS = BookRecordStatus.RETURNED;
    private static final BookRecordStatus UPDATED_STATUS = BookRecordStatus.READING;

    @Autowired
    private BookRecordRepository bookRecordRepository;
    
    @Autowired
    private BookRecordService bookRecordService;

    /**
     * This repository is mocked in the com.zf.gidi.repository.search test package.
     *
     * @see com.zf.gidi.repository.search.BookRecordSearchRepositoryMockConfiguration
     */
    @Autowired
    private BookRecordSearchRepository mockBookRecordSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBookRecordMockMvc;

    private BookRecord bookRecord;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BookRecordResource bookRecordResource = new BookRecordResource(bookRecordService);
        this.restBookRecordMockMvc = MockMvcBuilders.standaloneSetup(bookRecordResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookRecord createEntity(EntityManager em) {
        BookRecord bookRecord = new BookRecord()
            .intention(DEFAULT_INTENTION)
            .borrowedAt(DEFAULT_BORROWED_AT)
            .returnDate(DEFAULT_RETURN_DATE)
            .status(DEFAULT_STATUS);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        bookRecord.setUser(user);
        // Add required entity
        Book book = BookResourceIntTest.createEntity(em);
        em.persist(book);
        em.flush();
        bookRecord.setBook(book);
        return bookRecord;
    }

    @Before
    public void initTest() {
        bookRecord = createEntity(em);
    }

    @Test
    @Transactional
    public void createBookRecord() throws Exception {
        int databaseSizeBeforeCreate = bookRecordRepository.findAll().size();

        // Create the BookRecord
        restBookRecordMockMvc.perform(post("/api/book-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookRecord)))
            .andExpect(status().isCreated());

        // Validate the BookRecord in the database
        List<BookRecord> bookRecordList = bookRecordRepository.findAll();
        assertThat(bookRecordList).hasSize(databaseSizeBeforeCreate + 1);
        BookRecord testBookRecord = bookRecordList.get(bookRecordList.size() - 1);
        assertThat(testBookRecord.getIntention()).isEqualTo(DEFAULT_INTENTION);
        assertThat(testBookRecord.getBorrowedAt()).isEqualTo(DEFAULT_BORROWED_AT);
        assertThat(testBookRecord.getReturnDate()).isEqualTo(DEFAULT_RETURN_DATE);
        assertThat(testBookRecord.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the BookRecord in Elasticsearch
        verify(mockBookRecordSearchRepository, times(1)).save(testBookRecord);
    }

    @Test
    @Transactional
    public void createBookRecordWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bookRecordRepository.findAll().size();

        // Create the BookRecord with an existing ID
        bookRecord.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookRecordMockMvc.perform(post("/api/book-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookRecord)))
            .andExpect(status().isBadRequest());

        // Validate the BookRecord in the database
        List<BookRecord> bookRecordList = bookRecordRepository.findAll();
        assertThat(bookRecordList).hasSize(databaseSizeBeforeCreate);

        // Validate the BookRecord in Elasticsearch
        verify(mockBookRecordSearchRepository, times(0)).save(bookRecord);
    }

    @Test
    @Transactional
    public void checkIntentionIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookRecordRepository.findAll().size();
        // set the field null
        bookRecord.setIntention(null);

        // Create the BookRecord, which fails.

        restBookRecordMockMvc.perform(post("/api/book-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookRecord)))
            .andExpect(status().isBadRequest());

        List<BookRecord> bookRecordList = bookRecordRepository.findAll();
        assertThat(bookRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkReturnDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookRecordRepository.findAll().size();
        // set the field null
        bookRecord.setReturnDate(null);

        // Create the BookRecord, which fails.

        restBookRecordMockMvc.perform(post("/api/book-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookRecord)))
            .andExpect(status().isBadRequest());

        List<BookRecord> bookRecordList = bookRecordRepository.findAll();
        assertThat(bookRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookRecordRepository.findAll().size();
        // set the field null
        bookRecord.setStatus(null);

        // Create the BookRecord, which fails.

        restBookRecordMockMvc.perform(post("/api/book-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookRecord)))
            .andExpect(status().isBadRequest());

        List<BookRecord> bookRecordList = bookRecordRepository.findAll();
        assertThat(bookRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBookRecords() throws Exception {
        // Initialize the database
        bookRecordRepository.saveAndFlush(bookRecord);

        // Get all the bookRecordList
        restBookRecordMockMvc.perform(get("/api/book-records?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].intention").value(hasItem(DEFAULT_INTENTION.toString())))
            .andExpect(jsonPath("$.[*].borrowedAt").value(hasItem(DEFAULT_BORROWED_AT.toString())))
            .andExpect(jsonPath("$.[*].returnDate").value(hasItem(DEFAULT_RETURN_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
    
    @Test
    @Transactional
    public void getBookRecord() throws Exception {
        // Initialize the database
        bookRecordRepository.saveAndFlush(bookRecord);

        // Get the bookRecord
        restBookRecordMockMvc.perform(get("/api/book-records/{id}", bookRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bookRecord.getId().intValue()))
            .andExpect(jsonPath("$.intention").value(DEFAULT_INTENTION.toString()))
            .andExpect(jsonPath("$.borrowedAt").value(DEFAULT_BORROWED_AT.toString()))
            .andExpect(jsonPath("$.returnDate").value(DEFAULT_RETURN_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBookRecord() throws Exception {
        // Get the bookRecord
        restBookRecordMockMvc.perform(get("/api/book-records/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBookRecord() throws Exception {
        // Initialize the database
        bookRecordService.save(bookRecord);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockBookRecordSearchRepository);

        int databaseSizeBeforeUpdate = bookRecordRepository.findAll().size();

        // Update the bookRecord
        BookRecord updatedBookRecord = bookRecordRepository.findById(bookRecord.getId()).get();
        // Disconnect from session so that the updates on updatedBookRecord are not directly saved in db
        em.detach(updatedBookRecord);
        updatedBookRecord
            .intention(UPDATED_INTENTION)
            .borrowedAt(UPDATED_BORROWED_AT)
            .returnDate(UPDATED_RETURN_DATE)
            .status(UPDATED_STATUS);

        restBookRecordMockMvc.perform(put("/api/book-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBookRecord)))
            .andExpect(status().isOk());

        // Validate the BookRecord in the database
        List<BookRecord> bookRecordList = bookRecordRepository.findAll();
        assertThat(bookRecordList).hasSize(databaseSizeBeforeUpdate);
        BookRecord testBookRecord = bookRecordList.get(bookRecordList.size() - 1);
        assertThat(testBookRecord.getIntention()).isEqualTo(UPDATED_INTENTION);
        assertThat(testBookRecord.getBorrowedAt()).isEqualTo(UPDATED_BORROWED_AT);
        assertThat(testBookRecord.getReturnDate()).isEqualTo(UPDATED_RETURN_DATE);
        assertThat(testBookRecord.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the BookRecord in Elasticsearch
        verify(mockBookRecordSearchRepository, times(1)).save(testBookRecord);
    }

    @Test
    @Transactional
    public void updateNonExistingBookRecord() throws Exception {
        int databaseSizeBeforeUpdate = bookRecordRepository.findAll().size();

        // Create the BookRecord

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookRecordMockMvc.perform(put("/api/book-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookRecord)))
            .andExpect(status().isBadRequest());

        // Validate the BookRecord in the database
        List<BookRecord> bookRecordList = bookRecordRepository.findAll();
        assertThat(bookRecordList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BookRecord in Elasticsearch
        verify(mockBookRecordSearchRepository, times(0)).save(bookRecord);
    }

    @Test
    @Transactional
    public void deleteBookRecord() throws Exception {
        // Initialize the database
        bookRecordService.save(bookRecord);

        int databaseSizeBeforeDelete = bookRecordRepository.findAll().size();

        // Get the bookRecord
        restBookRecordMockMvc.perform(delete("/api/book-records/{id}", bookRecord.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<BookRecord> bookRecordList = bookRecordRepository.findAll();
        assertThat(bookRecordList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the BookRecord in Elasticsearch
        verify(mockBookRecordSearchRepository, times(1)).deleteById(bookRecord.getId());
    }

    @Test
    @Transactional
    public void searchBookRecord() throws Exception {
        // Initialize the database
        bookRecordService.save(bookRecord);
        when(mockBookRecordSearchRepository.search(queryStringQuery("id:" + bookRecord.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(bookRecord), PageRequest.of(0, 1), 1));
        // Search the bookRecord
        restBookRecordMockMvc.perform(get("/api/_search/book-records?query=id:" + bookRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].intention").value(hasItem(DEFAULT_INTENTION.toString())))
            .andExpect(jsonPath("$.[*].borrowedAt").value(hasItem(DEFAULT_BORROWED_AT.toString())))
            .andExpect(jsonPath("$.[*].returnDate").value(hasItem(DEFAULT_RETURN_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookRecord.class);
        BookRecord bookRecord1 = new BookRecord();
        bookRecord1.setId(1L);
        BookRecord bookRecord2 = new BookRecord();
        bookRecord2.setId(bookRecord1.getId());
        assertThat(bookRecord1).isEqualTo(bookRecord2);
        bookRecord2.setId(2L);
        assertThat(bookRecord1).isNotEqualTo(bookRecord2);
        bookRecord1.setId(null);
        assertThat(bookRecord1).isNotEqualTo(bookRecord2);
    }
}
