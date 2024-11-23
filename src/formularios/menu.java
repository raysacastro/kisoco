
package formularios;

import java.awt.Dimension;
import java.awt.print.PrinterException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import sistema_kiosko.conectar;

public class menu extends javax.swing.JFrame {

    conectar cc = new conectar();
    Connection cn = (Connection) cc.conexion();
    public menu() {
        initComponents();
        total.setVisible(false);
        cargartodo.setVisible(false);
        boton1(); boton2();boton3(); boton4(); boton5();boton6(); boton7(); boton8();boton9(); boton10(); boton11();boton12();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
         buscar.requestFocus();
         cargar();
         cargar_dia();
         cargar_caja();
         numero_factura();
         contado.setSelected(true);
            Date fechaActual = new Date();
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            String fechaFormateada = formatoFecha.format(fechaActual);
            fecha.setText(fechaFormateada);
    }
    
    
    
public void addTable(String Name, Double Price, String Idart) {
    String Qty = JOptionPane.showInputDialog("Cantidad de Producto");
    Double tqty = Double.valueOf(Qty);
    Double Tot_Price = Price * tqty;
    
    DecimalFormat df = new DecimalFormat("00.00");
    String d11 = df.format(Tot_Price);

    DefaultTableModel dt = (DefaultTableModel) t_factura.getModel();
    boolean encontrado = false;

    // Buscar si el producto ya está en la factura
    for (int i = 0; i < dt.getRowCount(); i++) {
        if (dt.getValueAt(i, 0).equals(Idart)) {
            // Actualizar la cantidad y el precio total del producto
            Double cantidadExistente = Double.parseDouble(dt.getValueAt(i, 2).toString());
            Double precioExistente = Double.parseDouble(dt.getValueAt(i, 3).toString());
            cantidadExistente += tqty;
            Double nuevoPrecioTotal = precioExistente * cantidadExistente;
            dt.setValueAt(cantidadExistente, i, 2);
            dt.setValueAt(df.format(nuevoPrecioTotal), i, 4);
            encontrado = true;
            break;
        }
    }

    if (!encontrado) {
        Vector v = new Vector();
        v.add(Idart);
        v.add(Name);
        v.add(Qty);
        v.add(Price);
        v.add(d11);
        dt.addRow(v);
    }
    sumar_productos();
    cart_cal();
}
public void cart_cal(){
 
  int numofrow = t_factura.getRowCount();
  double total = 0 ;
     for (int i = 0; i < numofrow; i++) {
         double value = Double.valueOf(t_factura.getValueAt(i, 4).toString());
         total+= value;
         
     }
  
     DecimalFormat df = new DecimalFormat("00.00");
     String d1 = df.format(total);
     Too.setText(d1);
     
  
 }
public void bill_print() {
    try {
        bill.setText("\t                Kiosko Café \n");
        bill.setText(bill.getText() + "\tC/Héroes de la Restauración #45 \n");
        bill.setText(bill.getText() + "\t      Loma de Cabrera, Dajabón \n");
        bill.setText(bill.getText() + "\t             809-579-4882 \n");
        bill.setText(bill.getText() + "----------------------------------------------------------------.-------------\n");
        bill.setText(bill.getText() + "       Producto \t                 Cantidad          Precio          Total \n");
        bill.setText(bill.getText() + "----------------------------------------------------------------.-------------\n");
        
        DefaultTableModel df = (DefaultTableModel) t_factura.getModel();
        for (int i = 0; i < t_factura.getRowCount(); i++) {
            String name = df.getValueAt(i, 1).toString();
            String qt = df.getValueAt(i, 2).toString();
            String prc = df.getValueAt(i, 3).toString();
            String tf = df.getValueAt(i, 4).toString();
            // Si el nombre del producto es demasiado largo, dividirlo en varias líneas
            if (name.length() > 5) {
                int index = 5;
                while (index < name.length() && name.charAt(index) != ' ') {
                    index++;
                }
                if (index < name.length()) {
                    name = name.substring(0, index) + "\n" + name.substring(index + 1);
                }
            }

            bill.setText(bill.getText() + name + "\t                         " + qt + "\t                " + prc + "\t " + tf + " \n");
        }
        bill.setText(bill.getText() + "----------------------------------------------------------------.-------------\n");
        bill.setText(bill.getText() + "SubTotal :\t" + Too.getText() + "\n");
        bill.setText(bill.getText() + "Efectivo :\t" + Cash.getText() + "\n");
        bill.setText(bill.getText() + "Devuelta :\t" + Bal.getText() + "\n");
        bill.setText(bill.getText() + "====================================.=========\n");
        bill.setText(bill.getText() + "                     Gracias por su Compra...!" + "\n");
        bill.setText(bill.getText() + "----------------------------------------------------------------.-------------\n");
        bill.setText(bill.getText() + "                     Muy buen Provecho" + "\n");
        
        bill.print();
        
    } catch (PrinterException ex) {
        Logger.getLogger(ventas.class.getName()).log(Level.SEVERE, null, ex);
    }
}
void limpiar_datos() {
        id_articulo.setText("");
        desart.setText("");
        precio.setText("");
        cantidad.setText("");
        buscar.setText("");
    }     
void cargar_dia() {
    // Obtener la fecha actual
    DecimalFormat formateador = new DecimalFormat("#,###,###,###.##");
    Date fechaActual = new Date();
    SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
    String fecha = formatoFecha.format(fechaActual);

    // Consulta SQL para sumar los totales de las facturas del día actual
    String sql = "SELECT SUM(total_factura) AS ventas FROM factura WHERE DATE(fecha_factura) = ?";

    try {
        PreparedStatement pstmt = cn.prepareStatement(sql);
        pstmt.setString(1, fecha);
        ResultSet rs = pstmt.executeQuery();

        // Verificar si hay resultados
        if (rs.next()) {
            double ventas = rs.getDouble("ventas");
            ventasDelDia.setText(String.valueOf(formateador.format(ventas)));
        } else {
            // Si no hay ventas para el día actual, mostrar cero
            ventasDelDia.setText("0.0");
        }

        // Cerrar recursos
        rs.close();
        pstmt.close();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, ex);
    }
}
public void cargar_caja() {
    // Obtener la fecha actual
    DecimalFormat formateador = new DecimalFormat("#,###,###,###.##");
    Date fechaActual = new Date();
    SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
    String fecha = formatoFecha.format(fechaActual);

    // Consulta SQL para sumar los totales de la tabla caja del día actual
    String sqlCaja = "SELECT SUM(total) AS total_caja FROM caja WHERE DATE(fecha) = ?";

    try {
        // Sumar los totales de la tabla caja del día actual
        PreparedStatement pstmtCaja = cn.prepareStatement(sqlCaja);
        pstmtCaja.setString(1, fecha);
        ResultSet rsCaja = pstmtCaja.executeQuery();

        double totalCaja = 0.0;
        // Verificar si hay resultados
        if (rsCaja.next()) {
            totalCaja = rsCaja.getDouble("total_caja");
        }

        // Cerrar recursos de la consulta
        rsCaja.close();
        pstmtCaja.close();

        // Mostrar el total de la caja del día en el campo correspondiente
        ventasDelDia1.setText(String.valueOf(formateador.format(totalCaja)));

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, ex);
    }
}

