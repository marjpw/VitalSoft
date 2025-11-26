# Script para reemplazar todas las referencias de GestionClinica con ServiceManager
$sourceDir = "c:\Users\Acer\Desktop\martin\interVitalSoft\src"

# Archivos a modificar
$archivos = @(
    "MainFrame.java",
    "LoginDialog.java",
    "PanelAdmin.java",
    "PanelPaciente.java",
    "PanelRegistroPaciente.java",
    "PanelRegistroMedico.java",
    "PanelCrearCita.java",
    "PanelTriaje.java",
    "PanelVerPacientes.java",
    "PanelVerMedicos.java"
)

foreach ($archivo in $archivos) {
    $ruta = Join-Path $sourceDir $archivo
    if (Test-Path $ruta) {
        Write-Host "Procesando $archivo..."
        $content = Get-Content $ruta -Raw
        
        # Reemplazos principales
        $content = $content -replace 'private GestionClinica gestion;', 'private ServiceManager services;'
        $content = $content -replace 'GestionClinica gestion\)', 'ServiceManager services)'
        $content = $content -replace 'this\.gestion = gestion;', 'this.services = services;'
        $content = $content -replace 'GestionClinica\.listaPacientes', 'services.getPacienteService().getListaPacientes()'
        $content = $content -replace 'GestionClinica\.listaMedicos', 'services.getMedicoService().getListaMedicos()'
        
        # Actualizar referencias a métodos
        $content = $content -replace '([^\.A-Za-z])gestion\.([a-z])', '$1services.$2'
        
        Set-Content $ruta $content -NoNewline
        Write-Host "  V $archivo actualizado"
    }
}

Write-Host "`nV Actualizacion completada"
