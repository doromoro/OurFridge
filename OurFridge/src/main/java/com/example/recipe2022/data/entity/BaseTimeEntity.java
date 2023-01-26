package com.example.recipe2022.data.entity;

import io.swagger.annotations.Api;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Api
public class BaseTimeEntity {

    @CreatedDate
    @Column(name = "CREATE_DATE")
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name = "MODIFY_DATE")
    private LocalDateTime modifiedDate;

    @Column(name = "CREATE_SEQ")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int createSeq;

    @Column(name = "MODIFY_SEQ")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int modifySeq;

    @Column(name = "USE_YN")
    private char useYN = 'Y';
}