void cargar() {

        String[] registros = new String[6];
        String sql = "SELECT idart,desart,preven FROM articulo";

        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                registros[0] = rs.getString("idart");
                registros[1] = rs.getString("desart");
                registros[2] = rs.getString("preven");
            }
      
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
void numero_factura(){
        String id_factura="";
        try{
            Statement sq2 = cn.createStatement();
            ResultSet rq2 = sq2.executeQuery("select id_factura from contador");
            rq2.next();
            id_factura = rq2.getString("id_factura");
            numfac = rq2.getString("id_factura");
        }catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "ERROR\n" + ex.getMessage());
    }
        int secuencia = Integer.parseInt(id_factura);
        secuencia = secuencia + 1;
        num_factura.setText(String.valueOf(secuencia));
        
    }
void boton1(){
          String nombre_boton = "";
try {
    Statement sq2 = cn.createStatement();
    ResultSet rq2 = sq2.executeQuery("SELECT desart FROM articulo WHERE idart = 1");
    if (rq2.next()) {
       boton1.setPreferredSize(new Dimension(90, 67));
        nombre_boton = rq2.getString("desart");
         String nombreBoton =nombre_boton ;
       nombreBoton = "<html><div style='text-align: center;'>" + nombreBoton.replace(" ", "<br>") + "</div></html>";
        boton1.setText(nombreBoton);
    } else {
       // JOptionPane.showMessageDialog(null, "No se encontró ningún artículo con idart = 1");
    }
} catch (SQLException ex) {
    JOptionPane.showMessageDialog(null, "ERROR\n" + ex.getMessage());
}
    }
void boton2(){
          String nombre_boton = "";
try {
    Statement sq2 = cn.createStatement();
    ResultSet rq2 = sq2.executeQuery("SELECT desart FROM articulo WHERE idart = 2");
    if (rq2.next()) {
           boton2.setPreferredSize(new Dimension(90, 67));
        nombre_boton = rq2.getString("desart");
         String nombreBoton =nombre_boton ;
       nombreBoton = "<html><div style='text-align: center;'>" + nombreBoton.replace(" ", "<br>") + "</div></html>";
        boton2.setText(nombreBoton);
    } else {
       // JOptionPane.showMessageDialog(null, "No se encontró ningún artículo con idart = 1");
    }
} catch (SQLException ex) {
    JOptionPane.showMessageDialog(null, "ERROR\n" + ex.getMessage());
}
    }
void boton3(){
          String nombre_boton = "";
try {
    Statement sq2 = cn.createStatement();
    ResultSet rq2 = sq2.executeQuery("SELECT desart FROM articulo WHERE idart = 3");
    if (rq2.next()) {
       boton3.setPreferredSize(new Dimension(90, 67));
        nombre_boton = rq2.getString("desart");
         String nombreBoton =nombre_boton ;
       nombreBoton = "<html><div style='text-align: center;'>" + nombreBoton.replace(" ", "<br>") + "</div></html>";
        boton3.setText(nombreBoton);
    } else {
       // JOptionPane.showMessageDialog(null, "No se encontró ningún artículo con idart = 1");
    }
} catch (SQLException ex) {
    JOptionPane.showMessageDialog(null, "ERROR\n" + ex.getMessage());
}
    }
void boton4(){
          String nombre_boton = "";
try {
    Statement sq2 = cn.createStatement();
    ResultSet rq2 = sq2.executeQuery("SELECT desart FROM articulo WHERE idart = 4");
    if (rq2.next()) {
              boton4.setPreferredSize(new Dimension(90, 67));
        nombre_boton = rq2.getString("desart");
         String nombreBoton =nombre_boton ;
       nombreBoton = "<html><div style='text-align: center;'>" + nombreBoton.replace(" ", "<br>") + "</div></html>";
        boton4.setText(nombreBoton);
    } else {
       // JOptionPane.showMessageDialog(null, "No se encontró ningún artículo con idart = 1");
    }
} catch (SQLException ex) {
    JOptionPane.showMessageDialog(null, "ERROR\n" + ex.getMessage());
}
    }
void boton5(){
          String nombre_boton = "";
try {
    Statement sq2 = cn.createStatement();
    ResultSet rq2 = sq2.executeQuery("SELECT desart FROM articulo WHERE idart = 5");
    if (rq2.next()) {
               boton5.setPreferredSize(new Dimension(90, 67));
        nombre_boton = rq2.getString("desart");
         String nombreBoton =nombre_boton ;
       nombreBoton = "<html><div style='text-align: center;'>" + nombreBoton.replace(" ", "<br>") + "</div></html>";
        boton5.setText(nombreBoton);
    } else {
       // JOptionPane.showMessageDialog(null, "No se encontró ningún artículo con idart = 1");
    }
} catch (SQLException ex) {
    JOptionPane.showMessageDialog(null, "ERROR\n" + ex.getMessage());
}
    }
void boton6(){
          String nombre_boton = "";
try {
    Statement sq2 = cn.createStatement();
    ResultSet rq2 = sq2.executeQuery("SELECT desart FROM articulo WHERE idart = 6");
    if (rq2.next()) {
              boton6.setPreferredSize(new Dimension(90, 67));
        nombre_boton = rq2.getString("desart");
         String nombreBoton =nombre_boton ;
       nombreBoton = "<html><div style='text-align: center;'>" + nombreBoton.replace(" ", "<br>") + "</div></html>";
        boton6.setText(nombreBoton);
    } else {
       // JOptionPane.showMessageDialog(null, "No se encontró ningún artículo con idart = 1");
    }
} catch (SQLException ex) {
    JOptionPane.showMessageDialog(null, "ERROR\n" + ex.getMessage());
}
    }
void boton7(){
          String nombre_boton = "";
try {
    Statement sq2 = cn.createStatement();
    ResultSet rq2 = sq2.executeQuery("SELECT desart FROM articulo WHERE idart = 7");
    if (rq2.next()) {
             boton7.setPreferredSize(new Dimension(90, 67));
        nombre_boton = rq2.getString("desart");
         String nombreBoton =nombre_boton ;
       nombreBoton = "<html><div style='text-align: center;'>" + nombreBoton.replace(" ", "<br>") + "</div></html>";
        boton7.setText(nombreBoton);
    } else {
       // JOptionPane.showMessageDialog(null, "No se encontró ningún artículo con idart = 1");
    }
} catch (SQLException ex) {
    JOptionPane.showMessageDialog(null, "ERROR\n" + ex.getMessage());
}
    }
