package com.utopiapp.demo.service.interfaces;

import com.utopiapp.demo.model.Address;

import java.util.Map;

public interface AddressService {
    Map<String, Object> addressToJsonFormat(Address address);
}
