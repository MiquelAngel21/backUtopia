package com.utopiapp.demo.dto;

public class ClubWithAddressDto {
    private ClubDto club;
    private AddressDto address;

    public ClubDto getClub() {
        return club;
    }

    public void setClub(ClubDto club) {
        this.club = club;
    }

    public AddressDto getAddress() {
        return address;
    }

    public void setAddress(AddressDto address) {
        this.address = address;
    }
}
