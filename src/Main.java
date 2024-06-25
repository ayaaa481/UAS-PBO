import javax.swing.JFrame;
import view.ViewLogin;

public class Main {
    
    public static void main(String[] args) {
        // Inisialisasi frame utama
        JFrame frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Tambahkan panel login ke frame
        ViewLogin viewLogin = new ViewLogin();
        frame.add(viewLogin);
        
        // Atur ukuran frame sesuai kebutuhan
        frame.pack();
        
         // Menempatkan frame di tengah layar
        frame.setLocationRelativeTo(null);
        
        // Tampilkan frame
        frame.setVisible(true);
    }
}
