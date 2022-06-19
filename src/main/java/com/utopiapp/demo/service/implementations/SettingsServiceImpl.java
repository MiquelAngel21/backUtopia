package com.utopiapp.demo.service.implementations;

import com.utopiapp.demo.dto.FileDto;
import com.utopiapp.demo.dto.SettingsDataDto;
import com.utopiapp.demo.exceptions.*;
import com.utopiapp.demo.model.Client;
import com.utopiapp.demo.model.File;
import com.utopiapp.demo.repositories.mysql.FileRepo;
import com.utopiapp.demo.service.interfaces.ClientService;
import com.utopiapp.demo.service.interfaces.ClubService;
import com.utopiapp.demo.service.interfaces.SettingsService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class SettingsServiceImpl implements SettingsService {

    private final ClientService clientService;
    private final ClubService clubService;
    private final PasswordEncoder passwordEncoder;
    private final FileRepo fileRepo;

    public SettingsServiceImpl(ClientService clientService, ClubService clubService, PasswordEncoder passwordEncoder, FileRepo fileRepo) {
        this.clientService = clientService;
        this.clubService = clubService;
        this.passwordEncoder = passwordEncoder;
        this.fileRepo = fileRepo;
    }

    @Override
    public Map<String, Object> getSettingsData(Client currentClient) {
        Map<String, Object> settingsData = new HashMap<>();
        currentClient = clientService.getClientById(currentClient.getId());
        settingsData.put("client", clientService.getClientInJsonFormat(currentClient));
        if (currentClient.getClub() != null) {
            settingsData.put("club", clubService.convertClubToMap(currentClient.getClub()));
        } else {
            settingsData.put("club", new HashMap<>());
        }
        if (currentClient.getFile() != null) {
            settingsData.put("file", clubService.fileToMap(currentClient.getFile()));
        } else {
            settingsData.put("file", new HashMap<>());
        }

        return settingsData;
    }

    @Override
    public Client updateDataClient(SettingsDataDto settingsDataDto, Client currentClient) {
        updateClientCheckExcpetions(settingsDataDto, currentClient);

        assignFile(settingsDataDto, currentClient);

        currentClient.setName(settingsDataDto.getName());
        currentClient.setLastname(settingsDataDto.getLastname());
        currentClient.setDescription(settingsDataDto.getDescription());

        return clientService.save(currentClient);
    }

    private void updateClientCheckExcpetions(SettingsDataDto settingsDataDto, Client currentClient) {
        noRareCharactersInText(settingsDataDto.getEmail());
        noRareCharactersInText(settingsDataDto.getLastname());
        noRareCharactersInText(settingsDataDto.getName());
        if (settingsDataDto.getDescription() != null){
            noRareCharactersInText(settingsDataDto.getDescription());
        }


        if (currentClient.getPassword() == null || currentClient.getPassword().equals("")) {
            throw new EmptyPasswordException();
        }
        if (settingsDataDto.getName().equals("") || settingsDataDto.getLastname().equals("") || settingsDataDto.getEmail().equals("")) {
            throw new EmptyFieldsException();
        }
        if (!passwordEncoder.matches(settingsDataDto.getPassword(), currentClient.getPassword())) {
            throw new IncorrectPasswordException();
        }

        Client clientWithExistingEmail = clientService.getClientByEmail(settingsDataDto.getEmail());
        if (clientWithExistingEmail == null) {
            currentClient.setEmail(settingsDataDto.getEmail());
        } else if (Objects.equals(currentClient.getId(), clientWithExistingEmail.getId())){
            currentClient.setEmail(settingsDataDto.getEmail());
        } else {
            throw new EmailAlreadyExistsException();
        }

        fileOnlyTypeImage(settingsDataDto.getFile());

    }

    private void fileOnlyTypeImage(FileDto file) {
        if (file.getMediaType() != null && !file.getMediaType().contains("image")){
            throw new InvalidImageException();
        }
    }

    private void noRareCharactersInText(String text) {
        String newText = text.replaceAll("[*\\-\"\\\\/\\[\\]<>=%&|#$¬~·]*", "");
        if (!newText.equals(text)) {
            throw new RareCharacterException();
        }
    }

    private void assignFile(SettingsDataDto settingsDataDto, Client currentClient) {
        if (settingsDataDto.getFile().getContent() != null) {
            String content = settingsDataDto.getFile().getContent();
            if (settingsDataDto.getFile().getContent().contains("base64")) {
                content = settingsDataDto.getFile().getContent().split(",")[1];
                settingsDataDto.getFile().setContent(content);
            }
            File fileExists = clubService.fileDtoIntoFile(settingsDataDto.getFile());
            List<File> allFilesWithThisContent = fileRepo.findAllByContent(fileExists.getContent());
            if (allFilesWithThisContent != null && allFilesWithThisContent.size() > 0) {
                fileExists = allFilesWithThisContent.get(0);
                if (fileExists != null) {
                    currentClient.setFile(fileExists);
                } else {
                    settingsDataDto.getFile().setContent(content);
                    File file = clubService.fileDtoIntoFile(settingsDataDto.getFile());
                    file = fileRepo.save(file);
                    currentClient.setFile(file);
                }
            } else {
                settingsDataDto.getFile().setContent(content);
                File file = clubService.fileDtoIntoFile(settingsDataDto.getFile());
                file = fileRepo.save(file);
                currentClient.setFile(file);
            }

        }
    }

    @Override
    public File getProfileImageByClient(Long userId) {
        return fileRepo.findFileByClient_id(userId);
    }
}
