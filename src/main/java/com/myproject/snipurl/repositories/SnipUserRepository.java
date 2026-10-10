package com.myproject.snipurl.repositories;

import com.myproject.snipurl.models.SnipUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SnipUserRepository extends JpaRepository<SnipUser,Long> {

    Optional<SnipUser> findByUsername(String username);

}
