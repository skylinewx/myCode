package com.example.calculator;

import java.util.*;

/**
 * 操作符节点
 */
public abstract class Operator implements Node {

    private static final Map<Character, Class<? extends Operator>> operatorMaps = new HashMap<>();
    private static final Collection<List<Character>> sortList;

    static {
        Map<Integer, List<Character>> operatorPriorityMap = new TreeMap<>();
        ServiceLoader<Operator> load = ServiceLoader.load(Operator.class);
        for (Operator operator : load) {
            operatorMaps.put(operator.operator(), operator.getClass());
            List<Character> list = operatorPriorityMap.computeIfAbsent(operator.priority(), k -> new ArrayList<>());
            list.add(operator.operator());
        }
        sortList = Collections.unmodifiableCollection(operatorPriorityMap.values());
    }

    private Node left;
    private Node right;
    private Node leftNode;
    private Node rightNode;

    public static Operator valueOf(char operator) {
        Class<? extends Operator> clazz = operatorMaps.get(operator);
        if (clazz == null) {
            throw new RuntimeException("not support!");
        }
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("not support!");
    }

    public static Collection<List<Character>> getSortList() {
        return sortList;
    }

    @Override
    public final Node parse() {
        leftNode = left.parse();
        rightNode = right.parse();
        return this;
    }

    final void setLeft(Node left) {
        this.left = left;
    }

    final void setRight(Node right) {
        this.right = right;
    }

    final Object getLeftResult() {
        return leftNode.getValue();
    }

    final Object getRightResult() {
        return rightNode.getValue();
    }

    final public Node getLeftNode() {
        return leftNode;
    }

    final public Node getRightNode() {
        return rightNode;
    }

    @Override
    public String getText() {
        return String.valueOf(operator());
    }

    @Override
    public void accept(NodeVisitor nodeVisitor) {
        nodeVisitor.visitOperator(this);
    }

    /**
     * 优先级
     *
     * @return
     */
    public abstract int priority();

    /**
     * 操作符
     *
     * @return
     */
    public abstract char operator();
}
