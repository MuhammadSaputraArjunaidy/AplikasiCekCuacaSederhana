
# Aplikasi Cek Cuaca Sederhana



# Deskripsi
aplikasi ini digunakan untuk mencari cuaca di kota tertentu, menyimpan kota favorit, dan menampilkan informasi cuaca serta kondisi cuaca tersebut dalam bentuk teks dan gambar. Dan terdapat  tabel untuk mencatat kota-kota yang telah dicari sebelumnya beserta cuacanya.
# Features

Berikut adalah deskripsi fitur-fitur utama aplikasi ini:

1. Kolom Input:

- Cari Kota: Kolom teks untuk memasukkan nama kota yang ingin dicari cuacanya. Pada gambar ini, kota yang dimasukkan adalah "Banjarmasin."
- Kota Favorit: Dropdown menu yang memungkinkan pengguna memilih kota favorit yang telah disimpan sebelumnya. Fitur ini memungkinkan pengguna untuk dengan mudah mengakses cuaca kota yang sering dipantau.

2. Tombol :

- Cek Cuaca: Tombol yang digunakan untuk memulai pencarian cuaca berdasarkan kota yang dimasukkan pada kolom "Cari Kota.". Dengan mengklik tombol cek cuaca sebanyak 3 kali itu dapat menyimpan kota yang dipilih sebagai kota favorit
- Simpan Data: Tombol ini digunakan untuk menyimpan nama kota dan cuaca di Tabel

3. Informasi Cuaca:

- Cuaca: Menampilkan kondisi cuaca untuk kota yang dicari. 
- Gambar Cuaca: Gambar yang menunjukkan simbol cuaca.

4. Tabel Kota dan Cuaca:

- Tabel: Di bagian bawah terdapat tabel yang menampilkan daftar kota yang telah dicari beserta cuacanya. Hal ini memungkinkan pengguna untuk melihat cuaca beberapa kota yang telah diperiksa sebelumnya.
# Source Code

## AplikasiCuacaFrame.java

