package com.myproject.snipurl.repositories;

import com.myproject.snipurl.models.SnipUser;
import com.myproject.snipurl.models.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {

    UrlMapping findBySnipUrl(String snipUrl);

    List<UrlMapping> findByUser(SnipUser user);
}
