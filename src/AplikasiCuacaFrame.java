import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.json.JSONObject;
import javax.swing.table.DefaultTableModel;

public class AplikasiCuacaFrame extends javax.swing.JFrame {
    
    private int inputCount = 0; // Menyimpan jumlah input kota
    private String lastInputCity = ""; // Menyimpan input terakhir untuk verifikasi
    private JTable weatherTable; // JTable untuk menampilkan data cuaca

    
    public AplikasiCuacaFrame() {
         initComponents();

        // Menambahkan nama kota ke JComboBox
        cityComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{}));

        // Tambahkan Event Listener untuk tombol Cek Cuaca
        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String city = cityField.getText().trim(); // Mengambil input dari JTextField dan menghapus spasi

                if (city.isEmpty()) {
                    weatherLabel.setText("Masukkan nama kota!");
                    return; // Keluar jika JTextField kosong
                }

                WeatherService weatherService = new WeatherService();
                try {
                    JSONObject weatherData = weatherService.getWeather(city);
                    String weatherDescription = weatherData.getJSONArray("weather").getJSONObject(0).getString("description");
                    weatherLabel.setText("Cuaca: " + weatherDescription);

                    // Simpan data ke CSV
                    saveToCSV(city + "," + weatherDescription);

                    // Hitung jumlah input kota
                    if (city.equalsIgnoreCase(lastInputCity)) {
                        inputCount++; // Meningkatkan penghitung jika kota sama dengan input terakhir
                    } else {
                        inputCount = 1; // Reset penghitung jika kota berbeda
                        lastInputCity = city; // Perbarui kota terakhir
                    }

                    if (inputCount == 3) { // Jika sudah 3 kali memasukkan
                        addCityToFavorites(city); // Tambahkan kota ke favorit
                        inputCount = 0; // Reset penghitung
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    weatherLabel.setText("Error retrieving weather data.");
                }
            }
        });

        // Tambahkan action listener untuk JComboBox
        cityComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedCity = (String) cityComboBox.getSelectedItem();
                cityField.setText(selectedCity); // Mengatur JTextField dengan kota yang dipilih
            }
        });
        
        // Tombol untuk memuat data cuaca dari CSV
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadWeatherData();
            }
        });

        setVisible(true); // Menampilkan JFrame
    }
    
    // Method untuk menambahkan kota ke JComboBox sebagai kota favorit
    private void addCityToFavorites(String city) {
        DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) cityComboBox.getModel();
        boolean exists = false;

        // Cek apakah kota sudah ada dalam JComboBox
        for (int i = 0; i < model.getSize(); i++) {
            if (model.getElementAt(i).equalsIgnoreCase(city)) {
                exists = true;
                break;
            }
        }

        // Jika belum ada, tambahkan kota ke JComboBox
        if (!exists) {
            model.addElement(city);
        }
    }
    
    // Method untuk menyimpan data ke file CSV
    public void saveToCSV(String data) {
        try (FileWriter writer = new FileWriter("weather_data.csv", true)) {
            writer.append(data);
            writer.append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Method untuk memuat data cuaca dari file CSV
    private void loadWeatherData() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel(); // Menggunakan jTable1 dari UI
        model.setRowCount(0); // Menghapus baris yang ada

        try (BufferedReader br = new BufferedReader(new FileReader("weather_data.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(","); // Memisahkan data berdasarkan koma
                model.addRow(data); // Menambahkan baris baru ke tabel
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading weather data.");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        cityField = new javax.swing.JTextField();
        checkButton = new javax.swing.JButton();
        weatherLabel = new javax.swing.JLabel();
        cityComboBox = new javax.swing.JComboBox<>();
        loadButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        checkButton.setText("Cek Cuaca");

        weatherLabel.setText("Cuaca: ");

        cityComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        loadButton.setText("Simpan Data");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Kota", "Cuaca"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cityField, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(checkButton)
                        .addGap(18, 18, 18)
                        .addComponent(loadButton))
                    .addComponent(weatherLabel)
                    .addComponent(cityComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 455, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(cityField, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(checkButton)
                    .addComponent(loadButton))
                .addGap(18, 18, 18)
                .addComponent(weatherLabel)
                .addGap(39, 39, 39)
                .addComponent(cityComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
       new AplikasiCuacaFrame();
    }
    
 


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton checkButton;
    private javax.swing.JComboBox<String> cityComboBox;
    private javax.swing.JTextField cityField;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton loadButton;
    private javax.swing.JLabel weatherLabel;
    // End of variables declaration//GEN-END:variables
}
