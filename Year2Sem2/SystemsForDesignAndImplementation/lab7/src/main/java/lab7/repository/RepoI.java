package lab7.repository;

import lab7.domain.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@NoRepositoryBean
public interface RepoI<T extends BaseEntity<ID>, ID extends Serializable> extends JpaRepository<T,ID> {

}
