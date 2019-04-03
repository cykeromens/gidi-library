package com.zf.gidi.repository.search;

import com.zf.gidi.domain.BookRecord;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the BookRecord entity.
 */
public interface BookRecordSearchRepository extends ElasticsearchRepository<BookRecord, Long> {
}
