package formularios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import sistema_kiosko.conectar;

public class clientedatos extends javax.swing.JFrame {

    conectar cc = new conectar();
    Connection cn = (Connection) cc.conexion();

    public clientedatos() {
        initComponents();
cargar();
    }

    void actualizar() {
        try {
            String sexoseleccionado;
             sexoseleccionado = sexo.getSelectedItem().toString();
               if ("MASCULINO".equals(sexoseleccionado)) {
                   } else if ("FEMENINO".equals(sexoseleccionado)) {
                   } else if ("EMPRESA".equals(sexoseleccionado)) {
                   } 
            PreparedStatement psU = cn.prepareStatement("UPDATE cliente SET nomcli='"+nombre.getText()+"',numero='"+numero.getText()+"',cedula='"+cedula.getText()+"',direccion='"+direccion.getText()+"',sexo='"+sexoseleccionado+"' where idcli='"+id_cliente.getText()+"'");
            psU.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(clientedatos.class.getName()).log(Level.SEVERE, null, ex);
//            JOptionPane.showMessageDialog(null,ex);
        }
        JOptionPane.showMessageDialog(null, "ACTUALIZADO CON EXITO");
    }
    void borrar_datos() {
        try {

            PreparedStatement psU = cn.prepareStatement("DELETE from cliente where idcli='"+id_cliente.getText()+"'");
            psU.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(clientedatos.class.getName()).log(Level.SEVERE, null, ex);
//            JOptionPane.showMessageDialog(null,ex);
        }
        JOptionPane.showMessageDialog(null, "ELIMINADO CON EXITO");
    }
    void cargar_datos() throws SQLException {

        String[] registros = new String[10];
        String sql = "SELECT nomcli,numero,cedula,sexo,direccion FROM cliente where idcli='" + id_cliente.getText() + "'";
 
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                registros[0] = rs.getString("idcli");
                registros[1] = rs.getString("nomcli");
                registros[2] = rs.getString("cedula");
                registros[3] = rs.getString("sexo");
                registros[4] = rs.getString("numero");
                registros[5] = rs.getString("direccion");
            }
            nombre.setText(registros[0]);
            numero.setText(registros[1]);
            direccion.setText(registros[2]);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    void limpiar_datos() {
        id_cliente.setText("");
        nombre.setText("");
        numero.setText("");
        direccion.setText("");
        cedula.setText("");
        sexo.setSelectedIndex(0);
    }
    void cargar() {
        DefaultTableModel modelo2 = (DefaultTableModel) t_cliente.getModel();
        modelo2.getDataVector().clear();

        String[] registros = new String[10];
        String sql = "SELECT idcli,nomcli,cedula,numero,sexo,direccion FROM cliente";

        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                registros[0] = rs.getString("idcli");
                registros[1] = rs.getString("nomcli");
                registros[2] = rs.getString("cedula");
                registros[3] = rs.getString("sexo");
                registros[4] = rs.getString("numero");
                registros[5] = rs.getString("direccion");
                modelo2.addRow(registros);
            }
            t_cliente.setModel(modelo2);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    void cargar_filtro(String valor) {
        DefaultTableModel modelo2 = (DefaultTableModel) t_cliente.getModel();
        modelo2.getDataVector().clear();

        String[] registros = new String[10];
        String sql = "SELECT idcli,nomcli,numero,cedula,sexo,direccion FROM cliente where CONCAT(nomcli,'',cedula)LIKE '%" + valor + "%'";

        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                registros[0] = rs.getString("idcli");
                registros[1] = rs.getString("nomcli");
                registros[2] = rs.getString("cedula");
                registros[3] = rs.getString("sexo");
                registros[4] = rs.getString("numero");
                registros[5] = rs.getString("direccion");
                modelo2.addRow(registros);
            }
            t_cliente.setModel(modelo2);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    void guardar_datos(){
        try {
            String sexoseleccionado;
             sexoseleccionado = sexo.getSelectedItem().toString();
               if ("MASCULINO".equals(sexoseleccionado)) {
                   } else if ("FEMENINO".equals(sexoseleccionado)) {
                   } else if ("EMPRESA".equals(sexoseleccionado)) {
                   } 
            String sql = "";
            sql = "INSERT INTO cliente (nomcli,numero,cedula,direccion,sexo)VALUES('" + nombre.getText() + "','" + numero.getText() + "','" + cedula.getText() + "','" + direccion.getText() + "','" + sexoseleccionado + "')";
            PreparedStatement psz = cn.prepareStatement(sql);

            int n;
            n = psz.executeUpdate();
            if (n > 0) {

            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        JOptionPane.showMessageDialog(null, "GUARDADO CON EXITO");  
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        id_cliente = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        numero = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        direccion = new javax.swing.JTextField();
        limpiar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        t_cliente = new javax.swing.JTable();
        buscar_nombre = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        actualizar_datos = new javax.swing.JButton();
        actualizar_datos1 = new javax.swing.JButton();
        salir = new javax.swing.JButton();
        guardar = new javax.swing.JButton();
        nombre = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        cedula = new javax.swing.JTextField();
        sexo = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "REGISTRAR DATOS DE CLIENTE", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("ID CLIENTE");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, -1));

        id_cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                id_clienteActionPerformed(evt);
            }
        });
        jPanel1.add(id_cliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 60, 250, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("NUMERO");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, -1, -1));
        jPanel1.add(numero, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 150, 250, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("SEXO");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, -1, -1));
        jPanel1.add(direccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 180, 250, -1));

        limpiar.setBackground(new java.awt.Color(255, 0, 0));
        limpiar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        limpiar.setForeground(new java.awt.Color(255, 255, 255));
        limpiar.setText("LIMPIAR");
        limpiar.setPreferredSize(new java.awt.Dimension(84, 22));
        limpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                limpiarActionPerformed(evt);
            }
        });
        jPanel1.add(limpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 90, -1));

        t_cliente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID CLIENTE", "NOMBRE", "CEDULA", "SEXO", "NUMERO", "DIRECCION"
            }
        ));
        t_cliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                t_clienteMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(t_cliente);
        if (t_cliente.getColumnModel().getColumnCount() > 0) {
            t_cliente.getColumnModel().getColumn(0).setMinWidth(80);
            t_cliente.getColumnModel().getColumn(0).setMaxWidth(80);
            t_cliente.getColumnModel().getColumn(1).setMinWidth(200);
            t_cliente.getColumnModel().getColumn(1).setMaxWidth(200);
            t_cliente.getColumnModel().getColumn(2).setMinWidth(100);
            t_cliente.getColumnModel().getColumn(2).setMaxWidth(100);
            t_cliente.getColumnModel().getColumn(3).setMinWidth(120);
            t_cliente.getColumnModel().getColumn(3).setMaxWidth(120);
            t_cliente.getColumnModel().getColumn(4).setMinWidth(80);
            t_cliente.getColumnModel().getColumn(4).setMaxWidth(80);
        }

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 30, 850, 300));

        buscar_nombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                buscar_nombreKeyReleased(evt);
            }
        });
        jPanel1.add(buscar_nombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 290, 250, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("BUSCAR");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, -1, -1));

        actualizar_datos.setBackground(new java.awt.Color(255, 0, 0));
        actualizar_datos.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        actualizar_datos.setForeground(new java.awt.Color(255, 255, 255));
        actualizar_datos.setText("ACTUALIZAR");
        actualizar_datos.setPreferredSize(new java.awt.Dimension(84, 22));
        actualizar_datos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actualizar_datosActionPerformed(evt);
            }
        });
        jPanel1.add(actualizar_datos, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 250, 110, -1));

        actualizar_datos1.setBackground(new java.awt.Color(255, 0, 0));
        actualizar_datos1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        actualizar_datos1.setForeground(new java.awt.Color(255, 255, 255));
        actualizar_datos1.setText("ELIMINAR DATOS");
        actualizar_datos1.setPreferredSize(new java.awt.Dimension(84, 22));
        actualizar_datos1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actualizar_datos1ActionPerformed(evt);
            }
        });
        jPanel1.add(actualizar_datos1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 320, 160, -1));

        salir.setBackground(new java.awt.Color(255, 0, 0));
        salir.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        salir.setForeground(new java.awt.Color(255, 255, 255));
        salir.setText("SALIR");
        salir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salirActionPerformed(evt);
            }
        });
        jPanel1.add(salir, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 370, -1, -1));

        guardar.setBackground(new java.awt.Color(255, 0, 0));
        guardar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        guardar.setForeground(new java.awt.Color(255, 255, 255));
        guardar.setText("GUARDAR");
        guardar.setPreferredSize(new java.awt.Dimension(84, 22));
        guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarActionPerformed(evt);
            }
        });
        jPanel1.add(guardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 250, 100, -1));
        jPanel1.add(nombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 90, 250, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("NOMBRE");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, -1));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("CEDULA");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, -1, -1));
        jPanel1.add(cedula, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 120, 250, -1));

        sexo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELECCIONE", "MASCULINO", "FEMENINO", "EMPRESA" }));
        jPanel1.add(sexo, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 210, 250, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("DIRECCION");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 406, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void limpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_limpiarActionPerformed
        limpiar_datos();


    }//GEN-LAST:event_limpiarActionPerformed

    private void id_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_id_clienteActionPerformed
        try {
            cargar_datos();
        } catch (SQLException ex) {
            Logger.getLogger(clientedatos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_id_clienteActionPerformed

    private void buscar_nombreKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscar_nombreKeyReleased
        cargar_filtro(buscar_nombre.getText());
    }//GEN-LAST:event_buscar_nombreKeyReleased

    private void actualizar_datosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actualizar_datosActionPerformed
        if (id_cliente.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "EL ID NO PUEDE ESTAR EN BLANCO");
            id_cliente.requestFocus(true);
            return;
        }
        if (nombre.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "EL NOMBRE NO PUEDE ESTAR EN BLANCO");
            nombre.requestFocus(true);
            return;
        }
        if (numero.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "EL APELLIDO NO PUEDE ESTAR EN BLANCO");
            numero.requestFocus(true);
            return;
        }
        if (direccion.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "EL SEXO NO PUEDE ESTAR EN BLANCO");
            direccion.requestFocus(true);
            return;
        }
        actualizar();
        limpiar_datos();
        cargar();
    }//GEN-LAST:event_actualizar_datosActionPerformed

    private void actualizar_datos1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actualizar_datos1ActionPerformed
             if (id_cliente.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "EL ID NO PUEDE ESTAR EN BLANCO");
            id_cliente.requestFocus(true);
            return;
        }
        if (nombre.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "EL NOMBRE NO PUEDE ESTAR EN BLANCO");
            nombre.requestFocus(true);
            return;
        }
        if (numero.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "EL APELLIDO NO PUEDE ESTAR EN BLANCO");
            numero.requestFocus(true);
            return;
        }
        if (direccion.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "EL SEXO NO PUEDE ESTAR EN BLANCO");
            direccion.requestFocus(true);
            return;
        }
        borrar_datos();
        limpiar_datos();
        cargar();
    }//GEN-LAST:event_actualizar_datos1ActionPerformed

    private void salirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salirActionPerformed
