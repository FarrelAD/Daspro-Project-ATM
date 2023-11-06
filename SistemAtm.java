
// Ini adalah suatu program sistem ATM - Kelompok 4 - TI-1B
// Fitur yang tersedia: transfer, tarik tunai, setor tunai, pembayaran lain-lain, riwayat transaksi, cek saldo, dan ubah PIN
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;

public class SistemAtm {
    public static void main(String[] args) {

        Scanner scanner1 = new Scanner(System.in); //
        Scanner scanner2 = new Scanner(System.in);
        Scanner scanner3 = new Scanner(System.in);
        Scanner scanner4 = new Scanner(System.in);
        Scanner scanner5 = new Scanner(System.in);
        Scanner scannerTF = new Scanner(System.in);

        // inisialisasi dan deklarasi variabel yang dibutuhkan
        String[][] akunData = {
                { "1234567", "1234", "7000000", "aman" },
                { "7654321", "5678", "4000000", "aman" },
                { "7777777", "7777", "10000000", "aman" }
        };
        int riw = 10, count = 10, hasil = 0; // variabel untuk cetak riwayat dan semacam session manipulasi
                                             // saldo
        String[] riwayat = new String[riw]; // array riwayat
        boolean isBoleh = false, isValid = false; // variabel autentikasi login dan validasi nomor rekening
        int loginAttempts = 0;
        final int maxLoginAttempts = 3; // maksimal login yang bisa dilakukan pengguna

        // ASCII-UNICODE CHARACTER
        System.setProperty("file.encoding", "UTF-8");
        /*
         * char[] charASCII = {'\u26A0', '\u2714'};
         * String charWarning = new String(new char[] {charASCII[0]});
         * String charChecklist = new String(new char[] {charASCII[1]});
         */
        // String [] charASCII = {"\u26A0", "\u2714"};

        // Halaman utama sistem ATM
        System.out.println(
                "=====================================================================================================");
        System.out.println(
                "[  █████╗ ████████╗███╗   ███╗    ██████╗  ██████╗ ██╗     ██╗███╗   ██╗███████╗███╗   ███╗ █████╗  ]");
        System.out.println(
                "[ ██╔══██╗╚══██╔══╝████╗ ████║    ██╔══██╗██╔═══██╗██║     ██║████╗  ██║██╔════╝████╗ ████║██╔══██╗ ]");
        System.out.println(
                "[ ███████║   ██║   ██╔████╔██║    ██████╔╝██║   ██║██║     ██║██╔██╗ ██║█████╗  ██╔████╔██║███████║ ]");
        System.out.println(
                "[ ██╔══██║   ██║   ██║╚██╔╝██║    ██╔═══╝ ██║   ██║██║     ██║██║╚██╗██║██╔══╝  ██║╚██╔╝██║██╔══██║ ]");
        System.out.println(
                "[ ██║  ██║   ██║   ██║ ╚═╝ ██║    ██║     ╚██████╔╝███████╗██║██║ ╚████║███████╗██║ ╚═╝ ██║██║  ██║ ]");
        System.out.println(
                "[ ╚═╝  ╚═╝   ╚═╝   ╚═╝     ╚═╝    ╚═╝      ╚═════╝ ╚══════╝╚═╝╚═╝  ╚═══╝╚══════╝╚═╝     ╚═╝╚═╝  ╚═╝ ]");
        System.out.println(
                "[ ================================================================================================= ]");
        System.out.println(
                "[                         _    _ _____ _     _____ ________  ___ _____                              ]");
        System.out.println(
                "[                        | |  | |  ___| |   /  __ \\  _  |  \\/  ||  ___|                             ]");
        System.out.println(
                "[                        | |  | | |__ | |   | /  \\/ | | | .  . || |__                               ]");
        System.out.println(
                "[                        | |/\\| |  __|| |   | |   | | | | |\\/| ||  __|                              ]");
        System.out.println(
                "[                        \\  /\\  / |___| |___| \\__/\\ \\_/ / |  | || |___                              ]");
        System.out.println(
                "[                         \\/  \\/\\____/\\_____/\\____/\\___/\\_|  |_/\\____/                              ]");
        System.out.println(
                "=====================================================================================================");
        System.out.println("");

        // Pengecekan batas login
        while (loginAttempts <= maxLoginAttempts) {
            System.out.println(
                    "    ============================================================================================");
            System.out.print("    [  Masukkan nomor rekening : "); // Input nomor rekening
            String inputNoRek = scanner1.nextLine();

            System.out.print("    [  Masukkan PIN anda : "); // Input PIN pengguna
            String inputPIN = scanner1.nextLine();
            System.out.println(
                    "    ============================================================================================");
            System.out.println("");

            // Pengecekan apakah status akunnya diblokir atau tidak
            if (hasil != -1 && akunData[hasil][3].equals("diblokir")) {
                System.out.println("Akun anda berstatus " + akunData[hasil][3]);
                System.out.println(
                        "    ============================================================================================");
            }

            // Pengecekan kesesuaian nomor rekening dan PIN untuk login
            for (int i = 0; i < akunData.length; i++) {
                if (inputNoRek.equals(akunData[i][0]) && inputPIN.equals(akunData[i][1])
                        && akunData[i][3].equals("aman")) {
                    isBoleh = true; // autentifikasi
                    hasil = i; // session
                }
            }

            // Bagian di bawah ini adalah bagian menuju menu
            if (isBoleh) {
                // inisialisasi dan deklarasi variabel untuk transaksi
                String noRekTujuan;
                char konfirmasiTransaksi;
                int nomTransfer, nomTarik, nomSetor;
                int saldoPengguna = Integer.parseInt(akunData[hasil][2]);

                // Format nilai uang Indonesia Rupiah (IDR)
                NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

                // Perulangan menu berdasarkan konfirmasiTransaksi user
                do {
                    System.out.println(
                            "    ============================================================================================");
                    System.out.println(
                            "    [  Silakan memilih menu dibawah ini :                                                      ]");
                    System.out.println(
                            "    [   _________________                                                                      ]");
                    System.out.println(
                            "    [  |_1._Transfer_____|                                                                     ]");
                    System.out.println(
                            "    [   _________________                                                                      ]");
                    System.out.println(
                            "    [  |_2._Tarik tunai__|                                                                     ]");
                    System.out.println(
                            "    [   ________________                                                                       ]");
                    System.out.println(
                            "    [  |_3._Setor tunai_|                                                                      ]");
                    System.out.println(
                            "    [   __________________________                                                             ]");
                    System.out.println(
                            "    [  |_4._Pembayaran lain-lain__|                                                            ]");
                    System.out.println(
                            "    [   _______________________                                                                ]");
                    System.out.println(
                            "    [  |_5._Riwayat transaksi__|                                                               ]");
                    System.out.println(
                            "    [   ________________                                                                       ]");
                    System.out.println(
                            "    [  |_6._Cek Saldo___|                                                                      ]");
                    System.out.println(
                            "    [   ________________                                                                       ]");
                    System.out.println(
                            "    [  |_7._Ubah PIN____|                                                                      ]");
                    System.out.println(
                            "    ============================================================================================");
                    System.out.print("\tMenu yang dipilih (angka) : "); // User input pilihan menu berupa angka
                    int menu = scanner2.nextInt();
                    System.out.println(
                            "    ============================================================================================");

                    // Pilihan opsi menu
                    switch (menu) {
                        case 1: // OPSI FITUR TRANSFER
                            System.out.println(
                                    "    ============================================================================================");
                            System.out.println(
                                    "    [- - - - - - - - - - - - - - - - -╔╦╗╦═╗╔═╗╔╗╔╔═╗╔═╗╔═╗╦═╗- - - - - - - - - - - - - - - - -]");
                            System.out.println(
                                    "    [- - - - - - - - - - - - - - - - - ║ ╠╦╝╠═╣║║║╚═╗╠╣ ║╣ ╠╦╝- - - - - - - - - - - - - - - - -]");
                            System.out.println(
                                    "    [- - - - - - - - - - - - - - - - - ╩ ╩╚═╩ ╩╝╚╝╚═╝╚  ╚═╝╩╚═- - - - - - - - - - - - - - - - -]");
                            System.out.println(
                                    "    ============================================================================================");
                            System.out.print("\t-- Masukkan nomor rekening tujuan : ");
                            noRekTujuan = scannerTF.nextLine();
                            // Pengecekan apakah nomor rekening tujuan ada di database
                            for (int i = 0; i < akunData.length; i++) {
                                if (noRekTujuan.equals(akunData[i][0])) {
                                    isValid = true;
                                    break;
                                }
                            }

                            // Kondisi jika isValid true
                            if (isValid == true) {
                                System.out.print("\t-- Masukkan nominal transfer : Rp "); // User input nominal transfer
                                nomTransfer = scanner4.nextInt();
                                System.out.println(
                                        "    ============================================================================================");

                                // Konversi nilai output ke rupiah
                                String nomTransferRP = currencyFormat.format(nomTransfer);
                                System.out.println("    [  _______________________________________________  ]");
                                System.out.println("    [ |  $$$  - RINCIAN TRANSFER - $$$\t\t      | ]");
                                System.out.printf("    [ |  Rekening tujuan: %s\t\t      | ]\n", noRekTujuan);
                                System.out.printf("    [ |  Nominal transfer: %s\t\t\t| ]\n", nomTransferRP);
                                System.out.println("    [ ------------------------------------------------- ]");
                                System.out.println(
                                        "    ============================================================================================");
                                // Konfirmasi persetujuan transaksi
                                System.out.println("\t-- Konfirmasi transfer ke rekening " + noRekTujuan
                                        + " dengan nominal " + nomTransferRP + " ?");
                                System.out.print("\t-- Tekan 'Y' untuk Ya. Tekan 'T' untuk tidak.  -->  ");
                                char konfirmasiTF = scanner4.next().charAt(0);
                                System.out.println(
                                        "    ============================================================================================");

                                // Konfirmasi transaksi
                                if (konfirmasiTF == 'y' || konfirmasiTF == 'Y') {
                                    System.out.print("\t-- Masukkan PIN anda: ");
                                    inputPIN = scanner5.nextLine();
                                    System.out.println(
                                            "    ============================================================================================");

                                    // Pengecekan apakah input PIN sesuai dengan database
                                    if (inputPIN.equals(akunData[hasil][1])) {
                                        if (nomTransfer < saldoPengguna) {
                                            saldoPengguna -= nomTransfer; // Pengurangan saldo pengguna dengan nominal
                                                                          // transfer

                                            // Formatting penulisan rupiah pada output
                                            String nomTransferRupiah = currencyFormat.format(nomTransfer);
                                            String saldoRupiah = currencyFormat.format(saldoPengguna);

                                            System.out.println(
                                                    "    --------------------------------------------------------------------------------------------");
                                            System.out.println(
                                                    "     ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ TRANSAKSI BERHASIL ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ");
                                            System.out.println(
                                                    "    --------------------------------------------------------------------------------------------");
                                            System.out.println(
                                                    "    ============================================================================================");
                                            System.out.println("\t-- Sisa saldo anda : " + saldoRupiah);
                                            System.out.println(
                                                    "    ============================================================================================");
                                            // Pencatatan riwayat transaksi
                                            riwayat[riw - count] = "Telah melakukan transaksi ke rekening "
                                                    + noRekTujuan
                                                    + " sebesar " + nomTransferRupiah;
                                            count--;
                                        } else {
                                            // Kondisi jika nominal transfer melebihi jumlah saldo
                                            isValid = false; // Reset nilai isValid
                                            System.out.println(
                                                    "    --------------------------------------------------------------------------------------------");
                                            System.out.println(
                                                    "                     [  (!) Transaksi gagal. Saldo anda tidak mencukupi (!)  ]");
                                            System.out.println(
                                                    "    --------------------------------------------------------------------------------------------");
                                            System.out.println(
                                                    "    ============================================================================================");
                                        }
                                    } else {
                                        // Kondisi jika pengguna input PIN tidak sesuai dengan array akunData
                                        isValid = false; // Reset nilai isValid
                                        System.out.println(
                                                "    --------------------------------------------------------------------------------------------");
                                        System.out.println(
                                                "                                      [  (!) PIN SALAH! (!)  ]");
                                        System.out.println(
                                                "    --------------------------------------------------------------------------------------------");
                                        System.out.println(
                                                "    ============================================================================================");
                                    }
                                } else {
                                    // Kondisi jika pengguna input 't' atau 'T'
                                    isValid = false; // Reset nilai isValid
                                    System.out.println(
                                            "    --------------------------------------------------------------------------------------------");
                                    System.out.println(
                                            "                                 [  (!) TRANSAKSI DIBATALKAN (!)  ]");
                                    System.out.println(
                                            "    --------------------------------------------------------------------------------------------");
                                    System.out.println(
                                            "    ============================================================================================");
                                }
                            } else {
                                // Kondisi jika isValid bernilai FALSE
                                System.out.println(
                                        "    --------------------------------------------------------------------------------------------");
                                System.out.println(
                                        "                    [  (!) Transaksi gagal. Nomor rekening tujuan invalid (!)  ]");
                                System.out.println(
                                        "    --------------------------------------------------------------------------------------------");
                                System.out.println(
                                        "    ============================================================================================");
                            }
                            break; // Break case 1 - menu transfer
                        case 2: // OPSI FITUR TARIK TUNAI
                            System.out.println(
                                    "    ============================================================================================");
                            System.out.println(
                                    "    [- - - - - - - - - - - - - - - - ╔╦╗╔═╗╦═╗╦╦╔═  ╔╦╗╦ ╦╔╗╔╔═╗╦ - - - - - - - - - - - - - - -]");
                            System.out.println(
                                    "    [- - - - - - - - - - - - - - - - -║ ╠═╣╠╦╝║╠╩╗   ║ ║ ║║║║╠═╣║ - - - - - - - - - - - - - - -]");
                            System.out.println(
                                    "    [- - - - - - - - - - - - - - - - -╩ ╩ ╩╩╚═╩╩ ╩   ╩ ╚═╝╝╚╝╩ ╩╩ - - - - - - - - - - - - - - -]");
                            System.out.println(
                                    "    ============================================================================================");
                            System.out.print("\t-- Masukkan nominal tarik tunai : Rp "); // User input nominal tarik
                                                                                         // tunai
                            nomTarik = scanner3.nextInt();
                            System.out.println(
                                    "    ============================================================================================");
                            // Konversi nilai output ke Rupiah
                            String nomTarikRP = currencyFormat.format(nomTarik);
                            // Persetujuan konfirmasi transaksi
                            System.out.println("\t-- Konfirmasi Tarik tunai dengan nominal " + nomTarikRP + " ? ");
                            System.out.print("\t-- Tekan 'Y' untuk Ya. Tekan 'T' untuk tidak.  -->  ");
                            char konfirmasiTarik = scanner4.next().charAt(0);
                            System.out.println(
                                    "    ============================================================================================");

                            if (konfirmasiTarik == 'y' || konfirmasiTarik == 'Y') {
                                System.out.print("\t-- Masukkan PIN anda: ");
                                inputPIN = scanner5.nextLine();
                                System.out.println(
                                        "    ============================================================================================");

                                // Pengecekan apakah input PIN sesuai dengan database
                                if (inputPIN.equals(akunData[hasil][1])) {
                                    if (nomTarik < saldoPengguna) {
                                        // Pengecekan apakah nominal tarik kurang dari saldo pengguna
                                        if (nomTarik <= 5000000) {
                                            // Kondisi jika nomTarik < saldoPengguna dan nomTarik <= 5000000
                                            saldoPengguna -= nomTarik;
                                            // Formating penulisan rupiah pada output
                                            String nomTarikRupiah = currencyFormat.format(nomTarik);
                                            String saldoRupiah = currencyFormat.format(saldoPengguna);
                                            System.out.println(
                                                    "    --------------------------------------------------------------------------------------------");
                                            System.out.println(
                                                    "     ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ TRANSAKSI BERHASIL ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ");
                                            System.out.println(
                                                    "    --------------------------------------------------------------------------------------------");
                                            System.out.println(
                                                    "    ============================================================================================");
                                            System.out.println("\t-- Sisa saldo anda : " + saldoRupiah);
                                            System.out.println(
                                                    "    ============================================================================================");

                                            // Pencatatan riwayat transaksi
                                            riwayat[riw - count] = "Telah melakukan tarik tunai sebesar "
                                                    + nomTarikRupiah;
                                            count--;
                                        } else {
                                            // Kondisi jika nomTarik > 5.000.000
                                            System.out.println(
                                                    "    --------------------------------------------------------------------------------------------");
                                            System.out.println(
                                                    "           [  (!) Transaksi gagal, anda melebihi batas maksimum nominal tarik tunai (!)  ]");
                                            System.out.println(
                                                    "    --------------------------------------------------------------------------------------------");
                                            System.out.println(
                                                    "    ============================================================================================");
                                        }
                                    } else {
                                        // Kondisi jika nomTarik > saldoPengguna
                                        System.out.println(
                                                "    --------------------------------------------------------------------------------------------");
                                        System.out.println(
                                                "                      [  (!) Transaksi gagal, periksa kembali saldo anda (!)  ]");
                                        System.out.println(
                                                "    --------------------------------------------------------------------------------------------");
                                        System.out.println(
                                                "    ============================================================================================");
                                    }
                                } else {
                                    // Kondisi jika pengguna input PIN tidak sesuai dengan array akunData
                                    System.out.println(
                                            "    --------------------------------------------------------------------------------------------");
                                    System.out
                                            .println("                                       [  (!) PIN SALAH! (!)  ]");
                                    System.out.println(
                                            "    --------------------------------------------------------------------------------------------");
                                    System.out.println(
                                            "    ============================================================================================");
                                }
                            } else {
                                // Kondisi jika pengguna input 't' atau 'T'
                                System.out.println(
                                        "    --------------------------------------------------------------------------------------------");
                                System.out
                                        .println("                                 [  (!) TRANSAKSI DIBATALKAN (!)  ]");
                                System.out.println(
                                        "    --------------------------------------------------------------------------------------------");
                                System.out.println(
                                        "    ============================================================================================");
                            }
                            break; // Break case 2 - menu tarik tunai
                        case 3: // OPSI FITUR SETOR TUNAI
                            System.out.println(
                                    "    ============================================================================================");
                            System.out.println(
                                    "    [- - - - - - - - - - - - - - - -╔═╗╔═╗╔╦╗╔═╗╦═╗  ╔╦╗╦ ╦╔╗╔╔═╗╦- - - - - - - - - - - - - - -]");
                            System.out.println(
                                    "    [- - - - - - - - - - - - - - - -╚═╗║╣  ║ ║ ║╠╦╝   ║ ║ ║║║║╠═╣║- - - - - - - - - - - - - - -]");
                            System.out.println(
                                    "    [- - - - - - - - - - - - - - - -╚═╝╚═╝ ╩ ╚═╝╩╚═   ╩ ╚═╝╝╚╝╩ ╩╩- - - - - - - - - - - - - - -]");
                            System.out.println(
                                    "    ============================================================================================");
                            System.out.print("\t-- Masukkan nominal setor tunai : Rp "); // User input nominal setor
                                                                                         // tunai
                            nomSetor = scanner3.nextInt();
                            System.out.println(
                                    "    ============================================================================================");
                            String nomSetorRupiah = currencyFormat.format(nomSetor);
                            System.out.println("\t-- Konfirmasi setor tunai dengan nominal " + nomSetorRupiah + " ? ");
                            System.out.print("\t-- Tekan 'Y' untuk Ya. Tekan 'T' untuk tidak.  -->  ");
                            char konfirmasiSetor = scanner4.next().charAt(0);
                            System.out.println(
                                    "    ============================================================================================");

                            if (konfirmasiSetor == 'y' || konfirmasiSetor == 'Y') {
                                System.out.print("\t-- Masukkan PIN anda: ");
                                inputPIN = scanner5.nextLine();
                                System.out.println(
                                        "    ============================================================================================");
                                if (inputPIN.equals(akunData[hasil][1])) {
                                    saldoPengguna += nomSetor; // Penjumlahan saldo dengan nominal setor yang telah
                                                               // dilakukan
                                    System.out.println(
                                            "    --------------------------------------------------------------------------------------------");
                                    System.out.println(
                                            "     ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ TRANSAKSI BERHASIL ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ");
                                    System.out.println(
                                            "    --------------------------------------------------------------------------------------------");
                                    System.out.println(
                                            "    ============================================================================================");
                                    // Formatting penulisan rupiah pada output
                                    String saldoRupiah = currencyFormat.format(saldoPengguna);

                                    System.out.println("\t-- Sisa saldo anda : " + saldoRupiah);
                                    System.out.println(
                                            "    ============================================================================================");
                                    // Pencatatan riwayat transaksi
                                    riwayat[riw - count] = "Telah melakukan setor tunai sebesar " + nomSetorRupiah;
                                    count--;
                                } else {
                                    // Kondisi jika pengguna input PIN tidak sesuai dengan array akunData
                                    System.out.println(
                                            "    --------------------------------------------------------------------------------------------");
                                    System.out
                                            .println("                                       [  (!) PIN SALAH! (!)  ]");
                                    System.out.println(
                                            "    --------------------------------------------------------------------------------------------");
                                    System.out.println(
                                            "    ============================================================================================");
                                }
                            } else {
                                // Kondisi jika pengguna input 'T' atau 't'
                                System.out.println(
                                        "    --------------------------------------------------------------------------------------------");
                                System.out
                                        .println("                                 [  (!) TRANSAKSI DIBATALKAN (!)  ]");
                                System.out.println(
                                        "    --------------------------------------------------------------------------------------------");
                                System.out.println(
                                        "    ============================================================================================");
                            }
                            break; // Break case 3 - menu setor tunai
                        case 4: // OPSI FITUR PEMBAYARAN LAIN-LAIN
                            System.out.println(
                                    "    ============================================================================================");
                            System.out.println(
                                    "    [- - - - - - - - - -╔═╗╔═╗╔╦╗╔╗ ╔═╗╦ ╦╔═╗╦═╗╔═╗╔╗╔  ╦  ╔═╗╦╔╗╔╔╗╔╦ ╦╔═╗- - - - - - - - - - ]");
                            System.out.println(
                                    "    [- - - - - - - - - -╠═╝║╣ ║║║╠╩╗╠═╣╚╦╝╠═╣╠╦╝╠═╣║║║  ║  ╠═╣║║║║║║║╚╦╝╠═╣- - - - - - - - - - ]");
                            System.out.println(
                                    "    [- - - - - - - - - -╩  ╚═╝╩ ╩╚═╝╩ ╩ ╩ ╩ ╩╩╚═╩ ╩╝╚╝  ╩═╝╩ ╩╩╝╚╝╝╚╝ ╩ ╩ ╩- - - - - - - - - - ]");
                            System.out.println(
                                    "    ============================================================================================");
                            char konfirmasiPulsaUlang = 'y', konfirmasiListrikUlang = 'y',
                                    konfirmasiPendidikanUlang = 'y';

                            int[][] listrikData = { // array listrikData menampung ID PLN & tagihan
                                    { 123123123, 100000 },
                                    { 123456789, 70000 },
                                    { 333444555, 80000 },
                            };
                            int[][] pendidikanData = { // array pendidikanData menampung vritual account (VA) & tagihan
                                    { 232323, 1000000 },
                                    { 454545, 2500000 },
                                    { 909090, 5000000 },
                            };

                            int listrikPilihan = 0; // variabel untuk menampung posisi data listrik ID PLN
                            boolean listrikGate = false; // Sebagai gerbang untuk melakukan proses transaksi listrik
                            int pendidikanPilihan = 0; // variabel untuk menampung posisi data VA (pendidikan)
                            boolean pendidikanGate = false; // sebagai gerbang untuk melakukan proses transaksi
                                                            // pendidikan
                            System.out.println(
                                    "    ============================================================================================");
                            System.out.println(
                                    "    [  Silakan Pilih transaksi pembayaran berikut:                                             ]");
                            System.out.println(
                                    "    [   _____________                                                                          ]");
                            System.out.println(
                                    "    [  |_1._Pulsa____|                                                                         ]");
                            System.out.println(
                                    "    [   _____________                                                                          ]");
                            System.out.println(
                                    "    [  |_2._Listrik__|                                                                         ]");
                            System.out.println(
                                    "    [   ______________                                                                         ]");
                            System.out.println(
                                    "    [  |3._Pendidikan_|                                                                        ]");
                            System.out.println(
                                    "    ============================================================================================");
                            System.out.print("\t-- Menu yang anda pilih (angka): "); // User input pilihan menu
                            int menuBayar = scanner3.nextInt();
                            System.out.println(
                                    "    ============================================================================================");
                            // Opsi menu pembayaran
                            switch (menuBayar) {
                                case 1: // OPSI BAYAR PULSA
                                    do {
                                        System.out.println(
                                                "    ============================================================================================");
                                        System.out.println(
                                                "    [- - - - - - - - - - - - - - - - - - -╔═╗╦ ╦╦  ╔═╗╔═╗ - - - - - - - - - - - - - - - - - - -]");
                                        System.out.println(
                                                "    [- - - - - - - - - - - - - - - - - - -╠═╝║ ║║  ╚═╗╠═╣ - - - - - - - - - - - - - - - - - - -]");
                                        System.out.println(
                                                "    [- - - - - - - - - - - - - - - - - - -╩  ╚═╝╩═╝╚═╝╩ ╩ - - - - - - - - - - - - - - - - - - -]");
                                        System.out.println(
                                                "    ============================================================================================");
                                        System.out.println(
                                                "    ============================================================================================");
                                        System.out.println(
                                                "    [ Pilih operator seluler:                                                                  ]");
                                        System.out.println(
                                                "    [  ____________                                                                            ]");
                                        System.out.println(
                                                "    [ |1._Indosat__|                                                                           ]");
                                        System.out.println(
                                                "    [  ____________                                                                            ]");
                                        System.out.println(
                                                "    [ |2._XL_______|                                                                           ]");
                                        System.out.println(
                                                "    [  _____________                                                                           ]");
                                        System.out.println(
                                                "    [ |3._Telkomsel_|                                                                          ]");
                                        System.out.println(
                                                "    ============================================================================================");
                                        System.out.print("\t-- Operator yang anda pilih: "); // User input jenis
                                                                                             // operator
                                        String operatorPulsa = scanner2.next();
                                        if (operatorPulsa.equals("1")) {
                                            operatorPulsa = "Indosat"; // Perubahan nilai variabel dari "1" menjadi
                                                                       // "Indosat"
                                        } else if (operatorPulsa.equals("2")) {
                                            operatorPulsa = "XL"; // Perubahan nilai variabel dari "2" menjadi
                                                                  // "XL"
                                        } else if (operatorPulsa.equals("3")) {
                                            operatorPulsa = "Telkomsel"; // Perubahan nilai variabel dari "3" menjadi
                                                                         // "Telkomsel"
                                        } else {
                                            System.out.println("Operator invalid");
                                            konfirmasiPulsaUlang = 't';
                                        }
                                        System.out.print("\t-- Input nomor telepon anda: "); // User input nomor telepon
                                        String nomorTelepon = scanner1.nextLine();
                                        System.out.print("\t-- Input nominal pulsa: Rp "); // User input nominal pulsa
                                        int nomPulsa = scanner4.nextInt();
                                        System.out.println(
                                                "    ============================================================================================");
                                        // Konversi nilai nomPulsa ke rupiah
                                        String nomPulsaRP = currencyFormat.format(nomPulsa);
                                        // Menampilkan informasi transaksi sementara
                                        System.out
                                                .println("    [  _________________________________________________\t]");
                                        System.out.println("    [ |  $$$ RINCIAN PEMBAYARAN $$$\t\t\t|\t]");
                                        System.out.printf("    [ |  Operator seluler\t: %s\t\t|\t]\n", operatorPulsa);
                                        System.out.printf("    [ |  Nomor telepon\t\t: %s\t\t|\t]\n", nomorTelepon);
                                        System.out.printf("    [ |  Nominal pulsa\t\t: %s\t\t|\t]\n", nomPulsaRP);
                                        System.out
                                                .println("    [  -------------------------------------------------\t]");
                                        System.out.println(
                                                "    ============================================================================================");
                                        System.out.printf(
                                                "\t-- Konfirmasi transaksi ke nomor telepon %s dengan nominal %s\n",
                                                nomorTelepon, nomPulsaRP);
                                        System.out.print("\t-- Tekan 'Y' untuk Ya. Tekan 'T' untuk tidak.  -->  ");
                                        char konfirmasiPulsa = scanner4.next().charAt(0);
                                        if (konfirmasiPulsa == 'Y' || konfirmasiPulsa == 'y') {
                                            System.out.print("\t-- Masukkan PIN anda : "); // Input PIN pengguna
                                            inputPIN = scanner5.nextLine();
                                            System.out.println(
                                                    "    ============================================================================================");
                                            if (inputPIN.equals(akunData[hasil][1])) {
                                                if (nomPulsa < saldoPengguna) {
                                                    saldoPengguna -= nomPulsa;
                                                    // Formatting nilai ke Rupiah
                                                    String saldoRupiah2 = currencyFormat.format(saldoPengguna);

                                                    System.out.println(
                                                            "    --------------------------------------------------------------------------------------------");
                                                    System.out.println(
                                                            "     ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ TRANSAKSI BERHASIL ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ");
                                                    System.out.println(
                                                            "    --------------------------------------------------------------------------------------------");
                                                    System.out.println(
                                                            "    ============================================================================================");
                                                    // Menampilkan output transaksi berhasil
                                                    System.out.println(
                                                            "    [  _________________________________________________\t]");
                                                    System.out.println("    [ |  $$$ RINCIAN PEMBAYARAN $$$\t\t\t|\t]");
                                                    System.out.printf("    [ |  Operator seluler\t: %s\t\t|\t]\n",
                                                            operatorPulsa);
                                                    System.out.printf("    [ |  Nomor telepon\t\t: %s\t\t|\t]\n",
                                                            nomorTelepon);
                                                    System.out.printf("    [ |  Nominal pulsa\t\t: %s\t\t|\t]\n",
                                                            nomPulsaRP);
                                                    System.out.printf("    [ |  Sisa saldo anda\t: %s\t|\t]\n",
                                                            saldoRupiah2);
                                                    System.out.println(
                                                            "    [  -------------------------------------------------\t]");
                                                    System.out.println(
                                                            "    ============================================================================================");
                                                    // Pencatatan riwayat transaksi
                                                    riwayat[riw - count] = "Telah melakukan pembayaran pulsa senilai "
                                                            + nomPulsaRP;
                                                    count--;

                                                    konfirmasiPulsaUlang = 't'; // Perubahan nilai konfirmasiBayar
                                                                                // menjadi 't' agar
                                                    // proses looping bisa berhenti
                                                } else {
                                                    // Kondisi jika nominal transaksi > saldo pengguna
                                                    System.out.println(
                                                            "    --------------------------------------------------------------------------------------------");
                                                    System.out.println(
                                                            "                     [  (!) Transaksi gagal. Saldo anda tidak mencukupi (!)  ]");
                                                    System.out.println(
                                                            "    --------------------------------------------------------------------------------------------");
                                                    System.out.println(
                                                            "    ============================================================================================");
                                                    System.out.println("\t-- Ingin mengulangi pembayaran pulsa? ");
                                                    System.out.print(
                                                            "\t-- Tekan 'Y' untuk Ya. Tekan 'T' untuk tidak.  -->  ");
                                                    konfirmasiPulsaUlang = scanner4.next().charAt(0);
                                                    System.out.println(
                                                            "    ============================================================================================");
                                                }
                                            } else {
                                                // Kondisi jika PIN tidak sesuai dengan database
                                                System.out.println(
                                                        "    --------------------------------------------------------------------------------------------");
                                                System.out.println(
                                                        "                                      [  (!) PIN SALAH! (!)  ]");
                                                System.out.println(
                                                        "    --------------------------------------------------------------------------------------------");
                                                System.out.println(
                                                        "    ============================================================================================");
                                                System.out.println("\t-- Ingin mengulangi pembayaran pulsa? ");
                                                System.out.print(
                                                        "\t-- Tekan 'Y' untuk Ya. Tekan 'T' untuk tidak.  -->  ");
                                                konfirmasiPulsaUlang = scanner4.next().charAt(0);
                                                System.out.println(
                                                        "    ============================================================================================");
                                            }
                                        } else {
                                            // Kondisi jika pengguna input 'T' atau 't'
                                            System.out.println(
                                                    "    --------------------------------------------------------------------------------------------");
                                            System.out.println(
                                                    "                                 [  (!) TRANSAKSI DIBATALKAN (!)  ]");
                                            System.out.println(
                                                    "    --------------------------------------------------------------------------------------------");
                                            System.out.println(
                                                    "    ============================================================================================");
                                        }
                                    } while (konfirmasiPulsaUlang == 'y' || konfirmasiPulsaUlang == 'Y');
                                    break; // Break case 1 - menu pembayaran pulsa
                                case 2: // OPSI BAYAR LISTRIK
                                    do {
                                        System.out.println(
                                                "    ============================================================================================");
                                        System.out.println(
                                                "    [- - - - - - - - - - - - - - - - - - -╦  ╦╔═╗╔╦╗╦═╗╦╦╔═ - - - - - - - - - - - - - - - - - -]");
                                        System.out.println(
                                                "    [- - - - - - - - - - - - - - - - - - -║  ║╚═╗ ║ ╠╦╝║╠╩╗ - - - - - - - - - - - - - - - - - -]");
                                        System.out.println(
                                                "    [- - - - - - - - - - - - - - - - - - -╩═╝╩╚═╝ ╩ ╩╚═╩╩ ╩ - - - - - - - - - - - - - - - - - -]");
                                        System.out.println(
                                                "    ============================================================================================");
                                        System.out.print("\t-- Masukkan ID pelanggan PLN/Nomor meter: ");
                                        int inputPLN = scanner4.nextInt();
                                        System.out.println(
                                                "    ============================================================================================");

                                        // Pengecekan data ID pelanggan
                                        for (int i = 0; i < listrikData.length; i++) {
                                            if (inputPLN == listrikData[i][0]) {
                                                listrikPilihan = i;
                                                listrikGate = true;
                                                break;
                                            }
                                        }

                                        // Proses perhitungan tagihan listrik PLN
                                        if (listrikGate) {
                                            // Formatting output ke Rupiah
                                            String tagihanListrikRP = currencyFormat
                                                    .format(listrikData[listrikPilihan][1]);
                                            System.out.println(
                                                    "    [  _________________________________________________________\t]");
                                            System.out.println("    [ |  $$$ RINCIAN PEMBAYARAN $$$\t\t\t\t|\t]");
                                            System.out.printf("    [ |  ID PLN\t\t\t: %d\t\t\t|\t]\n", inputPLN);
                                            System.out.printf("    [ |  Total tagihan\t\t: %s\t\t\t|\t]\n",
                                                    tagihanListrikRP);
                                            System.out.println(
                                                    "    [  ---------------------------------------------------------\t]");
                                            System.out.println(
                                                    "    ============================================================================================");
                                            System.out.printf(
                                                    "\t-- Konfirmasi transaksi tagihan listrik dengan ID %s sebesar %s\n",
                                                    inputPLN, tagihanListrikRP);
                                            System.out.print("\t-- Tekan 'Y' untuk Ya. Tekan 'T' untuk tidak.  -->  ");
                                            char konfirmasiListrik = scanner4.next().charAt(0);
                                            if (konfirmasiListrik == 'Y' || konfirmasiListrik == 'y') {
                                                System.out.print("\t-- Masukkan PIN anda : "); // Input PIN pengguna
                                                inputPIN = scanner5.nextLine();
                                                System.out.println(
                                                        "    ============================================================================================");
                                                if (inputPIN.equals(akunData[hasil][1])) {
                                                    if (listrikData[listrikPilihan][1] < saldoPengguna) {
                                                        saldoPengguna -= listrikData[listrikPilihan][1];
                                                        // Formatting saldo pengguna ke Rupiah
                                                        String saldoRupiah3 = currencyFormat.format(saldoPengguna);
                                                        System.out.println(
                                                                "    --------------------------------------------------------------------------------------------");
                                                        System.out.println(
                                                                "     ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ TRANSAKSI BERHASIL ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ");
                                                        System.out.println(
                                                                "    --------------------------------------------------------------------------------------------");
                                                        System.out.println(
                                                                "    ============================================================================================");
                                                        System.out.println(
                                                                "    [  _________________________________________________________\t]");
                                                        System.out.println(
                                                                "    [ |  $$$ RINCIAN PEMBAYARAN $$$\t\t\t\t|\t]");
                                                        System.out.printf("    [ |  ID PLN\t\t\t: %d\t\t\t|\t]\n",
                                                                inputPLN);
                                                        System.out.printf("    [ |  Total tagihan\t\t: %s\t\t\t|\t]\n",
                                                                tagihanListrikRP);
                                                        System.out.printf("    [ |  Sisa saldo anda\t: %s\t\t|\t]\n",
                                                                saldoRupiah3);
                                                        System.out.println(
                                                                "    [  ---------------------------------------------------------\t]");
                                                        System.out.println(
                                                                "    ============================================================================================");

                                                        // Pencatatan riwayat transaksi
                                                        riwayat[riw
                                                                - count] = "Telah melakukan pembayaran tagihan listrik senilai "
                                                                        + tagihanListrikRP;
                                                        count--;
                                                        konfirmasiListrikUlang = 't';
                                                    } else {
                                                        // Kondisi jika tagihan listrik > saldo pengguna
                                                        System.out.println(
                                                                "    --------------------------------------------------------------------------------------------");
                                                        System.out.println(
                                                                "                     [  (!) Transaksi gagal. Saldo anda tidak mencukupi (!)  ]");
                                                        System.out.println(
                                                                "    --------------------------------------------------------------------------------------------");
                                                        System.out.println(
                                                                "    ============================================================================================");
                                                    }
                                                } else {
                                                    // Kondisi jika PIN tidak sesuai dengan database
                                                    System.out.println(
                                                            "    --------------------------------------------------------------------------------------------");
                                                    System.out.println(
                                                            "                                      [  (!) PIN SALAH! (!)  ]");
                                                    System.out.println(
                                                            "    --------------------------------------------------------------------------------------------");
                                                    System.out.println(
                                                            "    ============================================================================================");
                                                }
                                            } else {
                                                // Kondisi jika pengguna input 't' atau 'T'
                                                System.out.println(
                                                        "    --------------------------------------------------------------------------------------------");
                                                System.out.println(
                                                        "                                 [  (!) TRANSAKSI DIBATALKAN (!)  ]");
                                                System.out.println(
                                                        "    --------------------------------------------------------------------------------------------");
                                                System.out.println(
                                                        "    ============================================================================================");
                                            }
                                        } else {
                                            // Kondisi jika ID PLN tidak sesuai dengan data yang ada
                                            System.out.println(
                                                    "    --------------------------------------------------------------------------------------------");
                                            System.out.println(
                                                    "                   [  (!) ID PLN invalid. Silakan input ulang ID PLN anda! (!)  ]");
                                            System.out.println(
                                                    "    --------------------------------------------------------------------------------------------");
                                            System.out.println(
                                                    "    ============================================================================================");
                                            konfirmasiListrikUlang = 'y';
                                        }
                                    } while (konfirmasiListrikUlang == 'y' || konfirmasiListrikUlang == 'Y');
                                    break; // Break case 2 - menu pembayaran LISTRIK
                                case 3: // OPSI BAYAR TAGIHAN PENDIDIKAN
                                    do {
                                        System.out.println(
                                                "    ============================================================================================");
                                        System.out.println(
                                                "    [- - - - - - - - - - - - - - - - ╔═╗╔═╗╔╗╔╔╦╗╦╔╦╗╦╦╔═╔═╗╔╗╔ - - - - - - - - - - - - - - - -]");
                                        System.out.println(
                                                "    [- - - - - - - - - - - - - - - - ╠═╝║╣ ║║║ ║║║ ║║║╠╩╗╠═╣║║║ - - - - - - - - - - - - - - - -]");
                                        System.out.println(
                                                "    [- - - - - - - - - - - - - - - - ╩  ╚═╝╝╚╝═╩╝╩═╩╝╩╩ ╩╩ ╩╝╚╝ - - - - - - - - - - - - - - - -]");
                                        System.out.println(
                                                "    ============================================================================================");
                                        System.out.print("\t-- Masukkan nomor virtual account : ");
                                        int inputVA = scanner4.nextInt();
                                        System.out.println(
                                                "    ============================================================================================");
                                        // pengecekan data VA
                                        for (int i = 0; i < pendidikanData.length; i++) {
                                            if (inputVA == pendidikanData[i][0]) {
                                                pendidikanPilihan = i;
                                                pendidikanGate = true;
                                                break;
                                            }
                                        }
                                        // Proses perhitungan tagihan biaya pendidikan
                                        if (pendidikanGate) {
                                            String tagihanPendidikanRP = currencyFormat
                                                    .format(pendidikanData[pendidikanPilihan][1]);
                                            System.out.println(
                                                    "    [  _________________________________________________________\t]");
                                            System.out.println("    [ |  $$$ RINCIAN PEMBAYARAN $$$\t\t\t\t|\t]");
                                            System.out.printf("    [ |  Nomor VA\t\t: %d\t\t\t|\t]\n", inputVA);
                                            System.out.printf("    [ |  Total tagihan\t\t: %s\t\t|\t]\n",
                                                    tagihanPendidikanRP);
                                            System.out.println(
                                                    "    [  ---------------------------------------------------------\t]");
                                            System.out.println(
                                                    "    ============================================================================================");
                                            System.out.printf(
                                                    "\t-- Konfirmasi transaksi tagihan biaya pendidikan sebesar %s\n",
                                                    tagihanPendidikanRP);
                                            System.out.print("\t-- Tekan 'Y' untuk Ya. Tekan 'T' untuk tidak.  -->  ");
                                            char konfirmasiPendidikan = scanner4.next().charAt(0);
                                            if (konfirmasiPendidikan == 'Y' || konfirmasiPendidikan == 'y') {
                                                System.out.print("\t-- Masukkan PIN anda : "); // Input PIN pengguna
                                                inputPIN = scanner5.nextLine();
                                                System.out.println(
                                                        "    ============================================================================================");
                                                if (inputPIN.equals(akunData[hasil][1])) {
                                                    if (pendidikanData[pendidikanPilihan][1] < saldoPengguna) {
                                                        saldoPengguna -= pendidikanData[pendidikanPilihan][1];
                                                        // Formatting output ke Rupiah
                                                        String saldoRupiah3 = currencyFormat.format(saldoPengguna);
                                                        System.out.println(
                                                                "    --------------------------------------------------------------------------------------------");
                                                        System.out.println(
                                                                "     ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ TRANSAKSI BERHASIL ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ");
                                                        System.out.println(
                                                                "    --------------------------------------------------------------------------------------------");
                                                        System.out.println(
                                                                "    ============================================================================================");
                                                        System.out.println(
                                                                "    [  _________________________________________________________\t]");
                                                        System.out.println(
                                                                "    [ |  $$$ RINCIAN PEMBAYARAN $$$\t\t\t\t|\t]");
                                                        System.out.printf("    [ |  Nomor VA\t\t: %d\t\t\t|\t]\n",
                                                                inputVA);
                                                        System.out.printf("    [ |  Total tagihan\t\t: %s\t\t|\t]\n",
                                                                tagihanPendidikanRP);
                                                        System.out.printf("    [ |  Sisa saldo anda\t: %s\t\t|\t]\n",
                                                                saldoRupiah3);
                                                        System.out.println(
                                                                "    [  ---------------------------------------------------------\t]");
                                                        System.out.println(
                                                                "    ============================================================================================");

                                                        // Pencatatan riwayat transaksi
                                                        riwayat[riw
                                                                - count] = "Telah melakukan pembayaran tagihan pendidikan senilai "
                                                                        + tagihanPendidikanRP;
                                                        count--;
                                                        konfirmasiPendidikanUlang = 't';
                                                    } else {
                                                        // Kondisi jika tagihan pendidikan > saldo pengguna
                                                        System.out.println(
                                                                "    --------------------------------------------------------------------------------------------");
                                                        System.out.println(
                                                                "                     [  (!) Transaksi gagal. Saldo anda tidak mencukupi (!)  ]");
                                                        System.out.println(
                                                                "    --------------------------------------------------------------------------------------------");
                                                        System.out.println(
                                                                "    ============================================================================================");
                                                    }
                                                } else {
                                                    // Kondisi jika PIN tidak sesuai dengan database
                                                    System.out.println(
                                                            "    --------------------------------------------------------------------------------------------");
                                                    System.out.println(
                                                            "                                      [  (!) PIN SALAH! (!)  ]");
                                                    System.out.println(
                                                            "    --------------------------------------------------------------------------------------------");
                                                    System.out.println(
                                                            "    ============================================================================================");
                                                }
                                            } else {
                                                // Kondisi jika pengguna input 't' atau 'T'
                                                System.out.println(
                                                        "    --------------------------------------------------------------------------------------------");
                                                System.out.println(
                                                        "                                 [  (!) TRANSAKSI DIBATALKAN (!)  ]");
                                                System.out.println(
                                                        "    --------------------------------------------------------------------------------------------");
                                                System.out.println(
                                                        "    ============================================================================================");
                                            }
                                        } else {
                                            // Kondisi jika nomor VA tidak ditemukan di database
                                            System.out.println(
                                                    "    --------------------------------------------------------------------------------------------");
                                            System.out.println(
                                                    "                     [  (!) VA invalid. Silakan input ulang nomor VA anda! (!)  ]");
                                            System.out.println(
                                                    "    --------------------------------------------------------------------------------------------");
                                            System.out.println(
                                                    "    ============================================================================================");
                                            konfirmasiPendidikanUlang = 'y';
                                        }
                                    } while (konfirmasiPendidikanUlang == 'y' || konfirmasiPendidikanUlang == 'Y');
                                    break; // Break case 3 - menu pembayaran pendidikan
                                default:
                                    System.out.println(
                                            "    --------------------------------------------------------------------------------------------");
                                    System.out.println(
                                            "                   [  (!) Input tidak sesuai. Periksa kembali inputan anda (!)  ]");
                                    System.out.println(
                                            "    --------------------------------------------------------------------------------------------");
                                    System.out.println(
                                            "    ============================================================================================");
                                    break; // Break case default - menu pembayaran lain-lain
                            }
                            break; // Break case 4 - menu pembayaran lain-lain
                        case 5: // OPSI FITUR RIWAYAT TRANSAKSI
                            System.out.println(
                                    "    ============================================================================================");
                            System.out.println(
                                    "    [- - - - - - - - - - -╦═╗╦╦ ╦╔═╗╦ ╦╔═╗╔╦╗  ╔╦╗╦═╗╔═╗╔╗╔╔═╗╔═╗╦╔═╔═╗╦- - - - - - - - - - - -]");
                            System.out.println(
                                    "    [- - - - - - - - - - -╠╦╝║║║║╠═╣╚╦╝╠═╣ ║    ║ ╠╦╝╠═╣║║║╚═╗╠═╣╠╩╗╚═╗║- - - - - - - - - - - -]");
                            System.out.println(
                                    "    [- - - - - - - - - - -╩╚═╩╚╩╝╩ ╩ ╩ ╩ ╩ ╩    ╩ ╩╚═╩ ╩╝╚╝╚═╝╩ ╩╩ ╩╚═╝╩- - - - - - - - - - - -]");
                            System.out.println(
                                    "    ============================================================================================");
                            System.out.println(
                                    "    ============================================================================================");
                            System.out.println(
                                    "\t|                         ________________________________                        |");
                            System.out.println(
                                    "\t|                         \\RIWAYAT TRANSAKSI TERBARU ANDA/                        |");
                            System.out.println(
                                    "\t|                          ------------------------------                         |");
                            // Menampilkan output riwayat transaksi
                            int j = 0;
                            for (String i : riwayat) {
                                if (i != null) {
                                    j++;
                                    // Menampilkan output
                                    System.out.printf("\t| %d. %s\n", j, i);
                                }
                            }
                            System.out.println(
                                    "         ---------------------------------------------------------------------------------");
                            System.out.printf("        | Anda telah melakukan %d transaksi\t\t\t\t\t\t  |\n", j);
                            System.out.println(
                                    "    ============================================================================================");
                            break; // Break case 5
                        case 6: // OPSI FITUR CEK SALDO
                            System.out.println(
                                    "    ============================================================================================");
                            System.out.println(
                                    "    [- - - - - - - - - - - - - - - - ╔═╗╔═╗╦╔═  ╔═╗╔═╗╦  ╔╦╗╔═╗ - - - - - - - - - - - - - - - -]");
                            System.out.println(
                                    "    [- - - - - - - - - - - - - - - - ║  ║╣ ╠╩╗  ╚═╗╠═╣║   ║║║ ║ - - - - - - - - - - - - - - - -]");
                            System.out.println(
                                    "    [- - - - - - - - - - - - - - - - ╚═╝╚═╝╩ ╩  ╚═╝╩ ╩╩═╝═╩╝╚═╝ - - - - - - - - - - - - - - - -]");
                            System.out.println(
                                    "    ============================================================================================");
                            // Formatting penulisan rupiah pada output
                            String saldoRupiah3 = currencyFormat.format(saldoPengguna);
                            System.out.println(
                                    "    ============================================================================================");
                            System.out.println(
                                    "    [            ________________________________________________________________              ]");
                            System.out.printf("    [           | Saldo anda sebesar %s\t\t\t\t |\t       ]\n",
                                    saldoRupiah3);
                            System.out.println(
                                    "    [            ----------------------------------------------------------------              ]");
                            System.out.println(
                                    "    ============================================================================================");
                            break; // Break case 6
                        case 7: // OPSI UBAH PIN
                            System.out.println(
                                    "    ============================================================================================");
                            System.out.println(
                                    "    [- - - - - - - - - - - - - - - - - ╦ ╦╔╗ ╔═╗╦ ╦  ╔═╗╦╔╗╔- - - - - - - - - - - - - - - - - -]");
                            System.out.println(
                                    "    [- - - - - - - - - - - - - - - - - ║ ║╠╩╗╠═╣╠═╣  ╠═╝║║║║- - - - - - - - - - - - - - - - - -]");
                            System.out.println(
                                    "    [- - - - - - - - - - - - - - - - - ╚═╝╚═╝╩ ╩╩ ╩  ╩  ╩╝╚╝- - - - - - - - - - - - - - - - - -]");
                            System.out.println(
                                    "    ============================================================================================");
                            System.out.print("    Masukkan nomor rekening : "); // Input nomor rekening
                            inputNoRek = scanner1.nextLine();
                            for (int i = 0; i < akunData.length; i++) {
                                if (inputNoRek.equals(akunData[i][0])) {
                                    int ganti = i;
                                    System.out.print("    Masukkan PIN anda : "); // Input PIN pengguna
                                    String inputPIN2 = scanner1.nextLine();
                                    if (inputPIN2.equals(akunData[ganti][1])) {
                                        System.out.println(
                                                "    ============================================================================================");
                                        System.out.print("    Masukkan PIN baru: ");
                                        String inputPIN3 = scanner1.nextLine();
                                        System.out.print("    Konfirmasi PIN baru: ");
                                        String inputPIN4 = scanner1.nextLine();
                                        if (inputPIN3.equals(inputPIN4)) {
                                            akunData[ganti][1] = inputPIN3;
                                            System.out.println(
                                                    "    ============================================================================================");
                                            System.out.println("    PIN BERHASIL DIRUBAH");
                                            System.out.println(
                                                    "    ============================================================================================");
                                            break;
                                        } else {
                                            System.out.println(
                                                    "    ============================================================================================");
                                            System.out.println("    PIN BARU DAN KONFIRMASI TIDAK SAMA");
                                            System.out.println(
                                                    "    ============================================================================================");
                                            break;
                                        }
                                    } else {
                                        System.out.println(
                                                "    ============================================================================================");
                                        System.out.println("    PIN TIDAK SESUAI");
                                        System.out.println(
                                                "    ============================================================================================");
                                        break;
                                    }
                                } else {
                                    System.out.println(
                                            "    ============================================================================================");
                                    System.out.println("    NOMOR REKENING TIDAK VALID");
                                    System.out.println(
                                            "    ============================================================================================");
                                    break;
                                }
                            }
                            break; // Break switch-case 7
                        default:
                            System.out.println(
                                    "    ============================================================================================");
                            System.out.println(
                                    "                   [  (!) Input tidak sesuai. Periksa kembali inputan anda (!)  ]");
                            System.out.println(
                                    "    ============================================================================================");
                            break; // Break switch-case opsi menu
                    }
                    System.out.println("\t-- Lakukan transaksi lagi?");
                    System.out.print("\t-- Tekan 'Y' untuk Ya. Tekan 'T' untuk tidak.  -->  ");
                    konfirmasiTransaksi = scanner2.next().charAt(0);
                    if (konfirmasiTransaksi == 't' || konfirmasiTransaksi == 'T') {
                        System.out.println(
                                "    ============================================================================================");
                        System.out.println(
                                "     ~ ~ ~ ~ ~ ~ ~ Terimakasih telah bertransaksi! Semoga harimu selalu bahagia :) ~ ~ ~ ~ ~ ~ ~");
                        System.out.println(
                                "    ============================================================================================");
                        break;
                    }
                } while (konfirmasiTransaksi == 'y' || konfirmasiTransaksi == 'Y');
                break; // Break do-while opsi menu
            } else {
                System.out.println(
                        "    ============================================================================================");
                System.out.println(
                        "    --------------------------------------------------------------------------------------------");
                System.out.println(
                        "               [  (!) Gagal login, periksa kembali nomor rekening dan PIN anda (!)  ]");
                System.out.println(
                        "    --------------------------------------------------------------------------------------------");
                System.out.println(
                        "    ============================================================================================");
                loginAttempts++;

                if (loginAttempts >= maxLoginAttempts) {
                    System.out.println(
                            "    ============================================================================================");
                    System.out.println(
                            "    --------------------------------------------------------------------------------------------");
                    System.out.println(
                            "               [  (!) Anda telah gagal lebih dari 3 kali. Akun anda diblokir. (!)  ]");
                    System.out.println(
                            "    --------------------------------------------------------------------------------------------");
                    System.out.println(
                            "    ============================================================================================");
                    akunData[hasil][3] = "diblokir";
                    break; // Account diblokir, mengakhiri looping
                }
            } // Bagian akhir program menu
        }
        scanner1.close();
        scanner2.close();
        scanner3.close();
        scanner4.close();
        scanner5.close();
        scannerTF.close();
    }
}