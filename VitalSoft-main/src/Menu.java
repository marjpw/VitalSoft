import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {
    private static final Scanner sc = ScannerUtil.getScanner();

    // ====== MÉTODO AUXILIAR PARA LEER OPCIONES ======
    protected static int leerOpcion(String mensaje, int min, int max) {
        int opc;
        while (true){
            try {
                if (!mensaje.isEmpty()) System.out.print(mensaje);
                opc = sc.nextInt();
                sc.nextLine();
                if (opc >= min && opc <= max) return opc;
                else System.out.println("❌ Opción inválida. Ingrese un número entre " + min + " y " + max + ".");
            } catch (InputMismatchException e) {
                System.out.println("⚠️ Error: Solo se permiten números.");
                sc.nextLine();
            }
        }
    }
}
