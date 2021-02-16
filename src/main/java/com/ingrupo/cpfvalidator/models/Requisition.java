package com.ingrupo.cpfvalidator.models;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name="\"UserRequisition\"")
public class Requisition {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private Date date = new Date();

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}