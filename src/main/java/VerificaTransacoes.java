import java.math.BigDecimal;
import java.sql.*;
import java.util.Scanner;

public class VerificaTransacoes {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Pesquisar por nome do Consumidor: ");
        String nomePesquisa = input.nextLine();

        try (Connection conexao = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/vendasdb", "usuario", "senha");
             PreparedStatement comando = conexao.prepareStatement(
                     "select * from transacao where nome_consumidor like ?")) {
            comando.setString(1, "%" + nomePesquisa + "%");
            ResultSet resultado = comando.executeQuery();

            while (resultado.next()) {
                Long id = resultado.getLong("id");
                String nomeConsumidor = resultado.getString("nome_consumidor");
                BigDecimal valorTransacao = resultado.getBigDecimal("valor_transacao");
                Date dataTransacao = resultado.getDate("data_transacao");

                System.out.printf("%d - %s - R$%.2f - %s%n",
                        id, nomeConsumidor, valorTransacao, dataTransacao);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao acessar banco de dados");
            e.printStackTrace();
        }
    }

}
