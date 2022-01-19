package com.example.designpatterns.visitor;

import com.example.calculator.*;

/**
 * 格式化打印
 * @author skyline
 */
public class PrettyPrintVisitor implements NodeVisitor{

    private static final int[] COLORS = new int[]{31,32,33,34,35};

    private int index=0;

    private void colorLeftBrackets(){
        System.out.print((char)27+"["+ COLORS[index%5]+"m("+(char) 27+"[0m");
        index++;
    }

    private void colorRightBrackets(){
        index--;
        System.out.print((char)27+"["+ COLORS[index%5]+"m)"+(char) 27+"[0m");
    }

    @Override
    public void visitOperator(Operator operator) {
        Node leftNode = operator.getLeftNode();
        int currentPriority = operator.priority();
        boolean showBrackets = false;
        if(leftNode instanceof Operator){
            if (((Operator) leftNode).priority()< currentPriority) {
                colorLeftBrackets();
                showBrackets = true;
            }
        }
        leftNode.accept(this);
        if (showBrackets) {
            colorRightBrackets();
            showBrackets = false;
        }
        System.out.print(" "+operator.operator()+" ");
        Node rightNode = operator.getRightNode();
        if(rightNode instanceof Operator){
            if (((Operator) rightNode).priority()<= currentPriority) {
                colorLeftBrackets();
                showBrackets = true;
            }
        }
        rightNode.accept(this);
        if (showBrackets) {
            colorRightBrackets();
        }
    }

    @Override
    public void visitDataNode(DataNode dataNode) {
        System.out.print(dataNode.getText());
    }

    @Override
    public void visitStaticNode(StaticNode staticNode) {
        System.out.print(staticNode.getText());
    }

    @Override
    public void visitFunctionNode(FunctionNode functionNode) {
        System.out.print(functionNode.getFuncName());
        colorLeftBrackets();
        for (int i = 0; i < functionNode.getParams().size(); i++) {
            functionNode.getParams().get(i).accept(this);
           if(i<functionNode.getParams().size()-1){
               System.out.print(",");
           }
        }
        colorRightBrackets();
    }
}
