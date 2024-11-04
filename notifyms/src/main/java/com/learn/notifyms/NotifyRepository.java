package com.learn.notifyms;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotifyRepository extends JpaRepository<NotifyEntity,Long> {

    List<NotifyEntity> findByCompanyId(Long companyId);
}
