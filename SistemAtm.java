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
                { "7777777", "7777", "10000000", "aman" }
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

        Scanner scanner1 = new Scanner(System.in); // scanner 1
        Scanner scanner2 = new Scanner(System.in); // scanner 2
        Scanner scanner3 = new Scanner(System.in);
        Scanner scanner4 = new Scanner(System.in);
        Scanner scanner5 = new Scanner(System.in);
        Scanner scannerTF = new Scanner(System.in);
        
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
                    System.out.println("7. Ubah PIN");
                    System.out.print("Menu yang dipilih (angka) : "); // User input pilihan menu berupa angka
                    int menu = scanner2.nextInt();
                    System.out.println("**********************************************");

                    // Pilihan opsi menu
                    switch (menu) {
                        case 1: // OPSI FITUR TRANSFER
                            System.out.println("ANDA MEMILIH MENU TRANSFER");
                            System.out.println("**********************************************");
                            System.out.print("Masukkan nomor rekening tujuan : ");
                            no_rek_tujuan = scannerTF.nextLine();
                            // Pengecekan apakah nomor rekening tujuan ada di database
                            for (int i = 0; i < akunData.length; i++) {
                                if (no_rek_tujuan.equals(akunData[i][0])) {
                                    tujuan = i;
                                    isValid = true;
                                    break;
                                }
                            }

                            // Kondisi jika isValid true
                            if (isValid) {
                                System.out.print("Masukkan nominal transfer : Rp "); // User input nominal transfer
                                nom_transfer = scanner4.nextInt();
                                System.out.println("**********************************************");

                                // Pengecekan nilai nominal transfer dibandingkan dengan jumlah saldo
                                if (nom_transfer <= saldoPengguna) {
                                    System.out.println("RINCIAN TRANSFER");
                                    System.out.println("Rekening tujuan: " + no_rek_tujuan);
                                    System.out.println("Nominal transfer: " + nom_transfer);
                                    System.out.println("**********************************************");
                                    System.out.print("Konfirmasi Transfer ke rekening "+ no_rek_tujuan + " dengan nominal "+ nom_transfer + " ? (y/t)");
                                    char konfirmasiTF = scanner4.next().charAt(0);

                                    // Konfirmasi transaksi
                                    if (konfirmasiTF == 'y' || konfirmasiTF == 'Y') {
                                        System.out.print("Masukkan PIN anda: ");
                                        String inputPIN = scanner5.nextLine();
                                        System.out.println("**********************************************");

                                        // Pengecekan apakah input PIN sesuai dengan database
                                        if (inputPIN.equals(akunData[hasil][1])) {
                                            saldoPengguna -= nom_transfer; // Pengurangan saldo pengguna dengan nominal transfer
                                            // Formatting penulisan rupiah pada output
                                            String nom_transferRupiah = currencyFormat.format(nom_transfer);
                                            String saldoRupiah = currencyFormat.format(saldoPengguna);
                                            System.out.println("TRANSAKSI BERHASIL");
                                            System.out.println("Sisa saldo anda : " + saldoRupiah);
                                            System.out.println("**********************************************");
                                            // Pencatatan riwayat transaksi
                                            riwayat[riw - count] = "Telah melakukan transaksi ke rekening " + no_rek_tujuan
                                                    + " sebesar " + nom_transferRupiah;
                                            count--;
                                            System.out.println("**********************************************");
                                        } else {
                                            System.out.println("PIN SALAH!");
                                            System.out.println("**********************************************");
                                        }
                                    } else {
                                    // Kondisi jika pengguna input 't' atau 'T'
                                    System.out.println("TRANSAKSI DIBATALKAN");
                                    System.out.println("**********************************************");
                                    }
                                } else {
                                    // Kondisi jika nominal transfer melebihi jumlah saldo
                                    System.out.println("Transaksi gagal. Saldo anda tidak mencukupi");
                                    System.out.println("**********************************************");
                                }
                            } else {
                                // Kondisi jika isValid bernilai FALSE
                                System.out.println("Transaksi gagal, nomor rekening tujuan invalid");
                                System.out.println("**********************************************");
                            }
                            break; // break case 1 menu transfer
                        case 2: // OPSI FITUR TARIK TUNAI
                            System.out.println("ANDA MEMILIH MENU TARIK TUNAI");
                            System.out.println("**********************************************");
                            System.out.print("Masukkan nominal tarik tunai : Rp "); // User input nominal tarik tunai
                            nom_tarik = scanner3.nextInt();
                            System.out.println("**********************************************");
                            System.out.print("Konfirmasi Tarik tunai dengan nominal "+ nom_tarik + " ? (y/t) ");
                            char konfirmasiTarik = scanner4.next().charAt(0);

                            if (konfirmasiTarik == 'y' || konfirmasiTarik == 'Y'){
                                System.out.print("Masukkan PIN anda: ");
                                String inputPIN = scanner5.nextLine();
                                System.out.println("**********************************************");

                                // Pengecekan apakah input PIN sesuai dengan database
                                if (inputPIN.equals(akunData[hasil][1])) {
                                    saldoPengguna -= nom_tarik; // Pengurangan saldo pengguna dengan nominal transfer
                                    // Formatting penulisan rupiah pada output
                                    if (nom_tarik < saldoPengguna) {
                                    // Pengecekan apakah nominal tarik kurang dari atau sama dengan 5 juta       
                                        if (nom_tarik <= 5000000) {
                                         // Kondisi jika nom_tarik < saldoPengguna dan nom_tarik <= 5000000
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
                                             System.out.println("**********************************************");}
                                    }else{
                                    // Kondisi jika nom_tarik > saldoPengguna
                                    System.out.println("Transaksi gagal, periksa kembali saldo anda");
                                    System.out.println("**********************************************");                 
                                    }
                                }else{
                                    System.out.println("PIN SALAH!");
                                    System.out.println("**********************************************");
                                    }break;
                                                                                                               
                            } else {
                            // Kondisi jika pengguna input 't' atau 'T'
                            System.out.println("TRANSAKSI DIBATALKAN");
                            System.out.println("**********************************************");
                            }
                            
                            
                            // Pengecekan apakah nominal tarik kurang dari nilai saldo
                            if (nom_tarik < saldoPengguna) {
                                // Pengecekan apakah nominal tarik kurang dari atau sama dengan 5 juta
                                if (nom_tarik <= 5000000) {
                                    // Kondisi jika nom_tarik < saldoPengguna dan nom_tarik <= 5000000
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
                                // Kondisi jika nom_tarik > saldoPengguna
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
                            System.out.print("Masukkan PIN anda : "); // Input PIN pengguna
                            String pin1 = scanner1.nextLine();
                            saldoPengguna += nom_setor; // Penjumlahan saldo dengan nominal setor yang telah dilakukan
                            System.out.println("Setor tunai berhasil dilakukan");
                            // Formatting penulisan rupiah pada output
                            String nom_setorRupiah = currencyFormat.format(nom_setor);
                            String saldoRupiah = currencyFormat.format(saldoPengguna); 

                            // Menampilkan output sisa saldo
                            System.out.println("Sisa saldo anda : " + saldoRupiah);

                            // Pencatatan riwayat transaksi
                            riwayat[riw - count] = "Telah melakukan setor tunai sebesar " + nom_setorRupiah;
                            count--;
                            System.out.println("**********************************************");
                            break;
                        case 4: // OPSI FITUR PEMBAYARAN LAIN-LAIN
                            char konfirmasiPulsa = 'y', konfirmasiListrik = 'y', konfirmasiPendidikan = 'y'; 

                            int [][] listrikData = { // array listrikData menampung ID PLN & tagihan
                                {123123123, 100000},
                                {123456789, 70000},
                                {333444555, 80000},
                            };
                            int [][] pendidikanData = { // array pendidikanData menampung vritual account (VA) & tagihan
                                {232323, 1000000},
                                {454545, 2500000},
                                {909090, 5000000},
                            };

                            int listrikPilihan = 0; // variabel untuk menampung posisi data listrik ID PLN
                            boolean listrikGate = false; //  Sebagai gerbang untuk melakukan proses transaksi listrik
                            int pendidikanPilihan = 0; // variabel untuk menampung posisi data VA (pendidikan)
                            boolean pendidikanGate = false; // sebagai gerbang untuk melakukan proses transaksi pendidikan
                            
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
                                            operatorPulsa = "XL"; // Perubahan nilai variabel dari "2" menjadi 
                                                                  // "XL"
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
                                        System.out.print("Masukkan PIN anda : "); // Input PIN pengguna
                                        String pin3 = scanner1.nextLine();
                                        if (nomPulsa < saldoPengguna) {
                                            saldoPengguna -= nomPulsa;
                                            String nomPulsaRupiah = currencyFormat.format(nomPulsa);
                                            String saldoRupiah2 = currencyFormat.format(saldoPengguna);
                                        
                                            // Menampilkan output transaksi berhasil
                                            System.out.println("**********************************************");
                                            System.out.println("--- Operator seluler: "+operatorPulsa+"\t\t\t");
                                            System.out.println("--- Nomor telepon Anda: "+nomorTelepon);
                                            System.out.println("--- Pulsa senilai: "+nomPulsaRupiah);
                                            System.out.println("--- Sisa saldo anda "+saldoRupiah2);
                                            System.out.println("**********************************************");
                                            // Pencatatan riwayat transaksi
                                            riwayat[riw - count] = "Telah melakukan pembayaran pulsa senilai " + nomPulsaRupiah;
                                            count--;
                                            
                                            konfirmasiPulsa = 't'; // Perubahan nilai konfirmasiBayar menjadi 't' agar proses looping bisa berhenti
                                        } else {
                                            System.out.println("TRANSAKSI GAGAL! ");
                                            System.out.println("Ingin mengulangi input pembayaran? (Y/T) ");
                                            konfirmasiPulsa = scanner4.next().charAt(0);
                                            System.out.println("**********************************************");
                                        }
                                    } while (konfirmasiPulsa == 'y' || konfirmasiPulsa == 'Y');
                                    break; // Break switch case pada menu pembayaran pulsa
                                case 2: // OPSI BAYAR LISTRIK
                                    do {
                                        System.out.println("ANDA MEMILIH OPSI PEMBAYARAN TAGIHAN LISTRIK");
                                        System.out.println("**********************************************");
                                        System.out.print("Masukkan ID pelanggan PLN/Nomor meter: ");
                                        int inputPLN = scanner4.nextInt();
                                        System.out.println("**********************************************");

                                        // Pengecekan data ID pelanggan
                                        for  (int i = 0; i < listrikData.length; i++) {
                                            if (inputPLN == listrikData[i][0]) {
                                                listrikPilihan = i;
                                                listrikGate = true;
                                                break;
                                            }
                                        }

                                        // Proses perhitungan tagihan listrik PLN
                                        if (listrikGate) {
                                            if (listrikData[listrikPilihan][1] < saldoPengguna) {
                                            saldoPengguna -= listrikData[listrikPilihan][1];
                                            // Formatting output ke Rupiah
                                            String tagihanRupiah = currencyFormat.format(listrikData[listrikPilihan][1]);
                                            String saldoRupiah3 = currencyFormat.format(saldoPengguna);
                                            System.out.println("--- Jumlah tagihan listrik anda: " + tagihanRupiah);
                                            System.out.println("--- Sisa saldo anda: " + saldoRupiah3);
                                            System.out.println("**********************************************");

                                            // Pencatatan riwayat transaksi
                                            riwayat[riw - count] = "Telah melakukan pembayaran tagihan listrik senilai " + tagihanRupiah;
                                            count--;
                                            konfirmasiListrik = 't';
                                            }
                                        } else {
                                            System.out.println("ID PLN invalid. Silakan input ulang ID PLN anda!");
                                            System.out.println("**********************************************");
                                            konfirmasiListrik = 'y';
                                        }
                                    } while (konfirmasiListrik == 'y' || konfirmasiListrik == 'Y');
                                    break; //Break case 2 opsi LISTRIK
                                case 3 : // OPSI BAYAR TAGIHAN PENDIDIKAN
                                    do {
                                        System.out.println("ANDA MEMILIH OPSI PEMBAYARAN PENDIDIKAN");
                                        System.out.println("**********************************************");
                                        System.out.print("Masukkan nomor virtual account : ");
                                        int inputVA = scanner4.nextInt();
                                        System.out.println("**********************************************");
                                        //pengecekan data VA
                                        for  (int i = 0; i < pendidikanData.length; i++) {
                                            if (inputVA == pendidikanData[i][0]) {
                                                pendidikanPilihan = i;
                                                pendidikanGate = true;
                                                break;
                                            }
                                        }

                                        // Menampilkan nominal dan konfirmasi pembayaran
                                        System.out.println("Nominal yang perlu anda bayar: " + pendidikanData[pendidikanPilihan][1]);
                                        System.out.println("Apakah anda yakin untuk membayarnya? (Y/T)");
                                        char konfrimasiBayar;

                                        // Proses perhitungan tagihan biaya pendidikan
                                        if (pendidikanGate) {
                                            if (pendidikanData[pendidikanPilihan][1] < saldoPengguna) {
                                            saldoPengguna -= pendidikanData[pendidikanPilihan][1];
                                            // Formatting output ke Rupiah
                                            String tagihanRupiah = currencyFormat.format(pendidikanData[listrikPilihan][1]);
                                            String saldoRupiah3 = currencyFormat.format(saldoPengguna);
                                            System.out.println("--- Jumlah tagihan pendidikan anda: " + tagihanRupiah);
                                            System.out.println("--- Sisa saldo anda: " + saldoRupiah3);
                                            System.out.println("**********************************************");

                                            // Pencatatan riwayat transaksi
                                            riwayat[riw - count] = "Telah melakukan pembayaran tagihan pendidikan senilai " + tagihanRupiah;
                                            count--;
                                            konfirmasiListrik = 't';
                                            }
                                        } else {
                                            System.out.println("NIM invalid. Silakan input ulang NIM anda!");
                                            System.out.println("**********************************************");
                                            konfirmasiListrik = 'y';
                                        }
                                    
                                    
                                    } while (konfirmasiListrik == 'y' || konfirmasiListrik == 'Y');
                                    break; // Break case 3 pembayaran pendidikan
                                    default:
                                    System.out.println("Input tidak sesuai. Periksa kembali inputan anda");
                                    System.out.println("**********************************************");
                                    break; // Break switch-case pada menu pembayaran lain-lain
                            }
                            break; // Break switch-case pada case 4
                        case 5: // OPSI FITUR RIWAYAT TRANSAKSI
                            System.out.println("ANDA MEMILIH MENU RIWAYAT TRANSAKSI");
                            System.out.println("**********************************************");
                            System.out.println("Ini adalah riwayat transaksi terbaru anda:");
                            System.out.println("----------------------------------------------");
                            // Menampilkan output riwayat transaksi
                            int j = 0;
                            for (String i : riwayat) {
                                if (i != null) {
                                    j++;
                                    // Formatting tampilan output
                                    String formattedString = String.format("%d. %s", j, i);
                                    // Menampilkan output
                                    System.out.println(formattedString);
                                }
                            }
                            System.out.println("Anda telah melakukan " + j +" transaksi");
                            System.out.println("**********************************************");
                            break; // Break case 5
                        case 6: // OPSI FITUR CEK SALDO
                            System.out.println("ANDA MEMILIH MENU CEK SALDO");
                            System.out.println("**********************************************");

                            // Formatting penulisan rupiah pada output
                            String saldoRupiah3 = currencyFormat.format(saldoPengguna);
                            System.out.println("Saldo anda sebesar "+ saldoRupiah3);
                            break; // Break case 6
                        case 7: // opsi ubah PIN
                            System.out.println("ANDA MEMILIH OPSI ubah PIN");
                            System.out.println("**********************************************");
                            System.out.print("Masukkan nomor rekening : "); // Input nomor rekening
                            String input_no_rek1 = scanner1.nextLine();

                            System.out.print("Masukkan PIN anda : "); // Input PIN pengguna
                            String input_pin2 = scanner1.nextLine();

                            if (isBoleh){
                                System.out.print("Masukkan PIN baru: ");
                                String input_pin3 = scanner1.nextLine();
                                System.out.print("Konfirmasi PIN baru: ");
                                String input_pin4 = scanner1.nextLine();
                                System.out.println("PIN BERHASIL DIRUBAH");
                                
                                
                            } else {
                                System.out.println("PIN GAGAL Dirubah");
                            }
                            break; // Break switch-case 7
                        default:
                            System.out.println("Input tidak sesuai. Periksa kembali inputan anda");
                            System.out.println("**********************************************");
                            break; // Break switch-case opsi menu
                    //System.out.println("**********************************************");
                    }
                    System.out.print("Lakukan transaksi lagi? (Y/T) : ");
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
            System.out.println("======================================================================");
        }
        scanner1.close();
        scanner2.close();
        scanner3.close();
        scanner4.close();
    }
}