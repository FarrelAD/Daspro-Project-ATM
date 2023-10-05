import java.util.Scanner;

public class SistemAtm {
    public static void main(String[] args) {
        String no_rek = "1234567";
        String pin = "1234";
        String status = "aman";
        int loginAttempts = 0;
        final int maxLoginAttempts = 3;

        System.out.println("**************************");
        System.out.println("*                        *");
        System.out.println("*    Sistem mesin ATM    *");
        System.out.println("*                        *");
        System.out.println("**************************");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Masukkan nomor rekening :");
            String input_no_rek = scanner.nextLine();

            System.out.println("Masukkan pin anda :");
            String input_pin = scanner.nextLine();

            System.out.println("**************************");

            if (status.equals("diblokir")) {
                System.out.println("Akun anda berstatus " + status);
                System.out.println("**************************");
            }

            if (input_no_rek.equals(no_rek) && input_pin.equals(pin) && status.equals("aman")) {
                System.out.println("Berhasil login");
                break; // Successful login, exit the loop
            } else {
                System.out.println("Gagal login, periksa kembali detail anda");
                loginAttempts++;

                if (loginAttempts >= maxLoginAttempts) {
                    System.out.println("Anda telah gagal lebih dari 3 kali. Akun anda diblokir.");
                    status = "diblokir";
                    // break; // Account is blocked, exit the loop
                }
            }
            System.out.println("**************************");
        }

        scanner.close();
    }
}
