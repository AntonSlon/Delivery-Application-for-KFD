package org.example.deliveryapplication.database.entity

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.example.deliveryapplication.model.Role


@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "name")
    var name: String = "",

    @Column(name = "email")
    var email: String = "",

    @Column(name = "telephone_number")
    var telephoneNumber: String = "",

    @Column(name = "money")
    val money: Long = 0,

    @Column(name = "hash_password")
    var password: String = "",

    //@Enumerated(EnumType.STRING)
    @Column(name = "role")
    var role: String = ""
){
    @Column(name = "created_at")
    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val createdAt: LocalDateTime = LocalDateTime.now()
}
