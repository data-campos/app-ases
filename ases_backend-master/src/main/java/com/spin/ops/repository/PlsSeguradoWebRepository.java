package com.spin.ops.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spin.ops.model.PlsSeguradoWeb;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlsSeguradoWebRepository extends JpaRepository<PlsSeguradoWeb, Long> {

    @Query(value = "delete from pls_segurado_web where nr_seq_segurado = :nr_seq_segurado", nativeQuery = true)
	public void deleteUser(@Param("nr_seq_segurado") Long nr_seq_segurado);

}
