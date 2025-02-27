# **Module 1**

### **Refleksi 1**  

#### **1. Prinsip Clean Code yang Diterapkan:**  
**Penamaan yang Bermakna:**  
- Nama kelas dan metode (`ProductRepositoryTest`, `testCreateAndFind`, `testEditProduct`, dll.) sudah cukup jelas menggambarkan tujuannya.  
- Properti dalam `Product` seperti `productId`, `productName`, dan `productQuantity` mengikuti konvensi penamaan yang baik.  

**Single Responsibility Principle (SRP):**  
- `ProductRepository` hanya menangani penyimpanan produk, sedangkan `ProductTest` fokus pada pengujian.  

**Encapsulation:**  
- Properti dalam kelas `Product` bersifat `private`, sehingga aksesnya dikontrol melalui metode getter dan setter.  

**Kode Pengujian yang Mudah Dibaca dan Dikelola:**  
- `@BeforeEach` digunakan untuk menginisialisasi lingkungan pengujian, sehingga setiap pengujian bersifat independen.  
- Penggunaan `assertEquals()` dan `assertTrue()` membuat hasil pengujian lebih jelas.  

---

#### **2. Praktik Keamanan yang Diterapkan:**  
**Menghindari Akses Langsung ke Properti:**  
- Penggunaan metode getter dan setter untuk mengakses data produk meningkatkan keamanan.  

**Menjaga Integritas Data:**  
- Metode `edit()` memastikan atribut produk diperbarui dengan benar.  
- Metode `delete()` menggunakan `removeIf()` untuk menghindari penghapusan produk yang tidak disengaja.  

**Pengujian Unit untuk Validasi:**  
- Pengujian memastikan bahwa operasi CRUD bekerja dengan baik, sehingga mengurangi risiko kesalahan atau bug.  

---

### **3. Area yang Perlu Diperbaiki:**  
**Masalah 1: Tidak Ada Validasi untuk Data Null atau Kosong**  
- **Masalah:**  
  - Metode `edit()` dan `delete()` mengasumsikan bahwa produk selalu ada.  
- **Solusi:**  
  - Tambahkan validasi sebelum melakukan operasi:  
    ```java
    public void edit(Product updatedProduct) {
        if (updatedProduct == null || updatedProduct.getProductId() == null) {
            throw new IllegalArgumentException("Produk tidak boleh null");
        }
        for (Product p : productList) {
            if (p.getProductId().equals(updatedProduct.getProductId())) {
                p.setProductName(updatedProduct.getProductName());
                p.setProductQuantity(updatedProduct.getProductQuantity());
                return;
            }
        }
        throw new IllegalArgumentException("Produk tidak ditemukan");
    }
    ```

**Masalah 2: Penggunaan `Iterator<Product>` dalam `findAll()` Kurang Efisien**  
- **Masalah:**  
  - Pengembalian `Iterator<Product>` mungkin kurang optimal untuk jumlah data yang besar.  
- **Solusi:**  
  - Sebaiknya gunakan `List<Product>` agar lebih fleksibel:  
    ```java
    public List<Product> findAll() {
        return new ArrayList<>(productList);
    }
    ```

**Masalah 3: Tidak Ada Logging**  
- **Masalah:**  
  - Tidak ada pencatatan (logging) untuk operasi yang dilakukan.  
- **Solusi:**  
  - Gunakan `SLF4J` untuk mencatat operasi:  
    ```java
    private static final Logger logger = LoggerFactory.getLogger(ProductRepository.class);
    ```

---

### **Refleksi 2**  

#### **1. Pengalaman dalam Menulis Unit Test**  

**Bagaimana perasaan setelah menulis unit test?**  
Setelah menulis unit test, saya merasa lebih yakin bahwa kode yang saya buat berfungsi sesuai dengan yang diharapkan. Pengujian ini membantu menemukan potensi bug lebih awal sebelum kode digunakan di lingkungan produksi.  

**Berapa banyak unit test yang sebaiknya dibuat dalam satu kelas?**  
Jumlah unit test yang diperlukan tergantung pada kompleksitas kelas tersebut. Idealnya, setiap metode atau skenario penting yang dapat mempengaruhi perilaku program harus memiliki pengujiannya sendiri.  

**Bagaimana memastikan unit test sudah cukup untuk memverifikasi program?**  
- Menggunakan **code coverage** sebagai metrik untuk mengukur sejauh mana kode telah diuji.  
- Menggunakan **test case yang mencakup berbagai skenario**, termasuk skenario positif (valid input) dan negatif (error handling).  
- Menggunakan **boundary testing** untuk memastikan aplikasi dapat menangani batasan data tertentu.  

