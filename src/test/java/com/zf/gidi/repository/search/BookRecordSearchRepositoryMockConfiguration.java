package com.zf.gidi.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of BookRecordSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class BookRecordSearchRepositoryMockConfiguration {

    @MockBean
    private BookRecordSearchRepository mockBookRecordSearchRepository;

}
