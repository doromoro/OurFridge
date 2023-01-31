package com.example.recipe2022.data.entity;

import com.example.recipe2022.data.enumer.FilePurpose;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_file")
public class Files extends BaseTimeEntity{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "file_id")
    private int fileSeq;

    @Column(name = "file_path")
    private String storedFile;

    @Column(name = "file_nm")
    private String originalFile;

    @Column(name = "file_size")
    private Long fileSize;

    @Enumerated(EnumType.STRING)
    @Column(name = "file_purpose")
    private FilePurpose filePurpose;

    @OneToMany(mappedBy = "files")
    private List<Users> users = new ArrayList<>();

}