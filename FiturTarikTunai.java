import java.util.Scanner;
public class FiturTarikTunai {
    public static void main(String[] args) {
    Scanner scanner1 = new Scanner (System.in);

    // Deklarasi dan inisialisasi variabel
    char konfirmasi ='y';
    int nom_tarik;
    int[] saldo = { 5000000, 4000000, 1000000 };
    int riw = 10, count = 10, hasil = 0; //variabel untuk cetak riwayat dan semacam session manipulasi saldo
    String[] riwayat = new String[riw]; // array riwayat

    do {
        System.out.println("**************************");
        System.out.println("Anda memilih menu tarik tunai");
        System.out.println("**************************");
        System.out.print("Masukkan nominal tarik tunai : ");
        nom_tarik = scanner1.nextInt();

        if (nom_tarik > saldo[hasil]) {
        System.out.println("Transaksi gagal, periksa kembali saldo anda");
    } else {
        saldo[hasil] -= nom_tarik;
        System.out.println("Tarik tunai berhasil dilakukan");
        System.out.println("Sisa saldo anda : " + saldo[hasil]);
        riwayat[riw - count] = "Telah melakukan tarik tunai sebesar " + nom_tarik;
        count--;
        System.out.println("Apakah anda ingin transaksi lagi? (Y/T)");
        konfirmasi = scanner1.next().charAt(0);
    }
    } while (konfirmasi == 'y' || konfirmasi == 'Y');
    System.out.println("Terima kasih telah bertransaksi! :)");
    }
}
