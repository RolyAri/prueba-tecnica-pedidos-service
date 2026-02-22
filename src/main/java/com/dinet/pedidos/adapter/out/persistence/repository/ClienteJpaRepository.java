package com.dinet.pedidos.adapter.out.persistence.repository;

import com.dinet.pedidos.adapter.out.persistence.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClienteJpaRepository extends JpaRepository<ClienteEntity, String> {
}
