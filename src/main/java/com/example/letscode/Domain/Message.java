package com.example.letscode.Domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    @NotBlank(message = "Write sm")
    @Length(max = 2048,message = "too long" )
    private String text;
    @Length(max = 1024,message = "too long" )
    private String tag;
    private String filename;

    public String getAuthorName(){
        return author.getUsername();
    }
}
