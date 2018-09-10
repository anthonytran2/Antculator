//Simple Calculator VR 1.0.

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.awt.event.KeyEvent;

public class Calculator extends Expr implements ActionListener, KeyListener {
   private TextField txt = new TextField();           //GUI.
   private String display = "";                       //Text sent to screen.
   private String numRange = "0123456789";            //Numbers.
   private String operator = "*/+-";                  //Operators.
   private boolean opIndicator = false;               //Prevents incorrect operator input.
   private boolean valid = true;                      //Indicator for valid input.
   private int changecount = 0;

   //Main.
   public static void main(String[] args) {
      Calculator c = new Calculator();
   }

   //Calculator GUI
   public Calculator() {
      JFrame frame = new JFrame();
      frame.setResizable(false);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(new Dimension(320, 300));
      frame.setTitle("Calculator");
      frame.setLayout(new BorderLayout());
      frame.setLocationRelativeTo(null);
      //North
      JPanel northPanel = new JPanel(new GridLayout(3, 2));
      northPanel.add(new JLabel("ANTHONY'S CALCULATOR", SwingConstants.CENTER));
      txt.addKeyListener(this);
      northPanel.add(txt);
      //West
      JPanel westPanel = new JPanel(new GridLayout(4,1));
      buttonCreate("BKSP", westPanel);
      buttonCreate("CE", westPanel);
      buttonCreate("C", westPanel);
      for(int i = 7; i <= 9; i++) {
         buttonCreate("" + i , westPanel);
      }
      for(int i = 4; i <= 6; i++) {
         buttonCreate("" + i , westPanel);
      }
      for(int i = 1; i <= 3; i++) {
         buttonCreate("" + i , westPanel);
      }
      //East
      JPanel eastPanel = new JPanel(new GridLayout(4,0));
      buttonCreate("-" , eastPanel);
      buttonCreate("1/x" , eastPanel);
      buttonCreate("*" , eastPanel);
      buttonCreate("%" , eastPanel);
      buttonCreate("+" , eastPanel);
      buttonCreate("+/-" , eastPanel);
      buttonCreate("/" , eastPanel);
      buttonCreate("Sqrt" , eastPanel);
      //South
      JPanel southPanel = new JPanel(new GridLayout(1, 5));
      buttonCreate("0" , southPanel);
      buttonCreate("." , southPanel);
      buttonCreate("=" , southPanel);
      //Add panels to frame
      frame.add(northPanel, BorderLayout.NORTH);
      frame.add(westPanel, BorderLayout.WEST);
      frame.add(eastPanel, BorderLayout.EAST);
      frame.add(southPanel, BorderLayout.SOUTH);
      //Set visible
      frame.setVisible(true);
    }

    //Keyboard input actions
    public void keyPressed(KeyEvent e){
       if(e.getKeyCode() == e.VK_ENTER){
            equal();
       }
       if(e.getKeyCode() == e.VK_ESCAPE){
            C();
       }
    }
    public void keyTyped(KeyEvent e) {
    }
    public void keyReleased(KeyEvent e) {
    }

    //Click button actions.
    public void actionPerformed(ActionEvent event) {
        //Button clicked.
        String pressed = event.getActionCommand();
        display = txt.getText();
        //0-9
        if(numRange.contains(pressed)) {
           display += pressed;
           txt.setText(display);
           opIndicator = false;
        }
        //Operators
        if(operator.contains(pressed) && display.length() > 0){
            if(!opIndicator){
              display += " " + pressed + " ";
              txt.setText(display);
              opIndicator = true;
            }
        }
        //C
       if(pressed.equals("C") || pressed.equals("CE")){
            C();
       }
        //BKSP
        if(pressed.equals("BKSP")){
            if(display.length() > 0){
               display = display.substring(0, display.length()-1);
               txt.setText(display);
               Scanner scan = new Scanner(display);
               String last = "";
               while(scan.hasNext()){
                  last = scan.next();
               }
               if(operator.contains(last)){
                  opIndicator = true;
               } else {
                  opIndicator = false;
               }
            } else {
               txt.setText("");
               opIndicator = false;
            }
        }
        //(1/X)
        if(pressed.equals("1/x") && display.length() > 0){
            display = "" + eval(infixtopostfix("1 / " + display));
            txt.setText(display);
        }
        //(+/-)
        if(pressed.equals("+/-") && display.length() > 0){
            boolean hasIllegal = false;
            for(int i = 0; i < display.length(); i++){
               if(operator.contains(display.charAt(i)+"") && i != 0){ //dont change if an op, change if "-" is charAt 0.
                  hasIllegal = true;
               }
            }
            if(!hasIllegal){
               double convert = Double.parseDouble(display) * -1;
               display = convert + "";
               txt.setText(display);
            }
        }
        //Period
        if(pressed.equals(".") && !display.contains(".") && display.length() > 0){
            period();
        }
        //SQRT
        if(pressed.equals("Sqrt") && opIndicator == false && display.length() > 0 ){
            double numValue = Double.parseDouble(display);
            display = "" + Math.sqrt(numValue);
            txt.setText(display);
        }
        //=
        if(pressed.equals("=")){
          equal();
        }
    }

