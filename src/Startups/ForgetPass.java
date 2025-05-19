/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Startups;

import user.UserDashboard;
import Startups.Login;
import admin.*;
import java.awt.Color;
import config.Session;
import config.dbConnect;
import config.passwordHasher;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 */
public class ForgetPass extends javax.swing.JFrame {

    private String correctAnswer;

    private Color H;
    Color h = new Color(51,51,255);
    private Color D;
    Color d = new Color(240,240,240);
    
    public ForgetPass() {
        initComponents();
    }
    
    
    public static String Phone, usname;

    /*public boolean updateCheck() {
    dbConnect dbc = new dbConnect();
    Session sess = Session.getInstance();
    String p = phone.getText().trim();
    String us = MR_username.getText().trim();
    
    try {
    System.out.println("1");
    String query = "SELECT * FROM tbl_users WHERE (u_username='" + us + "'OR u_phone='" + p + "') AND u_id != '" + sess.getUid() + "'";
    ResultSet resultSet = dbc.getData(query);
    if (resultSet.next()) {
    Phone = resultSet.getString("u_phone");
    if (Phone.equals(p)) {
    JOptionPane.showMessageDialog(null, "Phone Number is Already Used");
    phone.setText("");
    }
    
    usname = resultSet.getString("u_username");
    if (usname.equals(us)) {
    JOptionPane.showMessageDialog(null, "Username is Already Used");
    MR_username.setText("");
    }
    return true;
    } else {
    return false;
    }
    } catch (SQLException ex) {
    System.out.println("" + ex);
    return false;
    }
    }*/
    
    
    
    
    public void logEvent(int userId, String username, String action) 
    {
        dbConnect dbc = new dbConnect();
        Connection con = dbc.getConnection();
        PreparedStatement pstmt = null;
        Timestamp time = new Timestamp(new Date().getTime());

        try 
        {
            String sql = "INSERT INTO tbl_logs (u_id, u_username, action_time, log_action) "
                    + "VALUES ('" + userId + "', '" + username + "', '" + time + "', '" + action + "')";
            pstmt = con.prepareStatement(sql);

            /*            pstmt.setInt(1, userId);
            pstmt.setString(2, username);
            pstmt.setTimestamp(3, new Timestamp(new Date().getTime()));
            pstmt.setString(4, userType);*/
            pstmt.executeUpdate();
            System.out.println("Login log recorded successfully.");
        } catch (SQLException e) 
        {
            JOptionPane.showMessageDialog(null, "Error recording log: " + e.getMessage());
        } finally //This is a database closer
        {
            try 
            {
                if (pstmt != null) 
                {
                    pstmt.close();
                }
                if (con != null) 
                {
                    con.close();
                }
            } catch (SQLException e) 
            {
                JOptionPane.showMessageDialog(null, "Error closing resources: " + e.getMessage());
            }
        }
    }
    
    
    
    
    
    
    private void fetchSecurityQuestion() {
        String username = un.getText();
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your username.");
            return;
        }

        // Create a database connection
        dbConnect db = new dbConnect();  // Instantiate dbConnector
        Connection con = db.getConnection(); // Get connection

        if (con == null) {
            JOptionPane.showMessageDialog(this, "Database connection failed. Please try again later.");
            return;
        }

