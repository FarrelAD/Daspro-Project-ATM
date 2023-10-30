// Ini adalah suatu program sistem ATM - Kelompok 4 - TI-1B
// Fitur yang tersedia: transfer, tarik tunai, setor tunai, pembayaran lain-lain, dan riwayat transaksi
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;

public class SistemAtm {
    public static void main(String[] args) {
        // inisialisasi dan deklarasi variabel yang dibutuhkan
        String[] no_rek = { "1234567", "7654321", "1357924" }; // array nomor rekening
        String[] pin = { "1234", "5678", "2468" }; // array pin yang sesuai dengan indeks array nomor rekening
        int riw = 10, count = 10, hasil = 0, tujuan = 0; // variabel untuk cetak riwayat dan semacam session manipulasi saldo
        String[] riwayat = new String[riw]; // array riwayat
        String status[] = { "aman", "aman", "aman" }; // array status akun
        boolean isBoleh = false, isValid = false; // variabel autentikasi login dan validasi nomor rekening
        int loginAttempts = 0;
        final int maxLoginAttempts = 3; // maksimal login yang bisa dilakukan pengguna

        // halaman utama sistem atm
        System.out.println("=======================================================================");
        System.out.println("-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --");
        System.out.println("II| II| II|  IIIIII|  II|      IIIIII|   IIIIIII|  IIIIIIIIII|  IIIIII|");
        System.out.println("II| II| II|  II|      II|      II|       II|  II|  II| II| II|  II|    ");
        System.out.println("II| II| II|  IIIIII|  II|      II|       II|  II|  II| II| II|  IIIIII|");
        System.out.println("II| II| II|  II|      II|      II|       II|  II|  II| II| II|  II|    ");
        System.out.println("IIIIIIIIII|  IIIIII|  IIIIII|  IIIIII|   IIIIIII|  II| II| II|  IIIIII|");
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("         ___ ___ _____   ___ ___ _    _ _   _ ____ _____ ___      ");
        System.out.println("         ||| ||| |||||   ||| ||| |    | |_  | |||| ||||| |||      ");
        System.out.println("         | |  |  | | |   | | | | |    | ||_ | |    | | | | |      ");
        System.out.println("         |||  |  | | |   ||| | | |    | | |_| |||| | | | |||      ");
        System.out.println("         | |  |  | | |   |   | | |___ | |  || |    | | | | |      ");
        System.out.println("         | |  |  | | |   |   ||| |||| | |   | |||| | | | | |      ");
        System.out.println("=======================================================================");

        Scanner scanner = new Scanner(System.in);
        Scanner scannerInt = new Scanner(System.in);

        // Pengecekan batas login
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

            // Pengecekan kesesuaian indeks no rek dan PIN untuk login
            for (int i = 0; i < no_rek.length; i++) {
                if (input_no_rek.equals(no_rek[i]) && input_pin.equals(pin[i]) && status[i].equals("aman")) {
                    isBoleh = true; //autentifikasi
                    hasil = i; //session
                }
            }

            // Bagian di bawah ini adalah bagian menuju menu
            if (isBoleh) {
                // inisialisasi dan deklarasi variabel untuk transaksi
                String no_rek_tujuan; 
                char konfirmasi; 
                int nom_transfer, nom_tarik, nom_setor;
                int[] saldo = { 7000000, 4000000, 10000000 };


                // Format nilai uang Indonesia Rupiah (IDR)
                NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id","ID"));

                // Perulangan menu berdasarkan konfirmasi user
                do {
                    System.out.println("Silakan memilih menu dibawah ini : ");
                    System.out.println("1. Transfer");
                    System.out.println("2. Tarik tunai");
                    System.out.println("3. Setor tunai");
                    System.out.println("4. Pembayaran lain-lain");
                    System.out.println("5. Riwayat transaksi");
                    System.out.println("6. Cek Saldo");
                    System.out.print("Menu yang dipilih (input dalam angka) : "); // User input pilihan menu berupa angka
                    int menu = scannerInt.nextInt();

                    System.out.println("*******************************");

                    // Pilihan opsi menu
                    switch (menu) {
                        case 1:
                            System.out.println("ANDA MEMILIH MENU TRANSFER");
                            System.out.println("*******************************");
                            System.out.print("Masukkan nomor rekening tujuan : ");
                            no_rek_tujuan = scanner.nextLine();
                            // Pengecekan apakah nomor rekening tujuan ada di database
                            for (int i = 0; i < no_rek.length; i++) {
                                if (no_rek_tujuan.equals(no_rek[i])) {
                                    tujuan = i;
                                    isValid = true;
                                    break;
                                }
                            }
                            
                            // Kondisi jika isValid true
                            if (isValid == true) {
                                System.out.print("Masukkan nominal transfer : Rp "); // User input nominal transfer
                                nom_transfer = scannerInt.nextInt();
                                // Pengecekan nilai nominal transfer dibandingkan dengan jumlah saldo
                                if (nom_transfer <= saldo[hasil]) {
                                    saldo[hasil] -= nom_transfer; // Pengurangan saldo pada rekening yang dimiliki
                                    saldo[tujuan] += nom_transfer; // Penambahan nilai saldo pada rekening yang dituju
                                    System.out.println("Transfer ke nomor " + no_rek_tujuan + " berhasil dilakukan");
                                    // Format penulisan rupiah pada nilai saldo yang dimiliki
                                    String saldoRupiah = currencyFormat.format(saldo[hasil]); 
                                    System.out.println("Sisa saldo anda : " + saldoRupiah);
                                    // Pencatatan riwayat transaksi
                                    riwayat[riw - count] = "Telah melakukan transaksi ke rekening " + no_rek_tujuan
                                            + " sebesar " + nom_transfer;
                                    count--;
                                } else {
                                    // Kondisi jika nom_transfer <= saldo[hasil]
                                    System.out.println("Transaksi gagal, saldo anda tidak mencukupi");
                                }
                            } else {
                                // Kondisi jika isValid bernilai FALSE
                                System.out.println("Transaksi gagal, nomor rekening tujuan invalid");
                            }
                            break;
                        case 2:
                            System.out.println("ANDA MEMILIH MENU TARIK TUNAI");
                            System.out.println("*******************************");
                            System.out.print("Masukkan nominal tarik tunai : Rp "); // User input nominal tarik tunai
                            nom_tarik = scannerInt.nextInt();
                            // Pengecekan apakah nominal tarik kurang dari nilai saldo
                            if (nom_tarik < saldo[hasil]) {
                                // Pengecekan apakah nominal tarik kurang dari atau sama dengan 5 juta
                                if (nom_tarik <= 5000000) { 
                                    // Kondisi jika nom_tarik < saldo[hasil] dan nom_tarik <= 5000000
                                    saldo[hasil] -= nom_tarik;
                                    System.out.println("Tarik tunai berhasil dilakukan");
                                    // Format penulisan rupiah pada nilai saldo yang dimiliki
                                    String saldoRupiah = currencyFormat.format(saldo[hasil]); 
                                    System.out.println("Sisa saldo anda : " + saldoRupiah);
                                    // Pencatatan riwayat transaksi
                                    riwayat[riw - count] = "Telah melakukan tarik tunai sebesar " + nom_tarik;
                                    count--;
                                } else {
                                    // Kondisi jika nom_tarik < saldo[hasil] dan nom_tarik > 5000000
                                    System.out.println("Transaksi gagal, anda melebihi batas maksimum nominal tarik tunai");
                                }
                            } else {
                                // Kondisi jika nom_tarik > saldo[hasil]
                                System.out.println("Transaksi gagal, periksa kembali saldo anda");
                            }
                            break;
                        case 3:
                            System.out.println("ANDA MEMILIH MENU SETOR TUNAI");
                            System.out.println("*******************************");
                            System.out.print("Masukkan nominal setor tunai : Rp "); // User input nominal setor tunai
                            nom_setor = scannerInt.nextInt();
                            saldo[hasil] += nom_setor; // Penjumlahan saldo dengan nominal setor yang telah dilakukan
                            System.out.println("Setor tunai berhasil dilakukan");
                            // Format penulisan rupiah pada nilai saldo yang dimiliki
                            String saldoRupiah = currencyFormat.format(saldo[hasil]); 
                            System.out.println("Sisa saldo anda : " + saldoRupiah);
                            
                            // Pencatatan riwayat transaksi
                            riwayat[riw - count] = "Telah melakukan setor tunai sebesar " + nom_setor;
                            count--;
                            break;
                        case 4:
                            System.out.println("ANDA MEMILIH MENU PEMBAYARAN LAIN-LAIN");
                            System.out.println("*******************************");
                            break;
                        case 5:
                            System.out.println("ANDA MEMILIH MENU RIWAYAT TRANSAKSI");
                            System.out.println("*******************************");
                            for (String i : riwayat) {
                                System.out.println(i);
                            }
                            break;
                        case 6:
                            System.out.println("ANDA MEMILIH MENU CEK SALDO");
                            System.out.println("*******************************");
                            System.out.println("Saldo anda sebesar Rp "+ saldo[hasil]);
                            break;
                        default:
                            System.out.println("Input tidak sesuai. Periksa kembali inputan anda");
                            System.out.println("*******************************");
                            break;
                    }
                    System.out.print("Apakah anda ingin melakukan transaksi lagi? (Y/T) : ");
                    konfirmasi = scanner.next().charAt(0);
                    if (konfirmasi == 't' || konfirmasi == 'T') {
                        System.out.println("Terimakasih telah bertransaksi! Semoga harimu selalu bahagia :)");
                        break;
                    }
                } while (konfirmasi == 'y' || konfirmasi == 'Y');
                break;
            } else {
                System.out.println("Gagal login, periksa kembali nomor rekening dan PIN anda");
                loginAttempts++;

                if (loginAttempts >= maxLoginAttempts) {
                    System.out.println("Anda telah gagal lebih dari 3 kali. Akun anda diblokir.");
                    status[hasil] = "diblokir";
                    break; // Account diblokir, mengakhiri looping
                }
            } // Bagian akhir program menu
            System.out.println("**************************");
        }

        scanner.close();
    }
}
