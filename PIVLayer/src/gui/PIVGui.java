package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;

import org.apache.commons.lang3.StringUtils;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import de.javasoft.plaf.synthetica.SyntheticaAluOxideLookAndFeel;
import manager.Constants;
import manager.ManagerException;
import manager.PluginFilterManager;
import pivLayer.Buffer;
import pivLayer.ElementoProcesable;
import pivLayer.FilterException;
import pivLayer.FiltroPIV;
import pivLayer.FiltroPostProcesamiento;
import pivLayer.FiltroPreProcesamiento;
import pivLayer.FiltroVisualizacion;
import pivLayer.Imagen;
import pivLayer.MapaVectores;
import pivLayer.PostProcesador;
import pivLayer.PreProcesador;
import pivLayer.Procesador;
import pivLayer.ProcesadorPIV;
import pivLayer.Seleccionador;
import pivLayer.SeleccionadorPares;
import utiles.FileHandling;
import utiles.FileHandlingException;

class PIVGui {

	private static JFrame PIV;

	private DefaultListModel<String> selectedImagesFilesModel;
	private JList<String> listImagesFiles;
	private List<Imagen> imagesList;

	private DefaultListModel<String> selectedVectorFilesModel;
	private JList<String> listVectorFiles;
	private List<MapaVectores> vectorList;

	private DefaultComboBoxModel<ComboItemFilter> preProcessingFiltersModel;
	private DefaultComboBoxModel<ComboItemFilter> postProcessingFiltersModel;
	private DefaultComboBoxModel<ComboItemFilter> pivProcessingFiltersModel;
	private DefaultComboBoxModel<ComboItemFilter> visualizationFiltersModel;

	private JPanel gridPreProcessingPanel;
	private JPanel gridPIVProcessingPanel;
	private JPanel gridPostProcessingPanel;
	private JPanel gridVisualizationPanel;
	private JComboBox<ComboItemFilter> comboBoxPreProcessing;
	private JComboBox<ComboItemFilter> comboBoxPIVProcessing;
	private JComboBox<ComboItemFilter> comboBoxPostProcessing;
	private JComboBox<ComboItemFilter> comboBoxVisualization;

	private List<FiltroPreProcesamiento> preProcessingFilterList;
	private List<Seleccionador> preProcessingSeleccionatorList;
	private List<FiltroPIV> pivProcessingFilterList;
	private List<Seleccionador> pivProcessingSeleccionatorList;
	private List<FiltroPostProcesamiento> postProcessingFilterList;
	private List<Seleccionador> postProcessingSeleccionatorList;
	private List<FiltroVisualizacion> visualizationFilterList;

	private Integer countVectorMap = 1;

	private JLabel lblLoading;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new PIVGui();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * 
	 * @throws ManagerException
	 */
	public PIVGui() throws ManagerException {
		initVariables();
		initialize();
		loadFilters();
		PIV.setVisible(true);
	}

	private void initVariables() {
		preProcessingFilterList = new ArrayList<>();
		pivProcessingFilterList = new ArrayList<>();
		postProcessingFilterList = new ArrayList<>();
		visualizationFilterList = new ArrayList<>();
		preProcessingSeleccionatorList = new ArrayList<>();
		pivProcessingSeleccionatorList = new ArrayList<>();
		postProcessingSeleccionatorList = new ArrayList<>();
	}

	public void addSelectedFile(File f) {
		try {
			if (FileHandling.getExtension(f).equals("jvc")) {
				addVectorList(f.getName(), new MapaVectores(FileHandling.readArrayFromFile(f.getPath())));
			} else {
				addImageList(f.getName(), new Imagen(FileHandling.getBufferedImage(f)));
			}
		} catch (FileHandlingException e) {
			new GUIException(e).inform();
		}

	}

	private void addVectorList(String name, MapaVectores vec) {
		this.selectedVectorFilesModel.addElement(name);
		vectorList.add(vec);
	}

