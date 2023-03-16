package com.todoapp.restTodo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.todoapp.authRequestJwt.user.UserModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_todo")
public class Todo {
    @Id
    @SequenceGenerator(
            name = "task_seq",
            sequenceName = "task_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "task_seq"
    )
    private Long taskId;
    private String title;
    private String category;
    private Boolean isCompleted;
    private LocalDate createdOn;
    private String tags;
//    private String username;
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "Fk_user_tasks")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private UserModel username;

}
