
// Program sistem ATM - Kelompok 4 - TI-1B
// Fitur yang tersedia: autentifikasi pengguna, transfer, tarik tunai, setor tunai, 
// pembayaran lain-lain, riwayat transaksi, cek saldo, ubah PIN, dan EXIT
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;

public class Function000SistemATM {
    static Scanner scanner1 = new Scanner(System.in);
    static Scanner scanner2 = new Scanner(System.in);
    static Scanner scanner3 = new Scanner(System.in);
    static Scanner scanner4 = new Scanner(System.in);
    static Scanner scanner5 = new Scanner(System.in);
    static Scanner scannerTF = new Scanner(System.in);
	
	private static final String red = "\u001B[31m";
	private static final String reset = "\u001B[0m";

    // inisialisasi dan deklarasi variabel yang dibutuhkan
    // array akun di ATM POLINEMA
    static String[][] accountData = {
            { "1234567", "1234", "7000000", "aman" },
            { "7654321", "5678", "4000000", "aman" },
            { "7777777", "7777", "10000000", "aman" },
            { "0000000", "0000", "900000000", "aman" }
    };

    // array listrikData menampung ID PLN & tagihan
    static int[][] listrikData = {
            { 123123123, 100000 },
            { 123456789, 70000 },
            { 333444555, 80000 },
    };

    // array pendidikanData menampung virtual account (VA) & tagihan
    static int[][] pendidikanData = {
            { 232323, 1000000 },
            { 454545, 2500000 },
            { 909090, 5000000 },
    };

    // array tagihan pada pdam menampung virtual account (VA) & tagihan
    static int[][] tagihanAirData = {
            { 232323, 100000 },
            { 454545, 250000 },
            { 909090, 500000 },
    };

    // array tagihan pada BPJS menampung virtual accounr (VA) & tagihan
    static int[][] BPJSdata = {
            { 232323, 125000 },
            { 454545, 250000 },
            { 909090, 500000 },
    };

    // 'Login' feature variables
    static String inputUser_AccountNumber;
    static int accountLineIndex = 0, loginAttempts = 1;
    static boolean isAccountValid = false, isAccountNumberValid = false,
            isAccountFind = false;
    static final int MAX_LOGIN_ATTEMPTS = 3;

    // 'Transfer' feature variables
    static int transferAmount;
    static String inputTarget_AccountNumber;
    static boolean isTargetAccountValid = false;

    // 'Tarik tunai' variables
    static int cashWithdrawalAmount;

    // 'Setor tunai' variables
    static int cashDepositAmount;

    // 'Cek saldo' variables
    static int userBalance = Integer.parseInt(accountData[accountLineIndex][2]);

    // 'Riwayat transaksi' features variables
    static int maxTransactionHistory = 10, transactionCount = 10;
    static String[] transactionHistory = new String[maxTransactionHistory];

    // 'Exit' feature variables
    static boolean isStopTransaction = false;

    // 'Listrik' feature variables
    static int indexListrik = 0;
    static boolean isListrikValid = false;

    // 'Pendidikan' feature variables
    static int indexPendidikan = 0;
    static boolean isPendidikanValid = false;

    // 'PDAM' feature variables
    static int indexPdam = 0;
    static boolean isPdamValid = false;

    // 'BPJS' feature variables
    static int indexBpjs = 0;
    static boolean isBpjsValid = false;

    // Konfirmasi transaksi ulang features variables
    static char continueTransaction = 't', userChoice = 't',
            userConfirmation;
    static boolean isContinueTransaction = false;

    // 'Validasi PIN' variables
    static String inputPin;

    // Format nilai uang Indonesia Rupiah (IDR)
    static NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    public static void main(String[] args) {
        while (!isAccountValid) {
            PageMenu();
            Login();
        }

        Menu();
    }

    public static void PageMenu() {
        ClearScreen();
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
    }

