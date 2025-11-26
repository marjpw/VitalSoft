# Script mejorado para reemplazar llamadas a servicios
$sourceDir = "c:\Users\Acer\Desktop\martin\interVitalSoft\src"

# Archivos a modificar
$archivos = @(
    "LoginDialog.java",
    "PanelRegistroPaciente.java",
    "PanelRegistroMedico.java",
    "PanelCrearCita.java",
    "PanelTriaje.java"
)

foreach ($archivo in $archivos) {
    $ruta = Join-Path $sourceDir $archivo
    if (Test-Path $ruta) {
        Write-Host "Procesando $archivo..."
        $content = Get-Content $ruta -Raw
        
        # Reemplazos de métodos de servicios
        $content = $content -replace 'services\.loginAdmin\(', 'services.getAdministradorService().loginAdmin('
        $content = $content -replace 'services\.loginMedico\(', 'services.getMedicoService().loginMedico('
        $content = $content -replace 'services\.loginPaciente\(', 'services.getPacienteService().loginPaciente('
        $content = $content -replace 'services\.registrarPaciente\(', 'services.getPacienteService().registrarPaciente('
        $content = $content -replace 'services\.registrarMedico\(', 'services.getMedicoService().registrarMedico('
        $content = $content -replace 'services\.registrarTriaje\(', 'services.getCitaService().registrarTriaje('
        $content = $content -replace 'services\.obtenerEspecialidades\(\)', 'services.getMedicoService().obtenerEspecialidades()'
        $content = $content -replace 'services\.obtenerMedicosPorEspecialidad\(', 'services.getMedicoService().obtenerMedicosPorEspecialidad('
        $content = $content -replace 'services\.crearCita\(', 'services.getCitaService().crearCita('
        
        Set-Content $ruta $content -NoNewline
        Write-Host "  V $archivo actualizado"
    }
}

Write-Host "`nV Actualizacion completada`n"
