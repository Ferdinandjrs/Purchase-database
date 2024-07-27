package Coders.maven.project.javadb;

import java.sql.*;
import java.util.Scanner;

public class Challenge {
    private static final String URL = "jdbc:mysql://localhost:3306/project_javadb";
    private static final String USER = "root";
    private static final String PASSWORD = "HSDgrc55##";
    private static Connection connection;

    public static void main(String[] args) {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            Scanner scanner = new Scanner(System.in);
            int choice;
            do {
                System.out.println("Menu:");
                System.out.println("1. Masukkan Data Transaksi ");
                System.out.println("2. Update Data Transaksi");
                System.out.println("3. Menampilkan Data Transaksi");
                System.out.println("4. Keluar");
                System.out.print("Masukkan Pilihan : ");
                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        insertData(scanner);
                        break;
                    case 2:
                        updateData(scanner);
                        break;
                    case 3:
                        displayData(scanner);
                        break;
                    case 4:
                        System.out.println("Keluar...");
                        break;
                    default:
                        System.out.println("Pilihan salah, Coba lagi.");
                }
            } while (choice != 4);
            scanner.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    private static void insertData(Scanner scanner) {
        try {
            System.out.print("Masukkan No Struk: ");
            String noStruk = scanner.nextLine();
            System.out.print("Masukkan Tanggal Transaksi (YYYY-MM-DD): ");
            String tanggalTransaksi = scanner.nextLine();
            System.out.print("Masukkan Kode Cabang: ");
            String kodeCabang = scanner.nextLine();
            System.out.print("Masukkan Tipe Transaksi (Eat In/Take Away/Online): ");
            String tipeTransaksi = scanner.nextLine();


            String insertTransaksiSQL = "INSERT INTO Transaksi (No_Struk, Tanggal_Transaksi, Kode_Cabang, Tipe_Transaksi) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatementTransaksi = connection.prepareStatement(insertTransaksiSQL)) {
                preparedStatementTransaksi.setString(1, noStruk);
                preparedStatementTransaksi.setDate(2, Date.valueOf(tanggalTransaksi));
                preparedStatementTransaksi.setString(3, kodeCabang);
                preparedStatementTransaksi.setString(4, tipeTransaksi);
                preparedStatementTransaksi.executeUpdate();
            }


            System.out.print("Masukkan kode produk: ");
            int numProducts = scanner.nextInt();
            scanner.nextLine();
            String insertDetailSQL = "INSERT INTO Detail_Transaksi (No_Struk, Kode_Produk, Jumlah, Total_Penjualan) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatementDetail = connection.prepareStatement(insertDetailSQL)) {
                for (int i = 0; i < numProducts; i++) {
                    System.out.print("Masukkan Kode Produk: ");
                    String kodeProduk = scanner.nextLine();
                    System.out.print("Masukkan Jumlah: ");
                    int jumlah = scanner.nextInt();
                    System.out.print("Masukkan Total Penjualan: ");
                    double totalPenjualan = scanner.nextDouble();
                    scanner.nextLine();

                    preparedStatementDetail.setString(1, noStruk);
                    preparedStatementDetail.setString(2, kodeProduk);
                    preparedStatementDetail.setInt(3, jumlah);
                    preparedStatementDetail.setDouble(4, totalPenjualan);
                    preparedStatementDetail.addBatch();
                }
                preparedStatementDetail.executeBatch();
            }
            System.out.println("Data berhasil dimasukkan.");
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }catch (IllegalArgumentException illegalArgumentException) {
            illegalArgumentException.printStackTrace();
        }
    }

    private static void updateData(Scanner scanner) {
        try {
            System.out.print("Masukkan No Struk untuk di update: ");
            String noStruk = scanner.nextLine();

            System.out.print("Masukkan Tanggal Transaksi yang baru (YYYY-MM-DD): ");
            String tanggalTransaksi = scanner.nextLine();
            System.out.print("Masukkan Tipe Transaksi yang baru (Eat In/Take Away/Online): ");
            String tipeTransaksi = scanner.nextLine();

            String updateTransaksiSQL = "UPDATE Transaksi SET Tanggal_Transaksi = ?, Tipe_Transaksi = ? WHERE No_Struk = ?";
            try (PreparedStatement preparedStatementUpdateTransaction = connection.prepareStatement(updateTransaksiSQL)) {
                preparedStatementUpdateTransaction.setDate(1, Date.valueOf(tanggalTransaksi));
                preparedStatementUpdateTransaction.setString(2, tipeTransaksi);
                preparedStatementUpdateTransaction.setString(3, noStruk);
                preparedStatementUpdateTransaction.executeUpdate();
            }


            System.out.print("Masukkan kode produk untuk update: ");
            int numProducts = scanner.nextInt();
            scanner.nextLine();
            String updateDetailSQL = "UPDATE Detail_Transaksi SET Kode_Produk = ?, Jumlah = ?, Total_Penjualan = ? WHERE No_Struk = ?";
            try (PreparedStatement preparedStatementUpdateDetail = connection.prepareStatement(updateDetailSQL)) {
                for (int i = 0; i < numProducts; i++) {
                    System.out.print("Masukkan Kode Produk yang baru : ");
                    String kodeProduk = scanner.nextLine();
                    System.out.print("Masukkan Jumlah yang baru: ");
                    int jumlah = scanner.nextInt();
                    System.out.print("Masukkan Total Penjualan yang baru : ");
                    double totalPenjualan = scanner.nextDouble();
                    scanner.nextLine();

                    preparedStatementUpdateDetail.setString(1, kodeProduk);
                    preparedStatementUpdateDetail.setInt(2, jumlah);
                    preparedStatementUpdateDetail.setDouble(3, totalPenjualan);
                    preparedStatementUpdateDetail.setString(4, noStruk);
                    preparedStatementUpdateDetail.addBatch();
                }
                preparedStatementUpdateDetail.executeBatch();
            }
            System.out.println("Data berhasil di update.");
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (IllegalArgumentException argumentException) {

            argumentException.printStackTrace();
        }
    }

    private static void displayData(Scanner scanner) {
        try {
            System.out.print("Enter Kode Cabang (atau ketik 'all' untuk menampilkan jumlah seluruh transaksi): ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("all")) {
                displayTotalByTransactionType();
            } else {
                displayTransactionDetailsByBranch(input);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (IllegalArgumentException argumentException) {
            argumentException.printStackTrace();
        }
    }

    private static void displayTotalByTransactionType() throws SQLException {
        String query = "SELECT Tipe_Transaksi, SUM(dt.Total_Penjualan) AS Total_Penjualan " +
                "FROM Transaksi t " +
                "JOIN Detail_Transaksi dt ON t.No_Struk = dt.No_Struk " +
                "GROUP BY Tipe_Transaksi";

        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(query)) {

            System.out.println("REKAP TOTAL PENJUALAN (TIPE TRANSAKSI)");
            while (resultSet.next()) {
                String tipeTransaksi = resultSet.getString("Tipe_Transaksi");
                double totalPenjualan = resultSet.getDouble("Total_Penjualan");

                System.out.printf("%s\t%.2f%n", tipeTransaksi, totalPenjualan);
            }
            System.out.println();
        }
    }

    private static void displayTransactionDetailsByBranch(String kodeCabang) throws SQLException {
        String query = "SELECT t.No_Struk, t.Tanggal_Transaksi, c.Nama_Cabang, t.Tipe_Transaksi, p.Nama_Produk, dt.Jumlah, p.Harga_Satuan, dt.Total_Penjualan " +
                "FROM Transaksi t " +
                "JOIN Detail_Transaksi dt ON t.No_Struk = dt.No_Struk " +
                "JOIN Produk p ON dt.Kode_Produk = p.Kode_Produk " +
                "JOIN Cabang c ON t.Kode_Cabang = c.Kode_Cabang " +
                "WHERE t.Kode_Cabang = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, kodeCabang);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                System.out.printf("%-10s %-15s %-10s %-20s %-15s %-25s %-10s %-10s %-15s%n",
                        "Tanggal", "Bill", "BranchNo", "Branch Name", "Type", "Product Code", "Qty", "Price", "Total");

                while (resultSet.next()) {
                    Date tanggalTransaksi = resultSet.getDate("Tanggal_Transaksi");
                    String noStruk = resultSet.getString("No_Struk");
                    String namaCabang = resultSet.getString("Nama_Cabang");
                    String tipeTransaksi = resultSet.getString("Tipe_Transaksi");
                    String namaProduk = resultSet.getString("Nama_Produk");
                    int jumlah = resultSet.getInt("Jumlah");
                    double hargaSatuan = resultSet.getDouble("Harga_Satuan");
                    double totalPenjualan = resultSet.getDouble("Total_Penjualan");

                    System.out.printf("%-10s %-15s %-10s %-20s %-15s %-25s %-10d %-10.2f %-15.2f%n",
                            tanggalTransaksi, noStruk, kodeCabang, namaCabang, tipeTransaksi, namaProduk, jumlah, hargaSatuan, totalPenjualan);
                }


                displayTotalByTransactionType();
            }
        }
    }
}
