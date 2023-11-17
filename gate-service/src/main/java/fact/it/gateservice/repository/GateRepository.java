package fact.it.gateservice.repository;

import fact.it.gateservice.model.Gate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GateRepository extends JpaRepository<Gate, Integer>{
}