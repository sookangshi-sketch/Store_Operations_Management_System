package attendance;

import core.Employee;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class AttendancePanel extends JPanel {
    private Employee currentEmployee; 
    private JTextArea logArea;

    public AttendancePanel(Employee user) {
        this.currentEmployee = user;
        setLayout(new BorderLayout());

        // --- 顶部按钮面板 ---
        JPanel btnPanel = new JPanel();
        JButton inBtn = new JButton("Clock In");
        JButton outBtn = new JButton("Clock Out");
        JButton historyBtn = new JButton("View History"); // 新增按钮

        // 美化一点按钮颜色
        inBtn.setBackground(new Color(152, 251, 152)); 
        outBtn.setBackground(new Color(255, 182, 193));
        
        btnPanel.add(new JLabel("Actions: "));
        btnPanel.add(inBtn);
        btnPanel.add(outBtn);
        btnPanel.add(historyBtn); // 添加到界面

        // --- 日志显示区域 ---
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        if (currentEmployee != null) {
            logArea.setText("User: " + currentEmployee.getName() + " (" + currentEmployee.getRole() + ")\n");
            logArea.append("Date: " + LocalDate.now() + "\n-----------------------------\n");
        } else {
            logArea.setText("Please Login First.\n");
        }

        add(btnPanel, BorderLayout.NORTH);
        add(new JScrollPane(logArea), BorderLayout.CENTER);

        // --- 事件监听器 ---

        inBtn.addActionListener(e -> {
            if (currentEmployee == null) return;
            String msg = AttendanceManager.clockIn(currentEmployee.getId());
            logArea.append(msg + "\n");
        });

        outBtn.addActionListener(e -> {
            if (currentEmployee == null) return;
            String msg = AttendanceManager.clockOut(currentEmployee.getId());
            logArea.append(msg + "\n");
        });

        // 查看历史记录按钮逻辑
        historyBtn.addActionListener(e -> showHistoryDialog());
    }

    private void showHistoryDialog() {
        if (currentEmployee == null) return;

        // 1. 获取过滤后的数据 (核心修改点)
        List<AttendanceRecord> records = AttendanceManager.getFilteredRecords(currentEmployee);

        if (records.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No attendance records found.");
            return;
        }

        // 2. 准备表格数据
        String[] columnNames = {"Date", "Employee ID", "Type", "Time"};
        String[][] data = new String[records.size()][4];

        for (int i = 0; i < records.size(); i++) {
            AttendanceRecord r = records.get(i);
            data[i][0] = r.getDate();
            data[i][1] = r.getEmployeeId();
            data[i][2] = r.getType();
            data[i][3] = r.getTime();
        }

        // 3. 构建表格
        JTable table = new JTable(new DefaultTableModel(data, columnNames));
        table.setEnabled(false); // 禁止编辑表格内容
        JScrollPane scrollPane = new JScrollPane(table);

        // 4. 设置窗口标题 (如果是经理，显示不同标题)
        boolean isManager = "Manager".equalsIgnoreCase(currentEmployee.getRole());
        String title = isManager ? "All Employee Attendance Records" : "My Attendance History";

        // 5. 弹出窗口
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), title, true);
        dialog.add(scrollPane);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}