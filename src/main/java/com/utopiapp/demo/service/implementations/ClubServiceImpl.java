package com.utopiapp.demo.service.implementations;

import com.utopiapp.demo.dto.AddressDto;
import com.utopiapp.demo.dto.ClubDto;
import com.utopiapp.demo.dto.ClubWithAddressDto;
import com.utopiapp.demo.dto.FileDto;
import com.utopiapp.demo.model.*;
import com.utopiapp.demo.repositories.mysql.AddressRepo;
import com.utopiapp.demo.repositories.mysql.ClubRepo;
import com.utopiapp.demo.repositories.mysql.FileRepo;
import com.utopiapp.demo.service.interfaces.AddressService;
import com.utopiapp.demo.service.interfaces.ClientService;
import com.utopiapp.demo.service.interfaces.ClubService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ClubServiceImpl implements ClubService {

    private final ClubRepo clubRepo;
    private final FileRepo fileRepo;
    private final AddressRepo addressRepo;
    private final ClientService clientService;

    public ClubServiceImpl(ClubRepo clubRepo, FileRepo fileRepo, AddressRepo addressRepo, ClientService clientService) {
        this.clubRepo = clubRepo;
        this.fileRepo = fileRepo;
        this.addressRepo = addressRepo;
        this.clientService = clientService;
    }

    @Override
    public Club createClub(ClubWithAddressDto clubWithAddressDto, Client currentClient) {
        ClubDto clubDto = clubWithAddressDto.getClub();
        Club club = new Club();
        club.setCif(clubDto.getCif());
        club.setCreateDate(LocalDate.now());
        club.setAccessCode(createAccessCode());
        club.setEmail(clubDto.getEmail());
        club.setOrganization(clubDto.getOrganization());
        club.setWhoAreWe(clubDto.getWhoAreWe());
        club.setName(clubDto.getName());

        club.setActivitySheets(new HashSet<>());
        club.setPetitions(new HashSet<>());

        Set<Client> clients = new HashSet<>();
        clients.add(currentClient);

        club.setClients(clients);

        club.setAddress(createAddressByAddressDto(clubWithAddressDto.getAddress()));

        clubRepo.save(club);

        currentClient.setClub(club);

        clientService.save(currentClient);

        Set<File> clubFiles = handleImages(clubDto.getLogo(), clubDto.getFrontPageFile(), club);

        club.setFiles(clubFiles);
        return club;
    }

    @Override
    public Map<String, Object> getPaginatedClubs(int start, int length) {
        Pageable paging = PageRequest.of(start / length, length);
        Page<Club> pageResult;

        pageResult = clubRepo.findAll(paging);

        long total = pageResult.getTotalElements();
        List<Map<String, Object>> data = convertClubListIntoJson(pageResult.getContent());

        Map<String, Object> json = new HashMap<>();
        json.put("data", data);
        json.put("recordsTotal", total);
        return json;
    }

    @Override
    public String getClubNameByClient(Client currentClient) {
        Set<Client> clients = new HashSet<>();
        clients.add(currentClient);
        Club club = clubRepo.findClubByClientsIn(clients);
        if (club != null){
            return club.getName();
        }
        return null;
    }

    private List<Map<String, Object>> convertClubListIntoJson(List<Club> clubs) {
        List<Map<String, Object>> allClubsInJsonFormat = new ArrayList<>();

        for (Club club : clubs){
            Map<String, Object> clubInFormatJson = convertClubToMap(club);
            allClubsInJsonFormat.add(clubInFormatJson);
        }

        return allClubsInJsonFormat;
    }

    private Map<String, Object> convertClubToMap(Club club) {
        Map<String, Object> clubInFormatJson = new HashMap<>();

        clubInFormatJson.put("id", club.getId());
        clubInFormatJson.put("name", club.getName());
        clubInFormatJson.put("cif", club.getCif());
        clubInFormatJson.put("email", club.getEmail());
        clubInFormatJson.put("createdDate", club.getCreateDate());
        clubInFormatJson.put("whoAreWe", club.getWhoAreWe());
        clubInFormatJson.put("organization", club.getOrganization());
        clubInFormatJson.put("accessCode", club.getAccessCode());
        clubInFormatJson.put("clients", clientService.getListOfClientsInJsonFormat(club.getClients()));
//        clubInFormatJson.put("files", arrayOfBase64Images(club.getFiles()));

        return clubInFormatJson;
    }

    /*private Object arrayOfBase64Images(Set<File> files) {

    }*/

    private Address createAddressByAddressDto(AddressDto addressDto) {
        Address address = new Address();
        address.setCity(addressDto.getCity());
        address.setNumber(addressDto.getNumber());
        address.setStreet(addressDto.getStreet());
        address.setZipCode(addressDto.getZipCode());

        return addressRepo.save(address);
    }

    private Set<File> handleImages(FileDto logoDto, FileDto frontPageFileDto, Club club) {
        logoDto.setName("logo_"+logoDto.getName());
        frontPageFileDto.setName("frontPage_"+frontPageFileDto.getName());

        File logo = fileDtoIntoFile(logoDto);
        logo.setClub(club);

        File frontPage = fileDtoIntoFile(frontPageFileDto);
        frontPage.setClub(club);

        Set<File> files = new HashSet<>();
        files.add(logo);
        files.add(frontPage);

        fileRepo.saveAll(files);
        return files;
    }

    private File fileDtoIntoFile(FileDto fileDto) {
        File file = new File();
        file.setContent(Base64.getDecoder().decode(fileDto.getContent()));
        file.setName(fileDto.getName());
        file.setMediaType(fileDto.getMediaType());
        return file;
    }

    private String createAccessCode() {
        String accessCode = "";

        for (int i = 0; i < 10; i++) {
            Random choosePossibility = new Random();
            int choosePossibilityInt = choosePossibility.nextInt(2);
            if (choosePossibilityInt == 1) {
                int charPosition = choosePossibility.nextInt(25);
                accessCode += (char) (charPosition+65);
            } else {
                int charPosition = choosePossibility.nextInt(10);
                accessCode += (char) (charPosition+48);
            }
            if (i == 9 && clubRepo.existsByAccessCode(accessCode)){
                i = -1;
                accessCode = "";
            }
        }
        return accessCode;
    }
}
