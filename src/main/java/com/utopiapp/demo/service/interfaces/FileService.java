package com.utopiapp.demo.service.interfaces;

import com.utopiapp.demo.model.Activity;
import com.utopiapp.demo.model.File;

import java.util.Set;

public interface FileService {
    void saveFiles(Set<File> files, Activity activity);
}
