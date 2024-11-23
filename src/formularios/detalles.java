
package formularios;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import sistema_kiosko.conectar;

public class detalles extends javax.swing.JFrame {

    conectar cc = new conectar();
    Connection cn = (Connection) cc.conexion();
    
    public detalles() {
        initComponents();
        cargar_facturas();
        id_factura.setVisible(false);
        todas.setSelected(true);
   
    }
        private void cargar_detalles(){
        try{
            String query = "SELECT * FROM detalle_factura WHERE (id_factura LIKE '%" + id_factura.getText() + "%') and estado = 1";
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(query);
            DefaultTableModel model_table = (DefaultTableModel)tabla_productos1.getModel();
            model_table.getDataVector().clear();
            String registros[] = new String[10];
            while(rs.next()){
                registros[0] = rs.getString("id_factura");
                registros[1] = rs.getString("id_articulo");
                registros[2] = rs.getString("des_articulo");
                registros[3] = rs.getString("cantidad");
                 registros[4] = rs.getString("precio");
                  registros[5] = rs.getString("importe");
                model_table.addRow(registros);
            }  
        } catch(Exception error){
            JOptionPane.showMessageDialog(null, "Error: " + error);
        }
    } 
        private void cargar_facturas(){
        try{
            String query = "SELECT * FROM factura WHERE (id_factura LIKE '%" + buscar.getText() + "%') and estado = 1";
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(query);
            DefaultTableModel model_table = (DefaultTableModel)tabla_productos.getModel();
            model_table.getDataVector().clear();
            String registros[] = new String[6];
            while(rs.next()){
                   registros[0] = rs.getString("id_factura");
                registros[1] = rs.getString("idcli");
                registros[3] = rs.getString("fecha_factura");
                registros[4] = rs.getString("total_factura");
                registros[2] = rs.getString("nom_cliente");
                model_table.addRow(registros);
            }  
        } catch(Exception error){
            JOptionPane.showMessageDialog(null, "Error: " + error);
        }
    }
       private void cargar_facturas1() {
    try {
        String filtro = buscar.getText().trim().toLowerCase();
        String estadoFiltrado = "";
        LocalDate hoy = LocalDate.now();

        // Construir la consulta SQL con el filtro para seleccionar las facturas de hoy
        String query = "SELECT * FROM factura WHERE (id_factura LIKE ? OR idcli LIKE ? OR fecha_factura LIKE ? OR total_factura LIKE ? OR nom_cliente LIKE ?) AND estado = 1";

        if (hoys.isSelected()) {
            estadoFiltrado = " AND DATE(fecha_factura) = CURDATE()";
        }

        query += estadoFiltrado;

        // Preparar la sentencia SQL
        PreparedStatement ps = cn.prepareStatement(query);
        for (int i = 1; i <= 5; i++) {
            ps.setString(i, "%" + filtro + "%");
        }

        // Ejecutar la consulta
        ResultSet rs = ps.executeQuery();

        // Limpiar la tabla antes de cargar nuevos datos
        DefaultTableModel model_table = (DefaultTableModel) tabla_productos.getModel();
        model_table.getDataVector().clear();

        // Iterar sobre los resultados y cargarlos en la tabla
        while (rs.next()) {
            String registros[] = new String[6];
            registros[0] = rs.getString("id_factura");
            registros[1] = rs.getString("idcli");
            registros[3] = rs.getString("fecha_factura");
            registros[4] = rs.getString("total_factura");
            registros[2] = rs.getString("nom_cliente");
            model_table.addRow(registros);
        }
    } catch (Exception error) {
        JOptionPane.showMessageDialog(null, "Error: " + error);
    }
}
        void cargar_filtro(String valor) {
        DefaultTableModel modelo2 = (DefaultTableModel) tabla_productos.getModel();
        modelo2.getDataVector().clear();

        String[] registros = new String[6];
        String sql = "SELECT id_factura,total_factura,fecha_factura,idcli,nom_cliente FROM factura where CONCAT(fecha_factura,'',nom_cliente)LIKE '%" + valor + "%'";

        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
              registros[0] = rs.getString("id_factura");
                registros[1] = rs.getString("idcli");
                registros[3] = rs.getString("fecha_factura");
                registros[4] = rs.getString("total_factura");
                registros[2] = rs.getString("nom_cliente");

                modelo2.addRow(registros);
            }
            tabla_productos.setModel(modelo2);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        detalles1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla_productos = new javax.swing.JTable();
        buscar = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabla_productos1 = new javax.swing.JTable();
        id_factura = new javax.swing.JTextField();
        todas = new javax.swing.JRadioButton();
        hoys = new javax.swing.JRadioButton();
        semana = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        mes = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(44, Short.MAX_VALUE))
        );

        buscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                buscarKeyReleased(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(102, 51, 0));
        jLabel5.setText("BUSCAR");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(102, 51, 0));
        jLabel6.setText("DETALLES FACTURAS");

        jPanel4.setBackground(new java.awt.Color(102, 51, 0));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tabla_productos1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        tabla_productos1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID FACTURA", "ID ARTICULO", "DESCRIPCION ARTICULO", "CANTIDAD", "PRECIO", "TOTAL"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabla_productos1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabla_productos1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabla_productos1);
        if (tabla_productos1.getColumnModel().getColumnCount() > 0) {
            tabla_productos1.getColumnModel().getColumn(0).setMinWidth(150);
            tabla_productos1.getColumnModel().getColumn(0).setMaxWidth(150);
            tabla_productos1.getColumnModel().getColumn(1).setMinWidth(150);
            tabla_productos1.getColumnModel().getColumn(1).setMaxWidth(150);
            tabla_productos1.getColumnModel().getColumn(3).setMinWidth(150);
            tabla_productos1.getColumnModel().getColumn(3).setMaxWidth(150);
            tabla_productos1.getColumnModel().getColumn(4).setMinWidth(150);
            tabla_productos1.getColumnModel().getColumn(4).setMaxWidth(150);
            tabla_productos1.getColumnModel().getColumn(5).setMinWidth(150);
            tabla_productos1.getColumnModel().getColumn(5).setMaxWidth(150);
        }

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1027, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        id_factura.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                id_facturaKeyReleased(evt);
            }
        });

        todas.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(todas);
        todas.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        todas.setForeground(new java.awt.Color(102, 51, 0));
        todas.setText("TODAS");
        todas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                todasActionPerformed(evt);
            }
        });

        hoys.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(hoys);
        hoys.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        hoys.setForeground(new java.awt.Color(102, 51, 0));
        hoys.setText("HOY");
        hoys.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hoysActionPerformed(evt);
            }
        });

        semana.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(semana);
        semana.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        semana.setForeground(new java.awt.Color(102, 51, 0));
        semana.setText("SEMANA");
        semana.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                semanaActionPerformed(evt);
            }
        });

        jRadioButton4.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup2.add(jRadioButton4);
        jRadioButton4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jRadioButton4.setForeground(new java.awt.Color(102, 51, 0));
        jRadioButton4.setText("CONTADO");

        jRadioButton5.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup2.add(jRadioButton5);
        jRadioButton5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jRadioButton5.setForeground(new java.awt.Color(102, 51, 0));
        jRadioButton5.setText("CREDITO");

        mes.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(mes);
        mes.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        mes.setForeground(new java.awt.Color(102, 51, 0));
        mes.setText("MES");
        mes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(todas)
                        .addGap(18, 18, 18)
                        .addComponent(hoys)
                        .addGap(18, 18, 18)
                        .addComponent(semana)
                        .addGap(18, 18, 18)
                        .addComponent(mes)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jRadioButton4)
                        .addGap(18, 18, 18)
                        .addComponent(jRadioButton5)
                        .addGap(132, 132, 132)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(182, 182, 182)
                .addComponent(id_factura, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(id_factura, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(todas)
                    .addComponent(hoys)
                    .addComponent(semana)
                    .addComponent(jRadioButton4)
                    .addComponent(jRadioButton5)
                    .addComponent(mes))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

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
            
 } 
        cargar_detalles();
    }//GEN-LAST:event_tabla_productosMouseClicked

    private void buscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscarKeyReleased
        cargar_filtro(buscar.getText());
    }//GEN-LAST:event_buscarKeyReleased

    private void tabla_productos1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabla_productos1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tabla_productos1MouseClicked

    private void id_facturaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_id_facturaKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_id_facturaKeyReleased

    private void todasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_todasActionPerformed
        todas.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        cargar_facturas();
    }
});
    }//GEN-LAST:event_todasActionPerformed

    private void hoysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hoysActionPerformed
             hoys.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        cargar_facturas1();
    }
});
    }//GEN-LAST:event_hoysActionPerformed

    private void semanaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_semanaActionPerformed
        semana.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        cargar_facturas();
    }
});
    }//GEN-LAST:event_semanaActionPerformed

    private void mesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mesActionPerformed
        mes.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        cargar_facturas();
    }
});
    }//GEN-LAST:event_mesActionPerformed

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
            java.util.logging.Logger.getLogger(detalles.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(detalles.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(detalles.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(detalles.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new detalles().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField buscar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton detalles1;
    private javax.swing.JRadioButton hoys;
    private javax.swing.JTextField id_factura;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JRadioButton mes;
    private javax.swing.JRadioButton semana;
    private javax.swing.JTable tabla_productos;
    private javax.swing.JTable tabla_productos1;
    private javax.swing.JRadioButton todas;
    // End of variables declaration//GEN-END:variables
}
