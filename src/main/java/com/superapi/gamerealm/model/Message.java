package com.superapi.gamerealm.model;

import javax.persistence.*;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender")
    private Account sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient")
    private Account recipient;

    private String content;


    public Message( Account sender, Account receiver, String content) {
        this.sender = sender;
        this.recipient = receiver;
        this.content = content;
    }

    public Message() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getSender() {
        return sender;
    }

    public void setSender(Account sender) {
        this.sender = sender;
    }

    public Account getReceiver() {
        return recipient;
    }

    public void setReceiver(Account receiver) {
        this.recipient = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
