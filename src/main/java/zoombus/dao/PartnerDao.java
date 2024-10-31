package zoombus.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import zoombus.entity.PartnerEntity;
import zoombus.entity.PassengerEntity;

import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface PartnerDao extends JpaRepository<PartnerEntity,String> {
    PartnerEntity getPartnerEntityById(String id);
    Optional<PartnerEntity>findByPhoneNumber(String phoneNumber);
}
