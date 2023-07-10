import javax.swing.*;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;


public class PrimeiraTela extends JFrame {
    private JPanel panel1;
    private JButton confirmarButton;
    private JButton sairButton;
    private JComboBox<String> optionBox;

    public PrimeiraTela() {
        setContentPane(panel1);
        setTitle("Menu Inicial");
        setSize(250, 250);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        String[] opcoes = {"conversor de moeda", "conversor de temperatura", "conversor de medida"};
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(opcoes);
        optionBox.setModel(model);


        confirmarButton.addActionListener(this::actionPerformed);

        sairButton.addActionListener(e -> dispose());

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PrimeiraTela inicio = new PrimeiraTela();
        });
    }

    private void actionPerformed(ActionEvent e) {
        String opcaoSelecionada = (String) optionBox.getSelectedItem();

        switch (opcaoSelecionada) {
            case "conversor de moeda" -> {
                double valor = 0;
                boolean valorIncorreto = true;

                while (valorIncorreto) {
                    String valorString = JOptionPane.showInputDialog(null, "Insira um valor:", "Insira um valor", JOptionPane.INFORMATION_MESSAGE);

                    try {
                        valor = Double.parseDouble(valorString);
                        if (valor != 0 && valor > 0) {
                            valorIncorreto = false;
                        } else {
                            JOptionPane.showMessageDialog(null, "Valor incorreto, digite novamente (diferente de zero / maior que zero)", "Valor incorreto", JOptionPane.WARNING_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Valor incorreto, digite novamente (número inválido)", "Valor incorreto", JOptionPane.WARNING_MESSAGE);
                    }
                }

                String[] possibilidades = {"De Reais a Dólares", "De Dólares a Reais", "De Reais a Euros", "De Euros a Reais",
                        "De Reais a Libras", "De Libras a Reais", "De Reais a Yenes", "De Yenes a Reais", "De Reais a Won Coreano", "De Won Coreano a Reais"};
                String escolha = (String) JOptionPane.showInputDialog(null, "Escolha a moeda para a qual você deseja converter seu dinheiro:",
                        "Conversão", JOptionPane.QUESTION_MESSAGE, null, possibilidades, possibilidades[0]);

                int indiceEscolhido = -1;
                double[] fatorConversao = {4.84, 5.27, 6.15, 0.03, 0.0037};

                for (int i = 0; i < possibilidades.length; i++) {
                    if (escolha.equals(possibilidades[i])) {
                        indiceEscolhido = i;
                        break;
                    }
                }

                if (indiceEscolhido != -1) {
                    double resultado;
                    DecimalFormat df = new DecimalFormat("#.##");

                    if (indiceEscolhido % 2 == 0) {
                        resultado = valor / fatorConversao[indiceEscolhido / 2];
                    } else {
                        resultado = valor * fatorConversao[indiceEscolhido / 2];
                    }

                    JOptionPane.showMessageDialog(null, "Resultado: " + df.format(resultado), "Resultado", JOptionPane.INFORMATION_MESSAGE, null);
                }
            }

            case "conversor de temperatura" -> {
                boolean valorIncorreto = true;
                double valor = 0; // Inicialize o valor com um valor padrão

                while (valorIncorreto) {
                    String valorString = JOptionPane.showInputDialog(null, "Insira um valor:", "Insira um valor", JOptionPane.INFORMATION_MESSAGE);
                    try {
                        valor = Double.parseDouble(valorString);
                        valorIncorreto = false; // Defina valorIncorreto como false se o valor for válido
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Valor incorreto, digite novamente (apenas números)", "Valor incorreto", JOptionPane.WARNING_MESSAGE);
                    }
                }

                String[] possibilidades = {"De Celsius (°C ) a Kelvin (K)", "De Kelvin (K) a Celsius (°C )",
                        "De Celsius (°C ) a Fahrenheit (°F)", "De Fahrenheit (°F) a Celsius (°C )"};
               String escolha = (String) JOptionPane.showInputDialog(null, "Escolha a escala:", "Conversão", JOptionPane.QUESTION_MESSAGE, null, possibilidades, possibilidades[0]);
                double resultado;
                DecimalFormat df = new DecimalFormat("#.##");
                if (escolha.equals(possibilidades[0])) {
                    resultado = valor + 273.15;
                    JOptionPane.showMessageDialog(null, "Resultado: " + df.format(resultado), "Resultado", JOptionPane.INFORMATION_MESSAGE, null);
                } else if (escolha.equals(possibilidades[1])) {
                    resultado = valor - 273.15;
                    JOptionPane.showMessageDialog(null, "Resultado: " + df.format(resultado), "Resultado", JOptionPane.INFORMATION_MESSAGE, null);
                } else if (escolha.equals(possibilidades[2])) {
                    resultado = (valor * 1.8) + 32.00;
                    JOptionPane.showMessageDialog(null, "Resultado: " + df.format(resultado), "Resultado", JOptionPane.INFORMATION_MESSAGE, null);
                } else if (escolha.equals(possibilidades[3])) {
                    resultado = (valor - 32) * 1.8;
                    JOptionPane.showMessageDialog(null, "Resultado: " + df.format(resultado), "Resultado", JOptionPane.INFORMATION_MESSAGE, null);
                }
            }

            case "conversor de medida" -> {
                double valor = Double.parseDouble(JOptionPane.showInputDialog(null, "insira um valor:", "insira um valor", JOptionPane.INFORMATION_MESSAGE));
                if (valor != 0 && valor > 0) {
                    String[] possibilidades = {"De m para km", "De Km a m", "De m a hm", "De hm a m", "De m a dam", "De dam a m", "De m a dm",
                            "De dm a m", "De m a cm", "De cm a m", "De m a mm", "De mm a m"};
                    JOptionPane.showInputDialog(null, "escolha a medida", "conversão", JOptionPane.QUESTION_MESSAGE, null, possibilidades, possibilidades[0]);
                } else
                    Double.parseDouble(JOptionPane.showInputDialog(null, "valor incorreto,digite novamente", "valor incorreto", JOptionPane.WARNING_MESSAGE));
            }
        }

    }
}