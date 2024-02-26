/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assign3;

/**
 *
 * @author lenovo
 */
import java.sql.*;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Database {
    private Connection connection;
   
    public Database() {
        try {
            // Assumes you have a local MySQL server running on the default port (3306)

            String dburl = "jdbc:mysql://localhost:3306/highscoresdb?serverTimezone=Europe/Berlin";
            connection = DriverManager.getConnection(dburl, "abedgotit", "Abedgotit33");
            System.out.println("Connected to MySQL database successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to connect to MySQL database.");
        }
    }
    
    public void insertHighScore(String name, int score) {
        String query = "INSERT INTO highscoretable (name, score) VALUES (?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, name);
            pst.setInt(2, score);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadHighScores(int round,JTable table) {
      /*  String query = "SELECT name, score FROM highscoretable ORDER BY score DESC LIMIT 10";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0); // Clear the existing rows
            
            while (rs.next()) {
                String playerName = rs.getString("name");
                int highScore = rs.getInt("score");
                model.addRow(new Object[]{playerName, highScore});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
*/
       try {
             String query = "SELECT name, score FROM highscoretable ORDER BY score DESC LIMIT 10";
            Statement stmt = connection.createStatement();
            ResultSet results = stmt.executeQuery(query);
              DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
            
            while (results.next()) {
            String name = results.getString("name");
            int score = results.getInt("score");
             System.out.println("Fetched - Name: " + name + ", Score: " + score); // Debugging line
            model.addRow(new Object[]{name, score});
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Disconnected from MySQL database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

   
}