this.dispose();
    }//GEN-LAST:event_salirActionPerformed

    private void t_clienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t_clienteMouseClicked
            int fila= t_cliente.getSelectedRow();
     if(fila>=0){
       menu.id_cliente.setText(t_cliente.getValueAt(fila, 0).toString());
       menu.cliente.setText(t_cliente.getValueAt(fila, 1).toString());  
       menu.buscar.requestFocus();
       this.dispose();

     } 
     
    }//GEN-LAST:event_t_clienteMouseClicked

    private void guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarActionPerformed
      if (nombre.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "EL NOMBRE NO PUEDE ESTAR EN BLANCO");
            nombre.requestFocus(true);
            return;
        }
        if (numero.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "EL NUMERO NO PUEDE ESTAR EN BLANCO");
            numero.requestFocus(true);
            return;
        }
        if (direccion.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "LA DIRECCION NO PUEDE ESTAR EN BLANCO");
            direccion.requestFocus(true);
            return;
        }

        guardar_datos();
        limpiar_datos();
        cargar();

    }//GEN-LAST:event_guardarActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new clientedatos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton actualizar_datos;
    private javax.swing.JButton actualizar_datos1;
    private javax.swing.JTextField buscar_nombre;
    private javax.swing.JTextField cedula;
    private javax.swing.JTextField direccion;
    private javax.swing.JButton guardar;
    private javax.swing.JTextField id_cliente;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton limpiar;
    private javax.swing.JTextField nombre;
    private javax.swing.JTextField numero;
    private javax.swing.JButton salir;
    private javax.swing.JComboBox<String> sexo;
    public javax.swing.JTable t_cliente;
    // End of variables declaration//GEN-END:variables

}
