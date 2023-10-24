import java.util.Scanner;

public class SistemAtm {
    public static void main(String[] args) {
        String[] no_rek = { "1234567", "7654321", "1357924" };
        String[] pin = { "1234", "5678", "2468" };
        int riw = 10, count = 10;
        String[] riwayat = new String[riw];
        String status = "aman";
        boolean isBoleh = false;
        int loginAttempts = 0;
        final int maxLoginAttempts = 3;

        System.out.println("**************************");
        System.out.println("*                        *");
        System.out.println("*    Sistem mesin ATM    *");
        System.out.println("*                        *");
        System.out.println("**************************");

        Scanner scanner = new Scanner(System.in);
        Scanner scannerInt = new Scanner(System.in);

        while (loginAttempts <= maxLoginAttempts) {
            System.out.print("Masukkan nomor rekening : ");
            String input_no_rek = scanner.nextLine();

            System.out.print("Masukkan pin anda : ");
            String input_pin = scanner.nextLine();

            System.out.println("**************************");

            if (status.equals("diblokir")) {
                System.out.println("Akun anda berstatus " + status);
                System.out.println("**************************");
            }

            for (int i = 0; i < no_rek.length; i++) {
                if (input_no_rek.equals(no_rek[i]) && input_pin.equals(pin[i])) {
                    isBoleh = true;
                }
            }
            if (isBoleh) {
                String no_rek_tujuan, konfirmasi;
                int nom_transfer, nom_tarik, nom_setor, saldo = 5000000;
                do {
                    System.out.println("Silahkan memilih menu dibawah ini : ");
                    System.out.println("1. Transfer");
                    System.out.println("2. Tarik tunai");
                    System.out.println("3. Setor tunai");
                    System.out.println("4. Pembayaran lain-lain");
                    System.out.println("5. Riwayat transaksi");
                    System.out.print("Menu yang dipilih :");
                    int menu = scannerInt.nextInt();

                    System.out.println("**************************");

                    switch (menu) {
                        case 1:
                            System.out.println("Anda memilih menu transfer");
                            System.out.println("**************************");
                            System.out.print("Masukkan nomor rekening tujuan :");
                            no_rek_tujuan = scanner.nextLine();
                            System.out.print("Masukkan nominal transfer : ");
                            nom_transfer = scannerInt.nextInt();
                            if (nom_transfer > saldo) {
                                System.out.println("Transaksi gagal, periksa kembali saldo anda");
                            } else {
                                saldo -= nom_transfer;
                                System.out.println("Transfer ke nomor " + no_rek_tujuan + " berhasil dilakukan");
                                System.out.println("Sisa saldo anda : " + saldo);
                                riwayat[riw - count] = "Telah melakukan transaksi ke rekening " + no_rek_tujuan
                                        + " sebesar " + nom_transfer;
                                count--;
                            }
                            break;
                        case 2:
                            System.out.println("Anda memilih menu tarik tunai");
                            System.out.println("**************************");
                            System.out.print("Masukkan nominal tarik tunai : ");
                            nom_tarik = scannerInt.nextInt();
                            if (nom_tarik > saldo) {
                                System.out.println("Transaksi gagal, periksa kembali saldo anda");
                            } else {
                                saldo -= nom_tarik;
                                System.out.println("Tarik tunai berhasil dilakukan");
                                System.out.println("Sisa saldo anda : " + saldo);
                                riwayat[riw - count] = "Telah melakukan tarik tunai sebesar " + nom_tarik;
                                count--;
                            }
                            break;
                        case 3:
                            System.out.println("Anda memilih menu setor tunai");
                            System.out.print("Masukkan nominal setor tunai : ");
                            nom_setor = scannerInt.nextInt();
                            saldo += nom_setor;
                            System.out.println("Setor tunai berhasil dilakukan");
                            System.out.println("Sisa saldo anda : " + saldo);
                            riwayat[riw - count] = "Telah melakukan setor tunai sebesar " + nom_setor;
                            count--;
                            break;
                        case 4:
                            System.out.println("Anda memilih menu pembayaran lain lain");
                            break;
                        case 5:
                            for (String i : riwayat) {
                                System.out.println(i);
                            }
                            break;
                        default:
                            System.out.println("Periksa kembali inputan anda");
                            break;
                    }
                    System.out.print("Apakah anda ingin mengulang transaksi? (Y/T) : ");
                    konfirmasi = scanner.nextLine();
                    if (konfirmasi.equalsIgnoreCase("t")) {
                        System.out.println("Terimakasih telah bertransaksi");
                        break;
                    }
                } while (konfirmasi.equalsIgnoreCase("y"));
                break;
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
