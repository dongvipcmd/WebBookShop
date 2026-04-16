package com.example.demowebshop.service;

import com.example.demowebshop.entity.Author;
import com.example.demowebshop.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public void createAuthor(Author author){
        authorRepository.save(author);
    }

    public void updateAuthor(Long id, Author newData){
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tác giả"));
        author.setFullName(newData.getFullName());
        author.setDob(newData.getDob());
        author.setBio(newData.getBio());
        authorRepository.save(author);
    }

    public void deleteAuthor(Long id){
        authorRepository.deleteById(id);
    }

    public List<Author> getAll(){
        return authorRepository.findAll();
    }

    public Author getById(Long id){
        return authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tác giả"));
    }

}