void boton8(){
          String nombre_boton = "";
try {
    Statement sq2 = cn.createStatement();
    ResultSet rq2 = sq2.executeQuery("SELECT desart FROM articulo WHERE idart = 8");
    if (rq2.next()) {
               boton8.setPreferredSize(new Dimension(90, 67));
        nombre_boton = rq2.getString("desart");
         String nombreBoton =nombre_boton ;
       nombreBoton = "<html><div style='text-align: center;'>" + nombreBoton.replace(" ", "<br>") + "</div></html>";
        boton8.setText(nombreBoton);
    } else {
       // JOptionPane.showMessageDialog(null, "No se encontró ningún artículo con idart = 1");
    }
} catch (SQLException ex) {
    JOptionPane.showMessageDialog(null, "ERROR\n" + ex.getMessage());
}
    }
void boton9(){
          String nombre_boton = "";
try {
    Statement sq2 = cn.createStatement();
    ResultSet rq2 = sq2.executeQuery("SELECT desart FROM articulo WHERE idart = 9");
    if (rq2.next()) {
       boton9.setPreferredSize(new Dimension(90, 67));
        nombre_boton = rq2.getString("desart");
         String nombreBoton =nombre_boton ;
       nombreBoton = "<html><div style='text-align: center;'>" + nombreBoton.replace(" ", "<br>") + "</div></html>";
        boton9.setText(nombreBoton);
    } else {
       // JOptionPane.showMessageDialog(null, "No se encontró ningún artículo con idart = 1");
    }
} catch (SQLException ex) {
    JOptionPane.showMessageDialog(null, "ERROR\n" + ex.getMessage());
}
    }
void boton10(){
          String nombre_boton = "";
try {
    Statement sq2 = cn.createStatement();
    ResultSet rq2 = sq2.executeQuery("SELECT desart FROM articulo WHERE idart = 10");
    if (rq2.next()) {
           boton10.setPreferredSize(new Dimension(90, 67));
        nombre_boton = rq2.getString("desart");
         String nombreBoton =nombre_boton ;
       nombreBoton = "<html><div style='text-align: center;'>" + nombreBoton.replace(" ", "<br>") + "</div></html>";
        boton10.setText(nombreBoton);
    } else {
       // JOptionPane.showMessageDialog(null, "No se encontró ningún artículo con idart = 1");
    }
} catch (SQLException ex) {
    JOptionPane.showMessageDialog(null, "ERROR\n" + ex.getMessage());
}
    }
void boton11(){
          String nombre_boton = "";
try {
    Statement sq2 = cn.createStatement();
    ResultSet rq2 = sq2.executeQuery("SELECT desart FROM articulo WHERE idart = 11");
    if (rq2.next()) {
       boton11.setPreferredSize(new Dimension(90, 67));
        nombre_boton = rq2.getString("desart");
         String nombreBoton =nombre_boton ;
       nombreBoton = "<html><div style='text-align: center;'>" + nombreBoton.replace(" ", "<br>") + "</div></html>";
        boton11.setText(nombreBoton);
    } else {
       // JOptionPane.showMessageDialog(null, "No se encontró ningún artículo con idart = 1");
    }
} catch (SQLException ex) {
    JOptionPane.showMessageDialog(null, "ERROR\n" + ex.getMessage());
}
    }
void boton12(){
          String nombre_boton = "";
try {
    Statement sq2 = cn.createStatement();
    ResultSet rq2 = sq2.executeQuery("SELECT desart FROM articulo WHERE idart = 12");
    if (rq2.next()) {
              boton12.setPreferredSize(new Dimension(90, 67));
        nombre_boton = rq2.getString("desart");
         String nombreBoton =nombre_boton ;
       nombreBoton = "<html><div style='text-align: center;'>" + nombreBoton.replace(" ", "<br>") + "</div></html>";
        boton12.setText(nombreBoton);
    } else {
       // JOptionPane.showMessageDialog(null, "No se encontró ningún artículo con idart = 1");
    }
} catch (SQLException ex) {
    JOptionPane.showMessageDialog(null, "ERROR\n" + ex.getMessage());
}
    }
void cargar_filtro(String valor) {
        DefaultTableModel modelo2 = (DefaultTableModel) tabla_productos.getModel();
        modelo2.getDataVector().clear();

        String[] registros = new String[6];
        String sql = "SELECT idart,desart,preven FROM articulo where CONCAT(desart,'',idart)LIKE '%" + valor + "%'";

        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                registros[0] = rs.getString("idart");
                registros[1] = rs.getString("desart");
                registros[2] = rs.getString("preven");

                modelo2.addRow(registros);
            }
            tabla_productos.setModel(modelo2);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
void sumar_productos(){
        DecimalFormat formateador = new DecimalFormat("#,###,###,###.##");
        DecimalFormat formateador2 = new DecimalFormat("##########.##");
        DefaultTableModel modelo2 = (DefaultTableModel)t_factura.getModel();
        float totalg=0,totalg2=0;
        float itbisg=0,itbisg2=0;
        float subtotalg=0,subtotalg2=0;
        
        for (int i = 0; i < t_factura.getRowCount(); i++){
//            totalg=Float.parseFloat(t_factura.getValueAt(i, 6).toString());
//            totalg2=totalg2+totalg;
//            itbisg=Float.parseFloat(t_factura.getValueAt(i, 4).toString());
//            itbisg2=itbisg2+itbisg;
            subtotalg=Float.parseFloat(t_factura.getValueAt(i, 4).toString());
            subtotalg2=subtotalg2+subtotalg;            
        }
        total.setText(formateador.format(subtotalg2));
        total2 =(formateador2.format(subtotalg2));
 cart_cal();

     }
