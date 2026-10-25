package com.test.task_management_api.repository;

import com.test.task_management_api.model.Task;
import com.test.task_management_api.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findAllByUser_UsernameAndTitleContainsIgnoreCase(
            String Username, String title, Pageable pageable
    );

    Page<Task> findByUser_Username(Pageable pageable, String username);

    Optional<Task> findByIdAndUser_Username(long id, String username);
}
