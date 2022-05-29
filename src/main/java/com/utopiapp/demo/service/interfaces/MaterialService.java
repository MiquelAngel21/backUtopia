package com.utopiapp.demo.service.interfaces;

import com.utopiapp.demo.model.Activity;
import com.utopiapp.demo.model.Material;

import java.util.List;
import java.util.Set;

public interface MaterialService {
    void saveMaterial(Set<Material> materials, Activity activity);
    List<Material> getMaterialByActivity(Long id);
}
