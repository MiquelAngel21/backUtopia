package com.utopiapp.demo.service.implementations;

import com.utopiapp.demo.model.Tag;
import com.utopiapp.demo.repositories.mysql.TagRepoMysqlImpl;
import com.utopiapp.demo.service.interfaces.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    TagRepoMysqlImpl tagRepo;

    @Override
    public List<Tag> getAll() throws Exception {
        try{
            List<Tag> allTags = tagRepo.findAll();
            return  allTags;
        } catch (Exception e){
            throw new Exception(e);
        }
    }
}
