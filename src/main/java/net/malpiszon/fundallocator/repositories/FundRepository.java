package net.malpiszon.fundallocator.repositories;

import java.util.Collection;
import java.util.List;

import net.malpiszon.fundallocator.models.Fund;
import net.malpiszon.fundallocator.models.FundType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FundRepository extends JpaRepository<Fund, Long> {
    List<Fund> findByFundTypeAndIdIn(FundType fundType, Collection<Long> ids);
}
