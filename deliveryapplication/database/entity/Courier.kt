package org.example.deliveryapplication.database.entity

import jakarta.persistence.*
import org.example.deliveryapplication.model.courier.State
import org.example.deliveryapplication.model.courier.Vehicle
import java.awt.Point


@Entity(name = "couriers")
class Courier (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "currentLocation")
    val currentLocation: Point,

    @Column(name = "status")
    val status: State,

    @Column(name = "vehicle")
    val vehicle: Vehicle,
)