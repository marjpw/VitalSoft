# Script final para corregir todos los errores de compilación
$sourceDir = "c:\Users\Acer\Desktop\martin\interVitalSoft\src"

# === 1. Corregir constructores ===
Write-Host "=== Corrigiendo constructores ==="

# PanelAdmin.java
$file = Join-Path $sourceDir "PanelAdmin.java"
$content = Get-Content $file -Raw
$content = $content -replace 'public PanelAdmin\(MainFrame mainFrame, GestionClinica gestion, Administrador admin\)', 'public PanelAdmin(MainFrame mainFrame, ServiceManager services, Administrador admin)'
$content = $content -replace 'new PanelRegistroMedico\(mainFrame, gestion, admin\)', 'new PanelRegistroMedico(mainFrame, services, admin)'
$content = $content -replace 'new PanelRegistroPaciente\(mainFrame, gestion, admin\)', 'new PanelRegistroPaciente(mainFrame, services, admin)'
$content = $content -replace 'new PanelTriaje\(mainFrame, gestion, admin\)', 'new PanelTriaje(mainFrame, services, admin)'
$content = $content -replace '([^\.])services\.eliminarMedico\(', '$1services.getMedicoService().eliminarMedico('
Set-Content $file $content -NoNewline
Write-Host "V PanelAdmin.java"

# PanelPaciente.java
$file = Join-Path $sourceDir "PanelPaciente.java"
$content = Get-Content $file -Raw
$content = $content -replace 'public PanelPaciente\(MainFrame mainFrame, GestionClinica gestion, Paciente paciente\)', 'public PanelPaciente(MainFrame mainFrame, ServiceManager services, Paciente paciente)'
$content = $content -replace 'new PanelCrearCita\(mainFrame, gestion, paciente\)', 'new PanelCrearCita(mainFrame, services, paciente)'
$content = $content -replace 'services\.verFacturasDePaciente\(', 'services.getPacienteService().verFacturasDePaciente('
$content = $content -replace 'services\.getHistorialPaciente\(', 'services.getPacienteService().getHistorialPaciente('
$content = $content -replace 'services\.consultarDiagnostico\(', 'services.getPacienteService().consultarDiagnostico('
$content = $content -replace 'services\.cancelarCita\(', 'services.getCitaService().cancelarCita('
Set-Content $file $content -NoNewline
Write-Host "V PanelPaciente.java"

# PanelTriaje.java
$file = Join-Path $sourceDir "PanelTriaje.java"
$content = Get-Content $file -Raw
$content = $content -replace 'services\.getCitasPendientesTriaje\(\)', 'services.getCitaService().getCitasPendientesTriaje()'
Set-Content $file $content -NoNewline
Write-Host "V PanelTriaje.java"

Write-Host "`n=== Correcciones completadas ==="
