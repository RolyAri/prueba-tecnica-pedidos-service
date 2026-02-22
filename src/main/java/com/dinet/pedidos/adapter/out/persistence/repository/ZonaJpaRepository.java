package com.dinet.pedidos.adapter.out.persistence.repository;

import com.dinet.pedidos.adapter.out.persistence.entity.ZonaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ZonaJpaRepository extends JpaRepository<ZonaEntity, String> {

}
