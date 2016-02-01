package net.krglok.realms.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JLabel;
import javax.swing.JTextField;
import com.jgoodies.forms.layout.Sizes;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import net.krglok.realms.builder.BuildPlanType;
import net.krglok.realms.core.Building;
import net.krglok.realms.core.ConfigBasis;
import net.krglok.realms.core.Item;
import net.krglok.realms.core.Settlement;
import net.krglok.realms.core.TradeOrder;
import net.krglok.realms.data.DataStorage;
import net.krglok.realms.kingdom.Lehen;
import net.krglok.realms.npc.NpcData;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ShowLehen extends JDialog
{

	private final JPanel contentPanel = new JPanel();
	private JTextField ed_Id;
	private JTextField ed_Name;
	private JTextField ed_Beds;
	private JTextField ed_Settler;
	private JTextField ed_Units;
	private JTextField ed_SettleTyp;
	private JTextField ed_Age;
	private JTextField ed_Bank;
	private JTextField ed_Required;
	private JTextField ed_Storage;
	private JTextField ed_Buildings;
	private JTextField ed_Barrack;
	private JTextField ed_SupportId;
	private JTextField ed_SupportName;
	private JTextField ed_BuyOrder;
	private JTextField ed_Routes;
	private JTable table;
	
	private String[][] dataRows = new String[][] {{"", "", ""},		};
	private String[] colHeader = new String[] {"ID", "Material", "Amount" };
	private Class[] columnTypes = new Class[] {	String.class, String.class, String.class};

	private Lehen lehen;
	private DataStorage data;
	
	/**
	 * Launch the application.
	 */
	public static void showMe(Lehen lehen, DataStorage data)
	{
		try
		{
			ShowLehen dialog = new ShowLehen();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
			dialog.lehen = lehen;
			dialog.data = data;
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}

	/**
	 * Create the dialog.
	 */
	public ShowLehen()
	{
		setTitle("Lehen ");
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				//
				setText();
			}
		});
		setBounds(100, 100, 828, 447);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				new ColumnSpec(ColumnSpec.FILL, Sizes.bounded(Sizes.DEFAULT, Sizes.constant("31dlu", true), Sizes.constant("49dlu", true)), 1),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("40dlu:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("38dlu:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("65dlu:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("min(10dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("50dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("40dlu:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("min(40dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("71dlu:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(113dlu;default):grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		{
			JLabel lblId = new JLabel("ID");
			contentPanel.add(lblId, "2, 2, right, default");
		}
		{
			ed_Id = new JTextField();
			contentPanel.add(ed_Id, "4, 2, left, default");
			ed_Id.setColumns(10);
		}
		{
			ed_Name = new JTextField();
			contentPanel.add(ed_Name, "6, 2, 3, 1, default, center");
			ed_Name.setColumns(10);
		}
		{
			JLabel lblAge = new JLabel("Age");
			contentPanel.add(lblAge, "12, 2, right, default");
		}
		{
			ed_Age = new JTextField();
			contentPanel.add(ed_Age, "14, 2, fill, default");
			ed_Age.setColumns(10);
		}
		{
			JLabel lblLehentyp = new JLabel("LehenTyp");
			contentPanel.add(lblLehentyp, "16, 2, right, default");
		}
		{
			ed_SettleTyp = new JTextField();
			contentPanel.add(ed_SettleTyp, "18, 2, fill, default");
			ed_SettleTyp.setColumns(10);
		}
		{
			JLabel lblSupportid = new JLabel("SupportID");
			contentPanel.add(lblSupportid, "2, 4, right, default");
		}
		{
			ed_SupportId = new JTextField();
			contentPanel.add(ed_SupportId, "4, 4, fill, default");
			ed_SupportId.setColumns(10);
		}
		{
			ed_SupportName = new JTextField();
			contentPanel.add(ed_SupportName, "6, 4, 3, 1, fill, default");
			ed_SupportName.setColumns(10);
		}
		{
			JLabel lblBeds = new JLabel("Beds");
			contentPanel.add(lblBeds, "12, 4, right, default");
		}
		{
			ed_Beds = new JTextField();
			contentPanel.add(ed_Beds, "14, 4, fill, default");
			ed_Beds.setColumns(10);
		}
		{
			JLabel lblSettler = new JLabel("Settler");
			contentPanel.add(lblSettler, "16, 4, right, default");
		}
		{
			ed_Settler = new JTextField();
			contentPanel.add(ed_Settler, "18, 4, fill, default");
			ed_Settler.setColumns(10);
		}
		{
			JLabel lblBank = new JLabel("Bank");
			contentPanel.add(lblBank, "2, 6, right, default");
		}
		{
			ed_Bank = new JTextField();
			contentPanel.add(ed_Bank, "4, 6, fill, default");
			ed_Bank.setColumns(10);
		}
		{
			JLabel lblBarrack = new JLabel("Barrack");
			contentPanel.add(lblBarrack, "12, 6, right, default");
		}
		{
			ed_Barrack = new JTextField();
			contentPanel.add(ed_Barrack, "14, 6, fill, default");
			ed_Barrack.setColumns(10);
		}
		{
			JLabel lblRequired = new JLabel("Required");
			contentPanel.add(lblRequired, "2, 8, right, default");
		}
		{
			ed_Required = new JTextField();
			contentPanel.add(ed_Required, "4, 8, fill, default");
			ed_Required.setColumns(10);
		}
		{
			JButton btn_Required = new JButton("Required");
			btn_Required.setHorizontalAlignment(SwingConstants.LEFT);
			btn_Required.setIcon(new ImageIcon(ShowLehen.class.getResource("/net/krglok/realms/gui/check.png")));
			contentPanel.add(btn_Required, "8, 8");
		}
		{
			JLabel lblStoreage = new JLabel("Storeage");
			contentPanel.add(lblStoreage, "12, 8, right, default");
		}
		{
			ed_Storage = new JTextField();
			contentPanel.add(ed_Storage, "14, 8, fill, default");
			ed_Storage.setColumns(10);
		}
		{
			JButton btn_Warehouse = new JButton("Warehouse");
			btn_Warehouse.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					doWarehouseList();
				}
			});
			btn_Warehouse.setHorizontalAlignment(SwingConstants.LEFT);
			btn_Warehouse.setIcon(new ImageIcon(ShowLehen.class.getResource("/net/krglok/realms/gui/check.png")));
			contentPanel.add(btn_Warehouse, "18, 8");
		}
		{
			JLabel lblBuyorder = new JLabel("BuyOrder");
			contentPanel.add(lblBuyorder, "2, 10, right, default");
		}
		{
			ed_BuyOrder = new JTextField();
			contentPanel.add(ed_BuyOrder, "4, 10, fill, default");
			ed_BuyOrder.setColumns(10);
		}
		{
			JButton btnNewButton = new JButton("BuyOrders");
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//
					doBuyOrderList();
				}
			});
			btnNewButton.setHorizontalAlignment(SwingConstants.LEFT);
			btnNewButton.setIcon(new ImageIcon(ShowLehen.class.getResource("/net/krglok/realms/gui/check.png")));
			contentPanel.add(btnNewButton, "8, 10");
		}
		{
			JLabel lblBuildings = new JLabel("Buildings");
			contentPanel.add(lblBuildings, "12, 10, right, default");
		}
		{
			ed_Buildings = new JTextField();
			contentPanel.add(ed_Buildings, "14, 10, fill, default");
			ed_Buildings.setColumns(10);
		}
		{
			JButton btnBuildings = new JButton("Buildings");
			btnBuildings.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//
					doBuildingList();
				}
			});
			btnBuildings.setHorizontalAlignment(SwingConstants.LEFT);
			btnBuildings.setIcon(new ImageIcon(ShowLehen.class.getResource("/net/krglok/realms/gui/check.png")));
			contentPanel.add(btnBuildings, "18, 10");
		}
		{
			JLabel lblRoute = new JLabel("Route");
			contentPanel.add(lblRoute, "2, 12, right, default");
		}
		{
			ed_Routes = new JTextField();
			contentPanel.add(ed_Routes, "4, 12, fill, default");
			ed_Routes.setColumns(10);
		}
		{
			JButton btnRoutes = new JButton("Routes");
			btnRoutes.setIcon(new ImageIcon(ShowLehen.class.getResource("/net/krglok/realms/gui/check.png")));
			btnRoutes.setHorizontalAlignment(SwingConstants.LEFT);
			contentPanel.add(btnRoutes, "8, 12");
		}
		{
			JLabel lblUnits = new JLabel("Units");
			contentPanel.add(lblUnits, "12, 12, right, default");
		}
		{
			ed_Units = new JTextField();
			contentPanel.add(ed_Units, "14, 12, fill, default");
			ed_Units.setColumns(10);
		}
		{
			JButton btnUnits = new JButton("Units");
			btnUnits.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//
					doUnitList();
				}
			});
			btnUnits.setHorizontalAlignment(SwingConstants.LEFT);
			btnUnits.setIcon(new ImageIcon(ShowLehen.class.getResource("/net/krglok/realms/gui/check.png")));
			contentPanel.add(btnUnits, "18, 12");
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, "2, 14, 7, 1, fill, fill");
			{
				table = new JTable();
				table.setModel(new DefaultTableModel(
						dataRows,
					colHeader
				) {
					public Class getColumnClass(int columnIndex) {
						return columnTypes[columnIndex];
					}
				});
				table.getColumnModel().getColumn(0).setPreferredWidth(25);
				table.getColumnModel().getColumn(0).setMinWidth(25);
				table.getColumnModel().getColumn(0).setMaxWidth(25);
				table.getColumnModel().getColumn(1).setMinWidth(75);
				table.getColumnModel().getColumn(1).setMaxWidth(150);
				table.getColumnModel().getColumn(2).setPreferredWidth(49);
				table.getColumnModel().getColumn(2).setMinWidth(35);
				table.getColumnModel().getColumn(2).setMaxWidth(55);
				scrollPane.setViewportView(table);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setIcon(new ImageIcon(ShowLehen.class.getResource("/net/krglok/realms/gui/check.png")));
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						closeDialog();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setIcon(new ImageIcon(ShowLehen.class.getResource("/net/krglok/realms/gui/delete.png")));
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						//
						closeDialog();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	private void closeDialog()
	{
		this.dispose();
	}
	

	private void setText()
	{
		ed_Id.setText(String.valueOf(lehen.getId()));
		ed_Name.setText(lehen.getName());
		ed_SettleTyp.setText(lehen.getSettleType().name());
		ed_SupportId.setText(String.valueOf(lehen.getSupportId()));
		Settlement settle = data.getSettlements().getSettlement(lehen.getSupportId());
		if (settle != null)
		{
			ed_SupportName.setText(settle.getName());
		}
		ed_Bank.setText(ConfigBasis.setStrformat2(lehen.getBank().getKonto(), 9));
		ed_Barrack.setText(String.valueOf(lehen.getBarrack().getUnitList().size()));
		ed_Required.setText(String.valueOf(lehen.getrequiredItems().size()));
		ed_Storage.setText(String.valueOf(lehen.getWarehouse().getFreeCapacity()));
		ed_BuyOrder.setText(String.valueOf(lehen.getTrader().getBuyOrders().size()));
		ed_Routes.setText(String.valueOf(lehen.getTrader().getRouteOrders().size()));
		ed_Buildings.setText(String.valueOf(lehen.getBuildingList().size()));
		ed_Units.setText(String.valueOf(lehen.getBarrack().getUnitList().size()));
	}
	
	private void printRequired()
	{
		dataRows = new String[lehen.getrequiredItems().size()][3];
		int index = 0;
		for (Item item : lehen.getrequiredItems().values())
		{
//			if (index <100)
			{
				dataRows[index][0] = ConfigBasis.setStrright(index,3);
				dataRows[index][1] = item.ItemRef();
				dataRows[index][2] = ConfigBasis.setStrright(item.value() ,6);
			}
			index ++;
		}
		table.setModel(new DefaultTableModel(dataRows,colHeader));
	}

	
	private void doUnitList()
	{
		Object[][] dataRows = new Object[lehen.getBarrack().getUnitList().size()][4];
		int index = 0;
		for (NpcData npc : lehen.getBarrack().getUnitList())
		{
//			if (index <100)
			{
				dataRows[index][0] = String.valueOf(npc.getId());
				dataRows[index][1] = npc.getName();
				dataRows[index][2] = npc.getUnitType().name();
				dataRows[index][3] = String.valueOf(npc.getHomeBuilding());
				dataRows[index][3] = String.valueOf(npc.getWorkBuilding());
			}
			index ++;
		}
		String[] colHeader = new String[] {	"ID", "Name", "UnitType","Home","Work"};
		Class[] columnTypes = new Class[] {String.class, Integer.class, Double.class};

		WarehouseList.showMe(dataRows, columnTypes, colHeader, "Buy Orders");
	}
	
	private void doBuyOrderList()
	{
		Object[][] dataRows = new Object[lehen.getTrader().getBuyOrders().size()][4];
		int index = 0;
		for (TradeOrder order : lehen.getTrader().getBuyOrders().values())
		{
//			if (index <100)
			{
				dataRows[index][0] = order.ItemRef();
				dataRows[index][1] = order.value();
				dataRows[index][2] = ConfigBasis.format2(order.getBasePrice());
				dataRows[index][3] = order.getStatus().name();
			}
			index ++;
		}
		String[] colHeader = new String[] {	"Item", "amount", "price","Status"};
		Class[] columnTypes = new Class[] {String.class, Integer.class, Double.class};

		WarehouseList.showMe(dataRows, columnTypes, colHeader, "Buy Orders");
	}
	
	
	private void doBuildingList()
	{
		Object[][] dataRows = new Object[lehen.getBuildingList().size()][4];
		int index = 0;
		for (Building building : lehen.getBuildingList().values())
		{
			if (BuildPlanType.getBuildGroup(building.getBuildingType())>100)
			{
				dataRows[index][0] = building.getBuildingType().name();
				dataRows[index][1] = String.valueOf(building.getSettler());
				dataRows[index][2] = building.getWorkerNeeded();
//				dataRows[index] = new Object[] {building.getBuildingType().name(), building.getSettler(), "0.0"};
				index ++;
			}
		}
		String[] colHeader = new String[] {	"Building", "Beds", "Worker"};
		Class[] columnTypes = new Class[] {String.class, Integer.class, Double.class};

		WarehouseList.showMe(dataRows, columnTypes, colHeader, "Building List");
	}
	
	private void doWarehouseList()
	{
		Object[][] dataRows = new Object[lehen.getWarehouse().getItemList().size()][3];
		int index = 0;
		for (String itemRef : lehen.getWarehouse().getItemList().sortItems())
		{
			Item item = lehen.getWarehouse().getItemList().getItem(itemRef);
//			if (index <100)
			{
				dataRows[index][0] = item.ItemRef();
				dataRows[index][1] = item.value();
			}
			index ++;
		}
		for (Item item : lehen.getRequiredProduction().values())
		{
			for (int i = 0; i < dataRows.length; i++)
			{
				if (item.ItemRef().equalsIgnoreCase(String.valueOf(dataRows[i][0])))
				{
					dataRows[i][2] = item.value();
				}
			}
		}
		String[] colHeader = new String[] {	"Items", "Amount","Required"};		
		@SuppressWarnings("rawtypes")
		Class[] columnTypes = new Class[] {String.class, Integer.class, String.class};
		WarehouseList.showMe(dataRows, columnTypes, colHeader, "Warehouse List");
	}
	
}
