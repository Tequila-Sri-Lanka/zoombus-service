package zoombus.util;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zoombus.dao.PassengerDao;
import zoombus.dto.PassengerDTO;
import zoombus.entity.PassengerEntity;

import java.util.List;

@Component
public class Mapping {
    @Autowired
    private ModelMapper modelMapper;
    public PassengerEntity convertToPassengerEntity(PassengerDTO passenger) {
        return modelMapper.map(passenger, PassengerEntity.class);
    }
    public PassengerDTO convertToPassengerDTO(PassengerEntity passenger) {
        return modelMapper.map(passenger, PassengerDTO.class);
    }
    public List<PassengerDTO> convertPassengerToDTOList(List<PassengerEntity> passengerEntities) {
        return modelMapper.map(passengerEntities, new TypeToken<List<PassengerDTO>>() {}.getType());
    }
}
