package com.pjatk.nbp.project.repository;

import com.pjatk.nbp.project.model.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoggingRepository extends JpaRepository<Entry, Long> {

    Entry save(Entry e);
}
