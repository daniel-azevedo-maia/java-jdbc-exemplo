import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class RegistroVendas {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String dml = """
                insert into transacao (
                    nome_consumidor,
                    valor_transacao,
                    data_transacao
                ) values (?, ?, ?)
                """;

        try (Connection conexao = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/vendasdb", "usuario", "senha");
             PreparedStatement comando = conexao.prepareStatement(dml, Statement.RETURN_GENERATED_KEYS)) {
            conexao.setAutoCommit(false);

            try {
                do {
                    System.out.print("Nome do Consumidor: ");
                    String nomeConsumidor = input.nextLine();

                    System.out.print("Valor da Transação: ");
                    BigDecimal valorTransacao = new BigDecimal(input.nextLine());

                    System.out.print("Data da Transação: ");
                    LocalDate dataTransacao = LocalDate.parse(input.nextLine(),
                            DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                    comando.setString(1, nomeConsumidor);
                    comando.setBigDecimal(2, valorTransacao);
                    comando.setDate(3, Date.valueOf(dataTransacao));
                    comando.executeUpdate();

                    ResultSet idResultSet = comando.getGeneratedKeys();
                    idResultSet.next();
                    long idTransacao = idResultSet.getLong(1);

                    System.out.println("Transação registrada com ID " + idTransacao + "!");

                    System.out.print("Registrar outra transação? ");
                } while ("sim".equalsIgnoreCase(input.nextLine()));

                conexao.commit();
            } catch (Exception e) {
                conexao.rollback();
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            System.out.println("Erro ao registrar transação");
            e.printStackTrace();
        }
    }

}
