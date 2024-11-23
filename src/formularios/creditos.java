
package formularios;

import static formularios.menu.cargartodo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import sistema_kiosko.conectar;

public class creditos extends javax.swing.JFrame {

    conectar cc = new conectar();
    Connection cn = (Connection) cc.conexion();
    
    public creditos() {
        initComponents();
        cargar_clientes();
        id_factura.setVisible(false);
   
    }

private void cargar_clientes() {
    try {
        String query = "SELECT * FROM credito WHERE idcli LIKE '%" + buscar.getText() + "%'";
        Statement st = cn.createStatement();
        ResultSet rs = st.executeQuery(query);
        DefaultTableModel model_table = (DefaultTableModel) tabla_productos.getModel();
        model_table.getDataVector().clear();
        String registros[] = new String[5]; // Cambia el tamaño del array según la cantidad de columnas de la tabla credito
        while (rs.next()) {
            double credito = Double.parseDouble(rs.getString("credito"));
            if (credito != 0) {
                registros[0] = rs.getString("idcredito");
                registros[1] = rs.getString("idcli");
                registros[2] = rs.getString("nomcli");
                registros[3] = rs.getString("fecha");
                registros[4] = rs.getString("credito");
                model_table.addRow(registros);
            }
        }
    } catch (Exception error) {
        JOptionPane.showMessageDialog(null, "Error: " + error);
    }
}

void cargar_filtro(String valor) {
    DefaultTableModel modelo2 = (DefaultTableModel) tabla_productos.getModel();
    modelo2.getDataVector().clear();

    try {
        String sql = "SELECT * FROM credito WHERE idcli LIKE ? OR nomcli LIKE ?";
        PreparedStatement ps = cn.prepareStatement(sql);
        ps.setString(1, "%" + valor + "%");
        ps.setString(2, "%" + valor + "%");

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String[] registros = new String[5]; // Ajusta el tamaño del arreglo según la cantidad de columnas de la tabla credito
            registros[0] = rs.getString("idcredito");
            registros[1] = rs.getString("idcli");
            registros[2] = rs.getString("nomcli");
            registros[4] = rs.getString("credito");
            registros[3] = rs.getString("fecha");
            modelo2.addRow(registros);
        }
        tabla_productos.setModel(modelo2);
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, ex);
    }
}
void realizarPagoDeuda() {
    try {
        // Obtener el monto del pago desde el JTextField
        double montoPago = Double.parseDouble(txtMonto.getText());

        // Obtener la fila seleccionada en la tabla
        int filaSeleccionada = tabla_productos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(null, "Por favor, selecciona una deuda para realizar el pago.");
            return;
        }

        // Obtener el ID de la deuda seleccionada en la tabla
        int idDeuda = Integer.parseInt(tabla_productos.getValueAt(filaSeleccionada, 0).toString());

        // Obtener el monto actual de la deuda
        double montoDeuda = Double.parseDouble(tabla_productos.getValueAt(filaSeleccionada, 4).toString());

        // Verificar si el monto del pago excede el monto de la deuda
        if (montoPago > montoDeuda) {
            JOptionPane.showMessageDialog(null, "El monto del pago excede el monto de la deuda.");
            return;
        }

        // Realizar el pago actualizando el monto de la deuda
        double nuevoMontoDeuda = montoDeuda - montoPago;

        // Actualizar el monto de la deuda en la base de datos
        String sql = "UPDATE credito SET credito = ? WHERE idcredito = ?";
        PreparedStatement ps = cn.prepareStatement(sql);
        ps.setDouble(1, nuevoMontoDeuda);
        ps.setInt(2, idDeuda);

        int resultado = ps.executeUpdate();
        if (resultado > 0) {
            JOptionPane.showMessageDialog(null, "Pago realizado con éxito.");
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo realizar el pago.");
        }

        // Actualizar la tabla de créditos
        cargar_clientes();

    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(null, "Por favor, ingresa un monto válido para el pago.");
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Error al realizar el pago: " + ex.getMessage());
    }
}
void guardar_dinero_caja() {
    try {
        // Consulta SQL para insertar el monto en la tabla caja
        String sql = "INSERT INTO caja (fecha, total) VALUES (NOW(), ?)";

        // Preparar la consulta
        PreparedStatement pstmt = cn.prepareStatement(sql);
        
        // Convertir el texto del campo txtMonto a tipo float
        float montoFloat = Float.parseFloat(txtMonto.getText());

        // Establecer el valor del parámetro
        pstmt.setFloat(1, montoFloat);

        // Ejecutar la consulta
        int rowsAffected = pstmt.executeUpdate();

        // Verificar si se insertaron filas
        if (rowsAffected > 0) {
            // Éxito al guardar en la caja
            //JOptionPane.showMessageDialog(null, "Monto guardado en la caja con éxito.");
        } else {
            // No se guardaron filas
           // JOptionPane.showMessageDialog(null, "Error al guardar el monto en la caja.");
        }

        // Cerrar recursos
        pstmt.close();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Error al ejecutar la consulta: " + ex.getMessage());
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(null, "Error al convertir el monto a float: " + ex.getMessage());
    }
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        detalles1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla_productos = new javax.swing.JTable();
        buscar = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        id_factura = new javax.swing.JTextField();
        txtMonto = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        detalles2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(102, 51, 0));

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("KIOSKO CAFE");

        detalles1.setBackground(new java.awt.Color(255, 0, 0));
        detalles1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        detalles1.setForeground(new java.awt.Color(255, 255, 255));
        detalles1.setText("SALIR");
        detalles1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detalles1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 679, Short.MAX_VALUE)
                .addComponent(detalles1, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(detalles1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 966, -1));

        jPanel3.setBackground(new java.awt.Color(102, 51, 0));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tabla_productos.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        tabla_productos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID FACTURA", "ID CLIENTE", "CLIENTE", "FECHA", "TOTAL"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabla_productos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabla_productosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabla_productos);
        if (tabla_productos.getColumnModel().getColumnCount() > 0) {
            tabla_productos.getColumnModel().getColumn(0).setMinWidth(150);
            tabla_productos.getColumnModel().getColumn(0).setMaxWidth(150);
            tabla_productos.getColumnModel().getColumn(1).setMinWidth(150);
            tabla_productos.getColumnModel().getColumn(1).setMaxWidth(150);
            tabla_productos.getColumnModel().getColumn(3).setMinWidth(150);
            tabla_productos.getColumnModel().getColumn(3).setMaxWidth(150);
            tabla_productos.getColumnModel().getColumn(4).setMinWidth(150);
            tabla_productos.getColumnModel().getColumn(4).setMaxWidth(150);
        }

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 922, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(44, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 102, 946, -1));

        buscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                buscarKeyReleased(evt);
            }
        });
        jPanel1.add(buscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(67, 76, 152, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(102, 51, 0));
        jLabel5.setText("BUSCAR");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 77, -1, -1));

        id_factura.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                id_facturaKeyReleased(evt);
            }
        });
        jPanel1.add(id_factura, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 76, 107, -1));

        txtMonto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMontoKeyReleased(evt);
            }
        });
        jPanel1.add(txtMonto, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 370, 152, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(102, 51, 0));
        jLabel6.setText("MONTO A PAGAR");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 370, -1, -1));

        detalles2.setBackground(new java.awt.Color(255, 0, 0));
        detalles2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        detalles2.setForeground(new java.awt.Color(255, 255, 255));
        detalles2.setText("REALIZAR PAGO");
        detalles2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detalles2ActionPerformed(evt);
            }
        });
        jPanel1.add(detalles2, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 370, 150, 24));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 570));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void detalles1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detalles1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_detalles1ActionPerformed

    private void tabla_productosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabla_productosMouseClicked
        int fila= tabla_productos.getSelectedRow();
        if(fila>=0){
            id_factura.setText(tabla_productos.getValueAt(fila, 0).toString());
            txtMonto.requestFocus();
 } 
       
    }//GEN-LAST:event_tabla_productosMouseClicked

    private void buscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscarKeyReleased
        cargar_filtro(buscar.getText());
    }//GEN-LAST:event_buscarKeyReleased

    private void id_facturaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_id_facturaKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_id_facturaKeyReleased

    private void txtMontoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMontoKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMontoKeyReleased

    private void detalles2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detalles2ActionPerformed
       realizarPagoDeuda();
       guardar_dinero_caja();
      cargartodo.doClick();
        this.dispose();
    new creditos().setVisible(true);
    }//GEN-LAST:event_detalles2ActionPerformed

    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(creditos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(creditos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(creditos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(creditos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new creditos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField buscar;
    private javax.swing.JButton detalles1;
    private javax.swing.JButton detalles2;
    private javax.swing.JTextField id_factura;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabla_productos;
    public static javax.swing.JTextField txtMonto;
    // End of variables declaration//GEN-END:variables
}
