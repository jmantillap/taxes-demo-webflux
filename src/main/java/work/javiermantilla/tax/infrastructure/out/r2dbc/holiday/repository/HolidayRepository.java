package work.javiermantilla.tax.infrastructure.out.r2dbc.holiday.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import work.javiermantilla.tax.infrastructure.out.r2dbc.holiday.entity.HolidayEntity;

@Repository
public interface HolidayRepository  extends ReactiveCrudRepository<HolidayEntity, Long> {
}
