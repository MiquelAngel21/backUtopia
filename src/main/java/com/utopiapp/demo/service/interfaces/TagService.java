package com.utopiapp.demo.service.interfaces;

import com.utopiapp.demo.model.Tag;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TagService {
    List<Tag> getAll() throws Exception;
}
