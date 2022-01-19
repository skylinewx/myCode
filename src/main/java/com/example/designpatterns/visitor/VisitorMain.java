package com.example.designpatterns.visitor;

import com.example.calculator.Calculation;
import com.example.calculator.Node;

/**
 * 观察者模式
 * @author skyline
 */
public class VisitorMain {
    public static void main(String[] args) {
        String exp = "(2-1)*3+2+(3*(9-(5+2)*1))";
        Node parse = Calculation.parse(exp);
        parse.accept(new PrettyPrintVisitor());
        System.out.println();
        String exp2 = "Max(8,Max(5,4))+plus100(max(3,9))";
        Node parse2 = Calculation.parse(exp2);
        parse2.accept(new PrettyPrintVisitor());
        System.out.println();
        String exp3 = "2-(3-4)";
        Node parse3 = Calculation.parse(exp3);
        parse3.accept(new PrettyPrintVisitor());
    }
}