	private void addImageList(String name, Imagen img) {
		this.selectedImagesFilesModel.addElement(name);
		imagesList.add(img);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		try {
			UIManager.setLookAndFeel(new SyntheticaAluOxideLookAndFeel());
		} catch (UnsupportedLookAndFeelException | ParseException e) {
			e.printStackTrace();
		}

		PIV = new JFrame();
		PIV.setTitle("PIV Viewver");
		PIV.setBounds(100, 100, 809, 495);
		PIV.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		PIV.getContentPane().setLayout(new BorderLayout(0, 0));

		JSplitPane splitPane = new JSplitPane();
		PIV.getContentPane().add(splitPane, BorderLayout.CENTER);

		JSplitPane splitPaneFiles = new JSplitPane();
		splitPaneFiles.setOrientation(JSplitPane.VERTICAL_SPLIT);

		JPanel panel = new JPanel();
		PIV.getContentPane().add(panel, BorderLayout.SOUTH);
		FlowLayout fl_panel = new FlowLayout(FlowLayout.RIGHT, 5, 5);
		fl_panel.setAlignOnBaseline(true);
		panel.setLayout(fl_panel);

		lblLoading = new JLabel("Procesando... ");
		ImageIcon loading = new ImageIcon(Constants.IMG_RESOURCES_PATH + "ajax_loader.gif");
		lblLoading.setIcon(loading);
		lblLoading.setHorizontalAlignment(SwingConstants.RIGHT);
		lblLoading.setVisible(false);
		panel.add(lblLoading);

		// -- MENU --//

		JToolBar toolBar = new JToolBar();
		PIV.getContentPane().add(toolBar, BorderLayout.NORTH);
		toolBar.setRollover(true);
		toolBar.setAlignmentX(Component.LEFT_ALIGNMENT);
		toolBar.setFloatable(false);
		JMenuBar menuBar = new JMenuBar();
		toolBar.add(menuBar);

		JMenu menuFile = new JMenu("Archivo");
		menuBar.add(menuFile);

		JMenuItem itemChooseFile = new JMenuItem("Seleccionar..");
		itemChooseFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				fc.setMultiSelectionEnabled(true);
				int returnVal = fc.showOpenDialog(PIV);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File[] files = fc.getSelectedFiles();
					for (File f : files) {
						addSelectedFile(f);
					}
				} else {
					System.out.println("Open command cancelled by user.");
				}

			}
		});
		menuFile.add(itemChooseFile);

		JMenuItem menuItem = new JMenuItem("Guardar Resultados");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int dialogResult = JOptionPane.showConfirmDialog(null, "¿Desea guardar los resultados?");
				int[] elem = listVectorFiles.getSelectedIndices();
				for (int i = elem.length - 1; i >= 0; i--) {
					MapaVectores vectorMap = vectorList.get(elem[i]);
					if (dialogResult == JOptionPane.YES_OPTION) {
						int resultSave = guardar(vectorMap);
						if (resultSave == 1) {
							selectedVectorFilesModel.remove(elem[i]);
							vectorList.remove(elem[i]);
						}
					}
				}
			}
		});
		menuFile.add(menuItem);

		JMenu menuPreProcesamiento = new JMenu("Pre Procesamiento");
		menuBar.add(menuPreProcesamiento);

		JMenuItem itemPreview = new JMenuItem("Previsualizar");
		itemPreview.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doPreview();
			}
		});
		menuPreProcesamiento.add(itemPreview);

		JMenu menuPIV = new JMenu("PIV");
		menuBar.add(menuPIV);

		JMenuItem itemDoPIV = new JMenuItem("Ejecutar PIV");
		itemDoPIV.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				procesarResultado();
			}
		});
		menuPIV.add(itemDoPIV);

		JMenu menuPreferences = new JMenu("Preferencias");
		menuBar.add(menuPreferences);

		JMenuItem itemConfig = new JMenuItem("Configuracion");
		itemConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SettingsFrame sf = new SettingsFrame();
				sf.setVisible(true);
			}
		});
		menuPreferences.add(itemConfig);

		// -- Archivos --//

		// -- Imagenes --//
		selectedImagesFilesModel = new DefaultListModel<String>();
		imagesList = new ArrayList<Imagen>();
		JTabbedPane tabbedPaneImagesFiles = new JTabbedPane(JTabbedPane.TOP);
		tabbedPaneImagesFiles.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		splitPaneFiles.setLeftComponent(tabbedPaneImagesFiles);
		splitPane.setLeftComponent(splitPaneFiles);

		JScrollPane scrollPaneFiles = new JScrollPane();
		tabbedPaneImagesFiles.addTab("Imagenes", null, scrollPaneFiles, null);

		listImagesFiles = new JList<String>();
		listImagesFiles.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent ke) {
				int code = ke.getKeyCode();
				if (code == KeyEvent.VK_DELETE)
					deleteSelectedImages();

			}
		});
		listImagesFiles.setModel(selectedImagesFilesModel);
		listImagesFiles.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() == 2) {
					ImageFrame f = new ImageFrame(imagesList.get(listImagesFiles.getSelectedIndex()));
					f.setVisible(true);
				}
			}
		});
		scrollPaneFiles.setViewportView(listImagesFiles);

		// -- Mapa de vectores --//

		selectedVectorFilesModel = new DefaultListModel<String>();
		vectorList = new ArrayList<MapaVectores>();
		JTabbedPane tabbedPaneVectorFiles = new JTabbedPane(JTabbedPane.TOP);
		tabbedPaneVectorFiles.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		splitPaneFiles.setRightComponent(tabbedPaneVectorFiles);

		JScrollPane scrollPaneVectorFiles = new JScrollPane();
		tabbedPaneVectorFiles.addTab("Resultados", null, scrollPaneVectorFiles, null);

		listVectorFiles = new JList<String>();
		listVectorFiles.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent ke) {
				int code = ke.getKeyCode();
				if (code == KeyEvent.VK_DELETE)
					deleteSelectedVector();

			}
		});
		listVectorFiles.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() == 2) {
					Buffer buffer = new Buffer(1);
					buffer.putElem(0, vectorList.get(listVectorFiles.getSelectedIndex()));
					visualizar(buffer);
				}
			}
		});
		listVectorFiles.setModel(selectedVectorFilesModel);
		scrollPaneVectorFiles.setViewportView(listVectorFiles);

		// -- Procesamiento --//
		JTabbedPane tabbedPaneProcessing = new JTabbedPane(JTabbedPane.TOP);
		splitPane.setRightComponent(tabbedPaneProcessing);

		// ----- PRE PROCESAMIENTO PANEL
		JPanel panelPreProcessing = new JPanel();
		tabbedPaneProcessing.addTab("Pre-Procesamiento", null, panelPreProcessing, null);
		preProcessingFiltersModel = new DefaultComboBoxModel<ComboItemFilter>();
		panelPreProcessing.setLayout(new BorderLayout(0, 0));

		JPanel panelAddPreProcessingFilters = new JPanel();
		FlowLayout fl_panelAddPreProcessingFilters = (FlowLayout) panelAddPreProcessingFilters.getLayout();
		fl_panelAddPreProcessingFilters.setAlignOnBaseline(true);
		fl_panelAddPreProcessingFilters.setAlignment(FlowLayout.LEFT);
		panelPreProcessing.add(panelAddPreProcessingFilters, BorderLayout.NORTH);

		comboBoxPreProcessing = new JComboBox<ComboItemFilter>();
		panelAddPreProcessingFilters.add(comboBoxPreProcessing);
		comboBoxPreProcessing.setModel(preProcessingFiltersModel);

		JButton btnAddPreProcessingFilter = new JButton("Agregar");
		btnAddPreProcessingFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String filterName = ((ComboItemFilter) comboBoxPreProcessing.getSelectedItem()).getFilterName();
				String filterClass = ((ComboItemFilter) comboBoxPreProcessing.getSelectedItem()).getFilterClassName();
				FiltroPreProcesamiento filtroPreProcesamiento;
				try {
					filtroPreProcesamiento = ((FiltroPreProcesamiento) PluginFilterManager.getInstance().getFilterInstance(filterClass));
					FilterRowPanel<FiltroPreProcesamiento> newRowPanel = new FilterRowPanel<FiltroPreProcesamiento>(filtroPreProcesamiento, new SeleccionadorPares(), filterName, preProcessingFilterList, preProcessingSeleccionatorList);
					newRowPanel.insertRowIn(gridPreProcessingPanel);
				} catch (ManagerException e) {
					new GUIException(e).inform();
				}
			}
		});
		panelAddPreProcessingFilters.add(btnAddPreProcessingFilter);

		JScrollPane scrollPaneSelectedPreProcessingFilters = new JScrollPane();
		scrollPaneSelectedPreProcessingFilters.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panelPreProcessing.add(scrollPaneSelectedPreProcessingFilters, BorderLayout.CENTER);

		gridPreProcessingPanel = new JPanel();
		scrollPaneSelectedPreProcessingFilters.setViewportView(gridPreProcessingPanel);
		FormLayout fl_gridPreProcessingPanel = new FormLayout(new ColumnSpec[] { ColumnSpec.decode("0px:grow"), }, new RowSpec[] {});
		gridPreProcessingPanel.setLayout(fl_gridPreProcessingPanel);

		JPanel newRowPreProcessing = new JPanel();
		newRowPreProcessing.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		// gridPreProcessingPanel.add(newRowPreProcessing, "1, 1, fill, fill");
		newRowPreProcessing.setLayout(null);

		JLabel lblPreProcessingFiltername = new JLabel("FilterName");
		lblPreProcessingFiltername.setBounds(20, 27, 255, 14);
		newRowPreProcessing.add(lblPreProcessingFiltername);

		JButton btnPreProcessingDeleteFilter = new JButton("Borrar");
		btnPreProcessingDeleteFilter.setBounds(285, 23, 91, 23);
		newRowPreProcessing.add(btnPreProcessingDeleteFilter);

		JButton btnPreProcessingEditFilter = new JButton("Editar");
		btnPreProcessingEditFilter.setBounds(185, 23, 91, 23);
		newRowPreProcessing.add(btnPreProcessingEditFilter);

		JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
			}
		});
		comboBox.setBounds(400, 23, 29, 22);
		newRowPreProcessing.add(comboBox);

		// ----- PIV PROCESAMIENTO PANEL
		JPanel panelPIVProcessing = new JPanel();
		tabbedPaneProcessing.addTab("Procesamiento PIV", null, panelPIVProcessing, null);
		pivProcessingFiltersModel = new DefaultComboBoxModel<ComboItemFilter>();
		panelPIVProcessing.setLayout(new BorderLayout(0, 0));

		JPanel panelAddPIVProcessingFilters = new JPanel();
		FlowLayout fl_panelAddPIVProcessingFilters = (FlowLayout) panelAddPIVProcessingFilters.getLayout();
		fl_panelAddPIVProcessingFilters.setAlignOnBaseline(true);
		fl_panelAddPIVProcessingFilters.setAlignment(FlowLayout.LEFT);
		panelPIVProcessing.add(panelAddPIVProcessingFilters, BorderLayout.NORTH);

		comboBoxPIVProcessing = new JComboBox<ComboItemFilter>();
		panelAddPIVProcessingFilters.add(comboBoxPIVProcessing);
		comboBoxPIVProcessing.setModel(pivProcessingFiltersModel);

		JButton btnAddPIVProcessingFilter = new JButton("Agregar");
		btnAddPIVProcessingFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (pivProcessingFilterList.size() > 0)
						throw new GUIException("Solo puede existir un filtro PIV simultaneamente");

					String filterName = ((ComboItemFilter) comboBoxPIVProcessing.getSelectedItem()).getFilterName();
					String filterClass = ((ComboItemFilter) comboBoxPIVProcessing.getSelectedItem()).getFilterClassName();
					FiltroPIV filtroPIVProcesamiento;
					filtroPIVProcesamiento = ((FiltroPIV) PluginFilterManager.getInstance().getFilterInstance(filterClass));
					FilterRowPanel<FiltroPIV> newRowPanel = new FilterRowPanel<FiltroPIV>(filtroPIVProcesamiento, new SeleccionadorPares(), filterName, pivProcessingFilterList, pivProcessingSeleccionatorList);
					newRowPanel.insertRowIn(gridPIVProcessingPanel);
				} catch (ManagerException | GUIException e) {
					new GUIException(e).inform();
				}
			}
		});
		panelAddPIVProcessingFilters.add(btnAddPIVProcessingFilter);

		JScrollPane scrollPaneSelectedPIVProcessingFilters = new JScrollPane();
		scrollPaneSelectedPIVProcessingFilters.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panelPIVProcessing.add(scrollPaneSelectedPIVProcessingFilters, BorderLayout.CENTER);

		gridPIVProcessingPanel = new JPanel();
		scrollPaneSelectedPIVProcessingFilters.setViewportView(gridPIVProcessingPanel);
		FormLayout fl_gridPIVProcessingPanel = new FormLayout(new ColumnSpec[] { ColumnSpec.decode("0px:grow"), }, new RowSpec[] {});
		gridPIVProcessingPanel.setLayout(fl_gridPIVProcessingPanel);

		// ----- POST PROCESAMIENTO PANEL
		JPanel panelPostProcessing = new JPanel();
		tabbedPaneProcessing.addTab("Post-Procesamiento", null, panelPostProcessing, null);
		postProcessingFiltersModel = new DefaultComboBoxModel<ComboItemFilter>();
		panelPostProcessing.setLayout(new BorderLayout(0, 0));

		JPanel panelAddPostProcessingFilters = new JPanel();
		FlowLayout fl_panelAddPostProcessingFilters = (FlowLayout) panelAddPostProcessingFilters.getLayout();
		fl_panelAddPostProcessingFilters.setAlignOnBaseline(true);
		fl_panelAddPostProcessingFilters.setAlignment(FlowLayout.LEFT);
		panelPostProcessing.add(panelAddPostProcessingFilters, BorderLayout.NORTH);

		comboBoxPostProcessing = new JComboBox<ComboItemFilter>();
		panelAddPostProcessingFilters.add(comboBoxPostProcessing);
		comboBoxPostProcessing.setModel(postProcessingFiltersModel);

		JButton btnAddPostProcessingFilter = new JButton("Agregar");
		btnAddPostProcessingFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String filterName = ((ComboItemFilter) comboBoxPostProcessing.getSelectedItem()).getFilterName();
				String filterClass = ((ComboItemFilter) comboBoxPostProcessing.getSelectedItem()).getFilterClassName();
				FiltroPostProcesamiento filtroPostProcesamiento;
				try {
					filtroPostProcesamiento = ((FiltroPostProcesamiento) PluginFilterManager.getInstance().getFilterInstance(filterClass));
					FilterRowPanel<FiltroPostProcesamiento> newRowPanel = new FilterRowPanel<FiltroPostProcesamiento>(filtroPostProcesamiento, new SeleccionadorPares(), filterName, postProcessingFilterList, postProcessingSeleccionatorList);
					newRowPanel.insertRowIn(gridPostProcessingPanel);
				} catch (ManagerException e) {
					new GUIException(e).inform();
				}
			}
		});
		panelAddPostProcessingFilters.add(btnAddPostProcessingFilter);

		JScrollPane scrollPaneSelectedPostProcessingFilters = new JScrollPane();
		scrollPaneSelectedPostProcessingFilters.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panelPostProcessing.add(scrollPaneSelectedPostProcessingFilters, BorderLayout.CENTER);

		gridPostProcessingPanel = new JPanel();
		scrollPaneSelectedPostProcessingFilters.setViewportView(gridPostProcessingPanel);
		FormLayout fl_gridPostProcessingPanel = new FormLayout(new ColumnSpec[] { ColumnSpec.decode("0px:grow"), }, new RowSpec[] {});
		gridPostProcessingPanel.setLayout(fl_gridPostProcessingPanel);

		// ----- VISUALIZACION PANEL
		JPanel panelVisualization = new JPanel();
		tabbedPaneProcessing.addTab("Visualizacion", null, panelVisualization, null);
		visualizationFiltersModel = new DefaultComboBoxModel<ComboItemFilter>();
		panelVisualization.setLayout(new BorderLayout(0, 0));

		JPanel panelAddVisualizationFilters = new JPanel();
		FlowLayout fl_panelAddVisualizationFilters = (FlowLayout) panelAddVisualizationFilters.getLayout();
		fl_panelAddVisualizationFilters.setAlignOnBaseline(true);
		fl_panelAddVisualizationFilters.setAlignment(FlowLayout.LEFT);
		panelVisualization.add(panelAddVisualizationFilters, BorderLayout.NORTH);

		comboBoxVisualization = new JComboBox<ComboItemFilter>();
		panelAddVisualizationFilters.add(comboBoxVisualization);
		comboBoxVisualization.setModel(visualizationFiltersModel);

		JButton btnAddVisualizationFilter = new JButton("Agregar");
		btnAddVisualizationFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String filterName = ((ComboItemFilter) comboBoxVisualization.getSelectedItem()).getFilterName();
				String filterClass = ((ComboItemFilter) comboBoxVisualization.getSelectedItem()).getFilterClassName();
				FiltroVisualizacion filtroVisualizacion;
				try {
					filtroVisualizacion = ((FiltroVisualizacion) PluginFilterManager.getInstance().getFilterInstance(filterClass));
					FilterRowPanel<FiltroVisualizacion> newRowPanel = new FilterRowPanel<FiltroVisualizacion>(filtroVisualizacion, null, filterName, visualizationFilterList, null);
					newRowPanel.insertRowIn(gridVisualizationPanel);
				} catch (ManagerException e) {
					new GUIException(e).inform();
				}
			}
		});
		panelAddVisualizationFilters.add(btnAddVisualizationFilter);

		JScrollPane scrollPaneSelectedVisualizationFilters = new JScrollPane();
		scrollPaneSelectedVisualizationFilters.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panelVisualization.add(scrollPaneSelectedVisualizationFilters, BorderLayout.CENTER);

		gridVisualizationPanel = new JPanel();
		scrollPaneSelectedVisualizationFilters.setViewportView(gridVisualizationPanel);
		FormLayout fl_gridVisualizationPanel = new FormLayout(new ColumnSpec[] { ColumnSpec.decode("0px:grow"), }, new RowSpec[] {});
		gridVisualizationPanel.setLayout(fl_gridVisualizationPanel);

	}

	public List<FiltroPreProcesamiento> getPreProcessingFilterList() {
		return preProcessingFilterList;
	}

	public List<FiltroPIV> getPivProcessingFilterList() {
		return pivProcessingFilterList;
	}

	public List<FiltroPostProcesamiento> getPostProcessingFilterList() {
		return postProcessingFilterList;
	}

	private void loadFilters() throws ManagerException {
		PluginFilterManager fm = PluginFilterManager.getInstance();
		HashMap<String, String> preProcessingFilters = fm.getFiltrosPreProcesamiento();
		HashMap<String, String> pivProcessingFilters = fm.getFiltrosPIVProcesamiento();
		HashMap<String, String> postProcessingFilters = fm.getFiltrosPostProcesamiento();
		HashMap<String, String> visualizationFilters = fm.getFiltrosVisualizacion();

		for (String key : preProcessingFilters.keySet())
			preProcessingFiltersModel.addElement(new ComboItemFilter(key, preProcessingFilters.get(key)));

		for (String key : pivProcessingFilters.keySet())
			pivProcessingFiltersModel.addElement(new ComboItemFilter(key, pivProcessingFilters.get(key)));

		for (String key : postProcessingFilters.keySet())
			postProcessingFiltersModel.addElement(new ComboItemFilter(key, postProcessingFilters.get(key)));

		for (String key : visualizationFilters.keySet())
			visualizationFiltersModel.addElement(new ComboItemFilter(key, visualizationFilters.get(key)));
	}

	public static void restatApplication() {
		try {
			PluginFilterManager.reloadInstance();
			PIV.dispose();
			new PIVGui();
		} catch (ManagerException e) {
			new GUIException(e).inform();
		}
	}

	private List<ElementoProcesable> getSelectedImages() {
		List<ElementoProcesable> inputImage = new ArrayList<ElementoProcesable>();

		int[] elem = listImagesFiles.getSelectedIndices();
		for (int i : elem) {
			Imagen im = imagesList.get(i);
			inputImage.add(im);
		}

		return inputImage;
	}

	@SuppressWarnings("unused")
	private List<ElementoProcesable> getSelectedVector() {
		List<ElementoProcesable> inputImage = new ArrayList<ElementoProcesable>();

		int[] elem = listVectorFiles.getSelectedIndices();
		for (int i : elem) {
			MapaVectores im = vectorList.get(i);
			inputImage.add(im);
		}

		return inputImage;
	}

	private void deleteSelectedImages() {
		int[] elem = listImagesFiles.getSelectedIndices();
		for (int i = elem.length - 1; i >= 0; i--) {
			int j = elem[i];
			imagesList.remove(j);
			selectedImagesFilesModel.remove(j);
		}
	}

	private void deleteSelectedVector() {
		int[] elem = listVectorFiles.getSelectedIndices();
		for (int i = elem.length - 1; i >= 0; i--) {
			int j = elem[i];
			vectorList.remove(j);
			selectedVectorFilesModel.remove(j);
		}
	}

	private void doPreview() {
		new Thread() {
			public void run() {
				
				double initial = System.currentTimeMillis();
				Buffer inputImage = new Buffer(getSelectedImages());

				Procesador preProcesador = new PreProcesador(getPreProcessingFilterList(), preProcessingSeleccionatorList);
				List<Buffer> bufferList = new ArrayList<Buffer>();
				try {
					bufferList = preProcesador.procesarList(inputImage);
				} catch (FilterException e) {
					e.printStackTrace();
				}
				List<List<ElementoProcesable>> resultList = new ArrayList<List<ElementoProcesable>>();
				
				
				lblLoading.setVisible(true);
				bufferList.get(bufferList.size() - 1).esperarCompletado();
				lblLoading.setVisible(false);
				System.out.println(System.currentTimeMillis() - initial);

				for (Buffer buffer : bufferList)
					resultList.add((List<ElementoProcesable>) buffer.getBufferList());

				PreviewFrame previewFrame = new PreviewFrame(resultList);
				previewFrame.setVisible(true);
				previewFrame.pack();
			}
		}.start();

	}

	private Buffer doPIV() throws GUIException {
		try {
			if (getPivProcessingFilterList().size() == 0)
				throw new GUIException("Debe existir un filtro PIV");

			// DEFINIR DESPUES POR CONFIGURACION
			Buffer outputPre = null;
			Buffer outputPIV = null;
			Buffer outputPost = null;

			Buffer inputImage = new Buffer(getSelectedImages());

			Procesador preProcesador = new PreProcesador(getPreProcessingFilterList(), preProcessingSeleccionatorList);
			Procesador pivProcesador = new ProcesadorPIV(getPivProcessingFilterList().get(0), pivProcessingSeleccionatorList.get(0));
			Procesador postProcesador = new PostProcesador(getPostProcessingFilterList(), postProcessingSeleccionatorList);

			outputPre = preProcesador.procesar(inputImage);
			outputPIV = pivProcesador.procesar(outputPre);
			outputPost = postProcesador.procesar(outputPIV);

			// outputPost.isCompleted();

			return outputPost;

		} catch (FilterException | GUIException e) {
			throw new GUIException(e);
		}

	}

	private void procesarResultado() {
		new Thread() {
			public void run() {
				try {
					double initial = System.currentTimeMillis();
					Buffer results = doPIV();
					if (results.size() == 0)
						return;
					lblLoading.setVisible(true);
					results.esperarCompletado();
					lblLoading.setVisible(false);
					visualizar(results);
					System.out.println(System.currentTimeMillis() - initial);
					guardarResultados(results);					
				} catch (GUIException e) {
					e.inform();
				}				
			}
		}.start();
	}

	private void visualizar(Buffer visualizationBuffer) {
		for (ElementoProcesable vectorMap : visualizationBuffer.getBufferList())
			for (FiltroVisualizacion filter : visualizationFilterList)
				filter.visualizar(vectorMap);
	}

	private void guardarResultados(Buffer resultBuffer) {
		int dialogResult = JOptionPane.showConfirmDialog(null, "¿Desea guardar los resultados?");
		for (ElementoProcesable vectorMap : resultBuffer.getBufferList()) {
			if (dialogResult == JOptionPane.YES_OPTION) {
				int resultSave = guardar(vectorMap);
				if (resultSave == 1)
					continue;
			}
			addVectorList("sinNombre" + countVectorMap, (MapaVectores) vectorMap);
			countVectorMap++;
		}
	}

	private int guardar(ElementoProcesable vectorMap) {
		JFileChooser chooser = new JFileChooser();
		int retrival = chooser.showSaveDialog(null);
		if (retrival == JFileChooser.APPROVE_OPTION) {
			DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance(Locale.US);
			df.applyPattern("+0.0000E00;-0.0000E00");
			try {
				String selectedFileName = chooser.getSelectedFile().toString();
				if (StringUtils.isBlank(FileHandling.getExtension(chooser.getSelectedFile())))
					selectedFileName = selectedFileName + ".jvc";
				FileHandling.writeArrayToFile(((MapaVectores) vectorMap).getMapaVectores(), selectedFileName, df);
				addSelectedFile(new File(selectedFileName));
				return 1;
			} catch (IOException e) {
				new GUIException("Error al guardar el mapa de vectores", e).inform();
			}
		}
		return 0;
	}

}
