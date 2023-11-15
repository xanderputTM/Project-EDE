package fact.it.gaterepository.repository;

import fact.it.gateservice.model.Gate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GateRepository extends JpaRepository<Gate, Long>{
}