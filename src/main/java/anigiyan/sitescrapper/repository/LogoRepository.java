package anigiyan.sitescrapper.repository;

import anigiyan.sitescrapper.model.Logo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogoRepository extends JpaRepository<Logo, Long> {
}
