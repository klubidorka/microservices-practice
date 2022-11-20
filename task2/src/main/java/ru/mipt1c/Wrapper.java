package ru.mipt1c;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "wrapper123")
public class Wrapper {
    private final int DOXUISCHA = 100000;
    @Id
    @Column(name = "key", length = DOXUISCHA, unique = true)
    byte[] myKey;
    @Column(name = "value", length = DOXUISCHA)
    byte[] myValue;

    public Wrapper() {}
    public Wrapper(byte[] key, byte[] value) {
        this.myKey = key;
        this.myValue = value;
    }
}