void llenar_datos() {
    DecimalFormat formateador = new DecimalFormat("##########.##");
    DefaultTableModel modelo2 = (DefaultTableModel) t_factura.getModel();
    String idProducto = id_articulo.getText();

    // Buscar si el producto ya está en la factura
    int filaExistente = -1;
    for (int i = 0; i < modelo2.getRowCount(); i++) {
        if (modelo2.getValueAt(i, 0).equals(idProducto)) {
            filaExistente = i;
            break;
        }
    }

    float precio2 = Float.parseFloat(precio.getText());
    float cantidad2 = Float.parseFloat(cantidad.getText());
    float total2 = precio2 * cantidad2;

    // Si el producto ya está en la factura, actualizar la cantidad y el total
    if (filaExistente != -1) {
        float nuevaCantidad = Float.parseFloat(modelo2.getValueAt(filaExistente, 2).toString()) + cantidad2;
        float nuevoTotal = Float.parseFloat(modelo2.getValueAt(filaExistente, 3).toString()) * nuevaCantidad;

        modelo2.setValueAt(formateador.format(nuevaCantidad), filaExistente, 2);
        modelo2.setValueAt(formateador.format(nuevoTotal), filaExistente, 4);
    } else {
        String[] registros = new String[10];
        registros[0] = id_articulo.getText();
        registros[1] = desart.getText();
        registros[2] = cantidad.getText();
        registros[3] = precio.getText();
        registros[4] = formateador.format(total2);
    
        modelo2.addRow(registros);
    }

    t_factura.setModel(modelo2);
}
void guardar_dinero_caja() {
    try {
        // Consulta SQL para insertar el monto en la tabla caja
        String sql = "INSERT INTO caja (fecha, total) VALUES (NOW(), ?)";

        // Preparar la consulta
        PreparedStatement pstmt = cn.prepareStatement(sql);
        
        // Convertir el total2 a tipo float
        float totalFloat = Float.parseFloat(total2);

        // Establecer el valor del parámetro
        pstmt.setFloat(1, totalFloat);

        // Ejecutar la consulta
        int rowsAffected = pstmt.executeUpdate();

        // Verificar si se insertaron filas
        if (rowsAffected > 0) {
            // Éxito al guardar en la caja
           // JOptionPane.showMessageDialog(null, "Monto guardado en la caja con éxito.");
        } else {
            // No se guardaron filas
            //JOptionPane.showMessageDialog(null, "Error al guardar el monto en la caja.");
        }

        // Cerrar recursos
        pstmt.close();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Error al ejecutar la consulta: " + ex.getMessage());
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(null, "Error al convertir el total a float: " + ex.getMessage());
    }
}

