package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import pivLayer.Filtro;
import pivLayer.Seleccionador;
import pivLayer.SeleccionadorCascada;
import pivLayer.SeleccionadorPares;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class FilterRowPanel<T extends Filtro> extends JPanel {

	private static final long serialVersionUID = 1L;

	private T filtro;
	private Seleccionador seleccionador;
	private JPanel filtersPanel;
	private List<T> filterList;
	private List<Seleccionador> seleccionadores;

	public FilterRowPanel(T filtro, Seleccionador seleccionador, String filterName, List<T> filterList, List<Seleccionador> seleccionadores) {
		this.filtro = filtro;
		this.seleccionador = seleccionador;
		this.filterList = filterList;
		this.seleccionadores = seleccionadores;
		this.filterList.add(filtro);
		if (seleccionadores != null)
			this.seleccionadores.add(seleccionador);
		createFilterRowPanel(filterName);
	}

	public void insertRowIn(JPanel filtersPanel) {
		this.filtersPanel = filtersPanel;
		FormLayout layoutRowFilters = (FormLayout) filtersPanel.getLayout();
		layoutRowFilters.appendRow(RowSpec.decode("75px"));
		int n = layoutRowFilters.getRowCount();
		filtersPanel.add(this, "1, " + n + ", fill, fill");
	}

	public T getFiltro() {
		return filtro;
	}

	public void setFiltro(T filtro) {
		this.filtro = filtro;
	}

	private void createFilterRowPanel(String filterName) {

		setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setLayout(null);

		JLabel lblFiltername = new JLabel(filterName);
		lblFiltername.setBounds(20, 27, 255, 14);
		add(lblFiltername);

		JButton btnDeleteFilter = new JButton("Borrar");
		btnDeleteFilter.setBounds(285, 23, 91, 23);
		btnDeleteFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JPanel removeRowPanel = (JPanel) ((JButton) e.getSource()).getParent();
				FormLayout layoutRowFilters = (FormLayout) filtersPanel.getLayout();
				int n = layoutRowFilters.getConstraints(removeRowPanel).gridY;
				removeRowPanel.removeAll();
				JPanel parentPanel = (JPanel) removeRowPanel.getParent();
				parentPanel.remove(removeRowPanel);
				layoutRowFilters.removeLayoutComponent(removeRowPanel);
				layoutRowFilters.removeRow(n);
				parentPanel.repaint();
				filterList.remove(n-1);
				if (seleccionadores != null)
					seleccionadores.remove(n-1);
			}
		});
		add(btnDeleteFilter);

		JButton btnEditFilter = new JButton("Editar");
		btnEditFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FilterConfigurationFrame frame = new FilterConfigurationFrame(filtro);
				frame.setVisible(true);
				frame.setLocationRelativeTo(getParent());
			}
		});

		btnEditFilter.setBounds(185, 23, 91, 23);
		if (filtro.getParametros() == null)
			btnEditFilter.setEnabled(false);
		add(btnEditFilter);

		if (seleccionador == null)
			return;

		JComboBox<Seleccionador> comboBox = new JComboBox<Seleccionador>();
		comboBox.setModel(new DefaultComboBoxModel<Seleccionador>(new Seleccionador[] { new SeleccionadorPares(), new SeleccionadorCascada() }));
		comboBox.setBounds(386, 23, 102, 22);
		comboBox.setSelectedItem(seleccionador);
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				JPanel editedRowPanel = (JPanel) ((JComboBox<?>) arg0.getSource()).getParent();
				FormLayout layoutRowFilters = (FormLayout) filtersPanel.getLayout();
				int index = layoutRowFilters.getConstraints(editedRowPanel).gridY;
				seleccionadores.remove(index-1);
				seleccionador = ((Seleccionador) arg0.getItem());
				seleccionadores.add(index-1, seleccionador);
			}
		});
		add(comboBox);
	}

}