    public static boolean Login() {
        loginAttempts = 0;
        boolean tryToLogin = false;

        do {
            System.out.print("Masukkan nomor rekening anda: ");
            inputUser_AccountNumber = scanner1.nextLine();

            // Pengecekan kesesuaian input nomor rekening dengan data yang ada
            isAccountFind = false;
            for (int i = 0; i < accountData.length; i++) {
                if (inputUser_AccountNumber.equals(accountData[i][0])) {
                    isAccountFind = true;
                    accountLineIndex = i;
                    break;
                }
            }

            if (!isAccountFind) {
                System.out.println(
                        "Nomor rekening tidak ditemukan. Mohon input nomor rekening anda lagi!");
                EnterForContinue();
                ClearScreen();
                return false;
            }

            isAccountNumberValid = false;
            if (isAccountFind) {
                if (accountData[accountLineIndex][3].equals("aman")) {
                    isAccountNumberValid = true;
                }
            }

            if (isAccountNumberValid) {
                // Check the PIN with a maximum of MAX_LOGIN_ATTEMPTS attempts
                for (loginAttempts = 1; loginAttempts <= MAX_LOGIN_ATTEMPTS; loginAttempts++) {
                    // Check if the entered PIN matches the account PIN
                    if (PinValidation()) {
                        isAccountValid = true;
                        return true; // Successful login
                    } else {
                        ClearScreen();
                        WrongPin();
                        System.out.println("Percobaan login " + loginAttempts + "/"
                                + MAX_LOGIN_ATTEMPTS);
                        EnterForContinue();
                        ClearScreen();
                    }
                }
            } else {
                tryToLogin = true;
                ClearScreen();
                System.out.printf(
                        "Nomor rekening anda (%s) telah diblokir. Silakan masukkan nomor rekening yang lain\n",
                        inputUser_AccountNumber);
                EnterForContinue();
                ClearScreen();
                return false;
            }

            // If the maximum login attempts are reached and status akun will change to
            // "TERBLOKIR"
            if (loginAttempts > MAX_LOGIN_ATTEMPTS) {
                System.out.println(
                        "Anda telah salah memasukkan PIN sebanyak 3 kali. Mohon maaf, nomor rekening Anda kami blokir.");
                accountData[accountLineIndex][3] = "TERBLOKIR";
                System.out.println("STATUS AKUN ANDA : " + accountData[accountLineIndex][3]);
                EnterForContinue();
            }
        } while (tryToLogin || !isAccountFind);
        return false;
    }

    public static void WrongPin() {
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
    }