```java
        import javax.swing.*;
        import java.awt.event.ActionEvent;
        import java.awt.event.ActionListener;
        import java.io.BufferedReader;
        import java.io.FileReader;
        import java.io.FileWriter;
        import java.io.IOException;
        import org.json.JSONObject;
        import javax.swing.table.DefaultTableModel;
        import java.awt.Image;

        public class AplikasiCuacaFrame extends javax.swing.JFrame {
            
            private int inputCount = 0; // Menyimpan jumlah input kota
            private String lastInputCity = ""; // Menyimpan input terakhir untuk verifikasi
            private JTable weatherTable; // JTable untuk menampilkan data cuaca
        // Label untuk menampilkan ikon cuaca

            
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
                            
                            // Menampilkan gambar berdasarkan kondisi cuaca
                            displayWeatherIcon(weatherDescription);

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
            
            // Method untuk menampilkan gambar berdasarkan kondisi cuaca
        private void displayWeatherIcon(String weatherDescription) {
                    String iconPath;

                    // Jika deskripsi cuaca null, atur gambar default
                    if (weatherDescription == null) {
                        iconPath = "resources/default.png"; // Gambar default
                    } else {
                        // Mengubah deskripsi menjadi huruf kecil
                        String description = weatherDescription.toLowerCase();

                        // Tentukan path gambar berdasarkan deskripsi cuaca
                        if (description.contains("clear") || description.contains("sunny")) {
                            iconPath = "C:\\Users\\Putra\\Pictures\\cuaca\\sunny.png";
                        } else if (description.contains("cloud") || description.contains("overcast")) {
                            iconPath = "C:\\Users\\Putra\\Pictures\\cuaca\\cloudy.png";
                        } else if (description.contains("rain")) {
                            iconPath = "C:\\Users\\Putra\\Pictures\\cuaca\\rainy.png";
                        } else if (description.contains("snow")) {
                            iconPath = "C:\\Users\\Putra\\Pictures\\cuaca\\snowy.png";
                        } else {
                            iconPath = "resources/default.png"; // Gambar default jika kondisi tidak dikenali
                        }
                    }

                    // Pastikan weatherIconLabel telah diinisialisasi
                    if (weatherIconLabel == null) {
                        System.err.println("weatherIconLabel belum diinisialisasi.");
                        return;
                    }

                    // Memuat gambar
                    ImageIcon icon = new ImageIcon(iconPath);
                    Image image = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH); // Menyesuaikan ukuran gambar
                    weatherIconLabel.setIcon(new ImageIcon(image)); // Mengatur ikon pada label
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
            // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
            private void initComponents() {

                jPanel1 = new javax.swing.JPanel();
                cityField = new javax.swing.JTextField();
                checkButton = new javax.swing.JButton();
                weatherLabel = new javax.swing.JLabel();
                cityComboBox = new javax.swing.JComboBox<>();
                loadButton = new javax.swing.JButton();
                jScrollPane1 = new javax.swing.JScrollPane();
                jTable1 = new javax.swing.JTable();
                jLabel1 = new javax.swing.JLabel();
                jLabel2 = new javax.swing.JLabel();
                weatherIconLabel = new javax.swing.JLabel();
                jLabel3 = new javax.swing.JLabel();

                setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

                checkButton.setText("Cek Cuaca");

                weatherLabel.setText("Cuaca saat ini : ");

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

                jLabel1.setText("Cari Kota :");

                jLabel2.setText("Kota Favorit");

                jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
                jLabel3.setText("Aplikasi Cek Cuaca Sederhana");

                javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
                jPanel1.setLayout(jPanel1Layout);
                jPanel1Layout.setHorizontalGroup(
                    jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(88, 88, 88)
                        .addComponent(weatherLabel)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(79, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(checkButton)
                                        .addGap(18, 18, 18)
                                        .addComponent(loadButton))
                                    .addComponent(weatherIconLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 455, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(cityField, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel1))
                                        .addGap(40, 40, 40)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel2)
                                            .addComponent(cityComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(69, 69, 69))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(140, 140, 140))))
                );
                jPanel1Layout.setVerticalGroup(
                    jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3)
                        .addGap(53, 53, 53)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cityField, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cityComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(checkButton)
                            .addComponent(loadButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(weatherIconLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(weatherLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26))
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
            }// </editor-fold>                        

            /**
            * @param args the command line arguments
            */
            public static void main(String args[]) {
            new AplikasiCuacaFrame();
            }
            
        


            // Variables declaration - do not modify                     
            private javax.swing.JButton checkButton;
            private javax.swing.JComboBox<String> cityComboBox;
            private javax.swing.JTextField cityField;
            private javax.swing.JLabel jLabel1;
            private javax.swing.JLabel jLabel2;
            private javax.swing.JLabel jLabel3;
            private javax.swing.JPanel jPanel1;
            private javax.swing.JScrollPane jScrollPane1;
            private javax.swing.JTable jTable1;
            private javax.swing.JButton loadButton;
            private javax.swing.JLabel weatherIconLabel;
            private javax.swing.JLabel weatherLabel;
            // End of variables declaration                   
        }

## WeatherService.java

 
        import java.io.BufferedReader;
        import java.io.InputStreamReader;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import org.json.JSONObject;

        public class WeatherService extends javax.swing.JFrame {
            
            private final String API_KEY = "14c772f36d70984025f7f9eaf2c1a51f"; // Ganti dengan API key Anda
            private final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather";

            public JSONObject getWeather(String city) throws Exception {
                String urlString = BASE_URL + "?q=" + city + "&appid=" + API_KEY + "&units=metric";
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return new JSONObject(response.toString());
            }
            
            public WeatherService() {
                initComponents();
            }

            /**
            * This method is called from within the constructor to initialize the form.
            * WARNING: Do NOT modify this code. The content of this method is always
            * regenerated by the Form Editor.
            */
            @SuppressWarnings("unchecked")
            // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
            private void initComponents() {

                setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 400, Short.MAX_VALUE)
                );
                layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 300, Short.MAX_VALUE)
                );

                pack();
            }// </editor-fold>                        

            /**
            * @param args the command line arguments
            */
            public static void main(String args[]) {
                /* Set the Nimbus look and feel */
                //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
                /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
                * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
                */
                try {
                    for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                        if ("Nimbus".equals(info.getName())) {
                            javax.swing.UIManager.setLookAndFeel(info.getClassName());
                            break;
                        }
                    }
                } catch (ClassNotFoundException ex) {
                    java.util.logging.Logger.getLogger(WeatherService.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                } catch (InstantiationException ex) {
                    java.util.logging.Logger.getLogger(WeatherService.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    java.util.logging.Logger.getLogger(WeatherService.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                    java.util.logging.Logger.getLogger(WeatherService.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
                //</editor-fold>

                /* Create and display the form */
                java.awt.EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        new WeatherService().setVisible(true);
                    }
                });
            }

            // Variables declaration - do not modify                     
            // End of variables declaration                   
        }
```
# Authors

- MuhammadSaputraArjunaidy
- 2210010300
- 5B Reg Pagi Banjarmasin
- [@MuhammadSaputraArjunaidy](https://www.github.com/MuhammadSaputraArjunaidy)


#  Screenshot

![image](https://github.com/user-attachments/assets/6eb2fee8-e794-4e79-9437-9254d497477d)