    //Creates button in panel.
    public void buttonCreate(String name, JPanel panel){
      JButton button = new JButton(name);
      button.addActionListener(this);
      panel.add(button);
    }
    //Period.
    public void period(){
      display = display + ".";
      txt.setText(display);
    }
    //C.
    public void C(){
       display = "";
       txt.setText(display);
       opIndicator = false;
    }
    //Equals robust test method.
    public void equal() {
       String getTxt = txt.getText();
       int getTxtLeng = getTxt.length();
       int count;


       //First input not a number check.
       if(getTxtLeng > 0 && !(numRange+"-").contains(getTxt.charAt(0)+"") || (getTxtLeng == 1 && "-".contains(getTxt.charAt(0)+""))){
          valid = false;
          JOptionPane.showMessageDialog(null, "Invalid Input");
       }
       if(getTxtLeng > 0) {
         count = 1;
         char cur;
         while(count <= getTxtLeng){
            cur = getTxt.charAt(getTxtLeng - count);
            //If not a op, num, . , " ".
            if( !(operator+numRange+"E. ").contains(cur+"") ){
               valid = false;
               JOptionPane.showMessageDialog(null, "Invalid Input");
               count = getTxtLeng+1;
            }
            count++;
        }
       //Prevents consecutive operators
       Scanner s = new Scanner(getTxt);
       int tk = 0;
       while(s.hasNext()){
         tk++;
         String dump = s.next();
       }
       if(valid == true && getTxtLeng > 1){
          count = 0;
          Scanner opscan = new Scanner(getTxt);
          String prev = opscan.next();
          String curtoken = prev;
          while(opscan.hasNext()){
            prev = curtoken;
            curtoken = opscan.next();
            if(operator.contains(prev) && operator.contains(curtoken)) {
               valid = false;
               JOptionPane.showMessageDialog(null, "Invalid Input");
               count = getTxtLeng+1;
            }
          }
          //Prevents user from ending with operator.
          if(operator.contains(curtoken)){
            valid = false;
            JOptionPane.showMessageDialog(null, "Invalid Input");
          }
        }
        //Test if from idx 1 to length of tokens for bad input.
        Scanner scx = new Scanner(getTxt);
        Boolean stop = false;
        int chcount = 0;
        int periodcount = 0;
        if(scx.hasNext()){
         while(scx.hasNext() && stop == false){
           chcount = 0;
           periodcount = 0;
           String btoken = scx.next();
           while(getTxtLeng > 1 && chcount < btoken.length()){
               if(".".contains(btoken.charAt(chcount)+"")){
                  periodcount++;
               }
               if(chcount > 0 && !((numRange+"E.").contains(btoken.charAt(chcount)+"")) && periodcount > 1){
                  valid = false;
                  JOptionPane.showMessageDialog(null, "Invalid Input");
                  stop = true;
                  chcount = btoken.length()+1;
               }
               chcount++;
           }
         }
        }

       //Fix incorrect operators from screen.
       count = 1;
       changecount = 0; //fix
       String opchecked = "";
       while(count < getTxtLeng){
         if(operator.contains(getTxt.charAt(count)+"") && !opchecked.contains(getTxt.charAt(count)+"")){
            //count changes
            String thisop = getTxt.charAt(count) + "";
            opchecked += thisop;
            for(int i = 0; i < getTxt.length(); i++){
               if( thisop.equals(getTxt.charAt(i)+"") ){
                  changecount++;
               }
            }
            getTxt = getTxt.replace(getTxt.charAt(count)+"", " " + getTxt.charAt(count) + " ");
            count++;
            getTxtLeng += changecount * 2;
         }
         //Check.
         System.out.println("count: " + count + " Length: " + getTxtLeng + "  Result: " + getTxt + "\n");
         count++;
         changecount = 0;
       }

        //If contains more than one period.
        count = 0;
        periodcount = 0;
        while(count < getTxtLeng){
            if(".".equals(getTxt.charAt(count)+"")){
               periodcount++;
               if(periodcount > 1){
                  valid = false;
                  JOptionPane.showMessageDialog(null, "Invalid Input9");
               }
            }
            count++;
        }
        //If expression is valids set display.
        if(valid && getTxtLeng > 1){
          display = getTxt;
        }
        //If empty expression.
        if(display.equals("") && txt.getText().equals("") || getTxt.equals(" ")) {
           display = "";
           txt.setText(display);
           opIndicator = false;
        } else {
          //ELSE is Valid
          if(valid && getTxtLeng > 1){
            System.out.println(getTxt);
            display = infixtopostfix(display);
            System.out.println(display);
            display = "" + eval(display);
            txt.setText(display);
            opIndicator = false;
            txt.setCaretPosition(display.length());
          }
          valid = true;
        }
      }
    }
}
