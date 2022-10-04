package com.example.objectdesign.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DateObjectRepository extends JpaRepository<DateObject, Long> {
}
