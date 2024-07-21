package com.myproject.Mail_2_base64_Handler.Repository;


import com.myproject.Mail_2_base64_Handler.Entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    }

