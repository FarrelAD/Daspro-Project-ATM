import java.util.Scanner;

public class cadangan {
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

        while (loginAttempts <= maxLoginAttempts) {
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
                String no_rek_tujuan, konfirmasi;
                int nom_transfer, nom_tarik, nom_setor, saldo_awal = 5000000, sisa_saldo;
                System.out.println("Anda berhasil login, silahkan memilih menu dibawah ini :");
                System.out.println("1. Transfer");
                System.out.println("2. Tarik tunai");
                System.out.println("3. Setor tunai");
                System.out.println("4. Pembayaran lain-lain");
                System.out.println("Menu yang dipilih :");
                int menu = scanner.nextInt();

                System.out.println("**************************");

                switch (menu) {
                    case 1:
                        do {
                            Scanner scanner2 = new Scanner(System.in);
                            System.out.println("Anda memilih menu transfer");
                            System.out.println("**************************");
                            System.out.println("Masukkan nomor rekening tujuan :");
                            no_rek_tujuan = scanner2.nextLine();
                            System.out.println("Masukkan nominal transfer : ");
                            nom_transfer = scanner.nextInt();
                            if (nom_transfer > saldo_awal) {
                                System.out.println("Transaksi gagal, periksa kembali saldo anda");
                            } else {
                                sisa_saldo = -nom_transfer;
                                System.out.println("Transfer ke nomor " + no_rek_tujuan + " berhasil dilakukan");
                                System.out.println("Sisa saldo anda : " + sisa_saldo);
                            }
                            System.out.println("Apakah anda ingin mengulangi transaksi?");
                            konfirmasi = scanner2.nextLine();
                            if (konfirmasi.equalsIgnoreCase("t")) {
                                System.out.println("Terimakasih telah bertransaksi");
                                break;
                            }
                        } while (konfirmasi.equalsIgnoreCase("y"));
                        break;
                    case 2:
                        System.out.println("Anda memilih menu tarik tunai");
                        System.out.println("**************************");
                        System.out.println("Masukkan nominal tarik tunai : ");
                        nom_tarik = scanner.nextInt();
                        if (nom_tarik > saldo_awal) {
                            System.out.println("Transaksi gagal, periksa kembali saldo anda");
                        } else {
                            sisa_saldo = saldo_awal - nom_tarik;
                            System.out.println("Tarik tunai berhasil dilakukan");
                            System.out.println("Sisa saldo anda : " + sisa_saldo);
                        }
                        break;
                    case 3:
                        System.out.println("Anda memilih menu setor tunai");
                        System.out.println("Masukkan nominal setor tunai : ");
                        nom_setor = scanner.nextInt();
                        sisa_saldo = saldo_awal + nom_setor;
                        System.out.println("Setor tunai berhasil dilakukan");
                        System.out.println("Sisa saldo anda : " + sisa_saldo);
                        break;
                    case 4:
                        System.out.println("Anda memilih menu pembayaran lain lain");
                        break;
                    default:
                        System.out.println("Periksa kembali inputan anda");
                        break;
                }
                break; // Successful login, exit the loop
            } else {
                System.out.println("Gagal login, periksa kembali detail anda");
                loginAttempts++;

                if (loginAttempts >= maxLoginAttempts) {
                    System.out.println("Anda telah gagal lebih dari 3 kali. Akun anda diblokir.");
                    status = "diblokir";
                    break; // Account is blocked, exit the loop
                }
            }
            System.out.println("**************************");
        }

        scanner.close();
    }
}
