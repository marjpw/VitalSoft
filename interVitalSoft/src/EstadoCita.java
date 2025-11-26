public enum EstadoCita {
    PENDIENTE, // Cuando se crea la cita
    ATENDIDO, // Cuando el médico ya dio el diagnóstico
    CANCELADO, // Cuando el paciente la anula
    NO_ASISTIO // Si pasó la fecha y no vino
}