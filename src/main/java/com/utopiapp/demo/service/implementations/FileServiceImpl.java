package com.utopiapp.demo.service.implementations;

import com.utopiapp.demo.model.Activity;
import com.utopiapp.demo.model.File;
import com.utopiapp.demo.model.Material;
import com.utopiapp.demo.repositories.mysql.FileRepoMysqlImpl;
import com.utopiapp.demo.service.interfaces.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileRepoMysqlImpl fileRepoMysql;

    public void saveFiles(Set<File> files, Activity activity){
        files.forEach(e -> {
            e.setActivity(activity);
        });
        fileRepoMysql.saveAll(files);
    }
}