        try {
            PreparedStatement stmt = con.prepareStatement(
                    "SELECT security_question, security_answer FROM tbl_users WHERE u_username = ?"
            );
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                sq.removeAllItems();
                sq.addItem(rs.getString("security_question"));
                sq.setEnabled(true);
                correctAnswer = rs.getString("security_answer");
                confirm.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(this, "Username not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while fetching the security question.");
        } finally {
            try {
                if (con != null) {
                    con.close(); // Close the connection after use
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void resetPassword() {
        String enteredAnswer = ans.getText();
        String newPassword = new String(Newpass.getPassword());

        if (correctAnswer == null) {
            JOptionPane.showMessageDialog(this, "Please search for your username first.");
            return;
        }

        if (!enteredAnswer.equalsIgnoreCase(correctAnswer)) {
            JOptionPane.showMessageDialog(this, "Incorrect security answer.");
            return;
        }

        if (newPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password cannot be empty.");
            return;
        }

        try {
            // Hash the new password before storing it
            String hashedPassword = passwordHasher.hashPassword(newPassword);

            // Instantiate the database connection
            dbConnect db = new dbConnect();
            Connection con = db.getConnection();

            if (con == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed. Please try again later.");
                return;
            }

            try {
                // Update password in the database
                PreparedStatement stmt = con.prepareStatement(
                        "UPDATE tbl_users SET u_password = ? WHERE u_username = ?"
                );
                stmt.setString(1, hashedPassword);  // Store the hashed password
                stmt.setString(2, un.getText());

                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Password successfully reset!");
                    dispose(); // Close the window after successful password reset
                } else {
                    JOptionPane.showMessageDialog(this, "Error. Username not found or password update failed.");
                }
            } finally {
                con.close(); // Close the database connection after use
            }

        } catch (NoSuchAlgorithmException ex) {
            JOptionPane.showMessageDialog(this, "Error hashing password: " + ex.getMessage());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "An error occurred while updating the password.");
        }
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Main = new javax.swing.JPanel();
        confirm = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        sq = new javax.swing.JComboBox<>();
        ans = new javax.swing.JTextField();
        un = new javax.swing.JTextField();
        acc_id1 = new javax.swing.JLabel();
        confirm1 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        acc_id2 = new javax.swing.JLabel();
        Newpass = new javax.swing.JPasswordField();
        Cpass = new javax.swing.JPasswordField();
        acc_id3 = new javax.swing.JLabel();
        check = new javax.swing.JCheckBox();
        check1 = new javax.swing.JCheckBox();
        logout = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        Main.setBackground(new java.awt.Color(255, 255, 255));
        Main.setMinimumSize(new java.awt.Dimension(780, 520));
        Main.setPreferredSize(new java.awt.Dimension(780, 520));
        Main.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        confirm.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                confirmMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                confirmMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                confirmMouseExited(evt);
            }
        });
        confirm.setLayout(null);

        jLabel11.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Confirm");
        confirm.add(jLabel11);
        jLabel11.setBounds(0, 10, 90, 10);

        Main.add(confirm, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 410, 90, 30));

        sq.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "What is your favorite food?", "What is the name of the first school you attended?", "What city were you born in?", "What is your mother’s first name?", "What is the name of your favorite restaurant?", "What was the name of your first pet?" }));
        Main.add(sq, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 200, 250, 30));
        Main.add(ans, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 240, 250, 30));
        Main.add(un, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 130, 240, 30));

        acc_id1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        acc_id1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        acc_id1.setText("Username:");
        Main.add(acc_id1, new org.netbeans.lib.awtextra.AbsoluteConstraints(235, 130, 180, 30));

        confirm1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                confirm1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                confirm1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                confirm1MouseExited(evt);
            }
        });
        confirm1.setLayout(null);

        jLabel12.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Search");
        confirm1.add(jLabel12);
        jLabel12.setBounds(0, 10, 90, 10);

        Main.add(confirm1, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 130, 90, 30));

        acc_id2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        acc_id2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        acc_id2.setText("Enter New Password:");
        Main.add(acc_id2, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 300, 190, 30));

        Newpass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewpassActionPerformed(evt);
            }
        });
        Main.add(Newpass, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 300, 250, 30));

        Cpass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CpassActionPerformed(evt);
            }
        });
        Main.add(Cpass, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 350, 250, 30));

        acc_id3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        acc_id3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        acc_id3.setText("Confirm Password:");
        Main.add(acc_id3, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 350, 190, 30));

        check.setBackground(new java.awt.Color(255, 255, 255));
        check.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        check.setText("Show");
        check.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkActionPerformed(evt);
            }
        });
        Main.add(check, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 300, -1, 30));

        check1.setBackground(new java.awt.Color(255, 255, 255));
        check1.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        check1.setText("Show");
        check1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                check1ActionPerformed(evt);
            }
        });
        Main.add(check1, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 350, -1, 30));

        logout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logoutMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutMouseExited(evt);
            }
        });
        logout.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Back");
        logout.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 70, 30));

        Main.add(logout, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 410, 90, 30));

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8-codechef-100.png"))); // NOI18N
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 180, 110, 150));

        Main.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 230, 520));

        jLabel1.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Account Details");
        jLabel1.setToolTipText("");
        Main.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 20, 550, 50));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Main, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Main, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(796, 559));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void logoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutMouseClicked
        Login lg = new Login();
        lg.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_logoutMouseClicked

    private void logoutMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutMouseEntered
        logout.setBackground(h);
    }//GEN-LAST:event_logoutMouseEntered

    private void logoutMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutMouseExited
        logout.setBackground(d);
    }//GEN-LAST:event_logoutMouseExited

    private void confirmMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_confirmMouseClicked
        String u = un.getText().trim();
        String a = ans.getText().trim();
        String np = new String(Newpass.getPassword()).trim();
        String cp = new String(Cpass.getPassword()).trim();
        dbConnect connector = new dbConnect();
        int userId = 0;


        
        if (u.isEmpty() || a.isEmpty() || np.isEmpty() || cp.isEmpty()) 
        {
            JOptionPane.showMessageDialog(this, "Please fill in all fields before submitting.");
            return;
        }else if(!np.equals(cp))
        {
            JOptionPane.showMessageDialog(this, "Password does not match");
        }else
        {
            // Call resetPassword method to validate and update password
            resetPassword();
            
            
            try 
            {
                String query = "SELECT u_id FROM tbl_users WHERE u_username = '" + u + "'";
                PreparedStatement pstmt = connector.getConnection().prepareStatement(query);

                ResultSet resultSet = pstmt.executeQuery();

                if (resultSet.next()) 
                {
                    System.out.println("user was recieved");
                    userId = resultSet.getInt("u_id");   // Update the outer `userId` correctly
                }
            } catch (SQLException ex) 
            {
                System.out.println("SQL Exception: " + ex);
            }
            System.out.println("username: "+u);
            System.out.println("user ID: "+userId);

            logEvent(userId, u, "Forgot, and changed their password");
            
            
            Login lg = new Login();
            lg.setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_confirmMouseClicked

    private void confirmMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_confirmMouseEntered
        confirm.setBackground(h);
    }//GEN-LAST:event_confirmMouseEntered

    private void confirmMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_confirmMouseExited
        confirm.setBackground(d);
    }//GEN-LAST:event_confirmMouseExited

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
    
    }//GEN-LAST:event_formWindowActivated

    private void confirm1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_confirm1MouseClicked
        dbConnect connector = new dbConnect();
        String username = un.getText();  
        String answer = null;
        
    if (username.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter your username.");
        return;
    }

    // Create a database connection
    dbConnect db = new dbConnect();  // Instantiate dbConnector
    Connection con = db.getConnection(); // Get connection
        try {
            String query = "SELECT * FROM tbl_users WHERE u_username = '" + username + "'"; //was not searching * or all when finding security_asnwer column
            PreparedStatement pstmt = connector.getConnection().prepareStatement(query);

            ResultSet resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                answer = resultSet.getString("security_answer");
            }
        } catch (SQLException ex) {
            System.out.println("SQL Exception: " + ex);
        }

    if (con == null) {
        JOptionPane.showMessageDialog(this, "Database connection failed. Please try again later.");
        return;
    }else if(answer.equals(""))
    {
        JOptionPane.showMessageDialog(this, "You did not set a password recovery for your account.");
        Login l = new Login();
        l.setVisible(true);
        this.dispose();
    }else
    {

        try {
            PreparedStatement stmt = con.prepareStatement(
                "SELECT security_question, security_answer FROM tbl_users WHERE u_username = ?"
            );
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                sq.removeAllItems();
                sq.addItem(rs.getString("security_question"));
                sq.setEnabled(true);
                correctAnswer = rs.getString("security_answer");
                confirm.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(this, "Username not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while fetching the security question.");
        } finally {
            try {
                if (con != null) {
                    con.close(); // Close the connection after use
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    }//GEN-LAST:event_confirm1MouseClicked

    private void confirm1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_confirm1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_confirm1MouseEntered

    private void confirm1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_confirm1MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_confirm1MouseExited

    private void NewpassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewpassActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NewpassActionPerformed

    private void CpassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CpassActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CpassActionPerformed

    private void checkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkActionPerformed
        boolean isSelected = check.isSelected();

        if (isSelected) {
            Newpass.setEchoChar((char)0);
        } else {
            Newpass.setEchoChar('*');
        }
    }//GEN-LAST:event_checkActionPerformed

    private void check1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_check1ActionPerformed
        boolean isSelected = check1.isSelected();

        if (isSelected) {
            Cpass.setEchoChar((char)0);
        } else {
            Cpass.setEchoChar('*');
        }
    }//GEN-LAST:event_check1ActionPerformed

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
            java.util.logging.Logger.getLogger(ForgetPass.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ForgetPass.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ForgetPass.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ForgetPass.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ForgetPass().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPasswordField Cpass;
    private javax.swing.JPanel Main;
    private javax.swing.JPasswordField Newpass;
    private javax.swing.JLabel acc_id1;
    private javax.swing.JLabel acc_id2;
    private javax.swing.JLabel acc_id3;
    private javax.swing.JTextField ans;
    private javax.swing.JCheckBox check;
    private javax.swing.JCheckBox check1;
    private javax.swing.JPanel confirm;
    private javax.swing.JPanel confirm1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel logout;
    private javax.swing.JComboBox<String> sq;
    private javax.swing.JTextField un;
    // End of variables declaration//GEN-END:variables
}
