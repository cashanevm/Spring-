package com.example.letscode.Repository;

import com.example.letscode.Domain.Message;
import com.example.letscode.Domain.User;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, Long> {
    Iterable<Message> findByAuthor(User user);
}