void guardar_factura(){
        try {
            String sql2 = "";
            sql2 = "INSERT INTO factura (id_factura,fecha_factura,total_factura,idcli,nom_cliente,tipo)VALUES('" + numfac + "',now(),'" + total2 +"','" + id_cliente.getText() + "','" + cliente.getText() + "','" + tipo_factura + "')";
            PreparedStatement psz = cn.prepareStatement(sql2);

            int n;
            n = psz.executeUpdate();
            if (n > 0) {

            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }       
    }
void guardar_detalle_factura(){
      
        for (int i = 0; i < t_factura.getRowCount(); i++){
         try {
            String sql2 = "";
            sql2 = "INSERT INTO detalle_factura (id_factura,id_articulo,des_articulo,cantidad,precio,importe)VALUES('" + numfac + "','" + t_factura.getValueAt(i,0).toString()+ "','" + t_factura.getValueAt(i,1).toString()+"','" + t_factura.getValueAt(i,2).toString()+"','" + t_factura.getValueAt(i,3).toString() +"','" + t_factura.getValueAt(i,4).toString()+"')";
            PreparedStatement psz = cn.prepareStatement(sql2);

            int n;
            n = psz.executeUpdate();
            if (n > 0) {

            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        }
    }
private boolean guardar_credito() {
    try {
        // Obtener el crédito actual del cliente de la tabla cliente
        String sqlCreditoActual = "SELECT credito FROM cliente WHERE idcli = ?";
        PreparedStatement psCreditoActual = cn.prepareStatement(sqlCreditoActual);
        psCreditoActual.setString(1, id_cliente.getText());
        ResultSet rsCreditoActual = psCreditoActual.executeQuery();

        if (rsCreditoActual.next()) {
            double creditoActual = rsCreditoActual.getDouble("credito");
            double nuevoCredito = Double.parseDouble(Too.getText());

            // Verificar si el nuevo crédito excede el límite de crédito del cliente
            if (nuevoCredito > creditoActual) {
                JOptionPane.showMessageDialog(null, "El nuevo crédito excede el límite de crédito del cliente.");
                return false; // No se puede guardar el crédito
            } else {
                // Verificar si el cliente ya tiene una deuda registrada
                String sqlDeuda = "SELECT SUM(credito) AS deuda FROM credito WHERE idcli = ?";
                PreparedStatement psDeuda = cn.prepareStatement(sqlDeuda);
                psDeuda.setString(1, id_cliente.getText());
                ResultSet rsDeuda = psDeuda.executeQuery();

                if (rsDeuda.next()) {
                    double deudaExistente = rsDeuda.getDouble("deuda");

                    // Sumar la deuda existente con el nuevo crédito
                    double deudaTotal = deudaExistente + nuevoCredito;

                    // Verificar si la deuda total excede el límite de crédito del cliente
                    if (deudaTotal > creditoActual) {
                        JOptionPane.showMessageDialog(null, "La deuda total excede el límite de crédito del cliente.");
                        return false; // No se puede guardar el crédito
                    } else {
                        // Insertar el nuevo crédito en la tabla credito
                        String sqlInsertCredito = "INSERT INTO credito (idcli, nomcli, credito, fecha) VALUES (?, ?, ?, ?)";
                        PreparedStatement psInsertCredito = cn.prepareStatement(sqlInsertCredito);
                        psInsertCredito.setString(1, id_cliente.getText());
                        psInsertCredito.setString(2, cliente.getText());
                        psInsertCredito.setDouble(3, nuevoCredito);
                        java.util.Date fechaActual = new java.util.Date();
                        java.sql.Timestamp timestamp = new java.sql.Timestamp(fechaActual.getTime());
                        psInsertCredito.setTimestamp(4, timestamp);
                        int n = psInsertCredito.executeUpdate();
                        if (n > 0) {
                            return true; // Crédito guardado con éxito
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Error al obtener la deuda del cliente.");
                    return false; // No se puede guardar el crédito
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Cliente no encontrado.");
            return false; // No se puede guardar el crédito
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, ex);
        return false; // No se puede guardar el crédito
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(null, "El monto del crédito no es válido.");
        return false; // No se puede guardar el crédito
    }
    return false; // No se puede guardar el crédito (por defecto)
}




    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        boton2 = new javax.swing.JButton();
        boton1 = new javax.swing.JButton();
        boton3 = new javax.swing.JButton();
        boton4 = new javax.swing.JButton();
        boton8 = new javax.swing.JButton();
        boton7 = new javax.swing.JButton();
        boton6 = new javax.swing.JButton();
        boton5 = new javax.swing.JButton();
        boton9 = new javax.swing.JButton();
        boton10 = new javax.swing.JButton();
        boton12 = new javax.swing.JButton();
        boton11 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        Too = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        Bal = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        Cash = new javax.swing.JTextField();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        t_factura = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        bill = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        nombre_empleado = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        num_factura = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        fecha = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        id_cliente = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        cliente = new javax.swing.JTextField();
        jButton16 = new javax.swing.JButton();
        buscar = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        credito = new javax.swing.JRadioButton();
        contado = new javax.swing.JRadioButton();
        jPanel6 = new javax.swing.JPanel();
        id_articulo = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        desart = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        precio = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        cantidad = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tabla_productos = new javax.swing.JTable();
        jLabel15 = new javax.swing.JLabel();
        ventasDelDia = new javax.swing.JTextField();
        total = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        ventasDelDia1 = new javax.swing.JTextField();
        cargartodo = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(102, 51, 0));

        jButton1.setBackground(new java.awt.Color(204, 102, 0));
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/fast-food.png"))); // NOI18N
        jButton1.setText("ARTICULO");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(204, 102, 0));
        jButton2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/client.png"))); // NOI18N
        jButton2.setText("CLIENTE");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(204, 102, 0));
        jButton3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/sell (1).png"))); // NOI18N
        jButton3.setText("VENTAS");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(204, 102, 0));
        jButton4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/debt.png"))); // NOI18N
        jButton4.setText("CREDITO");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton4))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 102, 0), 2));

        boton2.setBackground(new java.awt.Color(102, 51, 0));
        boton2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        boton2.setForeground(new java.awt.Color(255, 255, 255));
        boton2.setText("Articulo 2");
        boton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton2ActionPerformed(evt);
            }
        });

        boton1.setBackground(new java.awt.Color(102, 51, 0));
        boton1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        boton1.setForeground(new java.awt.Color(255, 255, 255));
        boton1.setText("Articulo 1");
        boton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton1ActionPerformed(evt);
            }
        });

        boton3.setBackground(new java.awt.Color(102, 51, 0));
        boton3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        boton3.setForeground(new java.awt.Color(255, 255, 255));
        boton3.setText("Articulo 3");
        boton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton3ActionPerformed(evt);
            }
        });

        boton4.setBackground(new java.awt.Color(102, 51, 0));
        boton4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        boton4.setForeground(new java.awt.Color(255, 255, 255));
        boton4.setText("Articulo 4");
        boton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton4ActionPerformed(evt);
            }
        });

        boton8.setBackground(new java.awt.Color(102, 51, 0));
        boton8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        boton8.setForeground(new java.awt.Color(255, 255, 255));
        boton8.setText("Articulo 8");
        boton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton8ActionPerformed(evt);
            }
        });

        boton7.setBackground(new java.awt.Color(102, 51, 0));
        boton7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        boton7.setForeground(new java.awt.Color(255, 255, 255));
        boton7.setText("Articulo 7");
        boton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton7ActionPerformed(evt);
            }
        });

        boton6.setBackground(new java.awt.Color(102, 51, 0));
        boton6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        boton6.setForeground(new java.awt.Color(255, 255, 255));
        boton6.setText("Articulo 6");
        boton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton6ActionPerformed(evt);
            }
        });

        boton5.setBackground(new java.awt.Color(102, 51, 0));
        boton5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        boton5.setForeground(new java.awt.Color(255, 255, 255));
        boton5.setText("Articulo 5");
        boton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton5ActionPerformed(evt);
            }
        });

        boton9.setBackground(new java.awt.Color(102, 51, 0));
        boton9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        boton9.setForeground(new java.awt.Color(255, 255, 255));
        boton9.setText("Articulo 9");
        boton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton9ActionPerformed(evt);
            }
        });

        boton10.setBackground(new java.awt.Color(102, 51, 0));
        boton10.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        boton10.setForeground(new java.awt.Color(255, 255, 255));
        boton10.setText("Articulo 10");
        boton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton10ActionPerformed(evt);
            }
        });

        boton12.setBackground(new java.awt.Color(102, 51, 0));
        boton12.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        boton12.setForeground(new java.awt.Color(255, 255, 255));
        boton12.setText("Articulo 12");
        boton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton12ActionPerformed(evt);
            }
        });

        boton11.setBackground(new java.awt.Color(102, 51, 0));
        boton11.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        boton11.setForeground(new java.awt.Color(255, 255, 255));
        boton11.setText("Articulo 11");
        boton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton11ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(boton9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(boton10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(boton11, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(boton12, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(boton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(boton6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(boton7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(boton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(boton1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(boton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(boton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(boton4)))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(boton2, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(boton1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(boton4, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(boton3, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(boton6, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(boton5, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(boton8, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(boton7, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(boton10, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(boton9, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(boton12, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(boton11, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(0, 0, 102));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 102, 0), 2));

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Total :");

        Too.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
        Too.setForeground(new java.awt.Color(255, 255, 255));
        Too.setText("0");

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Devuelta:");

        Bal.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
        Bal.setForeground(new java.awt.Color(255, 255, 255));
        Bal.setText("0");

        jLabel5.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Dinero :");

        Cash.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
        Cash.setText("0");
        Cash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CashActionPerformed(evt);
            }
        });

        jButton14.setBackground(new java.awt.Color(0, 102, 255));
        jButton14.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jButton14.setForeground(new java.awt.Color(255, 255, 255));
        jButton14.setText("GUARDAR");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jButton15.setBackground(new java.awt.Color(255, 51, 51));
        jButton15.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jButton15.setForeground(new java.awt.Color(255, 255, 255));
        jButton15.setText("IMPRIMIR");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(Cash, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Bal, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Too, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton14, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                    .addComponent(jButton15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(Too)
                    .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Cash, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(Bal))
                        .addGap(140, 140, 140))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        t_factura.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        t_factura.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Producto", "Cantidad", "Precio", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        t_factura.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                t_facturaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(t_factura);
        if (t_factura.getColumnModel().getColumnCount() > 0) {
            t_factura.getColumnModel().getColumn(0).setMinWidth(80);
            t_factura.getColumnModel().getColumn(0).setMaxWidth(80);
            t_factura.getColumnModel().getColumn(2).setMinWidth(80);
            t_factura.getColumnModel().getColumn(2).setMaxWidth(80);
            t_factura.getColumnModel().getColumn(3).setMinWidth(80);
            t_factura.getColumnModel().getColumn(3).setMaxWidth(80);
            t_factura.getColumnModel().getColumn(4).setMinWidth(80);
            t_factura.getColumnModel().getColumn(4).setMaxWidth(80);
        }

        bill.setColumns(20);
        bill.setRows(5);
        jScrollPane2.setViewportView(bill);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addGap(10, 10, 10)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE))
                .addGap(139, 139, 139))
        );

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/srers.png"))); // NOI18N

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(102, 51, 0));
        jLabel12.setText("EMPLEADO");

        nombre_empleado.setText("ADRIEL CRUZ");
        nombre_empleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nombre_empleadoActionPerformed(evt);
            }
        });
        nombre_empleado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nombre_empleadoKeyReleased(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(102, 51, 0));
        jLabel11.setText("NO. FACTURA");

        num_factura.setEditable(false);
        num_factura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                num_facturaActionPerformed(evt);
            }
        });
        num_factura.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                num_facturaKeyReleased(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(102, 51, 0));
        jLabel6.setText("FECHA");

        fecha.setEditable(false);
        fecha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                fechaKeyReleased(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(102, 51, 0));
        jLabel17.setText("ID CLIENTE");

        id_cliente.setEditable(false);
        id_cliente.setText("1");
        id_cliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                id_clienteKeyReleased(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(102, 51, 0));
        jLabel13.setText("CLIENTE");

        cliente.setText("Cliente Generico");
        cliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                clienteKeyReleased(evt);
            }
        });

        jButton16.setBackground(new java.awt.Color(102, 51, 0));
        jButton16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton16.setForeground(new java.awt.Color(255, 255, 255));
        jButton16.setText("...");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarActionPerformed(evt);
            }
        });
        buscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                buscarKeyReleased(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(102, 51, 0));
        jLabel7.setText("BUSCAR");

        credito.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(credito);
        credito.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        credito.setForeground(new java.awt.Color(102, 51, 0));
        credito.setText("CREDITO");

        contado.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(contado);
        contado.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        contado.setForeground(new java.awt.Color(102, 51, 0));
        contado.setText("CONTADO");

        jPanel6.setBackground(new java.awt.Color(102, 51, 0));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("ID ARTICULO");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("DESCRIPCION");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("PRECIO U");

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("CANTIDAD");

        cantidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cantidadActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(id_articulo, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(precio, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(desart, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(334, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(id_articulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(desart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(precio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.setBackground(new java.awt.Color(102, 51, 0));
        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tabla_productos.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        tabla_productos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID ARTICULO", "DESCRIPCION ARTICULO", "PRECIO"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabla_productos.setGridColor(new java.awt.Color(153, 51, 0));
        tabla_productos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabla_productosMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tabla_productos);
        if (tabla_productos.getColumnModel().getColumnCount() > 0) {
            tabla_productos.getColumnModel().getColumn(0).setMinWidth(100);
            tabla_productos.getColumnModel().getColumn(0).setMaxWidth(100);
            tabla_productos.getColumnModel().getColumn(2).setMinWidth(80);
            tabla_productos.getColumnModel().getColumn(2).setMaxWidth(80);
        }

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(102, 51, 0));
        jLabel15.setText("VENTAS DEL DIA");

        ventasDelDia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ventasDelDiaActionPerformed(evt);
            }
        });
        ventasDelDia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ventasDelDiaKeyReleased(evt);
            }
        });

        total.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                totalKeyReleased(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(102, 51, 0));
        jLabel16.setText("TOTAL CAJA");

        ventasDelDia1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ventasDelDia1KeyReleased(evt);
            }
        });

        cargartodo.setBackground(new java.awt.Color(102, 51, 0));
        cargartodo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cargartodo.setForeground(new java.awt.Color(255, 255, 255));
        cargartodo.setText("...");
        cargartodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cargartodoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel11)
                                    .addGap(18, 18, 18)
                                    .addComponent(num_factura))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel12)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(nombre_empleado, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(25, 25, 25)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel15)
                                        .addGap(18, 18, 18)
                                        .addComponent(ventasDelDia, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel16)
                                        .addGap(18, 18, 18)
                                        .addComponent(ventasDelDia1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel17))
                                .addGap(34, 34, 34)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cliente)
                                    .addComponent(id_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButton16)
                                        .addGap(18, 18, 18)
                                        .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cargartodo)
                                .addGap(100, 100, 100)))
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(credito)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(contado))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2185, 2185, 2185))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1))
                                .addGap(17, 17, 17)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel12)
                                            .addComponent(nombre_empleado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(10, 10, 10)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel6)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel17)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(id_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(buscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel7)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(jLabel13)))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(141, 141, 141)
                                .addComponent(cargartodo, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(num_factura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11)
                            .addComponent(jLabel15)
                            .addComponent(ventasDelDia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16)
                            .addComponent(ventasDelDia1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(credito)
                            .addComponent(contado))))
                .addContainerGap(220, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1342, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
      new articulos().setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void boton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton2ActionPerformed
                  String nombre_boton = "";
            int precios = 0;
try {
    Statement sq2 = cn.createStatement();
    ResultSet rq2 = sq2.executeQuery("SELECT desart,preven FROM articulo WHERE idart = 2");
    if (rq2.next()) {
        nombre_boton = rq2.getString("desart");
        precios = rq2.getInt("preven");
        double decimal = precios;
        addTable(nombre_boton, decimal,"2");
    } else {
       // JOptionPane.showMessageDialog(null, "No se encontró ningún artículo con idart = 3");
    }
} catch (SQLException ex) {
    JOptionPane.showMessageDialog(null, "ERROR\n" + ex.getMessage());
}
    }//GEN-LAST:event_boton2ActionPerformed

    private void boton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton1ActionPerformed
                  String nombre_boton = "";
            int precios = 0;
