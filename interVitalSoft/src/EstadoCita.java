public enum EstadoCita {
    PENDIENTE, // Cuando se crea la cita
    ATENDIDA, // Cuando el médico ya dio el diagnóstico
    CANCELADA, // Cuando el paciente la anula
    NO_ASISTIO // Si pasó la fecha y no vino
}