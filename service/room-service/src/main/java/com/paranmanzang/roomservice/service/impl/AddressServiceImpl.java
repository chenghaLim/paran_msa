package com.paranmanzang.roomservice.service.impl;


import com.paranmanzang.roomservice.model.domain.AddressModel;
import com.paranmanzang.roomservice.model.domain.AddressUpdateModel;
import com.paranmanzang.roomservice.model.entity.Address;
import com.paranmanzang.roomservice.model.repository.AddressRepository;
import com.paranmanzang.roomservice.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;

    @Override
    public Boolean save(AddressModel model) {
        return addressRepository.save(Address.builder()
                .address(model.getAddress())
                .longitude(model.getLongitude())
                .latitude(model.getLatitude())
                .detailAddress(model.getDetailAddress())
                .roomId(model.getRoomId())
                .build()) == null ? Boolean.FALSE : Boolean.TRUE;
    }

    @Override
    public Boolean update(AddressUpdateModel model) {
        if (addressRepository.findById(model.getId()).isEmpty()) {
            return Boolean.FALSE;
        }
        return addressRepository.findById(model.getId()).map(address -> {
            address.setAddress(model.getAddress());
            address.setDetailAddress(model.getDetailAddress());
            address.setLongitude(model.getLongitude());
            address.setLatitude(model.getLatitude());
            return addressRepository.save(address);
        }) == null ? Boolean.FALSE : Boolean.TRUE;
    }

    @Override
    public Boolean delete(Long id) {
        addressRepository.delete(Address.builder().id(id).build());
        return addressRepository.findById(id).isEmpty();
    }

    @Override
    public AddressModel findById(Long id) {
        return addressRepository.findById(id).map(address ->
                new AddressModel(address.getId(), address.getAddress(), address.getDetailAddress(), address.getLatitude(), address.getLongitude(), address.getRoomId())
        ).orElse(null);
    }

    @Override
    public List<?> findAll() {
        return addressRepository.findAll().stream().map(address ->
                new AddressModel(address.getId(), address.getAddress(), address.getDetailAddress(), address.getLatitude(), address.getLongitude(), address.getRoomId())
        ).toList();
    }
}
