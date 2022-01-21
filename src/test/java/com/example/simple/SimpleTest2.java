package com.example.simple;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangxing
 * @date 2022/1/21
 **/
public class SimpleTest2 {
    String aa = "# 楔子\n" +
            "\n" +
            "本来最近一直在写[设计模式系列](https://blog.csdn.net/wx10301075wx/category_11462286.html)，本来要写visitor模式了，但是据说visitor模式本来是应用到编译器级别的组件上的。为了我的visitor模式，所以我决定手写一个抽象语法树出来。\n" +
            "\n" +
            "# 需求\n" +
            "\n" +
            "1. 给定一个输入`(2-1)*3+2+(3*(9-(5+2)*1))`，程序自动算出来运算的结果。\n" +
            "2. 程序可以支持自定义函数，函数需要能够进行嵌套，如计算`Max(8,Max(5,4))+plus100(max(3,9))`，其中`max`和`plus100`都是自定义的函数。\n" +
            "3. 程序可以支持自定义变量，并通过上下文传入自定义变量的值，如计算`Max(8,Max(5,cc))+plus100(max(aa*2,bb))`，其中aa、bb、cc都是自定义变量。\n" +
            "\n" +
            "# 思考\n" +
            "\n" +
            "## BNF范式\n" +
            "\n" +
            "先从最简单的来，其实我也不知道怎么办，所以我去百度了一下...网上的各种帖子要么说的云里雾里的，要么直接说让你用现成的api。但是通过查询，我了解到了一个关键词：**BNF范式**。~~但是这个东西专业的解释过于专业~~ (废话)。相对来说，我更喜欢知乎上的关于BNF范式的一个答案：[BNF范式（巴科斯范式）到底是什么](https://www.zhihu.com/question/27051306)。用一句简单说一下就是：**BNF范式提供了一种可以向下递归降解的语法解析方式**。\n" +
            "\n" +
            "## BNF范式应用\n" +
            "\n" +
            "首先明确一下四则运算符的运算规则：\n" +
            "\n" +
            "- 先乘除、后加减\n" +
            "- 遇到括号，时先算括号中的表达式\n" +
            "\n" +
            "然后我们再回来看这个表达式`(2-1)*3+2+(3*(9-(5+2)*1))`，那么这个表达式应该怎么拆分呢？我画了一个图来表示拆分的过程\n" +
            "![运算拆分](http://10.2.6.29:9700/upload/2022/01/b947859497df413a98a22d62979f8fa6.png)\n" +
            "\n" +
            "> 为什么从**右向左拆**，而把不是从左向右拆呢？想想这个表达式要怎么拆`2-3-4-5`\n" +
            "\n" +
            "在上面的图中，一共有三种节点：\n" +
            "\n" +
            "- 浅蓝色的节点表示当前是一个表达式节点`Expression`，还可以继续向下拆分\n" +
            "- 浅紫色的节点表示当前是一个运算符节点`Operator`，运算符用于连接两个不同的节点\n" +
            "- 绿色的节点表示当前是一个数据节点`DataNode`，数据节点是最末级节点，不可以继续拆分了。\n" +
            "\n" +
            "按照相同的思路，我们拆分一下`Max(8,Max(5,4))+plus100(max(3,9))`\n" +
            "![函数分解](http://10.2.6.29:9700/upload/2022/01/abc21833a9824342a879c6ecfcc8df28.png)\n" +
            "在这次的分解中，多了橘黄色的部分，橘黄色的节点表示当前是一个函数节点`FunctionNode`，函数节点还可以继续向下分解，但是分解的内容为函数的参数。\n" +
            "最后，我们再分解一下`Max(8,Max(5,cc))+plus100(max(aa*2,bb))`\n" +
            "![带常量的分解](http://10.2.6.29:9700/upload/2022/01/6eaf65ae60174cdfb8b3a397e5686c05.png)\n" +
            "这次又多出来了蓝色的节点，蓝色表示当前节点是一个常量节点`StaticNode`，不可继续拆分。\n" +
            "\n" +
            "## 回顾\n" +
            "\n" +
            "通过上面对计算表达式进行拆分，我们一共拆解出五类节点\n" +
            "\n" +
            "1. 表达式节点`Expression`，可以继续进行拆分\n" +
            "2. 运算符节点`Operator`，用来连接两个表达式\n" +
            "3. 数据节点`DataNode`，最末级节点，不可以进行拆分\n" +
            "4. 函数节点`FunctionNode`，函数节点还可以继续向下分解，但是分解的内容为函数的参数\n" +
            "5. 常量节点`StaticNode`，不可继续拆分\n" +
            "\n" +
            "然后我们用代码来实现一下吧。\n" +
            "\n" +
            "# 实现\n" +
            "\n" +
            "## 公共的Node节点\n" +
            "\n" +
            "```java\n" +
            "/**\n" +
            " * 计算节点\n" +
            " */\n" +
            "public interface Node {\n" +
            "\n" +
            "    /**\n" +
            "     * 节点表达式\n" +
            "     * @return\n" +
            "     */\n" +
            "    String getText();\n" +
            "\n" +
            "    /**\n" +
            "     * 节点值\n" +
            "     * @return\n" +
            "     */\n" +
            "    Object getValue();\n" +
            "\n" +
            "    /**\n" +
            "     * 解析之后的节点\n" +
            "     * @return\n" +
            "     */\n" +
            "    Node parse();\n" +
            "}\n" +
            "```\n" +
            "\n" +
            "## 表达式节点 Expression\n" +
            "\n" +
            "```java\n" +
            "/**\n" +
            " * 表达式节点\n" +
            " */\n" +
            "public class Expression implements Node {\n" +
            "    Map<String, Object> env;\n" +
            "    Map<String, IFunction> functionMap;\n" +
            "    private String text;\n" +
            "\n" +
            "    public Expression() {\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public Node parse() {\n" +
            "        Collection<List<Character>> sortList = Operator.getSortList();\n" +
            "        for (List<Character> characters : sortList) {\n" +
            "            Node operator = getNode(characters);\n" +
            "            if (operator != null) {\n" +
            "                return operator;\n" +
            "            }\n" +
            "        }\n" +
            "        if (text.indexOf('(') == -1 || isStartAndEndWidthBrackets(text)) {\n" +
            "            StaticNode staticNode = new StaticNode();\n" +
            "            staticNode.setText(text);\n" +
            "            staticNode.setEnv(env);\n" +
            "            return staticNode;\n" +
            "        }\n" +
            "        FunctionNode functionNode = new FunctionNode();\n" +
            "        functionNode.setFunctionMap(functionMap);\n" +
            "        functionNode.setEnv(env);\n" +
            "        functionNode.setText(text);\n" +
            "        return functionNode;\n" +
            "    }\n" +
            "\n" +
            "    private Node getNode(List<Character> opts) {\n" +
            "        int length = text.length();\n" +
            "        int brackets = 0;\n" +
            "        boolean isDataNode = true;\n" +
            "        for (int i = length-1; i >=0; i--) {\n" +
            "            char charAt = text.charAt(i);\n" +
            "            if (isDataNode && (charAt > '9' || charAt < '0')) {\n" +
            "                isDataNode = false;\n" +
            "            }\n" +
            "            if (charAt == ')') {\n" +
            "                brackets++;\n" +
            "            }\n" +
            "            if (charAt == '(') {\n" +
            "                brackets--;\n" +
            "            }\n" +
            "            if (opts.contains(charAt)) {\n" +
            "                if (brackets == 0) {\n" +
            "                    Expression left = new Expression();\n" +
            "                    left.setText(text.substring(0, i).trim());\n" +
            "                    left.setFunctionMap(functionMap);\n" +
            "                    left.setEnv(env);\n" +
            "                    Operator operator = Operator.valueOf(charAt);\n" +
            "                    operator.setLeft(left);\n" +
            "                    Expression right = new Expression();\n" +
            "                    right.setText(text.substring(i + 1).trim());\n" +
            "                    right.setFunctionMap(functionMap);\n" +
            "                    right.setEnv(env);\n" +
            "                    operator.setRight(right);\n" +
            "                    operator.parse();\n" +
            "                    return operator;\n" +
            "                }\n" +
            "            }\n" +
            "        }\n" +
            "        if (length > 0 && isDataNode) {\n" +
            "            DataNode expression = new DataNode();\n" +
            "            expression.setText(text);\n" +
            "            return expression;\n" +
            "        }\n" +
            "        return null;\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public String getText() {\n" +
            "        return text;\n" +
            "    }\n" +
            "\n" +
            "    public void setText(String text) {\n" +
            "        String trim = text.trim();\n" +
            "        if (isStartAndEndWidthBrackets(trim)) {\n" +
            "            trim = trim.substring(1, trim.length() - 1);\n" +
            "        }\n" +
            "        this.text = trim;\n" +
            "    }\n" +
            "\n" +
            "    private boolean isStartAndEndWidthBrackets(String text) {\n" +
            "        if (text.charAt(0) == '(') {\n" +
            "            int length = text.length();\n" +
            "            int brackets = 0;\n" +
            "            for (int i = 0; i < length; i++) {\n" +
            "                char charAt = text.charAt(i);\n" +
            "                if (charAt == '(') {\n" +
            "                    brackets++;\n" +
            "                }\n" +
            "                if (charAt == ')') {\n" +
            "                    brackets--;\n" +
            "                    if (brackets == 0) {\n" +
            "                        return i == length - 1;\n" +
            "                    }\n" +
            "                }\n" +
            "            }\n" +
            "        }\n" +
            "        return false;\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public Object getValue() {\n" +
            "        throw new RuntimeException(\"not support!\");\n" +
            "    }\n" +
            "\n" +
            "    public void setEnv(Map<String, Object> env) {\n" +
            "        this.env = env;\n" +
            "    }\n" +
            "\n" +
            "    public void setFunctionMap(Map<String, IFunction> functionMap) {\n" +
            "        this.functionMap = functionMap;\n" +
            "    }\n" +
            "}\n" +
            "```\n" +
            "\n" +
            "## 静态变量节点 StaticNode\n" +
            "\n" +
            "```java\n" +
            "/**\n" +
            " * 变量节点\n" +
            " */\n" +
            "public class StaticNode extends Expression{\n" +
            "\n" +
            "    @Override\n" +
            "    public String getText() {\n" +
            "        return super.getText();\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public Object getValue() {\n" +
            "        if (env == null) {\n" +
            "            return null;\n" +
            "        }\n" +
            "        return env.get(getText());\n" +
            "    }\n" +
            "}\n" +
            "```\n" +
            "\n" +
            "## 数据节点 DataNode\n" +
            "\n" +
            "```java\n" +
            "/**\n" +
            " * 数值节点\n" +
            " */\n" +
            "public class DataNode extends Expression{\n" +
            "\n" +
            "    @Override\n" +
            "    public String getText() {\n" +
            "        return super.getText();\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public Object getValue() {\n" +
            "        String text = getText();\n" +
            "        int i = text.indexOf('.');\n" +
            "        if (i == -1) {\n" +
            "            try {\n" +
            "                return Integer.parseInt(text);\n" +
            "            } catch (NumberFormatException e) {\n" +
            "                return new BigInteger(text);\n" +
            "            }\n" +
            "        }\n" +
            "        int precision = text.length() - i;\n" +
            "        if (precision <= 6) {\n" +
            "            try {\n" +
            "                return Float.parseFloat(text);\n" +
            "            } catch (NumberFormatException e) {\n" +
            "                return new BigDecimal(text);\n" +
            "            }\n" +
            "        }\n" +
            "        if (precision <= 15) {\n" +
            "            try {\n" +
            "                return Double.parseDouble(text);\n" +
            "            } catch (NumberFormatException e) {\n" +
            "                return new BigDecimal(text);\n" +
            "            }\n" +
            "        }\n" +
            "        return new BigDecimal(text);\n" +
            "    }\n" +
            "}\n" +
            "```\n" +
            "\n" +
            "## 函数节点 FunctionNode\n" +
            "\n" +
            "```java\n" +
            "/**\n" +
            " * 函数节点\n" +
            " */\n" +
            "public class FunctionNode extends Expression {\n" +
            "\n" +
            "    private String funcName;\n" +
            "    private List<Node> params;\n" +
            "\n" +
            "    @Override\n" +
            "    public String getText() {\n" +
            "        return super.getText();\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public void setText(String text) {\n" +
            "        super.setText(text);\n" +
            "        String txt = getText();\n" +
            "        int index = txt.indexOf('(');\n" +
            "        funcName = txt.substring(0, index).toUpperCase(Locale.ROOT);\n" +
            "        params = new ArrayList<>();\n" +
            "        String substring = txt.substring(index + 1, txt.length() - 1);\n" +
            "        int length = substring.length();\n" +
            "        int brackets = 0;\n" +
            "        int begin = 0;\n" +
            "        for (int i = 0; i < length; i++) {\n" +
            "            char charAt = substring.charAt(i);\n" +
            "            if (charAt == '(') {\n" +
            "                brackets++;\n" +
            "            }\n" +
            "            if (charAt == ')') {\n" +
            "                brackets--;\n" +
            "            }\n" +
            "            if (charAt == ',') {\n" +
            "                if (brackets == 0) {\n" +
            "                    Expression expression = new Expression();\n" +
            "                    expression.setFunctionMap(functionMap);\n" +
            "                    expression.setText(substring.substring(begin, i));\n" +
            "                    expression.setEnv(env);\n" +
            "                    Node parse = expression.parse();\n" +
            "                    params.add(parse);\n" +
            "                    begin = i + 1;\n" +
            "                }\n" +
            "            }\n" +
            "        }\n" +
            "        if (begin != length) {\n" +
            "            Expression expression = new Expression();\n" +
            "            expression.setFunctionMap(functionMap);\n" +
            "            expression.setText(substring.substring(begin));\n" +
            "            expression.setEnv(env);\n" +
            "            Node parse = expression.parse();\n" +
            "            params.add(parse);\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public Object getValue() {\n" +
            "        IFunction iFunction = functionMap.get(funcName);\n" +
            "        int size = params.size();\n" +
            "        Object[] paramVals = new Object[size];\n" +
            "        for (int i = 0; i < size; i++) {\n" +
            "            paramVals[i] = params.get(i).getValue();\n" +
            "        }\n" +
            "        return iFunction.exec(paramVals, env);\n" +
            "    }\n" +
            "}\n" +
            "```\n" +
            "\n" +
            "## 操作符节点 Operator\n" +
            "\n" +
            "在处理操作符时，使用`ServiceLoader`将具体的加减乘除操作与抽象的`Operator` 进行解耦，**方便日后扩展其他类型的操作符**，比如取摸、求余等操作。\n" +
            "\n" +
            "```java\n" +
            "/**\n" +
            " * 操作符节点\n" +
            " */\n" +
            "public abstract class Operator implements Node {\n" +
            "\n" +
            "    private static final Map<Character, Class<? extends Operator>> operatorMaps = new HashMap<>();\n" +
            "    private static final Collection<List<Character>> sortList;\n" +
            "\n" +
            "    static {\n" +
            "        Map<Integer, List<Character>> operatorPriorityMap = new TreeMap<>();\n" +
            "        ServiceLoader<Operator> load = ServiceLoader.load(Operator.class);\n" +
            "        for (Operator operator : load) {\n" +
            "            operatorMaps.put(operator.operator(), operator.getClass());\n" +
            "            List<Character> list = operatorPriorityMap.computeIfAbsent(operator.priority(), k -> new ArrayList<>());\n" +
            "            list.add(operator.operator());\n" +
            "        }\n" +
            "        sortList = Collections.unmodifiableCollection(operatorPriorityMap.values());\n" +
            "    }\n" +
            "\n" +
            "    private Node left;\n" +
            "    private Node right;\n" +
            "    private Node leftResult;\n" +
            "    private Node rightResult;\n" +
            "\n" +
            "    public static Operator valueOf(char operator) {\n" +
            "        Class<? extends Operator> clazz = operatorMaps.get(operator);\n" +
            "        if (clazz == null) {\n" +
            "            throw new RuntimeException(\"not support!\");\n" +
            "        }\n" +
            "        try {\n" +
            "            return clazz.newInstance();\n" +
            "        } catch (InstantiationException | IllegalAccessException e) {\n" +
            "            e.printStackTrace();\n" +
            "        }\n" +
            "        throw new RuntimeException(\"not support!\");\n" +
            "    }\n" +
            "\n" +
            "    public static Collection<List<Character>> getSortList() {\n" +
            "        return sortList;\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public final Node parse() {\n" +
            "        leftResult = left.parse();\n" +
            "        rightResult = right.parse();\n" +
            "        return this;\n" +
            "    }\n" +
            "\n" +
            "    final void setLeft(Node left) {\n" +
            "        this.left = left;\n" +
            "    }\n" +
            "\n" +
            "    final void setRight(Node right) {\n" +
            "        this.right = right;\n" +
            "    }\n" +
            "\n" +
            "    final Object getLeftResult() {\n" +
            "        return leftResult.getValue();\n" +
            "    }\n" +
            "\n" +
            "    final Object getRightResult() {\n" +
            "        return rightResult.getValue();\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public String getText() {\n" +
            "        return String.valueOf(operator());\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     * 优先级\n" +
            "     *\n" +
            "     * @return\n" +
            "     */\n" +
            "    public abstract int priority();\n" +
            "\n" +
            "    /**\n" +
            "     * 操作符\n" +
            "     *\n" +
            "     * @return\n" +
            "     */\n" +
            "    public abstract char operator();\n" +
            "}\n" +
            "```\n" +
            "\n" +
            "### 加法处理节点 Add\n" +
            "\n" +
            "```java\n" +
            "/**\n" +
            " * 加法操作符节点\n" +
            " */\n" +
            "public class Add extends Operator {\n" +
            "\n" +
            "    @Override\n" +
            "    public char operator() {\n" +
            "        return '+';\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public Object getValue() {\n" +
            "        Object leftResult = getLeftResult();\n" +
            "        Object rightResult = getRightResult();\n" +
            "        if (leftResult instanceof Integer) {\n" +
            "            int left = (int) leftResult;\n" +
            "            if (rightResult instanceof Integer) {\n" +
            "                return left + (Integer) rightResult;\n" +
            "            }\n" +
            "            if (rightResult instanceof Double) {\n" +
            "                return left + (Double) rightResult;\n" +
            "            }\n" +
            "            if (rightResult instanceof Float) {\n" +
            "                return left + (Float) rightResult;\n" +
            "            }\n" +
            "            if (rightResult instanceof BigInteger) {\n" +
            "                return ((BigInteger) rightResult).add(BigInteger.valueOf(left));\n" +
            "            }\n" +
            "            if (rightResult instanceof BigDecimal) {\n" +
            "                return ((BigDecimal) rightResult).add(BigDecimal.valueOf(left));\n" +
            "            }\n" +
            "        }\n" +
            "        if (leftResult instanceof Double) {\n" +
            "            double left = (Double) leftResult;\n" +
            "            if (rightResult instanceof Integer) {\n" +
            "                return left + (Integer) rightResult;\n" +
            "            }\n" +
            "            if (rightResult instanceof Double) {\n" +
            "                return left + (Double) rightResult;\n" +
            "            }\n" +
            "            if (rightResult instanceof Float) {\n" +
            "                return left + (Float) rightResult;\n" +
            "            }\n" +
            "            if (rightResult instanceof BigInteger) {\n" +
            "                throw new RuntimeException(\"not support!\");\n" +
            "            }\n" +
            "            if (rightResult instanceof BigDecimal) {\n" +
            "                return ((BigDecimal) rightResult).add(new BigDecimal(String.valueOf(leftResult)));\n" +
            "            }\n" +
            "        }\n" +
            "        if (leftResult instanceof Float) {\n" +
            "            float left = (float) leftResult;\n" +
            "            if (rightResult instanceof Integer) {\n" +
            "                return left + (Integer) rightResult;\n" +
            "            }\n" +
            "            if (rightResult instanceof Double) {\n" +
            "                return left + (Double) rightResult;\n" +
            "            }\n" +
            "            if (rightResult instanceof Float) {\n" +
            "                return left + (Float) rightResult;\n" +
            "            }\n" +
            "            if (rightResult instanceof BigInteger) {\n" +
            "                throw new RuntimeException(\"not support!\");\n" +
            "            }\n" +
            "            if (rightResult instanceof BigDecimal) {\n" +
            "                return ((BigDecimal) rightResult).add(new BigDecimal(String.valueOf(rightResult)));\n" +
            "            }\n" +
            "        }\n" +
            "        if (leftResult instanceof BigInteger) {\n" +
            "            if (rightResult instanceof Integer) {\n" +
            "                throw new RuntimeException(\"not support!\");\n" +
            "            }\n" +
            "            if (rightResult instanceof Double) {\n" +
            "                throw new RuntimeException(\"not support!\");\n" +
            "            }\n" +
            "            if (rightResult instanceof Float) {\n" +
            "                throw new RuntimeException(\"not support!\");\n" +
            "            }\n" +
            "            if (rightResult instanceof BigInteger) {\n" +
            "                return ((BigInteger) rightResult).add((BigInteger) leftResult);\n" +
            "            }\n" +
            "            if (rightResult instanceof BigDecimal) {\n" +
            "                throw new RuntimeException(\"not support!\");\n" +
            "            }\n" +
            "        }\n" +
            "        if (leftResult instanceof BigDecimal) {\n" +
            "            BigDecimal left = (BigDecimal) leftResult;\n" +
            "            if (rightResult instanceof Integer || rightResult instanceof Double || rightResult instanceof Float) {\n" +
            "                return left.add(new BigDecimal(String.valueOf(rightResult)));\n" +
            "            }\n" +
            "            if (rightResult instanceof BigInteger) {\n" +
            "                throw new RuntimeException(\"not support!\");\n" +
            "            }\n" +
            "            if (rightResult instanceof BigDecimal) {\n" +
            "                return ((BigDecimal) rightResult).add(left);\n" +
            "            }\n" +
            "        }\n" +
            "        throw new RuntimeException(\"not support!\");\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public int priority() {\n" +
            "        return 0;\n" +
            "    }\n" +
            "}\n" +
            "```\n" +
            "\n" +
            "### 减法处理节点 Minus\n" +
            "\n" +
            "```java\n" +
            "/**\n" +
            " * 减法操作符\n" +
            " */\n" +
            "public class Minus extends Operator {\n" +
            "\n" +
            "    @Override\n" +
            "    public char operator() {\n" +
            "        return '-';\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public Object getValue() {\n" +
            "        Object leftResult = getLeftResult();\n" +
            "        Object rightResult = getRightResult();\n" +
            "        if (leftResult instanceof Integer) {\n" +
            "            int left = (int) leftResult;\n" +
            "            if (rightResult instanceof Integer) {\n" +
            "                return left - (Integer) rightResult;\n" +
            "            }\n" +
            "            if (rightResult instanceof Double) {\n" +
            "                return left - (Double) rightResult;\n" +
            "            }\n" +
            "            if (rightResult instanceof Float) {\n" +
            "                return left - (Float) rightResult;\n" +
            "            }\n" +
            "            if (rightResult instanceof BigInteger) {\n" +
            "                return BigInteger.valueOf(left).subtract(((BigInteger) rightResult));\n" +
            "            }\n" +
            "            if (rightResult instanceof BigDecimal) {\n" +
            "                return BigDecimal.valueOf(left).subtract(((BigDecimal) rightResult));\n" +
            "            }\n" +
            "        }\n" +
            "        if (leftResult instanceof Double) {\n" +
            "            double left = (Double) leftResult;\n" +
            "            if (rightResult instanceof Integer) {\n" +
            "                return left - (Integer) rightResult;\n" +
            "            }\n" +
            "            if (rightResult instanceof Double) {\n" +
            "                return left - (Double) rightResult;\n" +
            "            }\n" +
            "            if (rightResult instanceof Float) {\n" +
            "                return left - (Float) rightResult;\n" +
            "            }\n" +
            "            if (rightResult instanceof BigInteger) {\n" +
            "                throw new RuntimeException(\"not support!\");\n" +
            "            }\n" +
            "            if (rightResult instanceof BigDecimal) {\n" +
            "                return new BigDecimal(String.valueOf(leftResult)).subtract(((BigDecimal) rightResult));\n" +
            "            }\n" +
            "        }\n" +
            "        if (leftResult instanceof Float) {\n" +
            "            float left = (float) leftResult;\n" +
            "            if (rightResult instanceof Integer) {\n" +
            "                return left - (Integer) rightResult;\n" +
            "            }\n" +
            "            if (rightResult instanceof Double) {\n" +
            "                return left - (Double) rightResult;\n" +
            "            }\n" +
            "            if (rightResult instanceof Float) {\n" +
            "                return left - (Float) rightResult;\n" +
            "            }\n" +
            "            if (rightResult instanceof BigInteger) {\n" +
            "                throw new RuntimeException(\"not support!\");\n" +
            "            }\n" +
            "            if (rightResult instanceof BigDecimal) {\n" +
            "                return new BigDecimal(String.valueOf(leftResult)).subtract(((BigDecimal) rightResult));\n" +
            "            }\n" +
            "        }\n" +
            "        if (leftResult instanceof BigInteger) {\n" +
            "            if (rightResult instanceof Integer || rightResult instanceof Double || rightResult instanceof Float || rightResult instanceof BigDecimal) {\n" +
            "                throw new RuntimeException(\"not support!\");\n" +
            "            }\n" +
            "            if (rightResult instanceof BigInteger) {\n" +
            "                return ((BigInteger) leftResult).subtract(((BigInteger) rightResult));\n" +
            "            }\n" +
            "        }\n" +
            "        if (leftResult instanceof BigDecimal) {\n" +
            "            BigDecimal left = (BigDecimal) leftResult;\n" +
            "            if (rightResult instanceof Integer || rightResult instanceof Double || rightResult instanceof Float) {\n" +
            "                return left.subtract(new BigDecimal(String.valueOf(rightResult)));\n" +
            "            }\n" +
            "            if (rightResult instanceof BigInteger) {\n" +
            "                throw new RuntimeException(\"not support!\");\n" +
            "            }\n" +
            "            if (rightResult instanceof BigDecimal) {\n" +
            "                return left.subtract(((BigDecimal) rightResult));\n" +
            "            }\n" +
            "        }\n" +
            "        throw new RuntimeException(\"not support!\");\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public int priority() {\n" +
            "        return 0;\n" +
            "    }\n" +
            "}\n" +
            "```\n" +
            "\n" +
            "### 乘法处理节点 Multiply\n" +
            "\n" +
            "```java\n" +
            "/**\n" +
            " * 乘法操作符\n" +
            " */\n" +
            "public class Multiply extends Operator {\n" +
            "\n" +
            "    @Override\n" +
            "    public char operator() {\n" +
            "        return '*';\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public Object getValue() {\n" +
            "        Object leftResult = getLeftResult();\n" +
            "        Object rightResult = getRightResult();\n" +
            "        if (leftResult instanceof Integer) {\n" +
            "            int left = (int) leftResult;\n" +
            "            if (rightResult instanceof Integer) {\n" +
            "                return left * (Integer) rightResult;\n" +
            "            }\n" +
            "            if (rightResult instanceof Double) {\n" +
            "                return left * (Double) rightResult;\n" +
            "            }\n" +
            "            if (rightResult instanceof Float) {\n" +
            "                return left * (Float) rightResult;\n" +
            "            }\n" +
            "            if (rightResult instanceof BigInteger) {\n" +
            "                return ((BigInteger) rightResult).multiply(BigInteger.valueOf(left));\n" +
            "            }\n" +
            "            if (rightResult instanceof BigDecimal) {\n" +
            "                return ((BigDecimal) rightResult).multiply(BigDecimal.valueOf(left));\n" +
            "            }\n" +
            "        }\n" +
            "        if (leftResult instanceof Double) {\n" +
            "            double left = (Double) leftResult;\n" +
            "            if (rightResult instanceof Integer) {\n" +
            "                return left * (Integer) rightResult;\n" +
            "            }\n" +
            "            if (rightResult instanceof Double) {\n" +
            "                return left * (Double) rightResult;\n" +
            "            }\n" +
            "            if (rightResult instanceof Float) {\n" +
            "                return left * (Float) rightResult;\n" +
            "            }\n" +
            "            if (rightResult instanceof BigInteger) {\n" +
            "                throw new RuntimeException(\"not support!\");\n" +
            "            }\n" +
            "            if (rightResult instanceof BigDecimal) {\n" +
            "                return ((BigDecimal) rightResult).multiply(new BigDecimal(String.valueOf(leftResult)));\n" +
            "            }\n" +
            "        }\n" +
            "        if (leftResult instanceof Float) {\n" +
            "            float left = (float) leftResult;\n" +
            "            if (rightResult instanceof Integer) {\n" +
            "                return left * (Integer) rightResult;\n" +
            "            }\n" +
            "            if (rightResult instanceof Double) {\n" +
            "                return left * (Double) rightResult;\n" +
            "            }\n" +
            "            if (rightResult instanceof Float) {\n" +
            "                return left * (Float) rightResult;\n" +
            "            }\n" +
            "            if (rightResult instanceof BigInteger) {\n" +
            "                throw new RuntimeException(\"not support!\");\n" +
            "            }\n" +
            "            if (rightResult instanceof BigDecimal) {\n" +
            "                return ((BigDecimal) rightResult).multiply(new BigDecimal(String.valueOf(rightResult)));\n" +
            "            }\n" +
            "        }\n" +
            "        if (leftResult instanceof BigInteger) {\n" +
            "            if (rightResult instanceof Integer || rightResult instanceof Double || rightResult instanceof Float || rightResult instanceof BigDecimal) {\n" +
            "                throw new RuntimeException(\"not support!\");\n" +
            "            }\n" +
            "            if (rightResult instanceof BigInteger) {\n" +
            "                return ((BigInteger) rightResult).multiply((BigInteger) leftResult);\n" +
            "            }\n" +
            "        }\n" +
            "        if (leftResult instanceof BigDecimal) {\n" +
            "            BigDecimal left = (BigDecimal) leftResult;\n" +
            "            if (rightResult instanceof Integer || rightResult instanceof Double || rightResult instanceof Float) {\n" +
            "                return left.multiply(new BigDecimal(String.valueOf(rightResult)));\n" +
            "            }\n" +
            "            if (rightResult instanceof BigInteger) {\n" +
            "                throw new RuntimeException(\"not support!\");\n" +
            "            }\n" +
            "            if (rightResult instanceof BigDecimal) {\n" +
            "                return ((BigDecimal) rightResult).multiply(left);\n" +
            "            }\n" +
            "        }\n" +
            "        throw new RuntimeException(\"not support!\");\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public int priority() {\n" +
            "        return 1;\n" +
            "    }\n" +
            "}\n" +
            "```\n" +
            "\n" +
            "### 除法处理节点 Divide\n" +
            "\n" +
            "```java\n" +
            "/**\n" +
            " * 除法操作符\n" +
            " */\n" +
            "public class Divide extends Operator {\n" +
            "\n" +
            "    @Override\n" +
            "    public char operator() {\n" +
            "        return '/';\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public Object getValue() {\n" +
            "        Object leftResult = getLeftResult();\n" +
            "        Object rightResult = getRightResult();\n" +
            "        if (leftResult instanceof Integer) {\n" +
            "            int left = (int) leftResult;\n" +
            "            if (rightResult instanceof Integer) {\n" +
            "                return left / (Integer) rightResult;\n" +
            "            }\n" +
            "            if (rightResult instanceof Double) {\n" +
            "                return left / (Double) rightResult;\n" +
            "            }\n" +
            "            if (rightResult instanceof Float) {\n" +
            "                return left / (Float) rightResult;\n" +
            "            }\n" +
            "            if (rightResult instanceof BigInteger) {\n" +
            "                return (new BigInteger(String.valueOf(leftResult))).divide(((BigInteger) rightResult));\n" +
            "            }\n" +
            "            if (rightResult instanceof BigDecimal) {\n" +
            "                return (new BigDecimal(String.valueOf(leftResult))).divide(((BigDecimal) rightResult));\n" +
            "            }\n" +
            "        }\n" +
            "        if (leftResult instanceof Double) {\n" +
            "            double left = (Double) leftResult;\n" +
            "            if (rightResult instanceof Integer) {\n" +
            "                return left / (Integer) rightResult;\n" +
            "            }\n" +
            "            if (rightResult instanceof Double) {\n" +
            "                return left / (Double) rightResult;\n" +
            "            }\n" +
            "            if (rightResult instanceof Float) {\n" +
            "                return left / (Float) rightResult;\n" +
            "            }\n" +
            "            if (rightResult instanceof BigInteger) {\n" +
            "                throw new RuntimeException(\"not support!\");\n" +
            "            }\n" +
            "            if (rightResult instanceof BigDecimal) {\n" +
            "                return new BigDecimal(String.valueOf(leftResult)).divide(((BigDecimal) rightResult));\n" +
            "            }\n" +
            "        }\n" +
            "        if (leftResult instanceof Float) {\n" +
            "            float left = (float) leftResult;\n" +
            "            if (rightResult instanceof Integer) {\n" +
            "                return left / (Integer) rightResult;\n" +
            "            }\n" +
            "            if (rightResult instanceof Double) {\n" +
            "                return left / (Double) rightResult;\n" +
            "            }\n" +
            "            if (rightResult instanceof Float) {\n" +
            "                return left / (Float) rightResult;\n" +
            "            }\n" +
            "            if (rightResult instanceof BigInteger) {\n" +
            "                throw new RuntimeException(\"not support!\");\n" +
            "            }\n" +
            "            if (rightResult instanceof BigDecimal) {\n" +
            "                return (new BigDecimal(String.valueOf(leftResult))).divide((BigDecimal) rightResult);\n" +
            "            }\n" +
            "        }\n" +
            "        if (leftResult instanceof BigInteger) {\n" +
            "            if (rightResult instanceof Integer || rightResult instanceof Double || rightResult instanceof Float || rightResult instanceof BigDecimal) {\n" +
            "                throw new RuntimeException(\"not support!\");\n" +
            "            }\n" +
            "            if (rightResult instanceof BigInteger) {\n" +
            "                return ((BigInteger) leftResult).divide((BigInteger) rightResult);\n" +
            "            }\n" +
            "        }\n" +
            "        if (leftResult instanceof BigDecimal) {\n" +
            "            BigDecimal left = (BigDecimal) leftResult;\n" +
            "            if (rightResult instanceof Integer || rightResult instanceof Double || rightResult instanceof Float) {\n" +
            "                return left.divide(new BigDecimal(String.valueOf(rightResult)));\n" +
            "            }\n" +
            "            if (rightResult instanceof BigInteger) {\n" +
            "                throw new RuntimeException(\"not support!\");\n" +
            "            }\n" +
            "            if (rightResult instanceof BigDecimal) {\n" +
            "                return left.divide(((BigDecimal) rightResult));\n" +
            "            }\n" +
            "        }\n" +
            "        throw new RuntimeException(\"not support!\");\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public int priority() {\n" +
            "        return 1;\n" +
            "    }\n" +
            "}\n" +
            "```\n" +
            "\n" +
            "## 自定义函数扩展\n" +
            "\n" +
            "### 函数接口 IFunction\n" +
            "\n" +
            "```java\n" +
            "/**\n" +
            " * 扩展的函数接口\n" +
            " */\n" +
            "public interface IFunction {\n" +
            "\n" +
            "    /**\n" +
            "     * 具体的函数执行逻辑\n" +
            "     * @param params\n" +
            "     * @param env\n" +
            "     * @return\n" +
            "     */\n" +
            "    Object exec(Object[] params, Map<String, Object> env);\n" +
            "\n" +
            "    /**\n" +
            "     * 函数名\n" +
            "     * @return\n" +
            "     */\n" +
            "    String getName();\n" +
            "\n" +
            "    /**\n" +
            "     * 函数参数\n" +
            "     * @return\n" +
            "     */\n" +
            "    List<FunctionParam> getParams();\n" +
            "}\n" +
            "```\n" +
            "\n" +
            "### 函数参数说明 FunctionParam\n" +
            "\n" +
            "```java\n" +
            "public class FunctionParam {\n" +
            "    private String name;\n" +
            "    private Class<?> type;\n" +
            "\n" +
            "    public String getName() {\n" +
            "        return name;\n" +
            "    }\n" +
            "\n" +
            "    public void setName(String name) {\n" +
            "        this.name = name;\n" +
            "    }\n" +
            "\n" +
            "    public Class<?> getType() {\n" +
            "        return type;\n" +
            "    }\n" +
            "\n" +
            "    public void setType(Class<?> type) {\n" +
            "        this.type = type;\n" +
            "    }\n" +
            "}\n" +
            "```\n" +
            "\n" +
            "### 自定义函数 Max\n" +
            "\n" +
            "```java\n" +
            "/**\n" +
            " * 求两个整数的较大数的函数\n" +
            " */\n" +
            "public class Max implements IFunction{\n" +
            "\n" +
            "    @Override\n" +
            "    public Object exec(Object[] params, Map<String, Object> env) {\n" +
            "        return Math.max((Integer) params[0], (Integer) params[1]);\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public String getName() {\n" +
            "        return this.getClass().getSimpleName();\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public List<FunctionParam> getParams() {\n" +
            "        List<FunctionParam> params = new ArrayList<>();\n" +
            "        FunctionParam param1 = new FunctionParam();\n" +
            "        param1.setName(\"left\");\n" +
            "        param1.setType(Integer.class);\n" +
            "        FunctionParam param2 = new FunctionParam();\n" +
            "        param2.setName(\"right\");\n" +
            "        param2.setType(Integer.class);\n" +
            "        params.add(param1);\n" +
            "        params.add(param2);\n" +
            "        return params;\n" +
            "    }\n" +
            "}\n" +
            "```\n" +
            "\n" +
            "### 自定义函数 Plus100\n" +
            "\n" +
            "```java\n" +
            "/**\n" +
            " * 给一个整数加100的函数\n" +
            " */\n" +
            "public class Plus100 implements IFunction{\n" +
            "\n" +
            "    @Override\n" +
            "    public Object exec(Object[] params, Map<String, Object> env) {\n" +
            "        Object param = params[0];\n" +
            "        return (Integer) param + 100;\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public String getName() {\n" +
            "        return this.getClass().getSimpleName();\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public List<FunctionParam> getParams() {\n" +
            "        List<FunctionParam> params = new ArrayList<>();\n" +
            "        FunctionParam param1 = new FunctionParam();\n" +
            "        param1.setName(\"p1\");\n" +
            "        param1.setType(Integer.class);\n" +
            "        params.add(param1);\n" +
            "        return params;\n" +
            "    }\n" +
            "}\n" +
            "```\n" +
            "\n" +
            "## 入口\n" +
            "\n" +
            "函数的收集机制依赖于`ServiceLoader`\n" +
            "\n" +
            "```java\n" +
            "/**\n" +
            " * 计算器\n" +
            " * @author skyline\n" +
            " */\n" +
            "public class Calculation {\n" +
            "\n" +
            "    private static final Map<String, IFunction> FUNCTION_MAP = new HashMap<>();\n" +
            "\n" +
            "    static {\n" +
            "        ServiceLoader<IFunction> load = ServiceLoader.load(IFunction.class);\n" +
            "        for (IFunction iFunction : load) {\n" +
            "            FUNCTION_MAP.put(iFunction.getName().toUpperCase(Locale.ROOT), iFunction);\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    public static Object exec(String exp) {\n" +
            "        return exec(exp, null);\n" +
            "    }\n" +
            "\n" +
            "    public static Object exec(String exp, Map<String, Object> env) {\n" +
            "        Expression root = new Expression();\n" +
            "        root.setText(exp);\n" +
            "        root.setFunctionMap(FUNCTION_MAP);\n" +
            "        root.setEnv(env);\n" +
            "        Node parse = root.parse();\n" +
            "        return parse.getValue();\n" +
            "    }\n" +
            "}\n" +
            "```\n" +
            "\n" +
            "# 测试\n" +
            "\n" +
            "## 测试样例\n" +
            "\n" +
            "跑一下测试样例看看\n" +
            "\n" +
            "```java\n" +
            "/**\n" +
            " * @author skyline\n" +
            " * @date 2022/1/18\n" +
            " **/\n" +
            "public class CalculationTest {\n" +
            "\n" +
            "    private static final Logger logger = LoggerFactory.getLogger(CalculationMain.class);\n" +
            "\n" +
            "    @Test\n" +
            "    public void test1() {\n" +
            "        String exp = \"(2-1)*3+2+(3*(9-(5+2)*1))\";\n" +
            "        exe(exp, null);\n" +
            "    }\n" +
            "\n" +
            "    @Test\n" +
            "    public void test2() {\n" +
            "        String exp = \"Max(8,Max(5,4))+plus100(max(3,9))\";\n" +
            "        exe(exp, null);\n" +
            "    }\n" +
            "\n" +
            "\n" +
            "    @Test\n" +
            "    public void test3() {\n" +
            "        String exp = \"Max(8,Max(5,cc))*2+plus100(max(aa*2,bb))\";\n" +
            "        Map<String, Object> env = new HashMap<>();\n" +
            "        env.put(\"cc\", 4);\n" +
            "        env.put(\"aa\", 3);\n" +
            "        env.put(\"bb\", 9);\n" +
            "        exe(exp, env);\n" +
            "    }\n" +
            "\n" +
            "    private void exe(String exp, Map<String, Object> env) {\n" +
            "        logger.info(\"exp is {}\", exp);\n" +
            "        if (env != null) {\n" +
            "            logger.info(\"env is {}\", env);\n" +
            "        }\n" +
            "        Object exec = Calculation.exec(exp, env);\n" +
            "        logger.info(\"my answer is {}\", exec);\n" +
            "    }\n" +
            "}\n" +
            "```\n" +
            "\n" +
            "## 测试结果\n" +
            "\n" +
            "![在这里插入图片描述](http://10.2.6.29:9700/upload/2022/01/927885db435044819af98e83ff16a79e.png)\n" +
            "\n" +
            "# 总结\n" +
            "\n" +
            "以前总是觉得这个东西很神奇，想试试但又不知道从何下手，最近终于有机会尝试了一下，发现其实就是树形的递归操作，原来也没那么复杂。很多东西还是要动手的，**纸上得来终觉浅，绝知此事要躬行**。";


    @org.junit.Test
    public void test() {
        System.out.println(aa);
    }

    @org.junit.Test
    public void test2() {
        int length = aa.length();
        System.out.println("文章总字数：" + length);
        int step = 0;
        int begin = 0;
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            char charAt = aa.charAt(i);
            if (step == 0) {
                if (charAt == '!') {
                    step++;
                    begin = i;
                }
            } else if (step == 1) {
                if (charAt == '[') {
                    step++;
                } else {
                    step = 0;
                }
            } else if (step == 2) {
                if (charAt == ']') {
                    step++;
                }
            } else if (step == 3) {
                if (charAt == '(') {
                    step++;
                } else {
                    step = 0;
                }
            } else if (step == 4) {
                if (charAt == ')') {
                    strings.add(aa.substring(begin, i + 1));
                    step = 0;
                }
            }
        }
        for (String string : strings) {
            System.out.println(string);
        }
    }
}
