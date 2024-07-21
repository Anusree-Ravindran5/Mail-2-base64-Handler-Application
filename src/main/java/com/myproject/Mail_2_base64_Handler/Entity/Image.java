package com.myproject.Mail_2_base64_Handler.Entity;
import jakarta.persistence.*;

@Entity
@Table(name = "images")
    public class Image {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;

        @Lob
        @Column(columnDefinition = "MEDIUMBLOB")
        private byte[] data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}


