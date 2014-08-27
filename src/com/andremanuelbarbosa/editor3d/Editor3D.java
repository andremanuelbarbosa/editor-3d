package com.andremanuelbarbosa.editor3d;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;

public class Editor3D extends JFrame
{
    private static int number = 1;
    
    public static final String XY = "XY";
    public static final String XZ = "XZ";
    public static final String YZ = "YZ";
    
    private Tool tools[];
    
    public static final int MOVE_TOOL = 0;
    public static final int SELECT_TOOL = 1;
    public static final int BOX_TOOL = 2;
    public static final int PRISM_TOOL = 3;
    
    private int tool;
    
    private LinkedList solids;
    
    private JDesktopPane jDesktopPane;
    
    private JInternalFrame jInternalFrameXY;
    private JInternalFrame jInternalFrameXZ;
    private JInternalFrame jInternalFrameYZ;
    
    private MyJPanel myJPanelXY;
    private MyJPanel myJPanelXZ;
    private MyJPanel myJPanelYZ;
    
    private JPanel jPanelTools = new JPanel();
    private JPanel jPanelMousePosition = new JPanel();
    private JLabel jLabelMousePosition = new JLabel();
    private JTextField jTextFieldMousePosition = new JTextField();
    private JPanel jPanelSeparator1 = new JPanel();
    private JPanel jPanelObjects = new JPanel();
    private JToggleButton jToggleButtonSelect = new JToggleButton();
    private JToggleButton jToggleButtonBox = new JToggleButton();
    private JToggleButton jToggleButtonPrism = new JToggleButton();
    private JPanel jPanelSeparator10 = new JPanel();
    private JPanel jPanelInitialState = new JPanel();
    private JButton jButtonInitialState = new JButton();
    private JPanel jPanelSeparator2 = new JPanel();
    private JPanel jPanelSelection = new JPanel();
    private JLabel jLabelSelection = new JLabel();
    private JComboBox jComboBoxSelection = new JComboBox();
    private ItemListener jComboBoxSelectionIL;
    private JPanel jPanelAxes = new JPanel();
    private JCheckBox jCheckBoxAxes = new JCheckBox();
    private JPanel jPanelSeparator3 = new JPanel();
    private JPanel jPanelXYZReload = new JPanel();
    private JPanel jPanelXYZ = new JPanel();
    private JLabel jLabelX = new JLabel();
    private JLabel jLabelY = new JLabel();
    private JLabel jLabelZ = new JLabel();
    private JTextField jTextFieldX = new JTextField();
    private JTextField jTextFieldY = new JTextField();
    private JTextField jTextFieldZ = new JTextField();
    private JPanel jPanelReload = new JPanel();
    private JButton jButtonReload = new JButton();
    private JPanel jPanelSeparator4 = new JPanel();
    private JPanel jPanelTransformations = new JPanel();
    private JButton jButtonRotate = new JButton();
    private JButton jButtonTranslate = new JButton();
    private JButton jButtonScale = new JButton();
    private JPanel jPanelSeparator5 = new JPanel();
    private JPanel jPanelUtilsBorder = new JPanel();
    private JPanel jPanelUtils = new JPanel();
    private JButton jButtonUndo = new JButton();
    private JButton jButtonRedo = new JButton();
    private JButton jButtonDelete = new JButton();
    private JButton jButtonClear = new JButton();
    private JPanel jPanelSeparator6 = new JPanel();
    private JPanel jPanelAnimation = new JPanel();
    private JPanel jPanelAnimationTitle = new JPanel();
    private JLabel jLabelAnimationTitle = new JLabel();
    private JPanel jPanelSeparator7 = new JPanel();
    private JPanel jPanelAnimationCheckBoxs = new JPanel();
    private JCheckBox jCheckBoxX = new JCheckBox();
    private JCheckBox jCheckBoxY = new JCheckBox();
    private JCheckBox jCheckBoxZ = new JCheckBox();
    private JPanel jPanelSeparator8 = new JPanel();
    private JPanel jPanelSeparator9 = new JPanel();
    private JPanel jPanelAnimationRun = new JPanel();
    private JButton jButtonAnimationRun = new JButton();
    private JPanel jPanelInitialFrames = new JPanel();
    private JButton jButtonInitialFrames = new JButton();
    
    private static final int alpha = 2;
    
    private javax.swing.Timer animationTimer;
    private ActionListener animationTimerAL;
    
    private LinkedList animationSolids;
    
