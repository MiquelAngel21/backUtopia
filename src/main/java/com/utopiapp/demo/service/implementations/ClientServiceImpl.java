package com.utopiapp.demo.service.implementations;

import com.google.gson.Gson;
import com.utopiapp.demo.dto.GithubCodeDto;
import com.utopiapp.demo.dto.LoginDto;
import com.utopiapp.demo.dto.PasswordsSettingsDto;
import com.utopiapp.demo.dto.RegisterDto;
import com.utopiapp.demo.exceptions.*;
import com.utopiapp.demo.model.*;
import com.utopiapp.demo.repositories.mysql.ClientRepo;
import com.utopiapp.demo.repositories.mysql.ClubRepo;
import com.utopiapp.demo.repositories.mysql.CoordinatorRepo;
import com.utopiapp.demo.repositories.mysql.FileRepo;
import com.utopiapp.demo.service.interfaces.ClientService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepo clientRepo;
    private final PasswordEncoder passwordEncoder;
    private final HttpSession session;
    private final FileRepo fileRepo;
    private final ClubRepo clubRepo;
    private final CoordinatorRepo coordinatorRepo;

    public ClientServiceImpl(ClientRepo clientRepo, PasswordEncoder passwordEncoder, HttpSession session, FileRepo fileRepo, ClubRepo clubRepo, CoordinatorRepo coordinatorRepo) {
        this.clientRepo = clientRepo;
        this.passwordEncoder = passwordEncoder;
        this.session = session;
        this.fileRepo = fileRepo;
        this.clubRepo = clubRepo;
        this.coordinatorRepo = coordinatorRepo;
    }


    @Override
    public Client getClientByEmail(String email) {
        return clientRepo.findByEmail(email);
    }

    @Override
    public Client getClientById(Long id) {
        return clientRepo.getById(id);
    }

    @Override
    public Client save(Client newClient) {
        return clientRepo.save(newClient);
    }


    @Override
    public Client addClient(RegisterDto registerDTO) {
        verifyRegisterFormInformation(registerDTO);
        Client client = new Client(
                registerDTO.getName(),
                registerDTO.getUsername(),
                registerDTO.getLastname(),
                registerDTO.getEmail(),
                passwordEncoder.encode(registerDTO.getPassword()),
                LocalDateTime.now()
        );

        client = clientRepo.save(client);
        return client;
    }

    @Override
    public void verifyRegisterFormInformation(RegisterDto registerDto) {
        if (clientRepo.existsClientByEmail(registerDto.getEmail())){
            throw new EmailAlreadyExistsException();
        } else if (clientRepo.existsClientByUsername(registerDto.getUsername())){
            throw new UsernameAlreadyExistsException();
        }

        noRareCharactersInText(registerDto.getEmail());
        noRareCharactersInText(registerDto.getPassword());
        noRareCharactersInText(registerDto.getLastname());
        noRareCharactersInText(registerDto.getUsername());
        noRareCharactersInText(registerDto.getName());
        if (registerDto.getEmail().isEmpty() || registerDto.getPassword().isEmpty() ||
                registerDto.getLastname().isEmpty() || registerDto.getName().isEmpty() || registerDto.getUsername().isEmpty()){
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void noRareCharactersInText(String text) {
        String newText = text.replaceAll("[*\\-\"\\\\/\\[\\]<>=%&|#$¬~·]*", "");
        if (!newText.equals(text)) {
            throw new RareCharacterException();
        }
    }

    @Override
    public void verifyLoginFormInformation(LoginDto loginDTO) {
        noRareCharactersInText(loginDTO.getEmail());
        noRareCharactersInText(loginDTO.getPassword());
        if (loginDTO.getEmail().equals("") || loginDTO.getPassword().equals("")){
            throw new EmptyFieldsException();
        }
    }

    @Override
    public List<Map<String, Object>> getListOfClientsInJsonFormat(Collection<Client> clients){

        List<Map<String, Object>> allClientsInJsonFormat = new ArrayList<>();

        for (Client client : clients){
            Map<String, Object> clientInJsonFormat = getClientInJsonFormat(client);
            allClientsInJsonFormat.add(clientInJsonFormat);
        }

        return allClientsInJsonFormat;
    }

    @Override
    public Map<String, Object> getClientInJsonFormat(Client client) {
        Map<String, Object> currentUserData = new HashMap<>();
        currentUserData.put("id", client.getId().toString());
        currentUserData.put("name", client.getName());
        currentUserData.put("username", client.getUsername());
        currentUserData.put("lastname", client.getLastname());
        currentUserData.put("email", client.getEmail());
        currentUserData.put("Hearts", heartsToJsonFormat(client.getHearts()));
        currentUserData.put("description", client.getDescription());
        currentUserData.put("activities", getLazyActivitiesJson(client.getActivities()));
        if (client.getCoordinator() != null){
            currentUserData.put("coordinator", coordinatorToLazyMap(client.getCoordinator()));
        } else {
            currentUserData.put("coordinator", new HashMap<>());
        }

        return currentUserData;
    }

    private List<Map<String, Object>> getLazyActivitiesJson(Set<Activity> activities) {
        List<Map<String, Object>> getAllActivitiesByClient = new ArrayList<>();

        for (Activity activity : activities){
            getAllActivitiesByClient.add(lazyMapActivity(activity));
        }
        return getAllActivitiesByClient;
    }

    private Map<String, Object> lazyMapActivity(Activity activity) {
        Map<String, Object> activityJson = new HashMap<>();
        activityJson.put("id", activity.getId());
        activityJson.put("name", activity.getName());
        activityJson.put("createdDate", activity.getCreatedDate().toString());
        return activityJson;
    }

    private Map<String, Object> coordinatorToLazyMap(Coordinator coordinator) {
        Map<String, Object> coordinatorHashMap = new HashMap<>();
        coordinatorHashMap.put("id", coordinator.getId());
        coordinatorHashMap.put("club", coordinator.getClub().getId());
        coordinatorHashMap.put("person", coordinator.getPerson().getId());
        return coordinatorHashMap;
    }

    private Object heartsToJsonFormat(Set<Heart> hearts) {
        List<Map<String, Object>> heartsInJsonFormat = new ArrayList<>();

        for (Heart heart : hearts){
            Map<String, Object> jsonHeart = new HashMap<>();
            jsonHeart.put("id", heart.getId());
            jsonHeart.put("activity_id", heart.getActivity().getId());
            jsonHeart.put("client_id", heart.getClient().getId());
            heartsInJsonFormat.add(jsonHeart);
        }

        return heartsInJsonFormat;
    }

    @Override
    public File getImageById(Long id) {
        Optional<File> file = fileRepo.findById(id);
        return file.orElse(null);
    }

    @Override
    public HttpHeaders chooseImageType(File file) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (file.getMediaType().equals("image/png")){
            httpHeaders.setContentType(MediaType.IMAGE_PNG);
        } else if (file.getMediaType().equals("image/jpeg")){
            httpHeaders.setContentType(MediaType.IMAGE_JPEG);
        } else {
            httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        }
        return httpHeaders;
    }

    @Override
    public List<Client> getAllClientsByClub(Club club) {
        return clientRepo.findAllByClubOrderByCreatedDateDesc(club);
    }

    @Override
    public Map<String, Object> getPaginatedVolunteersByClub(String volunteerSearcher, Client client, Long clubId, int start, int length) {
        Pageable paging = PageRequest.of(start / length, length);
        Page<Client> pageResult;
        Club club = clubRepo.findClubById(clubId);

        if (coordinatorRepo.existsCoordinatorByPersonAndClub(client, club)){
            if (volunteerSearcher != null && !volunteerSearcher.equals("")){
                pageResult = clientRepo.findAllByNameLikeAndClubOrderByCreatedDateDesc("%"+volunteerSearcher+"%", club, paging);
            } else {
                pageResult = getVolunteersOfClub(club, paging);
            }
        } else {
            throw new NotCoordinatorException();
        }

        long total = pageResult.getTotalElements();
        List<Map<String, Object>> data = getListOfClientsInJsonFormat(pageResult.getContent());

        Map<String, Object> json = new HashMap<>();
        json.put("data", data);
        json.put("recordsTotal", total);
        return json;
    }

    @Override
    public Boolean isCoordinator(Client client) {
        return client.getCoordinator() != null;
    }

    @Override
    public Club deleteVolunteerFromClub(Long volunteerId) {
        Client client = clientRepo.findClientById(volunteerId);
        Club club = client.getClub();
        Set<Client> allVolunteers = club.getVolunteers();

        if(allVolunteers.size() == 1){
            clubRepo.delete(club);
            client.setClub(null);
            clientRepo.save(client);
            return null;
        } else {
            Set<Client> newListOfVolunteers = new HashSet<>();
            boolean atLeastOneCoordinator = false;
            for (Client volunteer : allVolunteers){
                if (!volunteer.equals(client)){
                    newListOfVolunteers.add(volunteer);
                    if (volunteer.getCoordinator() != null && volunteer.getCoordinator().getClub().equals(club)){
                        atLeastOneCoordinator = true;
                    }
                }
            }

            if (atLeastOneCoordinator){
                club.setVolunteers(newListOfVolunteers);
                client.setClub(null);
                clientRepo.save(client);
                clubRepo.save(club);

                if (client.getCoordinator() != null){
                    coordinatorRepo.delete(coordinatorRepo.findCoordinatorByPerson_id(client.getId()));
                }

            } else {
                throw new AtLeastOneCoordinatorException();
            }

        }
        return club;
    }

    @Override
    public Client getClientByUsername(String username) {
        return clientRepo.findClientByUsername(username);
    }

    @Override
    public Client getClientFromOauth2(String username, String name, String email) {
        Client clientFromUtopiWeb = new Client();
        Client clientFromDB = getClientByUsername(username);

        if (clientFromDB != null){
            return clientFromDB;
        } else {
            clientFromUtopiWeb.setUsername(username);
            if (name != null){
                clientFromUtopiWeb.setName(name);
                clientFromUtopiWeb.setLastname(name);
            } else {
                clientFromUtopiWeb.setName("GitHub");
                clientFromUtopiWeb.setLastname("User");
            }

            if (email != null){
                clientFromUtopiWeb.setEmail(email);
            } else {
                clientFromUtopiWeb.setEmail("");
            }
            clientFromUtopiWeb.setDescription("");
            clientFromUtopiWeb.setHearts(new HashSet<>());
            clientFromUtopiWeb.setCoordinator(null);
            clientFromUtopiWeb.setActivities(new HashSet<>());
            clientFromUtopiWeb.setCreatedDate(LocalDateTime.now());
            return clientRepo.save(clientFromUtopiWeb);
        }
    }

    @Override
    public String getToGitHub(String res, CloseableHttpClient client)  {
        try {
            String token = res.split("&")[0].split("=")[1];
            HttpGet get = new HttpGet("https://api.github.com/user");
            get.setHeader("Authorization", "Bearer "+token );
            CloseableHttpResponse response = client.execute(get);
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e){
            throw new OAuth2GitHubAuthenticationException();
        }

    }

    @Override
    public String postToGitHubWithOauth2Information(GithubCodeDto githubCodeDto, CloseableHttpClient client) {
        try {
            HttpPost post = new HttpPost("https://github.com/login/oauth/access_token");
            Gson gson = new Gson();
            StringEntity postingString = new StringEntity(gson.toJson(githubCodeDto));//gson.tojson() converts your pojo to json
            post.setEntity(postingString);
            post.setHeader("Content-type", "application/json");
            CloseableHttpResponse closeableHttpResponse = client.execute(post);
            return EntityUtils.toString(closeableHttpResponse.getEntity());
        } catch (IOException e){
            throw new OAuth2GitHubAuthenticationException();
        }
    }

    @Override
    public Long createUserId(Object idString, String username) {
        Long id = Long.parseLong(idString.toString().substring(0,1));
        while (true){
            Client clientCheckId = clientRepo.findClientByUsername(username);
            if (clientCheckId != null && clientCheckId.getUsername().equals(username)){
                return clientCheckId.getId();
            }
            if (clientCheckId != null){
                id*=1000;
            } else {
                break;
            }
        }
        return id;
    }

    @Override
    public void handlePasswords(Client client, PasswordsSettingsDto passwordsSettingsDto) {
        validatePasswordSettings(passwordsSettingsDto);
        if (client.getPassword() != null){
            if (!passwordEncoder.matches(passwordsSettingsDto.getConfirmPassword2(), client.getPassword())){
                throw new IncorrectPasswordException();
            }
        }

        client.setPassword(passwordEncoder.encode(passwordsSettingsDto.getNewPassword()));
        clientRepo.save(client);
    }

    private void validatePasswordSettings(PasswordsSettingsDto passwordsSettingsDto) {
        noRareCharactersInText(passwordsSettingsDto.getNewPassword());
        noRareCharactersInText(passwordsSettingsDto.getRepeatPassword());
        noRareCharactersInText(passwordsSettingsDto.getConfirmPassword2());
        if(passwordsSettingsDto.getNewPassword().equals("") || passwordsSettingsDto.getRepeatPassword().equals("")){
            throw new EmptyFieldsException();
        }
    }


    private Page<Client> getVolunteersOfClub(Club club, Pageable paging) {
        return clientRepo.findAllByClubOrderByCreatedDateDesc(club, paging);
    }

    @Override
    public void removeClubFromClient(Client client) {
        client.setClub(null);
        clientRepo.save(client);
    }

    @Override
    public String signInClub(Client client, String code) {
        Club club = clubRepo.findClubByAccessCode(code);
        if (club == null){
            throw new CodeNotExistsException();
        }
        client.setClub(club);
        clientRepo.save(client);
        return club.getName();
    }
}
