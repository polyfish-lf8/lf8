package de.szut.lf8_project.lf8.projekt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Time;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "projekt")
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int kunden_id, va_mitarbeiter_id, va_kunden_id;
    private String bezeichnung;
    private Time StartDatum, EndDatum;

    public ProjectEntity(int va_mitarbeiter_id, int va_kunden_id, String bezeichnung) {
        this.va_mitarbeiter_id = va_mitarbeiter_id;
        this.va_kunden_id = va_kunden_id;
        this.bezeichnung = bezeichnung;
    }
}