try {
    Statement sq2 = cn.createStatement();
    ResultSet rq2 = sq2.executeQuery("SELECT desart,preven FROM articulo WHERE idart = 1");
    if (rq2.next()) {
        nombre_boton = rq2.getString("desart");
        precios = rq2.getInt("preven");
        double decimal = precios;
        addTable(nombre_boton, decimal,"1");
    } else {
       // JOptionPane.showMessageDialog(null, "No se encontró ningún artículo con idart = 3");
    }
} catch (SQLException ex) {
    JOptionPane.showMessageDialog(null, "ERROR\n" + ex.getMessage());
}
    }//GEN-LAST:event_boton1ActionPerformed

    private void boton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton3ActionPerformed
            String nombre_boton = "";
            int precios = 0;
try {
    Statement sq2 = cn.createStatement();
    ResultSet rq2 = sq2.executeQuery("SELECT desart,preven FROM articulo WHERE idart = 3");
    if (rq2.next()) {
        nombre_boton = rq2.getString("desart");
        precios = rq2.getInt("preven");
        double decimal = precios;
        addTable(nombre_boton, decimal,"3");
    } else {
       // JOptionPane.showMessageDialog(null, "No se encontró ningún artículo con idart = 3");
    }
} catch (SQLException ex) {
    JOptionPane.showMessageDialog(null, "ERROR\n" + ex.getMessage());
}

    }//GEN-LAST:event_boton3ActionPerformed

    private void boton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton4ActionPerformed
                   String nombre_boton = "";
            int precios = 0;
