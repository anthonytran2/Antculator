/*
   Expression Evaluator Homework
   CSC 20, Fall 2014, 12/10/2014
   Written by: 1008
*/

import java.util.*;
import java.io.*;

public class Expr {

   //Converts infix expression to postfix expression.
   public static String infixtopostfix(String eq){
      String P = "()";                 //PARENTHESES
      String E = "^";                  //EXPONENT
      String MD = "*/";                //MUL/DIV
      String AS = "+-";                //ADD/SUB         
      String leftAssociated = "-/";    //Left associated operators. 
      String result = "";              //POSTFIX RESULT 
      String right = P.charAt(1) + ""; //For ")" test.
      boolean end = false;             //End of loop indicator infix to post.      
      Scanner in = new Scanner(eq);    //Scanner of infix.
      Stack<String> s = new Stack<String>(); //Stack of Operators.
      String exp = eq;
      
      while(in.hasNext()){                //Num goes to output.
         if(in.hasNextDouble()){         //Operator goes onto stack.
            result += in.nextDouble() + " ";      
         } else {          
           String op = in.next();          
           if(right.contains(op)){        //Parentheses
               String find = s.pop();      //Stack value.
               while(!find.equals("(")){
                  result += find + " ";
                  find = s.pop();
               }
           }  
           if(s.size() > 0 && (leftAssociated).contains(op) && AS.contains(s.peek()) && AS.contains(op) ){
                result += s.pop() + " ";            
                s.push(op);
           } else if(s.size() > 0 && (leftAssociated).contains(op) && MD.contains(s.peek()) && MD.contains(op) ) {
                result += s.pop() + " ";            
                s.push(op);           
           } else if(s.size() > 0 && E.contains(s.peek()) && (AS+MD).contains(op)) { //EXPONENT found && MD OR AS is next.
                String hOP = s.pop();                    //Higher op
                while(s.size() > 0 || E.contains(hOP)) {         
                  result += hOP + " ";                   //While contains E pop until lower.   
                  if(s.size() > 0){
                     hOP = s.pop();                      //Next on stack = ^ 
                  } else {
                     hOP = "-1";                         //else not found.
                  }
               }
               if((MD+AS).contains(hOP)){                //Add left over hOP to result;
                  result += hOP + " ";
               }
               s.push(op);
            } else if( s.size() > 0  && MD.contains(s.peek()) && 
                        AS.contains(op) ) {               
               String hOP = s.pop();                     //Higher op
               while(s.size() > 0 || MD.contains(hOP)) {          
                  result += hOP + " ";                   //While contains Mu/Di pop until lower.   
                  if(s.size() > 0){
                     hOP = s.pop();
                  } else {
                     hOP = "-1";
                  }
               }
               if(AS.contains(hOP)){                     //Add left over hOP to result;
                  result += hOP + " ";
               }
               s.push(op);
            } else if(!op.equals(")") && end == false){  //If
               s.push(op);
            }   
         }
         //end = false;
      }    
      while(!s.isEmpty()){                               //pop the rest off stack.
         result += s.pop() + " ";
      }
      return result;                                     //returns postfix.
   }
   
   //Evaluates postfix expression.
   public static double eval(String eq) {
      Scanner in = new Scanner(eq);
      Stack<Double> s = new Stack<Double>();
      String E = "^";

      while (in.hasNext()) {
         if(in.hasNextDouble()) {
            s.push(in.nextDouble());
         } else {
            String op = in.next();        //operator
            if (op.equals("+")) {         //add
               s.push(s.pop()+s.pop());
            } else if (op.equals("*")) {  //multiply
               s.push(s.pop()*s.pop());
            } else if (op.equals("/")) {  //divide
               double divisor = s.pop();
               double dividend = s.pop();
               s.push(dividend/divisor); 
            } else if (op.equals("-")) {  //subtract
               double num2 = s.pop(); 
               double num1 = s.pop();
               s.push(num1-num2); 
            } else if(op.equals("^")){    //exponent
               double exp = s.pop();
               double base = s.pop();
               s.push((double)Math.pow(base,exp)); 
            } else {          
               throw new RuntimeException();
            }
         }
      }
      if (s.size()==1) { //Last on stack is value.
         return s.pop();
      } else {
         throw new RuntimeException();
      }
   }   
   
  public static void main(String[] args)
      throws FileNotFoundException {
      int counts = 0;         
      Scanner in = new Scanner(new File("ninput.txt")); //Tests are taken from txt file.
      
      while(in.hasNextLine()) {  
         try {
            while(in.hasNextLine()){
               counts++;
               String s = Expr.infixtopostfix(in.nextLine());
               System.out.println("# " + counts + " "  + s + "= " + Expr.eval(s)); // + Expr.eval(s)        
            }
         } catch(Exception e) {
            System.out.println("Parse error: " + e);
         }
      }

         /*String numba = "6+6/1";
         String kill = "";
         numba = numba.replace("+", " + ");
         numba = numba.replace("/", " / ");
         System.out.println(numba);   
         
         String s = infixtopostfix("5 / 5 /  5");
         //s = "1 2 - 3 4 * +";
         //String s = "5 / 5 / 5";
         //1 2 3 4  -
         System.out.println("POSTFIX: " + s);
         System.out.println("ANS:     " + eval(s));*/
      }
} 