**Apakah 100% code coverage berarti tidak ada bug?**  
Tidak. Meskipun 100% code coverage berarti semua baris kode telah diuji, itu tidak menjamin bahwa kode bebas dari bug. Hal ini karena:  
- **Code coverage hanya mengukur apakah suatu bagian kode telah dieksekusi**, bukan apakah kode tersebut memiliki perilaku yang benar dalam semua situasi.  
- **Kemungkinan adanya bug dalam interaksi antara beberapa komponen**, yang mungkin tidak tertangkap dalam unit test.  
- **Kode bisa saja memiliki kesalahan logika** yang tidak terdeteksi oleh pengujian sederhana.  

---

#### **2. Evaluasi Functional Test Suite Baru**  

**Bagaimana kebersihan kode dari functional test suite yang baru?**  
Membuat functional test suite baru yang mirip dengan sebelumnya **dapat mengurangi kualitas kode** jika terdapat banyak duplikasi kode. Ini bisa menyebabkan:  
- **Kode sulit dipelihara** karena jika ada perubahan di satu tempat, semua test suite yang mirip harus diperbarui.  
- **Kurangnya reusabilitas**, di mana setup dan konfigurasi seharusnya bisa digunakan ulang tanpa perlu menulis ulang di setiap kelas.  
- **Peluang tinggi untuk inkonsistensi**, jika beberapa suite diuji dengan skenario yang sedikit berbeda.  

**Identifikasi masalah clean code dan solusinya**  
| Masalah | Penjelasan | Solusi |
|---------|-----------|--------|
| **Duplikasi kode** | Test suite baru mengulangi setup dan variabel yang sama | Refactor ke metode setup terpisah atau superclass test |
| **Kesulitan pemeliharaan** | Jika setup berubah, semua test suite harus diperbarui | Gunakan inheritance atau dependency injection |
| **Kurangnya modularitas** | Test suite berdiri sendiri tanpa bisa digunakan ulang | Buat metode utilitas untuk setup dan assertion yang berulang |

**Cara memperbaiki kode agar lebih bersih:**  
1. **Gunakan superclass atau helper class untuk setup yang berulang**  
    ```java
    @BeforeEach
    void setUp() {
        productRepository = new ProductRepository();
    }
    ```
2. **Gunakan parameterized tests jika ada pola pengujian yang berulang**  
    ```java
    @ParameterizedTest
    @ValueSource(ints = {1, 10, 100})
    void testProductQuantity(int quantity) {
        Product product = new Product("Sampo Cap Bambang", quantity);
        assertEquals(quantity, product.getProductQuantity());
    }
    ```
3. **Pisahkan logic setup ke dalam metode utilitas yang bisa digunakan kembali**  
    ```java
    Product createProduct(String id, String name, int quantity) {
        Product product = new Product();
        product.setProductId(id);
        product.setProductName(name);
        product.setProductQuantity(quantity);
        return product;
    }
    ```
---

# **Module 2**

### **Refleksi 1**  

Saat mengerjakan modul ini, saya menyadari bahwa workflow GitHub Actions masih menggunakan izin token default, yang terlalu rentan untuk melewati pemeriksaan keamanan dan pemindaian kode atau Scorecard. Untuk mengatasi hal ini, saya menambahkan konfigurasi berikut ke dalam direktori `.github/workflows/ci.yml`:

```yaml
  permission:
    contents: read
```

Langkah ini mengikuti prinsip hak akses minimal, memastikan bahwa workflow hanya memiliki izin baca yang benar-benar diperlukan. Dengan demikian, saya berhasil meningkatkan keamanan dan kualitas kode serta menyelesaikan isu yang terdeteksi.

---

### **Refleksi 2**

Implementasi dalam proyek saya sepenuhnya memenuhi prinsip CI/CD. Dalam proses CI, setiap perubahan kode secara otomatis diuji, dianalisis untuk memastikan kualitas, dan diperiksa terhadap potensi kerentanan keamanan sebelum digabungkan. Dengan pendekatan ini, hanya kode yang telah terverifikasi dengan baik yang dapat masuk ke branch utama, memastikan stabilitas dan keandalan proyek.  

Sementara itu, deployment ke Koyeb berjalan sepenuhnya otomatis, memungkinkan setiap perubahan yang lolos semua pengujian untuk langsung diterapkan tanpa perlu intervensi manual. Hal ini sejalan dengan konsep Continuous Deployment, di mana setiap pembaruan yang memenuhi standar kualitas segera dirilis ke produksi. Dengan workflow yang efisien ini, saya dapat meminimalkan keterlambatan, mempercepat siklus rilis, dan memastikan pengiriman perangkat lunak yang cepat, stabil, dan aman.

---
