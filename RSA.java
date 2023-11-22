package TareaRSA;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.util.Random;

public class RSA {
    private BigInteger p;
    private BigInteger q;
    private BigInteger n;
    private BigInteger fi;
    private BigInteger e;
    private BigInteger d;
    private int tamprimo;

    private JTextField inputField;
    private JButton encryptButton;
    private JButton decryptButton;
    private JLabel resultLabel;
    private JLabel encryptedLabel;

    RSA(int tamprimo) {
        this.tamprimo = tamprimo;
        generarPrimos();
        generarClaves();

        JFrame frame = new JFrame("RSA");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);
    }

    public void generarPrimos() {
        p = new BigInteger(tamprimo, 10, new Random());
        do {
            q = new BigInteger(tamprimo, 10, new Random());
        } while (q.compareTo(p) == 0);
    }

    public void generarClaves() {
        n = p.multiply(q);
        fi = p.subtract(BigInteger.valueOf(1));
        fi = fi.multiply(q.subtract(BigInteger.valueOf(1)));
        do {
            e = new BigInteger(2 * tamprimo, new Random());
        } while ((e.compareTo(fi) != -1 || (e.gcd(fi).compareTo(BigInteger.valueOf(1)) != 0)));
        d = e.modInverse(fi);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel explicacion = new JLabel("En encriptado tambien se imprimio en la consola");
        explicacion.setBounds(10, 40, 500, 25);
        panel.add(explicacion);

        JLabel Bienvenida = new JLabel("Input:");
        Bienvenida.setBounds(10, 20, 80, 25);
        panel.add(Bienvenida);

        JLabel userLabel = new JLabel("Bienvenido al encriptado RSA de Jetro :D ");
        userLabel.setBounds(10, 0, 500, 25);
        panel.add(userLabel);

        inputField = new JTextField(20);
        inputField.setBounds(100, 20, 165, 25);
        panel.add(inputField);

        encryptButton = new JButton("Encrypt");
        encryptButton.setBounds(10, 90, 80, 25);
        encryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = inputField.getText();
                BigInteger[] encrypted = cifrar(input);
                StringBuilder sb = new StringBuilder();
                for (BigInteger bi : encrypted) {
                    sb.append(bi.toString());
                    sb.append(" ");
                }
                encryptedLabel.setText("Encrypted: " + sb.toString());
                System.out.println(sb.toString());
            }
        });
        panel.add(encryptButton);

        decryptButton = new JButton("Decrypt");
        decryptButton.setBounds(100, 90, 80, 25);
        decryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = encryptedLabel.getText().substring(11);
                String[] parts = input.split(" ");
                BigInteger[] encrypted = new BigInteger[parts.length];
                for (int i = 0; i < encrypted.length; i++) {
                    encrypted[i] = new BigInteger(parts[i]);
                }
                String decrypted = descifrar(encrypted);
                resultLabel.setText("Decrypted: " + decrypted);
            }
        });
        panel.add(decryptButton);

        encryptedLabel = new JLabel();
        JScrollPane scrollPane = new JScrollPane(encryptedLabel);
        scrollPane.setBounds(10, 150, 500, 50);
        panel.add(scrollPane);

        resultLabel = new JLabel();
        scrollPane = new JScrollPane(resultLabel);
        scrollPane.setBounds(10, 190, 500, 50);
        panel.add(scrollPane);
    }

    public BigInteger[] cifrar(String mensaje) {
        byte[] temp = new byte[1];
        byte[] digitos = mensaje.getBytes();
        BigInteger[] bigdigitos = new BigInteger[digitos.length];

        for (int i = 0; i < bigdigitos.length; i++) {
            temp[0] = digitos[i];
            bigdigitos[i] = new BigInteger(temp);
        }

        BigInteger[] cifrado = new BigInteger[bigdigitos.length];

        for (int i = 0; i < bigdigitos.length; i++) {
            cifrado[i] = bigdigitos[i].modPow(e, n);
        }

        return cifrado;
    }

    public String descifrar(BigInteger[] cifrado) {
        BigInteger[] descifrado = new BigInteger[cifrado.length];

        for (int i = 0; i < descifrado.length; i++) {
            descifrado[i] = cifrado[i].modPow(d, n);
        }

        char[] charArray = new char[descifrado.length];

        for (int i = 0; i < charArray.length; i++) {
            charArray[i] = (char) (descifrado[i].intValue());
        }

        return (new String(charArray));
    }

    public static void main(String[] args) {
        new RSA(1024);
    }
}
