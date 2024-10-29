package zoombus.dao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zoombus.entity.PassengerEntity;
@Repository
public interface PassengerDao extends  JpaRepository<PassengerEntity,String>{
     PassengerEntity getPassengerEntityById(String id);
}