try {
    Statement sq2 = cn.createStatement();
    ResultSet rq2 = sq2.executeQuery("SELECT desart,preven FROM articulo WHERE idart = 4");
    if (rq2.next()) {
        nombre_boton = rq2.getString("desart");
        precios = rq2.getInt("preven");
        double decimal = precios;
        addTable(nombre_boton, decimal,"4");
    } else {
       // JOptionPane.showMessageDialog(null, "No se encontró ningún artículo con idart = 3");
    }
} catch (SQLException ex) {
    JOptionPane.showMessageDialog(null, "ERROR\n" + ex.getMessage());
}
    }//GEN-LAST:event_boton4ActionPerformed

    private void boton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton8ActionPerformed
                   String nombre_boton = "";
            int precios = 0;
try {
    Statement sq2 = cn.createStatement();
    ResultSet rq2 = sq2.executeQuery("SELECT desart,preven FROM articulo WHERE idart = 8");
    if (rq2.next()) {
        nombre_boton = rq2.getString("desart");
        precios = rq2.getInt("preven");
        double decimal = precios;
        addTable(nombre_boton, decimal,"8");
    } else {
       // JOptionPane.showMessageDialog(null, "No se encontró ningún artículo con idart = 3");
    }
} catch (SQLException ex) {
    JOptionPane.showMessageDialog(null, "ERROR\n" + ex.getMessage());
}
    }//GEN-LAST:event_boton8ActionPerformed

    private void boton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton7ActionPerformed
                  String nombre_boton = "";
            int precios = 0;
try {
    Statement sq2 = cn.createStatement();
    ResultSet rq2 = sq2.executeQuery("SELECT desart,preven FROM articulo WHERE idart = 7");
    if (rq2.next()) {
        nombre_boton = rq2.getString("desart");
        precios = rq2.getInt("preven");
        double decimal = precios;
        addTable(nombre_boton, decimal,"7");
    } else {
       // JOptionPane.showMessageDialog(null, "No se encontró ningún artículo con idart = 3");
    }
} catch (SQLException ex) {
    JOptionPane.showMessageDialog(null, "ERROR\n" + ex.getMessage());
}
    }//GEN-LAST:event_boton7ActionPerformed

    private void boton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton6ActionPerformed
                  String nombre_boton = "";
            int precios = 0;
try {
    Statement sq2 = cn.createStatement();
    ResultSet rq2 = sq2.executeQuery("SELECT desart,preven FROM articulo WHERE idart = 6");
    if (rq2.next()) {
        nombre_boton = rq2.getString("desart");
        precios = rq2.getInt("preven");
        double decimal = precios;
        addTable(nombre_boton, decimal,"6");
    } else {
       // JOptionPane.showMessageDialog(null, "No se encontró ningún artículo con idart = 3");
    }
} catch (SQLException ex) {
    JOptionPane.showMessageDialog(null, "ERROR\n" + ex.getMessage());
}
    }//GEN-LAST:event_boton6ActionPerformed

    private void boton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton5ActionPerformed
                 String nombre_boton = "";
            int precios = 0;
try {
    Statement sq2 = cn.createStatement();
    ResultSet rq2 = sq2.executeQuery("SELECT desart,preven FROM articulo WHERE idart = 5");
    if (rq2.next()) {
        nombre_boton = rq2.getString("desart");
        precios = rq2.getInt("preven");
        double decimal = precios;
        addTable(nombre_boton, decimal,"5");
    } else {
       // JOptionPane.showMessageDialog(null, "No se encontró ningún artículo con idart = 3");
    }
} catch (SQLException ex) {
    JOptionPane.showMessageDialog(null, "ERROR\n" + ex.getMessage());
}
    }//GEN-LAST:event_boton5ActionPerformed

    private void boton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton9ActionPerformed
                   String nombre_boton = "";
            int precios = 0;
try {
    Statement sq2 = cn.createStatement();
    ResultSet rq2 = sq2.executeQuery("SELECT desart,preven FROM articulo WHERE idart = 9");
    if (rq2.next()) {
        nombre_boton = rq2.getString("desart");
        precios = rq2.getInt("preven");
        double decimal = precios;
        addTable(nombre_boton, decimal,"9");
    } else {
       // JOptionPane.showMessageDialog(null, "No se encontró ningún artículo con idart = 3");
    }
} catch (SQLException ex) {
    JOptionPane.showMessageDialog(null, "ERROR\n" + ex.getMessage());
}
    }//GEN-LAST:event_boton9ActionPerformed

    private void boton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton10ActionPerformed
                String nombre_boton = "";
            int precios = 0;
try {
    Statement sq2 = cn.createStatement();
    ResultSet rq2 = sq2.executeQuery("SELECT desart,preven FROM articulo WHERE idart = 10");
    if (rq2.next()) {
        nombre_boton = rq2.getString("desart");
        precios = rq2.getInt("preven");
        double decimal = precios;
        addTable(nombre_boton, decimal,"10");
    } else {
       // JOptionPane.showMessageDialog(null, "No se encontró ningún artículo con idart = 3");
    }
} catch (SQLException ex) {
    JOptionPane.showMessageDialog(null, "ERROR\n" + ex.getMessage());
}
    }//GEN-LAST:event_boton10ActionPerformed

    private void boton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton12ActionPerformed
                   String nombre_boton = "";
            int precios = 0;
try {
    Statement sq2 = cn.createStatement();
    ResultSet rq2 = sq2.executeQuery("SELECT desart,preven FROM articulo WHERE idart = 12");
    if (rq2.next()) {
        nombre_boton = rq2.getString("desart");
        precios = rq2.getInt("preven");
        double decimal = precios;
        addTable(nombre_boton, decimal,"12");
    } else {
       // JOptionPane.showMessageDialog(null, "No se encontró ningún artículo con idart = 3");
    }
} catch (SQLException ex) {
    JOptionPane.showMessageDialog(null, "ERROR\n" + ex.getMessage());
}
    }//GEN-LAST:event_boton12ActionPerformed

    private void boton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton11ActionPerformed
                    String nombre_boton = "";
            int precios = 0;
try {
    Statement sq2 = cn.createStatement();
    ResultSet rq2 = sq2.executeQuery("SELECT desart,preven FROM articulo WHERE idart = 11");
    if (rq2.next()) {
        nombre_boton = rq2.getString("desart");
        precios = rq2.getInt("preven");
        double decimal = precios;
        addTable(nombre_boton, decimal,"11");
    } else {
       // JOptionPane.showMessageDialog(null, "No se encontró ningún artículo con idart = 3");
    }
} catch (SQLException ex) {
    JOptionPane.showMessageDialog(null, "ERROR\n" + ex.getMessage());
}
    }//GEN-LAST:event_boton11ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
