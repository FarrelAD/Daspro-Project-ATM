// Ini adalah suatu program sistem ATM - Kelompok 4 - TI-1B
// Fitur yang tersedia: transfer, tarik tunai, setor tunai, pembayaran lain-lain, dan riwayat transaksi
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;

public class SistemAtm {
    public static void main(String[] args) {
        // inisialisasi dan deklarasi variabel yang dibutuhkan
        String[][] akunData = {
                { "1234567", "1234", "7000000", "aman" },
                { "7654321", "5678", "4000000", "aman" },
                { "1357924", "2468", "10000000", "aman" }
        };
        int riw = 10, count = 10, hasil = 0, tujuan = 0; // variabel untuk cetak riwayat dan semacam session manipulasi
                                                         // saldo
        String[] riwayat = new String[riw]; // array riwayat
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

        Scanner scanner1 = new Scanner(System.in);
        Scanner scanner2 = new Scanner(System.in);
        Scanner scanner3 = new Scanner(System.in);
        Scanner scanner4 = new Scanner(System.in);


        // Pengecekan batas login
        while (loginAttempts <= maxLoginAttempts) {
            System.out.print("Masukkan nomor rekening : "); // Input nomor rekening
            String input_no_rek = scanner1.nextLine();

            System.out.print("Masukkan PIN anda : "); // Input PIN pengguna
            String input_pin = scanner1.nextLine();

            System.out.println("**********************************************");

            // Pengecekan apakah status akunnya diblokir atau tidak
            if (hasil != -1 && akunData[hasil][3].equals("diblokir")) {
                System.out.println("Akun anda berstatus " + akunData[hasil][3]);
                System.out.println("**********************************************");
            }

            // Pengecekan kesesuaian nomor rekening dan PIN untuk login
            for (int i = 0; i < akunData.length; i++) {
                if (input_no_rek.equals(akunData[i][0]) && input_pin.equals(akunData[i][1])
                        && akunData[i][3].equals("aman")) {
                    isBoleh = true; // autentifikasi
                    hasil = i; // session
                }
            }

            // Bagian di bawah ini adalah bagian menuju menu
            if (isBoleh) {
                // inisialisasi dan deklarasi variabel untuk transaksi
                String no_rek_tujuan;
                char konfirmasi;
                int nom_transfer, nom_tarik, nom_setor;
                int saldoPengguna = Integer.parseInt(akunData[hasil][2]);

                // Format nilai uang Indonesia Rupiah (IDR)
                NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

                // Perulangan menu berdasarkan konfirmasi user
                do {
                    System.out.println("Silakan memilih menu dibawah ini : ");
                    System.out.println("1. Transfer");
                    System.out.println("2. Tarik tunai");
                    System.out.println("3. Setor tunai");
                    System.out.println("4. Pembayaran lain-lain");
                    System.out.println("5. Riwayat transaksi");
                    System.out.println("6. Cek Saldo");
                    System.out.print("Menu yang dipilih (angka) : "); // User input pilihan menu berupa angka
                    int menu = scanner2.nextInt();

                    System.out.println("**********************************************");

                    // Pilihan opsi menu
                    switch (menu) {
                        case 1: // OPSI FITUR TRANSFER
                            System.out.println("ANDA MEMILIH MENU TRANSFER");
                            System.out.println("**********************************************");
                            System.out.print("Masukkan nomor rekening tujuan : ");
                            no_rek_tujuan = scanner3.nextLine();
                            // Pengecekan apakah nomor rekening tujuan ada di database
                            for (int i = 0; i < akunData.length; i++) {
                                if (no_rek_tujuan.equals(akunData[i][0])) {
                                    tujuan = i;
                                    isValid = true;
                                    break;
                                }
                            }

                            // Kondisi jika isValid true
                            if (isValid == true) {
                                System.out.print("Masukkan nominal transfer : Rp "); // User input nominal transfer
                                nom_transfer = scanner4.nextInt();
                                System.out.println("**********************************************");
                                // Pengecekan nilai nominal transfer dibandingkan dengan jumlah saldo
                                if (nom_transfer <= saldoPengguna) {
                                    saldoPengguna -= nom_transfer; // Pengurangan saldo pada rekening yang dimiliki
                                    // saldo[tujuan] += nom_transfer; // Penambahan nilai saldo pada rekening yang
                                    // dituju
                                    System.out.println("Transfer ke nomor " + no_rek_tujuan + " berhasil dilakukan");

                                    // Formatting penulisan rupiah pada output
                                    String nom_transferRupiah = currencyFormat.format(nom_transfer);
                                    String saldoRupiah = currencyFormat.format(saldoPengguna);

                                    System.out.println("Sisa saldo anda : " + saldoRupiah);
                                    // Pencatatan riwayat transaksi
                                    riwayat[riw - count] = "Telah melakukan transaksi ke rekening " + no_rek_tujuan
                                            + " sebesar " + nom_transferRupiah;
                                    count--;
                                    System.out.println("**********************************************");
                                } else {
                                    // Kondisi jika nom_transfer <= saldo[hasil]
                                    System.out.println("Transaksi gagal, saldo anda tidak mencukupi");
                                    System.out.println("**********************************************");
                                }
                            } else {
                                // Kondisi jika isValid bernilai FALSE
                                System.out.println("Transaksi gagal, nomor rekening tujuan invalid");
                                System.out.println("**********************************************");
                            }
                            break;
                        case 2: // OPSI FITUR TARIK TUNAI
                            System.out.println("ANDA MEMILIH MENU TARIK TUNAI");
                            System.out.println("**********************************************");
                            System.out.print("Masukkan nominal tarik tunai : Rp "); // User input nominal tarik tunai
                            nom_tarik = scanner3.nextInt();
                            System.out.println("**********************************************");
                            // Pengecekan apakah nominal tarik kurang dari nilai saldo
                            if (nom_tarik < saldoPengguna) {
                                // Pengecekan apakah nominal tarik kurang dari atau sama dengan 5 juta
                                if (nom_tarik <= 5000000) {
                                    // Kondisi jika nom_tarik < saldo[hasil] dan nom_tarik <= 5000000
                                    saldoPengguna -= nom_tarik;
                                    System.out.println("Tarik tunai berhasil dilakukan");

                                    // Formating penulisan rupiah pada output
                                    String nom_tarikRupiah = currencyFormat.format(nom_tarik);
                                    String saldoRupiah = currencyFormat.format(saldoPengguna);

                                    System.out.println("Sisa saldo anda : " + saldoRupiah);
                                    // Pencatatan riwayat transaksi
                                    riwayat[riw - count] = "Telah melakukan tarik tunai sebesar " + nom_tarikRupiah;
                                    count--;
                                    System.out.println("**********************************************");
                                } else {
                                    // Kondisi jika nom_tarik < saldo[hasil] dan nom_tarik > 5000000

                                    System.out.println("Transaksi gagal, anda melebihi batas maksimum nominal tarik tunai");
                                    System.out.println("**********************************************");
                                }
                            } else {
                                // Kondisi jika nom_tarik > saldo[hasil]
                                System.out.println("Transaksi gagal, periksa kembali saldo anda");
                                System.out.println("**********************************************");
                            }
                            break;
                        case 3: // OPSI FITUR SETOR TUNAI
                            System.out.println("ANDA MEMILIH MENU SETOR TUNAI");
                            System.out.println("**********************************************");
                            System.out.print("Masukkan nominal setor tunai : Rp "); // User input nominal setor tunai
                            nom_setor = scanner3.nextInt();
                            System.out.println("**********************************************");
                            saldoPengguna += nom_setor; // Penjumlahan saldo dengan nominal setor yang telah dilakukan
                            System.out.println("Setor tunai berhasil dilakukan");
                            // Formatting penulisan rupiah pada output
                            String nom_setorRupiah = currencyFormat.format(nom_setor);
                            String saldoRupiah = currencyFormat.format(saldoPengguna); 

                            System.out.println("Sisa saldo anda : " + saldoRupiah);

                            // Pencatatan riwayat transaksi
                            riwayat[riw - count] = "Telah melakukan setor tunai sebesar " + nom_setorRupiah;
                            count--;
                            System.out.println("**********************************************");
                            break;
                        case 4: // OPSI FITUR PEMBAYARAN LAIN-LAIN
                            char konfirmasiPulsa = 'y'; 
                            System.out.println("ANDA MEMILIH MENU PEMBAYARAN LAIN-LAIN");
                            System.out.println("**********************************************");
                            System.out.println("Pilih opsi pembayaran: ");
                            System.out.println("1. Pulsa");
                            System.out.println("2. Listrik");
                            System.out.println("3. Pendidikan");
                            System.out.print("Menu yang anda pilih (angka): "); // User input pilihan menu
                            int menuBayar = scanner3.nextInt();
                            System.out.println("**********************************************");
                            // Opsi menu pembayaran
                            switch (menuBayar) {
                                case 1: // OPSI BAYAR PULSA
                                    do {
                                        System.out.println("ANDA MEMILIH OPSI PEMBAYARAN PULSA");
                                        System.out.println("**********************************************");
                                        System.out.println("Pilih operator seluler");
                                        System.out.println("1. Indosat");
                                        System.out.println("2. XL");
                                        System.out.println("3. Telkomsel");
                                        System.out.print("Operator yang anda pilih: "); // User input jenis operator
                                        String operatorPulsa = scanner2.next();
                                        System.out.println("**********************************************");
                                        if (operatorPulsa.equals("1")) {
                                            operatorPulsa = "Indosat"; // Perubahan nilai variabel dari "1" menjadi
                                                                       // "Indosat"
                                        } else if (operatorPulsa.equals("2")) {
                                            operatorPulsa = "XL"; // Perubahan nilai variabel dari "2" menjadi "XL"
                                        } else  if (operatorPulsa.equals("3")) {
                                            operatorPulsa = "Telkomsel"; // Perubahan nilai variabel dari "3" menjadi
                                                                         // "Telkomsel"
                                        } else {
                                            System.out.println("Operator invalid");
                                            konfirmasiPulsa = 't';
                                        }
                                        System.out.print("Input nomor telepon anda: "); // User input nomor telepon
                                        int nomorTelepon = scanner4.nextInt();
                                        System.out.println("**********************************************");
                                        System.out.print("Input nominal pulsa: Rp "); // User input nominal pulsa
                                        int nomPulsa = scanner4.nextInt();
                                        System.out.println("**********************************************");
                                        // Pengecekan kondisi apakah nominal pulsa yang diinput lebih kecil dari saldo
                                        if (nomPulsa < saldoPengguna) {
                                            saldoPengguna -= nomPulsa;
                                            String nomPulsaRupiah = currencyFormat.format(nomPulsa);
                                            String saldoRupiah2 = currencyFormat.format(saldoPengguna);
                                        
                                            // Menampilkan output transaksi berhasil
                                            System.out.println("**********************************************");
                                            System.out.println("Operator seluler: "+operatorPulsa);
                                            System.out.println("Nomor telepon Anda: "+nomorTelepon);
                                            System.out.println("Pulsa senilai: "+nomPulsaRupiah);
                                            System.out.println("Sisa saldo anda "+saldoRupiah2);
                                            System.out.println("**********************************************");
                                            // Pencatatan riwayat transaksi
                                            riwayat[riw - count] = "Telah melakukan pembayaran pulsa senilai " + nomPulsaRupiah;
                                            count--;
                                            
                                            konfirmasiPulsa = 't'; // Perubahan nilai konfirmasiBayar menjadi 't' agar proses looping bisa berhenti
                                        } else {
                                            System.out.println("TRANSAKSI GAGAL. ");
                                            System.out.println("Ingin mengulangi input pembayaran? (Y/T) ");
                                            konfirmasiPulsa = scanner4.next().charAt(0);
                                            System.out.println("**********************************************");
                                        }
                                    } while (konfirmasiPulsa == 'y' || konfirmasiPulsa == 'Y');
                                    break; // Break switch case pada menu pembayaran pulsa
                                default:
                                    System.out.println("Input tidak sesuai. Periksa kembali inputan anda");
                                    System.out.println("**********************************************");
                                    break; // Break switch-case pada menu pulsa pembayaran pulsa
                            }
                            break; // Break switch-case pada case 4
                        case 5: // OPSI FITUR RIWAYAT TRANSAKSI
                            System.out.println("ANDA MEMILIH MENU RIWAYAT TRANSAKSI");
                            System.out.println("**********************************************");
                            for (String i : riwayat) {
                                System.out.println(i);
                            }
                            break; // Break case 5
                        case 6: // OPSI FITUR CEK SALDO
                            System.out.println("ANDA MEMILIH MENU CEK SALDO");
                            System.out.println("**********************************************");

                            // Formatting penulisan rupiah pada output
                            String saldoRupiah3 = currencyFormat.format(saldoPengguna);
                            System.out.println("Saldo anda sebesar "+ saldoRupiah3);
                            break; // Break case 6
                        default:
                            System.out.println("Input tidak sesuai. Periksa kembali inputan anda");
                            System.out.println("**********************************************");
                            break; // Break switch-case opsi menu
                    }
                    System.out.print("Apakah anda ingin mengulang transaksi? (Y/T) : ");
                    konfirmasi = scanner2.next().charAt(0);
                    System.out.println("**********************************************");
                    if (konfirmasi == 't' || konfirmasi == 'T') {
                        System.out.println("Terimakasih telah bertransaksi! Semoga harimu selalu bahagia :)");
                        System.out.println("================================================================");
                        break;
                    }
                } while (konfirmasi == 'y' || konfirmasi == 'Y');
                break; // Break do-while opsi menu
            } else {
                System.out.println("Gagal login, periksa kembali nomor rekening dan PIN anda");
                loginAttempts++;
                System.out.println("**********************************************");

                if (loginAttempts >= maxLoginAttempts) {
                    System.out.println("Anda telah gagal lebih dari 3 kali. Akun anda diblokir.");
                    akunData[hasil][3] = "diblokir";
                    break; // Account diblokir, mengakhiri looping
                }
            } // Bagian akhir program menu
            System.out.println("================================================================");
        }

        scanner1.close();
        scanner2.close();
        scanner3.close();
        scanner4.close();
    }
}