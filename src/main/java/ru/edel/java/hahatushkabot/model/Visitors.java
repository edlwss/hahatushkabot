package ru.edel.java.hahatushkabot.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "visitors")

public class Visitors {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "your_sequence_generator")
    @SequenceGenerator(name="your_sequence_generator", sequenceName="your_sequence_name", allocationSize=1)
    private Long id;

    @Column(name = "visitor_id")
    private Long visitorId;

    @Column(name = "date")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime date;

    @Column(name = "action")
    private String action;

    @ManyToOne
    @JoinColumn(name = "joke_id")
    private JokesModel joke;

    @PrePersist
    public void prePersist() {
        this.date = LocalDateTime.now();
    }
}
