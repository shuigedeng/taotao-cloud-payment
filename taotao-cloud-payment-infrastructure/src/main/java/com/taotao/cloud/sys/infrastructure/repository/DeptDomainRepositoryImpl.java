package com.taotao.cloud.payment.infrastructure.repository;

import com.taotao.cloud.payment.domain.dept.entity.DeptEntity;
import com.taotao.cloud.payment.domain.dept.repository.DeptDomainRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeptDomainRepositoryImpl implements DeptDomainRepository {


	@Override
	public void create(DeptEntity dept) {

	}

	@Override
	public void modify(DeptEntity dept) {

	}

	@Override
	public void remove(Long[] ids) {

	}
}
