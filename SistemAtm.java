import java.util.Scanner;

public class SistemAtm {
    public static void main(String[] args) {
        String no_rek = "1234567";
        String pin = "1234";

        System.out.println("**************************");
        System.out.println("*                        *");
        System.out.println("*    Sistem mesin ATM    *");
        System.out.println("*                        *");
        System.out.println("**************************");

        System.out.println("Masukkan nomor rekening :");
        Scanner scanner = new Scanner(System.in);
        String input_no_rek = scanner.nextLine();

        System.out.println("Masukkan pin anda :");
        String input_pin = scanner.nextLine();

        System.out.println("**************************");

        if (input_no_rek.equals(no_rek) && input_pin.equals(pin)) {
            System.out.println("Berhasil");
        } else {
            System.out.println("Gagal");
        }
    }
}