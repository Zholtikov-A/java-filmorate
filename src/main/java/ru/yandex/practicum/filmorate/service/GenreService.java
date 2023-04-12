package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreService {

    private final GenreDao genreDao;

    public List<Genre> findAll() {
        return genreDao.findAll();
    }

    public Genre findGenreById(Long id) {
        return genreDao.findGenreById(id);
    }

}
