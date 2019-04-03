package com.zf.gidi.repository;

import com.zf.gidi.domain.BookRecord;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the BookRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BookRecordRepository extends JpaRepository<BookRecord, Long> {

    @Query("select book_record from BookRecord book_record where book_record.user.login = ?#{principal.username}")
    List<BookRecord> findByUserIsCurrentUser();

}
