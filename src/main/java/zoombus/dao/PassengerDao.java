package zoombus.dao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zoombus.entity.PassengerEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface PassengerDao extends  JpaRepository<PassengerEntity,String>{
     PassengerEntity getPassengerEntityById(String id);
     Optional<PassengerEntity> findByEmailOrPhoneNumber(String email,String phoneNumber);
}