if (t_factura.getRowCount() == 0) {
    JOptionPane.showMessageDialog(null, "Debes facturar un producto primero.");
} else {
    if (credito.isSelected()) {
        tipo_factura = "CREDITO";
        if (!guardar_credito()) {
            return; // Si guardar_credito devuelve false, salimos del método
        }
    }
    if (contado.isSelected()) {
        tipo_factura = "CONTADO";
        guardar_dinero_caja(); // Llama a la función para guardar el dinero en caja
    }

    guardar_factura();
    guardar_detalle_factura();
    cargar_dia();
    cargar_caja();
    try {
        PreparedStatement psU = cn.prepareStatement("UPDATE contador SET id_factura=id_factura+1");
        psU.executeUpdate();
    } catch (SQLException ex) {
    }
    JOptionPane.showMessageDialog(null, "FACTURA REGISTRADA CON EXITO");
    this.dispose();
    new menu().setVisible(true);
}

           
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        bill_print();
    }//GEN-LAST:event_jButton15ActionPerformed

    private void nombre_empleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nombre_empleadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nombre_empleadoActionPerformed

    private void nombre_empleadoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nombre_empleadoKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_nombre_empleadoKeyReleased

    private void num_facturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_num_facturaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_num_facturaActionPerformed

    private void num_facturaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_num_facturaKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_num_facturaKeyReleased

    private void fechaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fechaKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_fechaKeyReleased

    private void id_clienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_id_clienteKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_id_clienteKeyReleased

    private void clienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_clienteKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_clienteKeyReleased

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
       new clientedatos().setVisible(true);
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
       new cliente().setVisible(true);       
    }//GEN-LAST:event_jButton2ActionPerformed

    private void buscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscarKeyReleased
        cargar_filtro(buscar.getText());
    }//GEN-LAST:event_buscarKeyReleased

    private void buscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_buscarActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        new detalles().setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void cantidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cantidadActionPerformed
        llenar_datos();
        sumar_productos();
        limpiar_datos();
        buscar.requestFocus();
    }//GEN-LAST:event_cantidadActionPerformed

    private void tabla_productosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabla_productosMouseClicked
        int fila= tabla_productos.getSelectedRow();
        if(fila>=0){
            id_articulo.setText(tabla_productos.getValueAt(fila, 0).toString());
            desart.setText(tabla_productos.getValueAt(fila, 1).toString());
            precio.setText(tabla_productos.getValueAt(fila, 2).toString());
            cantidad.requestFocus(true);
        }
    }//GEN-LAST:event_tabla_productosMouseClicked

    private void ventasDelDiaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ventasDelDiaKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_ventasDelDiaKeyReleased

    private void totalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_totalKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_totalKeyReleased

    private void t_facturaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t_facturaMouseClicked
int fila = t_factura.getSelectedRow();
if (fila >= 0) {
    String idArticulo = t_factura.getValueAt(fila, 0).toString();
    String desArt = t_factura.getValueAt(fila, 1).toString();
    String precioActual = t_factura.getValueAt(fila, 3).toString();
    String cantidadActual = t_factura.getValueAt(fila, 2).toString();
    
    JTextField cantidadField = new JTextField(cantidadActual);
    JTextField precioField = new JTextField(precioActual);

    Object[] message = {
        "Cantidad:", cantidadField,
        "Precio:", precioField
    };

    int option = JOptionPane.showOptionDialog(null, message, "Modificar Producto",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null,
            new String[]{"Modificar", "Eliminar"}, "Modificar");
    
    if (option == JOptionPane.OK_OPTION) {
        // Obtener los nuevos valores de cantidad y precio
        String nuevaCantidad = cantidadField.getText();
        String nuevoPrecio = precioField.getText();
        
        // Calcular el nuevo total
        double total = Double.parseDouble(nuevaCantidad) * Double.parseDouble(nuevoPrecio);
        
        // Actualizar la tabla con los nuevos valores
        t_factura.setValueAt(nuevaCantidad, fila, 2);
        t_factura.setValueAt(nuevoPrecio, fila, 3);
        t_factura.setValueAt(String.valueOf(total), fila, 4);
        
        // Llamar a las funciones para actualizar la suma de productos y el total del carrito
        sumar_productos();
        cart_cal();
    } else if (option == 1) {
        // Eliminar el producto de la tabla
        DefaultTableModel model = (DefaultTableModel) t_factura.getModel();
        model.removeRow(fila);
        
        // Llamar a las funciones para actualizar la suma de productos y el total del carrito
        sumar_productos();
        cart_cal();
    }
} else {
    JOptionPane.showMessageDialog(null, "Selecciona una fila para modificar o eliminar.");
}


    }//GEN-LAST:event_t_facturaMouseClicked

    private void CashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CashActionPerformed
  Double tot = Double.valueOf(Too.getText());
        Double chs = Double.valueOf(Cash.getText());
        Double bal = chs - tot ;

        DecimalFormat df = new DecimalFormat("00.00");
        Bal.setText(String.valueOf(df.format(bal)));       
    }//GEN-LAST:event_CashActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
      new creditos().setVisible(true);       
    }//GEN-LAST:event_jButton4ActionPerformed

    private void ventasDelDia1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ventasDelDia1KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_ventasDelDia1KeyReleased

    private void ventasDelDiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ventasDelDiaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ventasDelDiaActionPerformed

    private void cargartodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cargartodoActionPerformed
       cargar_caja();
    }//GEN-LAST:event_cargartodoActionPerformed

  
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
            java.util.logging.Logger.getLogger(menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new menu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Bal;
    private javax.swing.JTextField Cash;
    private javax.swing.JLabel Too;
    private javax.swing.JTextArea bill;
    private javax.swing.JButton boton1;
    private javax.swing.JButton boton10;
    private javax.swing.JButton boton11;
    private javax.swing.JButton boton12;
    private javax.swing.JButton boton2;
    private javax.swing.JButton boton3;
    private javax.swing.JButton boton4;
    private javax.swing.JButton boton5;
    private javax.swing.JButton boton6;
    private javax.swing.JButton boton7;
    private javax.swing.JButton boton8;
    private javax.swing.JButton boton9;
    public static javax.swing.JTextField buscar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JTextField cantidad;
    public static javax.swing.JButton cargartodo;
    public static javax.swing.JTextField cliente;
    private javax.swing.JRadioButton contado;
    private javax.swing.JRadioButton credito;
    private javax.swing.JTextField desart;
    private javax.swing.JTextField fecha;
    private javax.swing.JTextField id_articulo;
    public static javax.swing.JTextField id_cliente;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTextField nombre_empleado;
    private javax.swing.JTextField num_factura;
    private javax.swing.JTextField precio;
    private javax.swing.JTable t_factura;
    private javax.swing.JTable tabla_productos;
    private javax.swing.JTextField total;
    private javax.swing.JTextField ventasDelDia;
    private javax.swing.JTextField ventasDelDia1;
    // End of variables declaration//GEN-END:variables
  String total2 = "", numfac = "", sub_total2 = "";
     String tipo_factura="";
   
}
