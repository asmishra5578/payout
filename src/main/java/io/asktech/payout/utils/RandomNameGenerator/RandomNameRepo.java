package io.asktech.payout.utils.RandomNameGenerator;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RandomNameRepo extends JpaRepository<RandomNameModel, String> {
    
    @Query(value = "select name from RandomNameModel order by RAND() limit 1", nativeQuery = true)
    String getRandomName();

}
