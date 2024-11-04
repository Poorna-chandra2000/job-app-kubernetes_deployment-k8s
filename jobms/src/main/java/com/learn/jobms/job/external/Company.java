package com.learn.jobms.job.external;






import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
