// Sistem transfer ATM
import java.util.Scanner;
public class FiturTransfer {
    public static void main(String[] args) {
    Scanner scanner1 = new Scanner (System.in);
    Scanner scanner2 = new Scanner (System.in);

    System.out.println("**************************");
    System.out.println("Anda memilih menu transfer");
    System.out.println("**************************");

    // Deklarasi dan inisialisasi variabel
    String[] no_rek = { "1234567", "7654321", "1357924" }; // array nomor rekening
    String[] pin = { "1234", "5678", "2468" }; // array pin yang sesuai dengan indeks array nomor rekening
    int riw = 10, count = 10, hasil = 0, tujuan = 0; //variabel untuk cetak riwayat dan semacam session manipulasi saldo
    String[] riwayat = new String[riw]; // array riwayat
    boolean isValid = false;
    char konfirmasi;
    String no_rek_tujuan; 
    int nom_transfer;
    int[] saldo = { 5000000, 4000000, 1000000 };

    do {
        // Input nomor rekening tujuan
        System.out.print("Masukkan nomor rekening tujuan : ");
        no_rek_tujuan = scanner1.nextLine();

        // Pengecekan apakah nomor rekening yang diinput sesuai dengan data yang tersedia
        for (int i = 0; i < no_rek.length; i++) {
            if (no_rek_tujuan.equals(no_rek[i])) {
                tujuan = i;
                isValid = true; // Merubah nilai isValid dari false menjadi true
                break;
            }
        }

        // Pengecekan apakah nilai isValid bernilai true
        if (isValid == true) {
            System.out.print("Masukkan nominal transfer : ");
            nom_transfer = scanner2.nextInt();
            // Pengecekan apakah nominal transfer kurang dari atau sama dengan 0
            if (nom_transfer <= saldo[hasil]) {
                saldo[hasil] -= nom_transfer;
                saldo[tujuan] += nom_transfer;
                System.out.println("Transfer ke nomor " + no_rek_tujuan + " berhasil dilakukan");
                System.out.println("Sisa saldo anda : " + saldo[hasil]);
                riwayat[riw - count] = "Telah melakukan transaksi ke rekening " + no_rek_tujuan + " sebesar " + nom_transfer;
                count--;
            } else {
                System.out.println("Transaksi gagal, saldo anda tidak mencukupi");
            }
        } else {
            System.out.println("Transaksi gagal, nomor rekening tujuan invalid");
        }

        // Konfirmasi apakah user ingin transaksi lagi
        System.out.println("Apakah Anda ingin transaksi lagi? (Y/T)");
        konfirmasi = scanner2.next().charAt(0);
    } while (konfirmasi == 'y' || konfirmasi == 'Y');
    }
}
