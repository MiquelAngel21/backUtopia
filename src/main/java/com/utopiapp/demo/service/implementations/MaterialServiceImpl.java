package com.utopiapp.demo.service.implementations;

import com.utopiapp.demo.model.Activity;
import com.utopiapp.demo.model.Material;
import com.utopiapp.demo.repositories.mysql.MaterialRepoMysqlImpl;
import com.utopiapp.demo.service.interfaces.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service
public class MaterialServiceImpl implements MaterialService {

    @Autowired
    private MaterialRepoMysqlImpl materialRepoMysql;

    public MaterialServiceImpl(MaterialRepoMysqlImpl materialRepoMysql) {
        this.materialRepoMysql = materialRepoMysql;
    }

    public void saveMaterial(Set<Material> material, Activity activity){
        material.forEach(e -> {
            if (e.getAmount() == 0 || e.getName().equals("")){
                material.remove(e);
            }
            //e.setActivity(activity);
        });
        if (!material.isEmpty()){
            materialRepoMysql.saveAll(material);
        }
    }

    @Override
    public List<Material> getMaterialByActivity(Long id) {
        return materialRepoMysql.getMaterialsByActivities_Id(id);
    }


}