    public Editor3D()
    {
        super("Editor3D");
        
        init();
        initComponents();
        addListeners();
        
        Dimension screenSize = getToolkit().getScreenSize();
        
        int width = (int) (screenSize.width * 0.9);
        int height = (int) (screenSize.height * 0.9);
        
        setSize(width,height);
        setLocation((screenSize.width - width) / 2,(screenSize.height - height) / 2);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        animationTimerAL = new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                Transform3D t3D;
                
                for(ListIterator listItr = animationSolids.listIterator() ; listItr.hasNext() ; )
                {
                    Solid solid = (Solid) listItr.next();
                    
                    if(jCheckBoxX.isSelected())
                    {
                        t3D = new Transform3D();
                        t3D.rotateX(alpha);
                        
                        solid.applyTransform3D(t3D);
                    }

                    if(jCheckBoxY.isSelected())
                    {
                        t3D = new Transform3D();
                        t3D.rotateY(alpha);
                        
                        solid.applyTransform3D(t3D);
                    }

                    if(jCheckBoxZ.isSelected())
                    {
                        t3D = new Transform3D();
                        t3D.rotateZ(alpha);

                        solid.applyTransform3D(t3D);
                    }
                }
                
                paintRequest();
            }
        };
        
        animationSolids = new LinkedList();
    }
    
    private void init()
    {
        tools = new Tool[4];
        
        tools[0] = new MoveTool();
        tools[1] = new SelectTool();
        tools[2] = new BoxTool();
        tools[3] = new PrismTool();
        
        tool = MOVE_TOOL;
        
        solids = new LinkedList();
    }
    
    private void initComponents()
    {
        jDesktopPane = new JDesktopPane();
        
        jInternalFrameXY = new JInternalFrame("Vista de frente (XY)",true,false,true,true);
        jInternalFrameXY.setVisible(true);
        
        jInternalFrameXZ = new JInternalFrame("Vista de topo (XZ)",true,false,true,true);
        jInternalFrameXZ.setVisible(true);
        
        jInternalFrameYZ = new JInternalFrame("Vista lateral (YZ)",true,false,true,true);
        jInternalFrameYZ.setVisible(true);
        
        myJPanelXY = new MyJPanelXY(this,jInternalFrameXY);
        myJPanelXZ = new MyJPanelXZ(this,jInternalFrameXZ);
        myJPanelYZ = new MyJPanelYZ(this,jInternalFrameYZ);
        
        myJPanelXY.addTool(tools[MOVE_TOOL]);
        myJPanelXY.addTool(tools[SELECT_TOOL]);
        myJPanelXY.addTool(tools[BOX_TOOL]);
        myJPanelXY.addTool(tools[PRISM_TOOL]);
        
        jInternalFrameXY.getContentPane().add((JPanel) myJPanelXY,BorderLayout.CENTER);
        jInternalFrameXZ.getContentPane().add((JPanel) myJPanelXZ,BorderLayout.CENTER);
        jInternalFrameYZ.getContentPane().add((JPanel) myJPanelYZ,BorderLayout.CENTER);
        
        jDesktopPane.add(jInternalFrameXY,JLayeredPane.DEFAULT_LAYER);
        jDesktopPane.add(jInternalFrameXZ,JLayeredPane.DEFAULT_LAYER);
        jDesktopPane.add(jInternalFrameYZ,JLayeredPane.DEFAULT_LAYER);
        
        getContentPane().add(jDesktopPane,BorderLayout.CENTER);

        String path = "/Editor3D/icons/";
        
        GridBagConstraints gridBagConstraints;
        
        jPanelTools.setLayout(new GridBagLayout());
        jPanelTools.setBackground(new Color(204,204,204));
        jPanelTools.setPreferredSize(new Dimension(160,10));
        jPanelTools.setBorder(new EtchedBorder());
        
        jPanelMousePosition.setLayout(new GridBagLayout());
        jPanelMousePosition.setBackground(new Color(204,204,204));
        jPanelMousePosition.setPreferredSize(new Dimension(150,40));
	jPanelMousePosition.setBorder(new EtchedBorder());
        jLabelMousePosition.setText("XYZ");
        jLabelMousePosition.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelMousePosition.setPreferredSize(new Dimension(30,20));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanelMousePosition.add(jLabelMousePosition,gridBagConstraints);
        jTextFieldMousePosition.setText("");
        jTextFieldMousePosition.setToolTipText("Posi��o do rato em cada janela");
        jTextFieldMousePosition.setHorizontalAlignment(SwingConstants.RIGHT);
        jTextFieldMousePosition.setPreferredSize(new Dimension(80,20));
        jTextFieldMousePosition.setEnabled(false);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanelMousePosition.add(jTextFieldMousePosition,gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanelTools.add(jPanelMousePosition,gridBagConstraints);
        
        jPanelSeparator1.setBackground(new Color(204,204,204));
        jPanelSeparator1.setPreferredSize(new Dimension(150,10));
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        jPanelTools.add(jPanelSeparator1,gridBagConstraints);
        
        jPanelObjects.setLayout(new GridBagLayout());
        jPanelObjects.setBackground(new Color(204,204,204));
        jPanelObjects.setPreferredSize(new Dimension(150,50));
	jPanelObjects.setBorder(new EtchedBorder());
        jToggleButtonSelect.setText("");
        jToggleButtonSelect.setToolTipText("Seleccionar objecto(s)");
        jToggleButtonSelect.setPreferredSize(new Dimension(40,40));
        jToggleButtonSelect.setIcon(new ImageIcon(getClass().getResource(path + "select.gif")));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanelObjects.add(jToggleButtonSelect,gridBagConstraints);
        jToggleButtonBox.setText("");
        jToggleButtonBox.setToolTipText("Desenhar um paralelip�pedo");
        jToggleButtonBox.setPreferredSize(new Dimension(40,40));
        jToggleButtonBox.setIcon(new ImageIcon(getClass().getResource(path + "box.gif")));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanelObjects.add(jToggleButtonBox,gridBagConstraints);
        jToggleButtonPrism.setText("");
        jToggleButtonPrism.setToolTipText("Desenhar um prisma");
        jToggleButtonPrism.setPreferredSize(new Dimension(40,40));
        jToggleButtonPrism.setIcon(new ImageIcon(getClass().getResource(path + "prism.gif")));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        jPanelObjects.add(jToggleButtonPrism,gridBagConstraints);        
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        jPanelTools.add(jPanelObjects,gridBagConstraints);

        jPanelSeparator10.setBackground(new Color(204,204,204));
        jPanelSeparator10.setPreferredSize(new Dimension(150,10));

	gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        jPanelTools.add(jPanelSeparator10,gridBagConstraints);

        jPanelInitialState.setLayout(new GridBagLayout());
        jPanelInitialState.setBackground(new Color(204,204,204));
        jPanelInitialState.setPreferredSize(new Dimension(150,20));
        jButtonInitialState.setText("Estado Inicial");
        jButtonInitialState.setToolTipText("Rep�e o estado inicial do(s) objecto(s) seleccionado(s)");
        jButtonInitialState.setPreferredSize(new Dimension(150,20));
        jButtonInitialState.setHorizontalAlignment(SwingConstants.CENTER);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanelInitialState.add(jButtonInitialState,gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        jPanelTools.add(jPanelInitialState,gridBagConstraints);
        
        jPanelSeparator2.setBackground(new Color(204,204,204));
        jPanelSeparator2.setPreferredSize(new Dimension(150,10));
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        jPanelTools.add(jPanelSeparator2,gridBagConstraints);
        
        jPanelSelection.setLayout(new GridBagLayout());
        jPanelSelection.setBackground(new Color(204,204,204));
        jPanelSelection.setPreferredSize(new Dimension(150,30));
	jPanelSelection.setBorder(new EtchedBorder());
        jLabelSelection.setText("S�lido");
        jLabelSelection.setPreferredSize(new Dimension(80,20));
        jLabelSelection.setHorizontalAlignment(SwingConstants.CENTER);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanelSelection.add(jLabelSelection,gridBagConstraints);
        jComboBoxSelection.setPreferredSize(new Dimension(40,20));
        jComboBoxSelection.setToolTipText("Seleccionar objecto atrav�s do n�mero correspondente");
        jComboBoxSelection.setMaximumRowCount(10);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanelSelection.add(jComboBoxSelection,gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        jPanelTools.add(jPanelSelection,gridBagConstraints);
        
        jPanelSeparator9.setBackground(new Color(204,204,204));
        jPanelSeparator9.setPreferredSize(new Dimension(150,10));
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        jPanelTools.add(jPanelSeparator9,gridBagConstraints);
        
        jPanelAxes.setLayout(new GridBagLayout());
        jPanelAxes.setBackground(new Color(204,204,204));
        jPanelAxes.setPreferredSize(new Dimension(150,30));
	jPanelAxes.setBorder(new EtchedBorder());
        jCheckBoxAxes.setText("Eixos");
        jCheckBoxAxes.setToolTipText("Activa/Desactiva a visualiza��o dos eixos");
        jCheckBoxAxes.setPreferredSize(new Dimension(60,20));
        jCheckBoxAxes.setSelected(true);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanelAxes.add(jCheckBoxAxes,gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        jPanelTools.add(jPanelAxes,gridBagConstraints);
        
        jPanelSeparator3.setBackground(new Color(204,204,204));
        jPanelSeparator3.setPreferredSize(new Dimension(150,10));
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        jPanelTools.add(jPanelSeparator3,gridBagConstraints);
        
	jPanelXYZReload.setLayout(new GridBagLayout());
	jPanelXYZReload.setBackground(new Color(204,204,204));
        jPanelXYZReload.setPreferredSize(new Dimension(150,70));
	jPanelXYZReload.setBorder(new EtchedBorder());

        jPanelXYZ.setLayout(new GridBagLayout());
        jPanelXYZ.setBackground(new Color(204,204,204));
        jPanelXYZ.setPreferredSize(new Dimension(140,40));
        jLabelX.setText("X");
        jLabelX.setPreferredSize(new Dimension(30,20));
        jLabelX.setHorizontalAlignment(SwingConstants.CENTER);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanelXYZ.add(jLabelX,gridBagConstraints);
        jLabelY.setText("Y");
        jLabelY.setPreferredSize(new Dimension(30,20));
        jLabelY.setHorizontalAlignment(SwingConstants.CENTER);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanelXYZ.add(jLabelY,gridBagConstraints);
        jLabelZ.setText("Z");
        jLabelZ.setPreferredSize(new Dimension(30,20));
        jLabelZ.setHorizontalAlignment(SwingConstants.CENTER);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        jPanelXYZ.add(jLabelZ,gridBagConstraints);
        jTextFieldX.setText("0");
        jTextFieldX.setToolTipText("Valor de X a transformar");
        jTextFieldX.setPreferredSize(new Dimension(30,20));
        jTextFieldX.setHorizontalAlignment(SwingConstants.RIGHT);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        jPanelXYZ.add(jTextFieldX,gridBagConstraints);
        jTextFieldY.setText("0");
        jTextFieldY.setToolTipText("Valor de Y a transformar");
        jTextFieldY.setPreferredSize(new Dimension(30,20));
        jTextFieldY.setHorizontalAlignment(SwingConstants.RIGHT);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        jPanelXYZ.add(jTextFieldY,gridBagConstraints);
        jTextFieldZ.setText("0");
        jTextFieldZ.setToolTipText("Valor de Z a transformar");
        jTextFieldZ.setPreferredSize(new Dimension(30,20));
        jTextFieldZ.setHorizontalAlignment(SwingConstants.RIGHT);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        jPanelXYZ.add(jTextFieldZ,gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanelXYZReload.add(jPanelXYZ,gridBagConstraints);
        
        jPanelReload.setLayout(new GridBagLayout());
        jPanelReload.setBackground(new Color(204,204,204));
        jPanelReload.setPreferredSize(new Dimension(140,20));
        jButtonReload.setText("Repor");
        jButtonReload.setToolTipText("Rep�e os valores de X,Y e Z a zero");
        jButtonReload.setPreferredSize(new Dimension(90,20));
        jButtonReload.setHorizontalAlignment(SwingConstants.CENTER);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanelReload.add(jButtonReload,gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        jPanelXYZReload.add(jPanelReload,gridBagConstraints);
        
	gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        jPanelTools.add(jPanelXYZReload,gridBagConstraints);

        jPanelSeparator4.setBackground(new Color(204,204,204));
        jPanelSeparator4.setPreferredSize(new Dimension(150,10));
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        jPanelTools.add(jPanelSeparator4,gridBagConstraints);
        
        jPanelTransformations.setLayout(new GridBagLayout());
        jPanelTransformations.setBackground(new Color(204,204,204));
        jPanelTransformations.setPreferredSize(new Dimension(150,50));
	jPanelTransformations.setBorder(new EtchedBorder());
        jButtonRotate.setText("");
        jButtonRotate.setToolTipText("Roda o(s) objecto(s) seleccionado(s)");
        jButtonRotate.setPreferredSize(new Dimension(40,40));
        jButtonRotate.setIcon(new ImageIcon(getClass().getResource(path + "rotate.gif")));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanelTransformations.add(jButtonRotate,gridBagConstraints);
        jButtonTranslate.setText("");
        jButtonTranslate.setToolTipText("Translada o(s) objecto(s) seleccionado(s)");
        jButtonTranslate.setPreferredSize(new Dimension(40,40));
        jButtonTranslate.setIcon(new ImageIcon(getClass().getResource(path + "translate.gif")));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanelTransformations.add(jButtonTranslate,gridBagConstraints);
        jButtonScale.setText("");
        jButtonScale.setToolTipText("Escala o(s) objecto(s) seleccionado(s)");
        jButtonScale.setPreferredSize(new Dimension(40,40));
        jButtonScale.setIcon(new ImageIcon(getClass().getResource(path + "scale.gif")));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        jPanelTransformations.add(jButtonScale,gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        jPanelTools.add(jPanelTransformations,gridBagConstraints);
        
        jPanelSeparator5.setBackground(new Color(204,204,204));
        jPanelSeparator5.setPreferredSize(new Dimension(150,10));
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        jPanelTools.add(jPanelSeparator5,gridBagConstraints);
        
	jPanelUtilsBorder.setLayout(new GridBagLayout());
        jPanelUtilsBorder.setBackground(new Color(204,204,204));
        jPanelUtilsBorder.setPreferredSize(new Dimension(150,54));
	jPanelUtilsBorder.setBorder(new EtchedBorder());

        jPanelUtils.setLayout(new GridBagLayout());
        jPanelUtils.setBackground(new Color(204,204,204));
        jPanelUtils.setPreferredSize(new Dimension(140,44));
        jButtonUndo.setText("");
        jButtonUndo.setToolTipText("Desfaz a �ltima transforma��o");
        jButtonUndo.setPreferredSize(new Dimension(22,22));
        jButtonUndo.setIcon(new ImageIcon(getClass().getResource(path + "undo.gif")));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanelUtils.add(jButtonUndo,gridBagConstraints);
        jButtonRedo.setText("");
        jButtonRedo.setToolTipText("Refaz a �ltima transforma��o");
        jButtonRedo.setPreferredSize(new Dimension(22,22));
        jButtonRedo.setIcon(new ImageIcon(getClass().getResource(path + "redo.gif")));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanelUtils.add(jButtonRedo,gridBagConstraints);
        jButtonDelete.setText("");
        jButtonDelete.setToolTipText("Apaga o(s) objecto(s) seleccionado(s)");
        jButtonDelete.setPreferredSize(new Dimension(22,22));
        jButtonDelete.setIcon(new ImageIcon(getClass().getResource(path + "delete.gif")));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        jPanelUtils.add(jButtonDelete,gridBagConstraints);
        jButtonClear.setText("");
        jButtonClear.setToolTipText("Apaga todos os objectos");
        jButtonClear.setPreferredSize(new Dimension(22,22));
        jButtonClear.setIcon(new ImageIcon(getClass().getResource(path + "clear.gif")));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        jPanelUtils.add(jButtonClear,gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanelUtilsBorder.add(jPanelUtils,gridBagConstraints);
        
	gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        jPanelTools.add(jPanelUtilsBorder,gridBagConstraints);

        jPanelSeparator6.setBackground(new Color(204,204,204));
        jPanelSeparator6.setPreferredSize(new Dimension(150,10));
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        jPanelTools.add(jPanelSeparator6,gridBagConstraints);
        
        jPanelAnimation.setLayout(new GridBagLayout());
        jPanelAnimation.setBackground(new Color(204,204,204));
        jPanelAnimation.setPreferredSize(new Dimension(150,90));
        jPanelAnimation.setBorder(new EtchedBorder());
        
        jPanelAnimationTitle.setLayout(new GridBagLayout());
        jPanelAnimationTitle.setBackground(new Color(204,204,204));
        jPanelAnimationTitle.setPreferredSize(new Dimension(140,20));
        jLabelAnimationTitle.setText("Anima��o");
        jLabelAnimationTitle.setPreferredSize(new Dimension(120,20));
        jLabelAnimationTitle.setHorizontalAlignment(SwingConstants.CENTER);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanelAnimationTitle.add(jLabelAnimationTitle,gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanelAnimation.add(jPanelAnimationTitle,gridBagConstraints);
        
        jPanelSeparator7.setLayout(new GridBagLayout());
        jPanelSeparator7.setBackground(new Color(204,204,204));
        jPanelSeparator7.setPreferredSize(new Dimension(140,10));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        jPanelAnimation.add(jPanelSeparator7,gridBagConstraints);
        
        jPanelAnimationCheckBoxs.setLayout(new GridBagLayout());
        jPanelAnimationCheckBoxs.setBackground(new Color(204,204,204));
        jPanelAnimationCheckBoxs.setPreferredSize(new Dimension(140,20));
        jCheckBoxX.setText("X");
        jCheckBoxX.setToolTipText("Activa/Desactiva a transforma��o de X na anima��o");
        jCheckBoxX.setPreferredSize(new Dimension(40,20));
        jCheckBoxX.setHorizontalAlignment(SwingConstants.CENTER);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanelAnimationCheckBoxs.add(jCheckBoxX,gridBagConstraints);
        jCheckBoxY.setText("Y");
        jCheckBoxY.setToolTipText("Activa/Desactiva a transforma��o de Y na anima��o");
        jCheckBoxY.setPreferredSize(new Dimension(40,20));
        jCheckBoxY.setHorizontalAlignment(SwingConstants.CENTER);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanelAnimationCheckBoxs.add(jCheckBoxY,gridBagConstraints);
        jCheckBoxZ.setText("Z");
        jCheckBoxX.setToolTipText("Activa/Desactiva a transforma��o de Z na anima��o");
        jCheckBoxZ.setPreferredSize(new Dimension(40,20));
        jCheckBoxZ.setHorizontalAlignment(SwingConstants.CENTER);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        jPanelAnimationCheckBoxs.add(jCheckBoxZ,gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        jPanelAnimation.add(jPanelAnimationCheckBoxs,gridBagConstraints);
        
        jPanelSeparator8.setLayout(new GridBagLayout());
        jPanelSeparator8.setBackground(new Color(204,204,204));
        jPanelSeparator8.setPreferredSize(new Dimension(140,10));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        jPanelAnimation.add(jPanelSeparator8,gridBagConstraints);
        
        jPanelAnimationRun.setLayout(new GridBagLayout());
        jPanelAnimationRun.setBackground(new Color(204,204,204));
        jPanelAnimationRun.setPreferredSize(new Dimension(140,20));
        jButtonAnimationRun.setText("Animar!");
        jButtonAnimationRun.setToolTipText("Come�a/Acaba a anima��o");
        jButtonAnimationRun.setPreferredSize(new Dimension(110,20));
        jButtonAnimationRun.setHorizontalAlignment(SwingConstants.CENTER);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanelAnimationRun.add(jButtonAnimationRun,gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        jPanelAnimation.add(jPanelAnimationRun,gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        jPanelTools.add(jPanelAnimation,gridBagConstraints);

        jPanelInitialFrames.setLayout(new GridBagLayout());
        jPanelInitialFrames.setBackground(new Color(204,204,204));
        jPanelInitialFrames.setPreferredSize(new Dimension(150,40));
        jButtonInitialFrames.setText("Janelas Iniciais");
        jButtonInitialFrames.setToolTipText("Rep�e as janelas no seu estado inicial");
        jButtonInitialFrames.setPreferredSize(new Dimension(150,20));
        jButtonInitialFrames.setHorizontalAlignment(SwingConstants.CENTER);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanelInitialFrames.add(jButtonInitialFrames,gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 17;
        jPanelTools.add(jPanelInitialFrames,gridBagConstraints);
        
        getContentPane().add(jPanelTools,BorderLayout.WEST);
    }
    
    private void addListeners()
    {
        jToggleButtonSelect.addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent e)
            {
                if(!jToggleButtonSelect.isSelected())
                {
                    jToggleButtonBox.setSelected(false);
                    jToggleButtonPrism.setSelected(false);
                    
                    tool = SELECT_TOOL;
                }
                else
                {
                    tool = MOVE_TOOL;
                }
            }
        });
        
        jToggleButtonBox.addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent e)
            {
                if(!jToggleButtonBox.isSelected())
                {
                    jToggleButtonSelect.setSelected(false);
                    jToggleButtonPrism.setSelected(false);
                    
                    tool = BOX_TOOL;
                }
                else
                {
                    tool = MOVE_TOOL;
                }
            }
        });
        
        jToggleButtonPrism.addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent e)
            {
                if(!jToggleButtonPrism.isSelected())
                {
                    jToggleButtonSelect.setSelected(false);
                    jToggleButtonBox.setSelected(false);
                    
                    tool = PRISM_TOOL;
                }
                else
                {
                    tool = MOVE_TOOL;
                }
            }
        });
        
        jButtonInitialState.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if(animationTimer != null)
                {
                    JOptionPane.showMessageDialog(null,"Tem de parar a anima��o que se encontra em execu��o","",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                for(ListIterator listItr = solids.listIterator() ; listItr.hasNext() ; )
                {
                    Solid solid = (Solid) listItr.next();
                    
                    if(solid.isSelected()) solid.initialState();
                }
                
                paintRequest();
            }
        });
        
        jComboBoxSelectionIL = new ItemListener()
        {
            public void itemStateChanged(ItemEvent e)
            {
                int i = jComboBoxSelection.getSelectedIndex() + 1;
                
                for(ListIterator listItr = solids.listIterator() ; listItr.hasNext() ; )
                {
                    Solid solid = (Solid) listItr.next();
                    
                    if(solid.getNumber() == i) solid.setSelected(true);
                    else solid.setSelected(false);
                }
                
                paintRequest();
            }
        };
        
        jComboBoxSelection.addItemListener(jComboBoxSelectionIL);
        
        jCheckBoxAxes.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                myJPanelXY.setAxis(jCheckBoxAxes.isSelected());
                myJPanelXZ.setAxis(jCheckBoxAxes.isSelected());
                myJPanelYZ.setAxis(jCheckBoxAxes.isSelected());
                
                paintRequest();
            }
        });
        
        jButtonReload.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                jTextFieldX.setText("0");
                jTextFieldY.setText("0");
                jTextFieldZ.setText("0");
            }
        });
        
        jButtonRotate.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if(animationTimer != null)
                {
                    JOptionPane.showMessageDialog(null,"Tem de parar a anima��o que se encontra em execu��o","",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                float alphaX;
                float alphaY;
                float alphaZ;
                
                Transform3D t3D;
                
                try
                {
                    alphaX = Float.parseFloat(jTextFieldX.getText());
                    
                    try
                    {
                        alphaY = Float.parseFloat(jTextFieldY.getText());
                        
                        try
                        {
                            alphaZ = Float.parseFloat(jTextFieldZ.getText());
                        }
                        catch(NumberFormatException e3)
                        {
                            JOptionPane.showMessageDialog(null,"O valor que introduziu para Z n�o � v�lido.","Valor incorrecto!",JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                    catch(NumberFormatException e2)
                    {
                        JOptionPane.showMessageDialog(null,"O valor que introduziu para Y n�o � v�lido.","Valor incorrecto!",JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                catch(NumberFormatException e1)
                {
                    JOptionPane.showMessageDialog(null,"O valor que introduziu para X n�o � v�lido.","Valor incorrecto!",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                for(ListIterator listItr = solids.listIterator() ; listItr.hasNext() ; )
                {
                    Solid solid = (Solid) listItr.next();
                    
                    if(solid.isSelected())
                    {
                        if(alphaX != 0)
                        {
                            t3D = new Transform3D();
                            t3D.rotateX(alphaX);
                            
                            solid.applyTransform3D(t3D);
                        }
                        
                        if(alphaY != 0)
                        {
                            t3D = new Transform3D();
                            t3D.rotateY(alphaY);
                            
                            solid.applyTransform3D(t3D);
                        }
                        
                        if(alphaZ != 0)
                        {
                            t3D = new Transform3D();
                            t3D.rotateZ(alphaZ);
                            
                            solid.applyTransform3D(t3D);
                        }
                    }
                }
                
                paintRequest();
            }
        });
        
        jButtonTranslate.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if(animationTimer != null)
                {
                    JOptionPane.showMessageDialog(null,"Tem de parar a anima��o que se encontra em execu��o","",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                float dx;
                float dy;
                float dz;
                
                Transform3D t3D;
                
                try
                {
                    dx = Float.parseFloat(jTextFieldX.getText());
                    
                    try
                    {
                        dy = Float.parseFloat(jTextFieldY.getText());
                        
                        try
                        {
                            dz = Float.parseFloat(jTextFieldZ.getText());
                            
                            for(ListIterator listItr = solids.listIterator() ; listItr.hasNext() ; )
                            {
                                Solid solid = (Solid) listItr.next();
                                
                                if(solid.isSelected())
                                {
                                    t3D = new Transform3D();
                                    t3D.translate(dx,dy,dz);
                                    
                                    solid.applyTransform3D(t3D);
                                }
                            }
                            
                            paintRequest();
                        }
                        catch(NumberFormatException e3)
                        {
                            JOptionPane.showMessageDialog(null,"O valor que introduziu para Z n�o � v�lido.","Valor incorrecto!",JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                    catch(NumberFormatException e2)
                    {
                        JOptionPane.showMessageDialog(null,"O valor que introduziu para Y n�o � v�lido.","Valor incorrecto!",JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                catch(NumberFormatException e1)
                {
                    JOptionPane.showMessageDialog(null,"O valor que introduziu para X n�o � v�lido.","Valor incorrecto!",JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        });
        
        jButtonScale.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if(animationTimer != null)
                {
                    JOptionPane.showMessageDialog(null,"Tem de parar a anima��o que se encontra em execu��o","",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                float sx;
                float sy;
                float sz;
                
                Transform3D t3D;
                
                try
                {
                    sx = Float.parseFloat(jTextFieldX.getText());
                    
                    try
                    {
                        sy = Float.parseFloat(jTextFieldY.getText());
                        
                        try
                        {
                            sz = Float.parseFloat(jTextFieldZ.getText());
                            
                            if(sx == 0) sx = 1;
                            if(sy == 0) sy = 1;
                            if(sz == 0) sz = 1;
                            
                            for(ListIterator listItr = solids.listIterator() ; listItr.hasNext() ; )
                            {
                                Solid solid = (Solid) listItr.next();
                                
                                if(solid.isSelected())
                                {
                                    t3D = new Transform3D();
                                    t3D.scale(sx,sy,sz);
                                    
                                    solid.applyTransform3D(t3D);
                                }
                            }
                            
                            paintRequest();
                        }
                        catch(NumberFormatException e3)
                        {
                            JOptionPane.showMessageDialog(null,"O valor que introduziu para Z n�o � v�lido.","Valor incorrecto!",JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                    catch(NumberFormatException e2)
                    {
                        JOptionPane.showMessageDialog(null,"O valor que introduziu para Y n�o � v�lido.","Valor incorrecto!",JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                catch(NumberFormatException e1)
                {
                    JOptionPane.showMessageDialog(null,"O valor que introduziu para X n�o � v�lido.","Valor incorrecto!",JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        });
        
        jButtonUndo.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if(animationTimer != null)
                {
                    JOptionPane.showMessageDialog(null,"Tem de parar a anima��o que se encontra em execu��o","",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                for(ListIterator listItr = solids.listIterator() ; listItr.hasNext() ; )
                {
                    Solid solid = (Solid) listItr.next();
                    
                    if(solid.isSelected()) solid.undo();
                }
                
                paintRequest();
            }
        });
        
        jButtonRedo.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if(animationTimer != null)
                {
                    JOptionPane.showMessageDialog(null,"Tem de parar a anima��o que se encontra em execu��o","",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                for(ListIterator listItr = solids.listIterator() ; listItr.hasNext() ; )
                {
                    Solid solid = (Solid) listItr.next();
                    
                    if(solid.isSelected()) solid.redo();
                }
                
                paintRequest();
            }
        });
        
        jButtonDelete.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if(animationTimer != null)
                {
                    JOptionPane.showMessageDialog(null,"Tem de parar a anima��o que se encontra em execu��o","",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                number = 1;
                
                for(ListIterator listItr = solids.listIterator() ; listItr.hasNext() ; )
                {
                    Solid solid = (Solid) listItr.next();
                    
                    if(solid.isSelected()) listItr.remove();
                    else solid.setNumber(number++);
                }
                
                jComboBoxSelection.removeItemListener(jComboBoxSelectionIL);
                jComboBoxSelection.removeAllItems();

                for(int j = 1 ; j < number ; j++)
                    jComboBoxSelection.addItem("" + j);

                jComboBoxSelection.setSelectedIndex(-1);
                jComboBoxSelection.addItemListener(jComboBoxSelectionIL);
                
                paintRequest();
            }
        });
        
        jButtonClear.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if(animationTimer != null)
                {
                    JOptionPane.showMessageDialog(null,"Tem de parar a anima��o que se encontra em execu��o","",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                solids.clear();
                
                number = 1;
                jComboBoxSelection.removeAllItems();
                
                paintRequest();
            }
        });
        
        jTextFieldX.addFocusListener(new FocusListener()
        {
            public void focusLost(FocusEvent e)
            {
            }
            
            public void focusGained(FocusEvent e)
            {
                jTextFieldX.selectAll();
            }
        });
        
        jTextFieldY.addFocusListener(new FocusListener()
        {
            public void focusLost(FocusEvent e)
            {
            }
            
            public void focusGained(FocusEvent e)
            {
                jTextFieldY.selectAll();
            }
        });
        
        jTextFieldZ.addFocusListener(new FocusListener()
        {
            public void focusLost(FocusEvent e)
            {
            }
            
            public void focusGained(FocusEvent e)
            {
                jTextFieldZ.selectAll();
            }
        });
        
        jButtonAnimationRun.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if(animationTimer == null)
                {
                    animationSolids.clear();
                    
                    for(ListIterator listItr = solids.listIterator() ; listItr.hasNext() ; )
                    {
                        Solid solid = (Solid) listItr.next();
                        
                        if(solid.isSelected()) animationSolids.addLast(solid);
                    }
                    
                    if(animationSolids.isEmpty())
                    {
                        JOptionPane.showMessageDialog(null,"Pelo menos 1 objecto tem de estar seleccionado.","",JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    myJPanelXY.listeners(false);
                    myJPanelXZ.listeners(false);
                    myJPanelYZ.listeners(false);
                    
                    jButtonAnimationRun.setText("Parar");
                    
                    animationTimer = new javax.swing.Timer(10,animationTimerAL);
                    animationTimer.start();
                }
                else
                {
                    animationTimer.stop();
                    animationTimer = null;
                    
                    myJPanelXY.listeners(true);
                    myJPanelXZ.listeners(true);
                    myJPanelYZ.listeners(true);
                    
                    jButtonAnimationRun.setText("Animar!");
                    
                    for(ListIterator listItr = animationSolids.listIterator() ; listItr.hasNext() ; )
                        ((Solid) listItr.next()).clearHistory();
                }
            }
        });
        
        jButtonInitialFrames.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    jInternalFrameXY.setIcon(false);
                    jInternalFrameXZ.setIcon(false);
                    jInternalFrameYZ.setIcon(false);
                    
                    jInternalFrameXY.setMaximum(false);
                    jInternalFrameXZ.setMaximum(false);
                    jInternalFrameYZ.setMaximum(false);
                    
                    initialMode();
                }
                catch(Exception e1)
                {
                }
            }
        });
    }
    
    public void show()
    {
        super.show();
        
        initialMode();
    }
    
    private void initialMode()
    {
        int i = 10;
        int j = 5;
        
        Dimension size = jDesktopPane.getSize();
        
        int width = (size.width - 2 * i - j) / 2;
        int height = (size.height - 2 * i - j) / 2;
        
        jInternalFrameXY.setSize(width,height);
        jInternalFrameXZ.setSize(width,height);
        jInternalFrameYZ.setSize(width,height);
        
        jInternalFrameXY.setLocation(i,i);
        jInternalFrameXZ.setLocation(i + width + j,i);
        jInternalFrameYZ.setLocation(i + width - width / 2,i + height + j);
    }
    
    public void paintRequest()
    {
        ((JPanel) myJPanelXY).repaint();
        ((JPanel) myJPanelXZ).repaint();
        ((JPanel) myJPanelYZ).repaint();
    }
    
    public void setMousePosition(String str)
    {
        jLabelMousePosition.setText(str);
    }
    
    public void setMousePosition(int x,int y)
    {
        jTextFieldMousePosition.setText(x + ", " + y);
    }
    
    public Tool getSelectedTool()
    {
        return tools[tool];
    }
    
    public LinkedList getSolids()
    {
        return solids;
    }
    
    public int updateNumbers()
    {
        int i = number;
        
        jComboBoxSelection.removeItemListener(jComboBoxSelectionIL);
        jComboBoxSelection.removeAllItems();
        
        for(int j = 1 ; j <= i ; j++)
            jComboBoxSelection.addItem("" + j);
        
        jComboBoxSelection.setSelectedIndex(-1);
        jComboBoxSelection.addItemListener(jComboBoxSelectionIL);
        
        number++;
        
        return i;
    }
    
    public static void main(String args[])
    {
        new Editor3D().show();
    }
}