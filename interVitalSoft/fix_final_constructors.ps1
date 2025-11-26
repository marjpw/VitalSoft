# Script para corregir los 4 paneles restantes
$sourceDir = "c:\Users\Acer\Desktop\martin\interVitalSoft\src"

$archivos = @("PanelCrearCita.java", "PanelRegistroMedico.java", "PanelRegistroPaciente.java", "PanelTriaje.java")

foreach ($archivo in $archivos) {
    $file = Join-Path $sourceDir $archivo
    $content = Get-Content $file -Raw
    
    # Reemplazar constructor
    $content = $content -replace 'GestionClinica gestion', 'ServiceManager services'
    
    Set-Content $file $content -NoNewline
    Write-Host "V $archivo"
}

Write-Host "`nV Todos los constructores corregidos"
