package ru.mipt1c;

import org.springframework.data.jpa.repository.JpaRepository;


public interface Repository extends JpaRepository<Wrapper, Integer> {

    Wrapper findByMyKey(byte[] key);

    boolean existsByMyKey(byte[] key);
}

