package work.javiermantilla.tax.infrastructure.out.r2dbc.holiday.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
@Builder
@Table("holiday")
public class HolidayEntity {
    @Id
    private Integer id;
    @Column("year")
    private String year;
    @Column("date")
    private LocalDate date;
    @Column("enabled")
    private Short enabled;
    @Column("created_at")
    private LocalDateTime createdAt;
    @Column("updated_at")
    private LocalDateTime updatedAt;
    @Column("id_user_create")
    private Integer idUserCreate;
    @Column("id_user_update")
    private Integer idUserUpdate;
}
