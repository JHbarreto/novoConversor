import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
import java.util.Objects;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;

public class PrimeiraTela extends JFrame {
    private JPanel panelPrincipal;
    private JButton confirmarButton;
    private JButton sairButton;
    private JComboBox<String> optionBox;

    private Map<String, String> mapaNotacaoMoeda;


    public PrimeiraTela() throws IOException, InterruptedException {
        setContentPane(panelPrincipal);
        setTitle("Menu Inicial");
        setSize(250, 120);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        String[] opcoes = {"conversor de moeda", "conversor de temperatura", "conversor de medida"};
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(opcoes);
        optionBox.setModel(model);


        confirmarButton.addActionListener(e1 -> {
            try {
                actionPerformed(e1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        sairButton.addActionListener(e -> dispose());

        mapaNotacaoMoeda = new HashMap<>();
        mapaNotacaoMoeda.put("Real Brasileiro para Dólar Americano", "BRLUSD");
        mapaNotacaoMoeda.put("Dólar Americano para Real Brasileiro", "USDBRL");
        mapaNotacaoMoeda.put("Real Brasileiro para Euro", "BRLEUR");
        mapaNotacaoMoeda.put("Euro para Real Brasileiro", "EURBRL");
        mapaNotacaoMoeda.put("Real Brasileiro para Libra Esterlina", "BRLGBP");
        mapaNotacaoMoeda.put("Libra Esterlina para Real Brasileiro", "GBPBRL");
        mapaNotacaoMoeda.put("Real Brasileiro para Iene Japonês", "BRLJPY");
        mapaNotacaoMoeda.put("Iene Japonês para Real Brasileiro", "JPYBRL");
        mapaNotacaoMoeda.put("Real Brasileiro para Won Sul-Coreano", "BRLKRW");
        mapaNotacaoMoeda.put("Won Sul-Coreano para Real Brasileiro", "KRWBRL");
        mapaNotacaoMoeda.put("Real Brasileiro para Peso Argentino", "BRLARS");
        mapaNotacaoMoeda.put("Peso Argentino para Real Brasileiro", "ARSBRL");
        mapaNotacaoMoeda.put("Real Brasileiro para Peso Chileno", "BRLCLP");
        mapaNotacaoMoeda.put("Peso Chileno para Real Brasileiro", "CLPBRL");
        mapaNotacaoMoeda.put("Real Brasileiro para Bitcoin", "BRLBTC");
        mapaNotacaoMoeda.put("Bitcoin para Real Brasileiro", "BTCBRL");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PrimeiraTela::run);
    }
    private static void run() {

        try {
            PrimeiraTela inicio = new PrimeiraTela();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void actionPerformed(ActionEvent e) throws InterruptedException {
        String opcaoSelecionada = (String) optionBox.getSelectedItem();

        switch (Objects.requireNonNull(opcaoSelecionada)) {
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

                String[] possibilidades = {"Real Brasileiro para Dólar Americano","Dólar Americano para Real Brasileiro","Real Brasileiro para Euro","Euro para Real Brasileiro",
                        "Real Brasileiro para Libra Esterlina","Libra Esterlina para Real Brasileiro","Real Brasileiro para Iene Japonês","Iene Japonês para Real Brasileiro",
                        "Real Brasileiro para Won Sul-Coreano","Won Sul-Coreano para Real Brasileiro","Real Brasileiro para Peso Argentino","Peso Argentino para Real Brasileiro",
                        "Real Brasileiro para Peso Chileno","Peso Chileno para Real Brasileiro","Real Brasileiro para Bitcoin","Bitcoin para Real Brasileiro"};
                String escolha = (String) JOptionPane.showInputDialog(null, "Escolha a moeda para a qual você deseja converter seu dinheiro:",
                        "Conversão", JOptionPane.QUESTION_MESSAGE, null, possibilidades, possibilidades[0]);

                int indiceEscolhido = -1;
                for (int i = 0; i < possibilidades.length; i++) {
                    if (escolha.equals(possibilidades[i])) {
                        indiceEscolhido = i;
                        break;
                    }
                }
                String notacaoMoeda = "";

                if (indiceEscolhido != -1) {
                    String[] pair = {"BRL-USD","USD-BRL","BRL-EUR","EUR-BRL","BRL-GBP","GBP-BRL",
                            "BRL-JPY","JPY-BRL","BRL-KRW","KRW-BRL","BRL-ARS","ARS-BRL","BRL-CLP","CLP-BRL","BRL-BTC","BTC-BRL"};
                    for (int i = 0; i < pair.length; i++) {
                        if (indiceEscolhido == i) {
                            notacaoMoeda = pair[i];
                            break;
                        }
                    }
                    String currencyPair = mapaNotacaoMoeda.get(escolha);

                    HttpClient httpClient = HttpClient.newBuilder().build();
                    HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create("https://economia.awesomeapi.com.br/last/" + notacaoMoeda)).build();
                    try {
                        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
                        String responseBody = httpResponse.body();

                        Gson gson = new Gson();

                        RespostaAPI respostaAPI = gson.fromJson(responseBody, RespostaAPI.class);

                        Moeda moeda ;
                        switch (currencyPair) {
                            case "BRLUSD" -> {
                                moeda = respostaAPI.getBrlusd();
                                break;}

                            case "USDBRL" -> {
                                moeda = respostaAPI.getUsdbrl();
                            break;}

                            case "BRLEUR"-> {
                                moeda = respostaAPI.getBrleur();
                                break;}

                            case "EURBRL"-> {
                                moeda = respostaAPI.getEurbrl();
                                break;}

                            case "BRLGBP"-> {
                                moeda = respostaAPI.getBrlgbp();
                                break;}

                            case "GBPBRL"-> {
                                moeda = respostaAPI.getGbpbrl();
                                break;}

                            case "BRLJPY"-> {
                                moeda = respostaAPI.getBrljpy();
                                break;}

                            case "JPYBRL"-> {
                                moeda = respostaAPI.getJpybrl();
                                break;}

                            case "BRLKRW"-> {
                                moeda = respostaAPI.getBrlkrw();
                                break;}

                            case "KRWBRL"-> {
                                moeda = respostaAPI.getKrwbrl();
                                break;}

                            case "BRLARS"-> {
                                moeda = respostaAPI.getBrlars();
                                break;}

                            case "ARSBRL"-> {
                                moeda = respostaAPI.getArsbrl();
                                break;}

                            case"BRLCLP" -> {
                                moeda = respostaAPI.getBrlclp();
                                break;
                            }

                            case"CLPBRL" -> {
                                moeda = respostaAPI.getClpbrl();
                                break;
                            }

                            case "BRLBTC"-> {
                                moeda = respostaAPI.getBrlbtc();
                                break;}

                            case "BTCBRL"-> {
                                moeda = respostaAPI.getBtcbrl();
                                break;}
                            default-> {
                                moeda = null;
                                break;}
                        }
                        if (moeda !=null){
                            moeda.setName(notacaoMoeda);
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "Moeda não encontrada na resposta da API", "Erro", JOptionPane.ERROR_MESSAGE);
                        }

                        double fatorConversao = Double.parseDouble(moeda.getBid());
                        double resultado;
                        DecimalFormat df = new DecimalFormat("#.##");

                        resultado = valor*fatorConversao;

                        JOptionPane.showMessageDialog(null, "Resultado: " + df.format(resultado), "Resultado", JOptionPane.INFORMATION_MESSAGE, null);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        throw new RuntimeException(ex);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }

            case "conversor de temperatura" -> {
                boolean valorIncorreto = true;
                double valor = 0;

                while (valorIncorreto) {
                    String valorString = JOptionPane.showInputDialog(null, "Insira um valor:", "Insira um valor", JOptionPane.INFORMATION_MESSAGE);
                    try {
                        valor = Double.parseDouble(valorString);
                        valorIncorreto = false;
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

                String[] possibilidades = {"De m para km", "De Km a m", "De m a hm", "De hm a m", "De m a dam", "De dam a m", "De m a dm",
                        "De dm a m", "De m a cm", "De cm a m", "De m a mm", "De mm a m"};
                String escolha = (String) JOptionPane.showInputDialog(null, "Escolha como você deseja converter :",
                        "Conversão", JOptionPane.QUESTION_MESSAGE, null, possibilidades, possibilidades[0]);

                int indiceEscolhido = -1;
                double baseConversao = 10;
                double[] expoenteConversao = {3,2,1,1,2,3};
                double[] fatorConversao = new double[expoenteConversao.length];


                for (int i = 0; i < expoenteConversao.length; i++) {
                    fatorConversao[i] = Math.pow(baseConversao, expoenteConversao[i]);
                }

                for (int i = 0; i < possibilidades.length; i++) {
                    if (escolha.equals(possibilidades[i])) {
                        indiceEscolhido = i;
                        break;
                    }
                }

                if (indiceEscolhido != -1) {
                    double resultado;

                    if (indiceEscolhido % 2 == 0 && indiceEscolhido <= 5) {
                        resultado = valor / fatorConversao[indiceEscolhido / 2];
                    } else if (indiceEscolhido % 2 != 0 && indiceEscolhido <= 5) {
                        resultado = valor * fatorConversao[indiceEscolhido / 2];
                    } else if (indiceEscolhido % 2 == 0 && indiceEscolhido > 5) {
                        resultado = valor * fatorConversao[indiceEscolhido / 2];
                    } else {
                        resultado = valor / fatorConversao[indiceEscolhido / 2];
                    }

                    JOptionPane.showMessageDialog(null, "Resultado: " + (resultado), "Resultado", JOptionPane.INFORMATION_MESSAGE, null);
                }
            }
        }
    }
}