    public static void Menu() {
        // Perulangan menu berdasarkan continueTransaction user
        do {
            ClearScreen();
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
                    "    [   ______________                                                                         ]");
            System.out.println(
                    "    [  |_8. Keluar____|                                                                        ]");
            System.out.println(
                    "    ============================================================================================");
            System.out.print("\tMenu yang dipilih (angka) : "); // User input pilihan menu berupa angka
            int userChoiceMenu = scanner2.nextInt();

            ClearScreen();
            switch (userChoiceMenu) {
                case 1:
                    Transfer();
                    break;
                case 2:
                    TarikTunai();
                    break;
                case 3:
                    SetorTunai();
                    break;
                case 4:
                    PembayaranLainnya();
                    break;
                case 5:
                    RiwayatTransaksi();
                    break;
                case 6:
                    CekSaldo();
                    break;
                case 7:
                    UbahPin();
                    break;
                case 8:
                    Exit();
                    break;
                default:
                    defaultCaseMenu();
                    break;
            }

            if (isStopTransaction) {
                isContinueTransaction = false;
            }

            if (userChoiceMenu != 8) {
                System.out.println(
                        "    ============================================================================================");
                System.out.println("\t-- Lakukan transaksi lagi?");
                System.out.print("\t-- Tekan 'Y' untuk Ya. Tekan 'T' untuk tidak.  -->  ");
                continueTransaction = scanner2.next().charAt(0);

                ClearScreen();

                if (continueTransaction == 'Y' || continueTransaction == 'y') {
                    isContinueTransaction = true;
                } else {
                    showingThanks();
                }
            }
        } while (isContinueTransaction);
    }

    public static void TransferView() {
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
    }

    public static void Transfer() {
        TransferView();
        System.out.print("\t-- Masukkan nomor rekening tujuan : ");
        inputTarget_AccountNumber = scannerTF.nextLine();
        // Pengecekan apakah nomor rekening tujuan ada di database
        isTargetAccountValid = false;
        for (int i = 0; i < accountData.length; i++) {
            if ((inputTarget_AccountNumber.equals(accountData[i][0]))
                    && (!inputTarget_AccountNumber.equals(inputUser_AccountNumber))) {
                isTargetAccountValid = true;
                break;
            }
        }
        // Kondisi jika isTargetAccountValid true
        if (isTargetAccountValid) {
            System.out.print("\t-- Masukkan nominal transfer : Rp "); // User input nominal transfer
            transferAmount = scanner4.nextInt();
            ClearScreen();
            // Konversi nilai output ke rupiah
            String transferAmountRupiah = currencyFormat.format(transferAmount);
            System.out.println(
                    "    ============================================================================================");
            System.out.println("    [  _______________________________________________  ]");
            System.out.println("    [ |  $$$  - RINCIAN TRANSFER - $$$\t\t      | ]");
            System.out.printf("    [ |  Rekening tujuan: %s\t\t      | ]\n", inputTarget_AccountNumber);
            System.out.printf("    [ |  Nominal transfer: %s\t\t\t| ]\n", transferAmountRupiah);
            System.out.println("    [ ------------------------------------------------- ]");
            System.out.println(
                    "    ============================================================================================");
            // Konfirmasi persetujuan transaksi
            System.out.println("\t-- Konfirmasi transfer ke rekening " + inputTarget_AccountNumber
                    + " dengan nominal " + transferAmountRupiah + " ?");
            UserConfirmation();
            ClearScreen();
            System.out.println(
                    "    ============================================================================================");

            // Konfirmasi transaksi
            if (userConfirmation == 'y' || userConfirmation == 'Y') {
                // Pengecekan apakah input PIN sesuai dengan database
                if (PinValidation()) {
                    if (transferAmount < userBalance) {
                        userBalance -= transferAmount; // Pengurangan saldo pengguna dengan
                        // nominal
                        // transfer

                        // Formatting penulisan rupiah pada output
                        String userBalanceRupiah = currencyFormat.format(userBalance);
                        viewTransactionSuccess();
                        System.out.println("\t-- Sisa saldo anda : " + userBalanceRupiah);
                        System.out.println(
                                "    ============================================================================================");
                        EnterForContinue();
                        ClearScreen();
                        isTargetAccountValid = false;
                        // Pencatatan riwayat transaksi
                        transactionHistory[maxTransactionHistory
                                - transactionCount] = "Telah melakukan transaksi ke rekening "
                                        + inputTarget_AccountNumber
                                        + " sebesar "
                                        + transferAmountRupiah;
                        transactionCount--;
                    } else {
                        // Kondisi jika nominal transfer melebihi jumlah saldo
                        viewBalanceIsNotEnough();
                    }
                } else {
                    // Kondisi jika pengguna input PIN tidak sesuai dengan array accountData
                    viewWrongPin();
                }
            } else {
                // Kondisi jika pengguna input 't' atau 'T'
                viewTransactionCancelled();
            }
        } else {
            // Kondisi jika isTargetAccountValid bernilai FALSE
            ClearScreen();
            System.out.println(
                    "    ============================================================================================");
            System.out.println(
                    "    --------------------------------------------------------------------------------------------");
            System.out.println(
                    "                    [  (!) Transaksi gagal. Nomor rekening tujuan invalid (!)  ]");
            System.out.println(
                    "    --------------------------------------------------------------------------------------------");
            System.out.println(
                    "    ============================================================================================");
        }
    }

    public static void TarikTunaiView() {
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
    }

    public static void TarikTunai() {
        TarikTunaiView();
        System.out.print("\t-- Masukkan nominal tarik tunai : Rp "); // User input nominal tarik
        // tunai
        cashWithdrawalAmount = scanner3.nextInt();

        ClearScreen();

        System.out.println(
                "    ============================================================================================");
        // Konversi nilai output ke Rupiah
        String cashWitdrawalRupiah = currencyFormat.format(cashWithdrawalAmount);
        // Persetujuan konfirmasi transaksi
        UserConfirmation();
        ClearScreen();
        if (userConfirmation == 'y' || userConfirmation == 'Y') {
            // Pengecekan apakah input PIN sesuai dengan database
            if (PinValidation()) {
                if (cashWithdrawalAmount < userBalance) {
                    // Pengecekan apakah nominal tarik kurang dari saldo pengguna
                    if (cashWithdrawalAmount <= 5000000) {
                        // Kondisi jika cashWithdrawalAmount < userBalance dan
                        // cashWithdrawalAmount <= 5000000
                        userBalance -= cashWithdrawalAmount;
                        // Formating penulisan rupiah pada output
                        String userBalanceRupiah = currencyFormat.format(userBalance);
                        viewTransactionSuccess();
                        System.out.println("\t-- Sisa saldo anda : " + userBalanceRupiah);
                        System.out.println(
                                "    ============================================================================================");

                        // Pencatatan riwayat transaksi
                        transactionHistory[maxTransactionHistory
                                - transactionCount] = "Telah melakukan tarik tunai sebesar "
                                        + cashWitdrawalRupiah;
                        transactionCount--;

                        EnterForContinue();

                        ClearScreen();
                    } else {
                        // Kondisi jika nominal Tarik > 5.000.000
                        System.out.println(
                                "    ============================================================================================");
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
                    viewBalanceIsNotEnough();
                }
            } else {
                viewWrongPin();
            }
        } else {
            viewTransactionCancelled();
        }
    }

    public static void SetorTunaiView() {
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

    }

    public static void SetorTunai() {
        SetorTunaiView();
        System.out.print("\t-- Masukkan nominal setor tunai : Rp ");
        cashDepositAmount = scanner3.nextInt();

        ClearScreen();

        System.out.println(
                "    ============================================================================================");
        String cashDepositRupiah = currencyFormat.format(cashDepositAmount);
        System.out.println("\t-- Konfirmasi setor tunai dengan nominal " + cashDepositRupiah + " ? ");
        UserConfirmation();

        ClearScreen();
        if (userConfirmation == 'y' || userConfirmation == 'Y') {
            ClearScreen();
            if (PinValidation()) {
                userBalance += cashDepositAmount; // Penjumlahan saldo dengan nominal setor yang telah
                // dilakukan
                viewTransactionSuccess();
                // Formatting penulisan rupiah pada output
                String userBalanceRupiah = currencyFormat.format(userBalance);

                System.out.println("\t-- Sisa saldo anda : " + userBalanceRupiah);
                System.out.println(
                        "    ============================================================================================");
                // Pencatatan riwayat transaksi
                transactionHistory[maxTransactionHistory
                        - transactionCount] = "Telah melakukan setor tunai sebesar "
                                + cashDepositRupiah;
                transactionCount--;

                EnterForContinue();

                ClearScreen();
            } else {
                viewWrongPin();
            }
        } else {
            viewTransactionCancelled();
        }
    }

    public static void PembayaranLainnyaView() {
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
    }

    public static void PembayaranLainnya() {
        PembayaranLainnyaView();
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
                "    [   _______________                                                                        ]");
        System.out.println(
                "    [  |_3._Pendidikan_|                                                                       ]");
        System.out.println(
                "    [   _______________                                                                        ]");
        System.out.println(
                "    [  |_4._PDAM_______|                                                                       ]");
        System.out.println(
                "    [   _______________                                                                        ]");
        System.out.println(
                "    [  |_5._BPJS_______|                                                                       ]");
        System.out.println(
                "    ============================================================================================");
        System.out.print("\t-- Menu yang anda pilih (angka): ");
        int menuBayar = scanner3.nextInt();
        ClearScreen();
        switch (menuBayar) {
            case 1:
                Pulsa();
                break;
            case 2:
                Listrik();
                break;
            case 3:
                Pendidikan();
                break;
            case 4:
                Pdam();
                break;
            case 5:
                Bpjs();
                break;
            default:
                defaultCaseMenu();
                break;
        }
    }

    public static void PulsaView() {
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
    }

    public static void Pulsa() {
        PulsaView();
        String nomorTelepon;
        int nomPulsa;
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
        System.out.print("\t-- Operator yang anda pilih: ");
        String operatorPulsa = scanner2.next();
        boolean isOperatorValid = false;
        switch (operatorPulsa) {
            case "1":
                operatorPulsa = "Indosat";
                isOperatorValid = true;
                break;
            case "2":
                operatorPulsa = "XL";
                isOperatorValid = true;
                break;
            case "3":
                operatorPulsa = "Telkomsel";
                isOperatorValid = true;
                break;
            default:
                System.out.println("Operator yang dipilih invalid!");
                break;
        }

        if (isOperatorValid) {
            System.out.print("\t-- Input nomor telepon anda: "); // User input nomor telepon
            nomorTelepon = scanner1.nextLine();
            System.out.print("\t-- Input nominal pulsa: Rp "); // User input nominal pulsa
            nomPulsa = scanner4.nextInt();
            ClearScreen();
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
            UserConfirmation();
            ClearScreen();
            if (userConfirmation == 'Y' || userConfirmation == 'y') {
                ClearScreen();
                if (PinValidation()) {
                    if (nomPulsa < userBalance) {
                        String saldoRupiah2 = currencyFormat.format(userBalance);

                        // Menampilkan output transaksi berhasil
                        viewTransactionSuccess();
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
                        transactionHistory[maxTransactionHistory
                                - transactionCount] = "Telah melakukan pembayaran pulsa senilai "
                                        + nomPulsaRP;
                        transactionCount--;

                        EnterForContinue();
                        ClearScreen();
                    } else {
                        viewBalanceIsNotEnough();
                    }
                } else {
                    viewWrongPin();
                }
            } else {
                viewTransactionCancelled();
            }
        }
    }

    public static void ListrikView() {
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
    }

    public static void Listrik() {
        ListrikView();
        System.out.print("\t-- Masukkan ID pelanggan PLN/Nomor meter: ");
        int inputPLN = scanner4.nextInt();
        ClearScreen();
        // Pengecekan data ID pelanggan
        isListrikValid = false;
        for (int i = 0; i < listrikData.length; i++) {
            if (inputPLN == listrikData[i][0]) {
                indexListrik = i;
                isListrikValid = true;
                break;
            }
        }

        // Proses perhitungan tagihan listrik PLN
        if (isListrikValid) {
            // Formatting output ke Rupiah
            String tagihanListrikRP = currencyFormat
                    .format(listrikData[indexListrik][1]);
            System.out.println(
                    "    ============================================================================================");
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
            UserConfirmation();
            ClearScreen();
            if (userConfirmation == 'Y' || userConfirmation == 'y') {
                if (PinValidation()) {
                    if (listrikData[indexListrik][1] < userBalance) {
                        userBalance -= listrikData[indexListrik][1];
                        // Formatting saldo pengguna ke Rupiah
                        String saldoRupiah3 = currencyFormat.format(userBalance);
                        viewTransactionSuccess();
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
                        transactionHistory[maxTransactionHistory
                                - transactionCount] = "Telah melakukan pembayaran tagihan listrik senilai "
                                        + tagihanListrikRP;
                        transactionCount--;

                        EnterForContinue();
                        ClearScreen();
                    } else {
                        viewBalanceIsNotEnough();
                    }
                } else {
                    viewWrongPin();
                }
            } else {
                viewTransactionCancelled();
            }
        } else {
            viewPaymentCodeInvalid();
        }
    }

    public static void PendidikanView() {
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
    }

    public static void Pendidikan() {
        PendidikanView();
        System.out.print("\t-- Masukkan nomor virtual account : ");
        int inputVA = scanner4.nextInt();
        ClearScreen();

        // pengecekan data VA
        isPendidikanValid = false;
        for (int i = 0; i < pendidikanData.length; i++) {
            if (inputVA == pendidikanData[i][0]) {
                indexPendidikan = i;
                isPendidikanValid = true;
                break;
            }
        }

        if (isPendidikanValid) {
            String tagihanPendidikanRP = currencyFormat
                    .format(pendidikanData[indexPendidikan][1]);
            System.out.println(
                    "    ============================================================================================");
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
            UserConfirmation();
            ClearScreen();
            if (userConfirmation == 'Y' || userConfirmation == 'y') {
                if (PinValidation()) {
                    if (pendidikanData[indexPendidikan][1] < userBalance) {
                        userBalance -= pendidikanData[indexPendidikan][1];
                        // Formatting output ke Rupiah
                        String saldoRupiah3 = currencyFormat.format(userBalance);
                        viewTransactionSuccess();
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
                        transactionHistory[maxTransactionHistory
                                - transactionCount] = "Telah melakukan pembayaran tagihan pendidikan senilai "
                                        + tagihanPendidikanRP;

                        EnterForContinue();
                        ClearScreen();
                    } else {
                        viewBalanceIsNotEnough();
                    }
                } else {
                    viewWrongPin();
                }
            } else {
                viewTransactionCancelled();
            }
        } else {
            viewPaymentCodeInvalid();
        }

    }

    public static void PdamView() {
        System.out.println(
                "    ============================================================================================");
        System.out.println(
                "    [- - - - - - - - - - - - - - - - - - - -╔═╗╔╦╗╔═╗╔╦╗- - - - - - - - - - - - - - - - - - - -]");
        System.out.println(
                "    [- - - - - - - - - - - - - - - - - - - -╠═╝ ║║╠═╣║║║- - - - - - - - - - - - - - - - - - - -]");
        System.out.println(
                "    [- - - - - - - - - - - - - - - - - - - -╩  ═╩╝╩ ╩╩ ╩- - - - - - - - - - - - - - - - - - - -]");
        System.out.println(
                "    ============================================================================================");
    }

    public static void Pdam() {
        PdamView();
        System.out.print("\t-- Masukkan nomor tagihan : ");
        int inputVA = scanner4.nextInt();
        ClearScreen();

        // pengecekan data VA
        isPdamValid = false;
        for (int i = 0; i < tagihanAirData.length; i++) {
            if (inputVA == tagihanAirData[i][0]) {
                indexPdam = i;
                isPdamValid = true;
                break;
            }
        }

        if (isPdamValid) {
            String tagihanPdamRp = currencyFormat
                    .format(tagihanAirData[indexPdam][1]);
            System.out.println(
                    "    ============================================================================================");
            System.out.println(
                    "    [  _________________________________________________________\t]");
            System.out.println("    [ |  $$$ RINCIAN PEMBAYARAN $$$\t\t\t\t|\t]");
            System.out.printf("    [ |  Nomor tagihan\t\t: %d\t\t\t|\t]\n", inputVA);
            System.out.printf("    [ |  Total tagihan\t\t: %s\t\t|\t]\n",
                    tagihanPdamRp);
            System.out.println(
                    "    [  ---------------------------------------------------------\t]");
            System.out.println(
                    "    ============================================================================================");
            System.out.printf(
                    "\t-- Konfirmasi transaksi tagihan biaya PDAM sebesar %s\n",
                    tagihanPdamRp);
            UserConfirmation();
            ClearScreen();
            if (userConfirmation == 'Y' || userConfirmation == 'y') {
                if (PinValidation()) {
                    if (tagihanAirData[indexPdam][1] < userBalance) {
                        userBalance -= tagihanAirData[indexPdam][1];
                        // Formatting output ke Rupiah
                        String saldoRupiah3 = currencyFormat.format(userBalance);
                        viewTransactionSuccess();
                        System.out.println(
                                "    [  _________________________________________________________\t]");
                        System.out.println(
                                "    [ |  $$$ RINCIAN PEMBAYARAN $$$\t\t\t\t|\t]");
                        System.out.printf("    [ |  Nomor VA\t\t: %d\t\t\t|\t]\n",
                                inputVA);
                        System.out.printf("    [ |  Total tagihan\t\t: %s\t\t|\t]\n",
                                tagihanPdamRp);
                        System.out.printf("    [ |  Sisa saldo anda\t: %s\t\t|\t]\n",
                                saldoRupiah3);
                        System.out.println(
                                "    [  ---------------------------------------------------------\t]");
                        System.out.println(
                                "    ============================================================================================");

                        // Pencatatan riwayat transaksi
                        transactionHistory[maxTransactionHistory
                                - transactionCount] = "Telah melakukan pembayaran tagihan PDAM senilai "
                                        + tagihanPdamRp;
                        transactionCount--;

                        EnterForContinue();
                        ClearScreen();
                    } else {
                        viewBalanceIsNotEnough();
                    }
                } else {
                    viewWrongPin();
                }
            } else {
                viewTransactionCancelled();
            }
        } else {
            viewPaymentCodeInvalid();
        }
    }

    public static void BpjsView() {
        System.out.println(
                "    ============================================================================================");
        System.out.println(
                "    [- - - - - - - - - - - - - - - - - - - - ╔╗ ╔═╗╦╔═╗- - - - - - - - - - - - - - - - - - - - ]");
        System.out.println(
                "    [- - - - - - - - - - - - - - - - - - - - ╠╩╗╠═╝║╚═╗- - - - - - - - - - - - - - - - - - - - ]");
        System.out.println(
                "    [- - - - - - - - - - - - - - - - - - - - ╚═╝╩ ╚╝╚═╝- - - - - - - - - - - - - - - - - - - - ]");
        System.out.println(
                "    ============================================================================================");
    }

    public static void Bpjs() {
        BpjsView();
        System.out.print("\t-- Masukkan nomor tagihan : ");
        int inputVA = scanner4.nextInt();
        ClearScreen();

        // pengecekan data VA
        isBpjsValid = false;
        for (int i = 0; i < BPJSdata.length; i++) {
            if (inputVA == BPJSdata[i][0]) {
                indexBpjs = i;
                isBpjsValid = true;
                break;
            }
        }

        if (isBpjsValid) {
            String tagihanBpjsRp = currencyFormat
                    .format(BPJSdata[indexBpjs][1]);
            System.out.println(
                    "    ============================================================================================");
            System.out.println(
                    "    [  _________________________________________________________\t]");
            System.out.println("    [ |  $$$ RINCIAN PEMBAYARAN $$$\t\t\t\t|\t]");
            System.out.printf("    [ |  Nomor tagihan\t\t: %d\t\t\t|\t]\n", inputVA);
            System.out.printf("    [ |  Total tagihan\t\t: %s\t\t|\t]\n",
                    tagihanBpjsRp);
            System.out.println(
                    "    [  ---------------------------------------------------------\t]");
            System.out.println(
                    "    ============================================================================================");
            System.out.printf(
                    "\t-- Konfirmasi transaksi tagihan biaya BPJS sebesar %s\n",
                    tagihanBpjsRp);

            UserConfirmation();
            ClearScreen();
            if (userConfirmation == 'Y' || userConfirmation == 'y') {
                if (PinValidation()) {
                    if (BPJSdata[indexBpjs][1] < userBalance) {
                        userBalance -= BPJSdata[indexBpjs][1];
                        // Formatting output ke Rupiah
                        String saldoRupiah3 = currencyFormat.format(userBalance);
                        viewTransactionSuccess();
                        System.out.println(
                                "    [  _________________________________________________________\t]");
                        System.out.println(
                                "    [ |  $$$ RINCIAN PEMBAYARAN $$$\t\t\t\t|\t]");
                        System.out.printf("    [ |  Nomor VA\t\t: %d\t\t\t|\t]\n",
                                inputVA);
                        System.out.printf("    [ |  Total tagihan\t\t: %s\t\t|\t]\n",
                                tagihanBpjsRp);
                        System.out.printf("    [ |  Sisa saldo anda\t: %s\t\t|\t]\n",
                                saldoRupiah3);
                        System.out.println(
                                "    [  ---------------------------------------------------------\t]");
                        System.out.println(
                                "    ============================================================================================");

                        // Pencatatan riwayat transaksi
                        transactionHistory[maxTransactionHistory
                                - transactionCount] = "Telah melakukan pembayaran tagihan BPJS senilai "
                                        + tagihanBpjsRp;
                        transactionCount--;

                        EnterForContinue();
                        ClearScreen();
                    } else {
                        viewBalanceIsNotEnough();
                    }
                } else {
                    viewWrongPin();
                }
            } else {
                viewTransactionCancelled();
            }
        } else {
            viewPaymentCodeInvalid();
        }
    }

    public static void RiwayatTransaksiView() {
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
    }

    public static void RiwayatTransaksi() {
        RiwayatTransaksiView();
        EnterForContinue();
    }

    public static void CekSaldoView() {
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
    }

    public static void CekSaldo() {
        CekSaldoView();
        String saldoRupiah3 = currencyFormat.format(userBalance);
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

        EnterForContinue();

        ClearScreen();
    }

    public static void UbahPinView() {
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
    }

    public static void UbahPin() {
        UbahPinView();
        System.out.print("    Masukkan nomor rekening : ");
        String inputUser_AccountNumber7 = scanner1.nextLine();
    }

    public static void ExitView() {
        System.out.println(
                "    ============================================================================================");
        System.out.println(
                "    [- - - - - - - - - - - - - - - - - - ╦╔═╔═╗╦  ╦ ╦╔═╗╦═╗ - - - - - - - - - - - - - - - - - - ]");
        System.out.println(
                "    [- - - - - - - - - - - - - - - - - - ╠╩╗║╣ ║  ║ ║╠═╣╠╦╝ - - - - - - - - - - - - - - - - - - ]");
        System.out.println(
                "    [- - - - - - - - - - - - - - - - - - ╩ ╩╚═╝╩═╝╚═╝╩ ╩╩╚═ - - - - - - - - - - - - - - - - - - ]");
        System.out.println(
                "    ============================================================================================");
    }

    public static boolean Exit() {
        ExitView();
        System.out.println("\t-- Apakah anda yakin untuk keluar?");
        UserConfirmation();
        ClearScreen();
        if (userConfirmation == 'Y' || userConfirmation == 'y') {
            showingThanks();
            return isStopTransaction = true;
        } else {
            return isContinueTransaction = true;
        }
    }

    public static void defaultCaseMenu() {
        ClearScreen();
        System.out.println(
                "    ============================================================================================");
        System.out.println(
                "                   [  (!) Input tidak sesuai. Periksa kembali inputan anda (!)  ]");
        System.out.println(
                "    ============================================================================================");
    }

    public static void viewBalanceIsNotEnough() {
        ClearScreen();
        System.out.println(
                "    ============================================================================================");
        System.out.println(
                "    --------------------------------------------------------------------------------------------");
        System.out.println(
                "                     [  (!) Transaksi gagal. Saldo anda tidak mencukupi (!)  ]");
        System.out.println(
                "    --------------------------------------------------------------------------------------------");
        System.out.println(
                "    ============================================================================================");
    }

    public static void viewWrongPin() {
        ClearScreen();
        System.out.println(
                "    ============================================================================================");
        System.out.println(
                "    --------------------------------------------------------------------------------------------");
        System.out.println(
                "                                      [  (!) PIN SALAH! (!)  ]");
        System.out.println(
                "    --------------------------------------------------------------------------------------------");
        System.out.println(
                "    ============================================================================================");
    }

    public static void viewTransactionSuccess() {
        ClearScreen();
        System.out.println(
                "    ============================================================================================");
        System.out.println(
                "    --------------------------------------------------------------------------------------------");
        System.out.println(
                "     ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ TRANSAKSI BERHASIL ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ");
        System.out.println(
                "    --------------------------------------------------------------------------------------------");
        System.out.println(
                "    ============================================================================================");
    }

    public static void viewTransactionCancelled() {
        ClearScreen();
        System.out.println(
                "    ============================================================================================");
        System.out.println(
                "    --------------------------------------------------------------------------------------------");
        System.out.println(
                "                                 [  (!) TRANSAKSI DIBATALKAN (!)  ]");
        System.out.println(
                "    --------------------------------------------------------------------------------------------");
        System.out.println(
                "    ============================================================================================");
    }

    public static void viewPaymentCodeInvalid() {
        ClearScreen();
        System.out.println(
                "    ============================================================================================");
        System.out.println(
                "    --------------------------------------------------------------------------------------------");
        System.out.println(
                red + "               [  (!) Kode pembayaran invalid. Silakan input ulang nomor VA anda! (!)  ]" + reset);
        System.out.println(
                "    --------------------------------------------------------------------------------------------");
        System.out.println(
                "    ============================================================================================");
    }

    public static void showingThanks() {
        System.out.println(
                "    ============================================================================================");
        System.out.println(
                "     ~ ~ ~ ~ ~ ~ ~ Terimakasih telah bertransaksi! Semoga harimu selalu bahagia :) ~ ~ ~ ~ ~ ~ ~");
        System.out.println(
                "    ============================================================================================");
    }

    public static char UserConfirmation() {
        System.out.print("Tekan 'Y' untuk IYA. Tekan 'T' untuk TIDAK --> ");
        userConfirmation = scanner2.next().charAt(0);
        return userConfirmation;
    }

    public static boolean PinValidation() {
        System.out.print("Masukkan PIN anda: ");
        inputPin = scanner1.nextLine();
        ClearScreen();
        if (inputPin.equals(accountData[accountLineIndex][1])) {
            return true;
        } else {
            return false;
        }
    }

    public static void EnterForContinue() {
        System.out.print("Enter untuk melanjutkan -->  ");
        scanner1.nextLine();
    }

    public static void ClearScreen() {
        System.out.println("\033[H\033[2J");
        System.out.flush();
    }

}