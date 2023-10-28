
// Ini adalah suatu program sistem ATM
// Fitur yang tersedia: transfer, tarik tunai, setor tunai, pembayaran lain-lain, dan riwayat transaksi
import java.util.Scanner;

public class SistemAtm {
    //inisialisasi dan deklarasi variabel yang dibutuhkan
    public static void main(String[] args) {
        String[] no_rek = { "1234567", "7654321", "1357924" }; // array nomor rekeninh
        String[] pin = { "1234", "5678", "2468" }; // array pin yang sesuai dengan indeks array nomor rekening
        int riw = 10, count = 10, hasil = 0, tujuan = 0; //variabel untuk cetak riwayat dan semacam session manipulasi saldo
        String[] riwayat = new String[riw]; // array riwayat
        String status[] = { "aman", "aman", "aman" }; // array status akun
        boolean isBoleh = false, isvalid = false; // variabel autentikasi login dan validasi nomor rekening
        int loginAttempts = 0;
        final int maxLoginAttempts = 3; // maksimal login

        // halaman utama sistem atm
        System.out.println("**************************");
        System.out.println("*                        *");
        System.out.println("*    Sistem mesin ATM    *");
        System.out.println("*                        *");
        System.out.println("**************************");

        Scanner scanner = new Scanner(System.in);
        Scanner scannerInt = new Scanner(System.in);

        //pengecekan batas login
        while (loginAttempts <= maxLoginAttempts) {
            System.out.print("Masukkan nomor rekening : "); // Input nomor rekening
            String input_no_rek = scanner.nextLine();

            System.out.print("Masukkan PIN anda : "); // Input PIN pengguna
            String input_pin = scanner.nextLine();

            System.out.println("**************************");

            // Pengecekan apakah status akunnya diblokir atau tidak
            if (status[hasil].equals("diblokir")) {
                System.out.println("Akun anda berstatus " + status);
                System.out.println("**************************");
            }

            //pengecekan kesesuaian indeks no rek dan pin untuk login
            for (int i = 0; i < no_rek.length; i++) {
                if (input_no_rek.equals(no_rek[i]) && input_pin.equals(pin[i]) && status[i].equals("aman")) {
                    isBoleh = true; //autentukasi
                    hasil = i; //session
                }
            }

            // Bagian di bawah ini adalah bagian menuju bagian menu
            if (isBoleh) {
                // inisialisasi variabel untuk transaksi
                String no_rek_tujuan, konfirmasi; 
                int nom_transfer, nom_tarik, nom_setor;
                int[] saldo = { 5000000, 4000000, 1000000 };
                // perulangan menu berdasarkan konfirmasi user
                do {
                    System.out.println("Silahkan memilih menu dibawah ini : ");
                    System.out.println("1. Transfer");
                    System.out.println("2. Tarik tunai");
                    System.out.println("3. Setor tunai");
                    System.out.println("4. Pembayaran lain-lain");
                    System.out.println("5. Riwayat transaksi");
                    System.out.println("6. Cek Saldo");
                    System.out.print("Menu yang dipilih :");
                    int menu = scannerInt.nextInt();

                    System.out.println("**************************");

                    // Pilihan opsi menu
                    switch (menu) {
                        case 1:
                            System.out.println("Anda memilih menu transfer");
                            System.out.println("**************************");
                            System.out.print("Masukkan nomor rekening tujuan : ");
                            no_rek_tujuan = scanner.nextLine();
                            for (int i = 0; i < no_rek.length; i++) {
                                if (no_rek_tujuan.equals(no_rek[i])) {
                                    tujuan = i;
                                    isvalid = true;
                                    break;
                                }
                            }
                            if (isvalid == true) {
                                System.out.print("Masukkan nominal transfer : ");
                                nom_transfer = scannerInt.nextInt();
                                if (nom_transfer <= saldo[hasil]) {
                                    saldo[hasil] -= nom_transfer;
                                    saldo[tujuan] += nom_transfer;
                                    System.out.println("Transfer ke nomor " + no_rek_tujuan + " berhasil dilakukan");
                                    System.out.println("Sisa saldo anda : " + saldo[hasil]);
                                    riwayat[riw - count] = "Telah melakukan transaksi ke rekening " + no_rek_tujuan
                                            + " sebesar " + nom_transfer;
                                    count--;
                                } else {
                                    System.out.println("Transaksi gagal, saldo anda tidak mencukupi");
                                }
                            } else {
                                System.out.println("Transaksi gagal, nomor rekening tujuan invalid");
                            }
                            break;
                        case 2:
                            System.out.println("Anda memilih menu tarik tunai");
                            System.out.println("**************************");
                            System.out.print("Masukkan nominal tarik tunai : ");
                            nom_tarik = scannerInt.nextInt();
                            if (nom_tarik > saldo[hasil]) {
                                System.out.println("Transaksi gagal, periksa kembali saldo anda");
                            } else {
                                saldo[hasil] -= nom_tarik;
                                System.out.println("Tarik tunai berhasil dilakukan");
                                System.out.println("Sisa saldo anda : " + saldo[hasil]);
                                riwayat[riw - count] = "Telah melakukan tarik tunai sebesar " + nom_tarik;
                                count--;
                            }
                            break;
                        case 3:
                            System.out.println("Anda memilih menu setor tunai");
                            System.out.print("Masukkan nominal setor tunai : ");
                            nom_setor = scannerInt.nextInt();
                            saldo[hasil] += nom_setor;
                            System.out.println("Setor tunai berhasil dilakukan");
                            System.out.println("Sisa saldo anda : " + saldo[hasil]);
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
                        case 6:
                            System.out.println("Saldo anda sebesar Rp. " + saldo[hasil]);
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
                    status[hasil] = "diblokir";
                    break; // Account is blocked, exit the loop
                }
            } // Bagian akhir program menu
            System.out.println("**************************");
        }

        scanner.close();
    }
}
