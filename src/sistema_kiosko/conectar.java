package sistema_kiosko;

import java.sql.*;
import javax.swing.JOptionPane;

public class conectar {
    private Connection conect = null;
    
    public Connection conexion(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conect = DriverManager.getConnection("jdbc:mysql://localhost:3306/kiosko", "root", "root1234");
            
            //JOptionPane.showMessageDialog(null, "CONECTADO CON ÉXITO A LA BASE DE DATOS");
            
        } catch(ClassNotFoundException | SQLException error){
            JOptionPane.showMessageDialog(null, "Hubo un error al conectar con la base de datos: " + error);
        }
        
        return conect;
    }
    
    public Connection getConnection(){
        return conect;
    }
    
    public void closeConnection(){
        try {
            if (conect != null) {
                conect.close();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cerrar la conexión: " + e);
        }
    }
}
