package com.learn.companyms.company;


import jakarta.persistence.*;
import jakarta.ws.rs.DefaultValue;
import lombok.*;



@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;


    private double avgRating;

//    @JsonIgnore
//    @OneToMany(mappedBy = "company")
//    private Long jobsId;
//
////    @OneToMany(mappedBy = "company")
//    private Long reviewsId;


}
