package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import pivLayer.ElementoProcesable;
import pivLayer.Imagen;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class PreviewFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel gridPreProcessingPanel;

	public PreviewFrame(List<List<ElementoProcesable>> resultList) {
		setTitle("Previsualizacion Preprocesamiento");
		initComponent();
		addGridRows(resultList);
	}

	private void addGridRows(List<List<ElementoProcesable>> resultList) {
		FormLayout layoutRowFilters = (FormLayout) gridPreProcessingPanel.getLayout();
		for (List<ElementoProcesable> listImages : resultList) {
			int n = layoutRowFilters.getRowCount() + 1;
			layoutRowFilters.appendRow(RowSpec.decode("165px"));
			gridPreProcessingPanel.add(getRowPanel(listImages), "1, " + n + ", fill, fill");
		}

	}

	private void initComponent() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(120, 100, 523, 197);
		getContentPane().setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);

		gridPreProcessingPanel = new JPanel();
		scrollPane.setViewportView(gridPreProcessingPanel);
		FormLayout fl_gridPreProcessingPanel = new FormLayout(new ColumnSpec[] { ColumnSpec.decode("default:grow"), }, new RowSpec[] {});
		gridPreProcessingPanel.setLayout(fl_gridPreProcessingPanel);

	}

	private JPanel getRowPanel(List<ElementoProcesable> listImages) {
		JPanel newRowPreProcessing = new JPanel();
		newRowPreProcessing.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		newRowPreProcessing.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		for (ElementoProcesable img : listImages) {
			ImageLabelExtended lblNewLabel = new ImageLabelExtended((Imagen) img);
			lblNewLabel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					ImageFrame f = new ImageFrame(((ImageLabelExtended) arg0.getSource()).getImagen());
					f.setVisible(true);
				}
			});
			newRowPreProcessing.add(lblNewLabel);
		}

		return newRowPreProcessing;
	}

}