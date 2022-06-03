package com.utopiapp.demo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Heart {
    @Id
    @GenericGenerator(name="gen" , strategy="increment")
    @GeneratedValue(generator="gen")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Activity activity;

    @ManyToOne(fetch = FetchType.EAGER)
    private Client client;

    public Heart() {
    }

    public Heart(Activity activity, Client client) {
        this.activity = activity;
        this.client = client;
    }

    public Heart(Long id, Activity activity, Client client) {
        this.id = id;
        this.activity = activity;
        this.client = client;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Heart heart = (Heart) o;
        return Objects.equals(id, heart.id) && Objects.equals(activity.getId(), heart.activity.getId()) && Objects.equals(client.getId(), heart.client.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, activity, client);
    }
}
