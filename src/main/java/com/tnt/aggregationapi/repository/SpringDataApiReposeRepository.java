package com.tnt.aggregationapi.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tnt.aggregationapi.repository.entity.ApiResponse;
import com.tnt.aggregationapi.repository.entity.ApiResponseDataId;

@Repository
public interface SpringDataApiReposeRepository extends JpaRepository<ApiResponse, ApiResponseDataId> {

	@Query(nativeQuery = true, value = "SELECT * FROM API_RESPONSE WHERE uuid=?1 and req_param in ?2 and res_param is not null")
	public List<ApiResponse> findRespose(UUID uuid, Set<String> reqParam);

	@Modifying
	@Query(nativeQuery = true, value = "DELETE FROM API_RESPONSE WHERE TIMESTAMPDIFF(MINUTE,CREATED_ON, CURRENT_TIMESTAMP())>10")
	public int deleteApiRespose();

	@Modifying
	@Query(nativeQuery = true, value = "INSERT INTO API_RESPONSE(uuid,req_type,req_param,res_param,created_on) VALUES(?1,?2,?3,?4,?5) ")
	public int createRequest(UUID uuid, String reqType, String reqParam, String res_param, Timestamp st);

}
