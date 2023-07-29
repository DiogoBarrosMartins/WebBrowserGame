package com.superapi.gamerealm.model;


import jakarta.persistence.*;

@Entity
@DiscriminatorValue("BARBARIAN")
public class BarbarianVillage extends Village {

}

