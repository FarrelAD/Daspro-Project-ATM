import java.util.Scanner;

public class nestedif {
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

        if (input_no_rek.equals(no_rek)) {
            if (input_pin.equals(pin)) {
                Scanner input = new Scanner(System.in);
                System.out.println("Pilih menu :");
                System.out.println("1. Transfer");
                System.out.println("2. Tarik tunai");
                System.out.println("3. Setor tunai");
                System.out.println("4. Pembayaran lain-lain");
                System.out.println("Menu yang dipilih :");

                int menu = input.nextInt();

                System.out.println("**************************");

                switch (menu) {
                    case 1:
                        System.out.println("Anda memilih menu transfer");
                        System.out.println("Masukkan nomor rekening tujuan :");
                        String no_rek_tujuan = scanner.nextLine();
                        System.out.println("Masukkan nominal transfer : ");
                        int nom_transfer = input.nextInt();
                        int saldo_awal = 5000000;
                        if (nom_transfer > saldo_awal) {
                            System.out.println("Transaksi gagal");
                        } else {
                            int sisa_saldo = saldo_awal - nom_transfer;
                            System.out.println("Transfer ke nomor " + no_rek_tujuan + " berhasil dilakukan");
                            System.out.println("Sisa saldo anda : " + sisa_saldo);
                        }
                        break;
                    case 2:
                        System.out.println("Anda memilih menu tarik tunai");
                        break;
                    case 3:
                        System.out.println("Anda memilih menu setor tunai");
                        break;
                    case 4:
                        System.out.println("Anda memilih menu pembayaran lain lain");
                        break;
                    default:
                        System.out.println("Periksa kembali inputan anda");
                        break;
                }
            }
        }
    }
}
