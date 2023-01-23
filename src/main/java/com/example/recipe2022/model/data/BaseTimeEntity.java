package com.example.recipe2022.model.data;

import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
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
    @NotNull
    private char useYN = 'Y';

    public char getUseYN(){
        return useYN;
    }
    public void setUseYN(char b) {
        this.useYN = b;
    }
    public int getCreateSeq(){
        return createSeq;
    }
}