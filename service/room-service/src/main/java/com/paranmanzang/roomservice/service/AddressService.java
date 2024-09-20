package com.paranmanzang.roomservice.service;


import com.paranmanzang.roomservice.model.domain.AddressModel;
import com.paranmanzang.roomservice.model.domain.AddressUpdateModel;

import java.util.List;

public interface AddressService {
    Boolean save(AddressModel model);

    Boolean update(AddressUpdateModel model);

    Boolean delete(Long id);

    AddressModel findById(Long id);

    List<?> findAll();
}
