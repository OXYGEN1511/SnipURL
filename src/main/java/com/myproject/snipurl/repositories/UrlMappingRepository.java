
package com.myproject.snipurl.repositories;

import com.myproject.snipurl.models.SnipUser;
import com.myproject.snipurl.models.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {

    // Existing methods
    UrlMapping findBySnipUrl(String snipUrl);

    List<UrlMapping> findByUser(SnipUser user);

    // Soft-delete methods

    // Get only active URLs for a user
    List<UrlMapping> findByUserAndDeletedFalse(SnipUser user);

    // Find an active URL belonging to a specific user
    Optional<UrlMapping> findBySnipUrlAndUserAndDeletedFalse(
            String snipUrl, SnipUser user);

    // Find an active URL for redirection
    Optional<UrlMapping> findBySnipUrlAndDeletedFalse(String snipUrl);
}
