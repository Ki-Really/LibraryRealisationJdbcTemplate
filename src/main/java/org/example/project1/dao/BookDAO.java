package org.example.project1.dao;

import org.example.project1.models.Book;
import org.example.project1.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BookDAO {
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> index(){
        return jdbcTemplate.query("SELECT * FROM Book", new BeanPropertyRowMapper<>(Book.class));
    }

    public Book show(int id){
        return jdbcTemplate.query("SELECT * FROM Book WHERE id = ?", new Object[]{id}, new BeanPropertyRowMapper<>(Book.class))
                .stream().findAny().orElse(null);
    }

    public void save(Book book){
        jdbcTemplate.update("INSERT INTO Book (name,author,year) VALUES (?,?,?)", book.getName(),book.getAuthor(), book.getYear());
    }

    public void update(int id,Book updatedBook){
        jdbcTemplate.update("UPDATE Book SET name=?,author=?,year=? WHERE id=?",updatedBook.getName(),updatedBook.getAuthor(),
                updatedBook.getYear(), id);
    }

    public void delete(int id){
        jdbcTemplate.update("DELETE FROM Book WHERE id = ?",id);
    }

    public void assign(int personId,int bookId){
        jdbcTemplate.update("Update Book Set person_id = ? WHERE Book.id = ?",personId,bookId);
    }

    public void release(int bookId){
        jdbcTemplate.update("Update Book SET person_id = NULL where Book.id = ?",bookId);
    }

    public Optional<Person> getBookOwner(int bookId){
        return jdbcTemplate.query("SELECT Person.* FROM Person INNER JOIN Book ON Book.person_id = Person.id WHERE Book.id = ?", new Object[]{bookId},
                new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
    }
}
