package com.utopiapp.demo.service.implementations;

import com.utopiapp.demo.dto.*;
import com.utopiapp.demo.exceptions.*;
import com.utopiapp.demo.model.*;
import com.utopiapp.demo.repositories.mysql.*;
import com.utopiapp.demo.service.interfaces.ClientService;
import com.utopiapp.demo.service.interfaces.ClubService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ClubServiceImpl implements ClubService {

    private final ClubRepo clubRepo;
    private final FileRepo fileRepo;
    private final PetitionRepo petitionRepo;
    private final AddressRepo addressRepo;
    private final ClientService clientService;
    private final CoordinatorRepo coordinatorRepo;

    public ClubServiceImpl(ClubRepo clubRepo, FileRepo fileRepo, PetitionRepo petitionRepo, AddressRepo addressRepo, ClientService clientService, CoordinatorRepo coordinatorRepo) {
        this.clubRepo = clubRepo;
        this.fileRepo = fileRepo;
        this.petitionRepo = petitionRepo;
        this.addressRepo = addressRepo;
        this.clientService = clientService;
        this.coordinatorRepo = coordinatorRepo;
    }

    @Override
    public Club createClub(ClubWithAddressDto clubWithAddressDto, Client currentClient) {
        Set<Client> clients = new HashSet<>();
        clients.add(currentClient);
        if (clubRepo.findClubByVolunteersIn(clients) == null){
            ClubDto clubDto = clubWithAddressDto.getClub();
            Club club = new Club();
            club.setCif(clubDto.getCif());
            club.setCreateDate(LocalDate.now());
            club.setAccessCode(createAccessCode());
            club.setEmail(clubDto.getEmail());
            club.setOrganization(clubDto.getOrganization());
            club.setWhoAreWe(clubDto.getWhoAreWe());
            club.setName(clubDto.getName());

            club.setPetitions(new HashSet<>());

            club.setAddress(createAddressByAddressDto(clubWithAddressDto.getAddress()));
            club.setVolunteers(new HashSet<>());

            clubRepo.save(club);

            currentClient.setClub(club);

            clientService.save(currentClient);

            Set<File> clubFiles = handleImages(clubDto.getLogo(), clubDto.getFrontPageFile(), club);
            club.setFiles(clubFiles);

            Coordinator coordinator = new Coordinator(club, currentClient);
            coordinatorRepo.save(coordinator);
            return club;
        }

        throw new AlreadyInAClubException();
    }

    @Override
    public Map<String, Object> getPaginatedClubs(String name, int start, int length) {
        Pageable paging = PageRequest.of(start / length, length);
        Page<Club> pageResult;

        if (name != null){
            pageResult = clubRepo.findAllByNameContainingOrderByName(name,paging);
        } else {
            pageResult = clubRepo.findAll(paging);
        }

        long total = pageResult.getTotalElements();
        List<Map<String, Object>> data = convertClubListIntoJson(pageResult.getContent());

        Map<String, Object> json = new HashMap<>();
        json.put("data", data);
        json.put("recordsTotal", total);
        return json;
    }

    @Override
    public Map<String, Object> getClubById(Long id) {
        return convertClubToMap(clubRepo.findClubById(id));
    }

    @Override
    public List<Map<String, Object>> getCoordinatorsByClub(Long clubId) {
        List<Coordinator> coordinators = coordinatorRepo.findAllByClub(clubRepo.findClubById(clubId));
        List<Map<String, Object>> coordinatorHashMapList = listOfCoordinatorsToMap(coordinators);

        return coordinatorHashMapList;
    }

    @Override
    public List<Map<String, Object>> getMonitorsByClub(Long clubId) {
        Club club = clubRepo.findClubById(clubId);
        List<Client> monitors = clientService.getAllClientsByClub(club);

        return clientService.getListOfClientsInJsonFormat(monitors);
    }

    @Override
    public Map<String, Object> getClubByCoordinator(Long clientId) {
        Coordinator coordinator = coordinatorRepo.findCoordinatorByPerson_id(clientId);
        Set<Coordinator> coordinators = new HashSet<>();
        coordinators.add(coordinator);
        Club club = clubRepo.findClubByCoordinatorsIn(coordinators);
        return convertClubToMap(club);
    }

    @Override
    public Map<String, Object> getPaginatedPetitionsByClub(Client client, Long clubId, int start, int length) {
        Pageable paging = PageRequest.of(start / length, length);
        Page<Petition> pageResult;
        Club club = clubRepo.findClubById(clubId);

        if (coordinatorRepo.existsCoordinatorByPersonAndClub(client, club)){
            pageResult = petitionRepo.findAllByClubOrderByCreatedDateDesc(club, paging);
        } else {
            throw new NotCoordinatorException();
        }

        long total = pageResult.getTotalElements();
        List<Map<String, Object>> data = listOfPetitionsToMap(pageResult.getContent());

        Map<String, Object> json = new HashMap<>();
        json.put("data", data);
        json.put("recordsTotal", total);
        return json;
    }

    @Override
    public Map<String,Object> acceptOrDenyPetitions(Client client, String newStatus, Long petitionId) {
        Status newStatusProp = choosePetitionStatus(newStatus);
        Petition petition = petitionRepo.findPetitionById(petitionId);
        if (petition.getStatus() == Status.ACCEPTED){
            throw new PetitionWasRejectedException();
        }
        if (!petitionContainsOneVolunteerAlreadyInClub(petition)) {
            if (client.getCoordinator().getClub().equals(petition.getClub())) {
                if (newStatusProp == Status.ACCEPTED) {
                    Petition newPetition = addUserToClub(client.getCoordinator().getClub(), petition, newStatusProp);
                    return petitionToMap(newPetition);
                } else if (newStatusProp == Status.DENIED) {
                    Petition newPetition = denyUserFromClub(petition, newStatusProp);
                    return petitionToMap(newPetition);
                }
            } else {
                throw new NotCoordinatorException();
            }
        } else {
            throw new AlreadyInAClubException();
        }
        return null;
    }

    @Override
    public Map<String, Object> ascentOrLowerAVolunteer(Client client, Long volunteerId) {
        Client volunteer = clientService.getClientById(volunteerId);
        if (client.getCoordinator().getClub().equals(volunteer.getClub())){
            if (volunteer.getCoordinator() != null){
                volunteer = lowerVolunteer(volunteer, client);
            } else {
                volunteer = ascentVolunteer(volunteer, client);
            }
        }
        return clientService.getClientInJsonFormat(volunteer);
    }

    private Client ascentVolunteer(Client volunteer, Client client) {
        Club club = client.getClub();
        Coordinator newCoordinator = new Coordinator();
        newCoordinator.setClub(club);
        newCoordinator.setPerson(volunteer);
        newCoordinator = coordinatorRepo.save(newCoordinator);

        volunteer.setCoordinator(newCoordinator);
        return clientService.save(volunteer);
    }

    private Client lowerVolunteer(Client volunteer, Client coordinatorParent) {
        Club club = coordinatorParent.getClub();
        if (club.getCoordinators().size() == 1){
            throw new AtLeastOneCoordinatorException();
        }
        Coordinator coordinator = coordinatorRepo.findCoordinatorByPerson_id(volunteer.getId());
        coordinatorRepo.delete(coordinator);
        volunteer.setCoordinator(null);
        return volunteer;
    }

    private Petition denyUserFromClub(Petition petition, Status newStatusProp) {
        petition.setStatus(newStatusProp);

        petitionRepo.save(petition);

        return petition;
    }

    private boolean petitionContainsOneVolunteerAlreadyInClub(Petition petition) {
        for (Client volunteer : petition.getClub().getVolunteers()){
            if (volunteer.equals(petition.getClient())){
                return true;
            }
        }
        return false;
    }

    private Petition addUserToClub(Club club, Petition petition, Status newStatus) {
        Set<Petition> petitions = club.getPetitions();
        petitions.remove(petition);
        petition.setStatus(newStatus);
        petitions.add(petition);

        club.setPetitions(petitions);
        Client clientOfPetition = petition.getClient();
        clientOfPetition.setClub(petition.getClub());

        petitionRepo.save(petition);
        clubRepo.save(club);
        clientService.save(clientOfPetition);

        return petition;
    }

    private Status choosePetitionStatus(String newStatus) {
        return switch (newStatus) {
            case "ACCEPTED" -> Status.ACCEPTED;
            case "DENIED" -> Status.DENIED;
            default -> Status.NONE;
        };
    }


    private Map<String, Object> coordinatorToCorrectMapping(Coordinator coordinator) {
        Map<String, Object> coordinatorHashMap = new HashMap<>();
        coordinatorHashMap.put("id", coordinator.getId());
        coordinatorHashMap.put("club", convertClubToMap(coordinator.getClub()));
        coordinatorHashMap.put("person", clientService.getClientInJsonFormat(coordinator.getPerson()));
        return coordinatorHashMap;
    }

    @Override
    public Map<String, Object> createNewPetition(Long clubId, DescriptionPetitionDto descriptionPetitionDto, Client currentClient) {
        Petition petition = new Petition();
        List<Petition> oldPetitions = petitionRepo.findAllByClientAndClubOrderByCreatedDateDesc(currentClient, clubRepo.findClubById(clubId));
        boolean timeExpiredToDoNewPetition = checkTimeOfLastPetition(oldPetitions);

        if (timeExpiredToDoNewPetition) {
            if (currentClient.getClub() == null){
                petition.setStatus(Status.PENDING);
                petition.setDescription(descriptionPetitionDto.getDescription());
                petition.setClub(clubRepo.findClubById(clubId));
                petition.setClient(currentClient);
                petition.setCreatedDate(LocalDateTime.now());

                petitionRepo.save(petition);
            } else {
                throw new AlreadyInAClubException();
            }
        } else {
            throw new PetitionTimeNotExpired();
        }
        return petitionToMap(petition);
    }

    private boolean checkTimeOfLastPetition(List<Petition> oldPetitions) {
        if (oldPetitions.size() > 0){
            LocalDateTime lastPetitionLocalDateTime = oldPetitions.get(0).getCreatedDate().plusDays(1);
            Calendar nextDayInCalendarFormat = Calendar.getInstance();
            nextDayInCalendarFormat.clear();
            nextDayInCalendarFormat.set(lastPetitionLocalDateTime.getYear(),lastPetitionLocalDateTime.getMonthValue(),
                    lastPetitionLocalDateTime.getDayOfMonth(),lastPetitionLocalDateTime.getHour(), lastPetitionLocalDateTime.getMinute(),
                    lastPetitionLocalDateTime.getSecond());
            long nextDayMilliseconds = nextDayInCalendarFormat.getTimeInMillis();
            return nextDayMilliseconds < Calendar.getInstance().getTimeInMillis();
        }
        return true;
    }

    private List<Map<String, Object>> convertClubListIntoJson(List<Club> clubs) {
        List<Map<String, Object>> allClubsInJsonFormat = new ArrayList<>();

        for (Club club : clubs){
            Map<String, Object> clubInFormatJson = convertClubToMap(club);
            allClubsInJsonFormat.add(clubInFormatJson);
        }

        return allClubsInJsonFormat;
    }

    @Override
    public Map<String, Object> convertClubToMap(Club club) {
        Map<String, Object> clubInFormatJson = new HashMap<>();

        clubInFormatJson.put("id", club.getId());
        clubInFormatJson.put("name", club.getName());
        clubInFormatJson.put("cif", club.getCif());
        clubInFormatJson.put("email", club.getEmail());
        clubInFormatJson.put("createdDate", club.getCreateDate());
        clubInFormatJson.put("whoAreWe", club.getWhoAreWe());
        clubInFormatJson.put("organization", club.getOrganization());
        clubInFormatJson.put("accessCode", club.getAccessCode());
        clubInFormatJson.put("volunteers", clientService.getListOfClientsInJsonFormat(club.getVolunteers()));
        clubInFormatJson.put("files", filesWithBase64Content(club.getFiles()));
        clubInFormatJson.put("coordinators", getClientInformationOfCoordinators(club.getCoordinators()));
        clubInFormatJson.put("petitions", listOfPetitionsToMap(club.getPetitions().stream().toList()));

        return clubInFormatJson;
    }

    private List<Map<String, Object>> listOfPetitionsToMap(List<Petition> petitions) {
        List<Map<String, Object>> listOfPetitionMap = new ArrayList<>();

        for (Petition petition : petitions){
            listOfPetitionMap.add(petitionToMap(petition));
        }

        return listOfPetitionMap;
    }

    private Map<String, Object> petitionToMap(Petition petition) {
        Map<String, Object> petitionMap = new HashMap<>();
        petitionMap.put("id", petition.getId());
        petitionMap.put("client", clientService.getClientInJsonFormat(petition.getClient()));
        petitionMap.put("description", petition.getDescription());
        petitionMap.put("status", petition.getStatus());
        return petitionMap;
    }

    private List<Map<String, Object>> getClientInformationOfCoordinators(Set<Coordinator> coordinators) {
        List<Map<String, Object>> listOfClientInformationMap = new ArrayList<>();
        for (Coordinator coordinator : coordinators){
            listOfClientInformationMap.add(clientService.getClientInJsonFormat(coordinator.getPerson()));
        }
        return listOfClientInformationMap;
    }

    private List<Map<String, Object>> listOfCoordinatorsToMap(List<Coordinator> coordinators) {
        List<Map<String, Object>> coordinatorHashMapList = new ArrayList<>();

        for (Coordinator coordinator : coordinators){
            coordinatorHashMapList.add(coordinatorToCorrectMapping(coordinator));
        }

        return coordinatorHashMapList;
    }

    private List<Map<String, Object>> filesWithBase64Content(Set<File> files) {
        List<Map<String, Object>> allJsonFormatFiles = new ArrayList<>();

        for (File file : files){
            allJsonFormatFiles.add(fileToMap(file));
        }

        return allJsonFormatFiles;
    }

    @Override
    public Map<String, Object> fileToMap(File file) {
        Map<String, Object> jsonFormatFile = new HashMap<>();
        jsonFormatFile.put("id", file.getId());
        jsonFormatFile.put("name", file.getName());
        jsonFormatFile.put("mediaType", file.getMediaType());
        jsonFormatFile.put("contentBase64", file.getContent());
        return jsonFormatFile;
    }


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
