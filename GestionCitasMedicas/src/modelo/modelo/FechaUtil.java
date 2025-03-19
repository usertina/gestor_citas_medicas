package modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FechaUtil {

    // Generar fechas laborales (días hábiles) a partir del día actual
    public static List<String> generarFechasLaborales(int dias) {
        List<String> fechas = new ArrayList<>();
        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        while (fechas.size() < dias) {
            // Solo añadir si es un día laborable (de lunes a viernes)
            if (fechaActual.getDayOfWeek().getValue() >= 1 && fechaActual.getDayOfWeek().getValue() <= 5) {
                fechas.add(fechaActual.format(formatter));
            }
            fechaActual = fechaActual.plusDays(1); // Avanzar al día siguiente
        }
        return fechas;
    }

    // Generar todos los horarios en un rango de tiempo con intervalos
    public static List<String> generarTodosLosHorarios(String inicio, String fin, int intervaloMinutos) {
        List<String> horarios = new ArrayList<>();
        LocalTime horaInicio = LocalTime.parse(inicio);
        LocalTime horaFin = LocalTime.parse(fin);

        while (horaInicio.isBefore(horaFin)) {
            horarios.add(horaInicio.toString());
            horaInicio = horaInicio.plusMinutes(intervaloMinutos);
        }

        return horarios;
    }
}
