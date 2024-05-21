package trees;

public class Node implements Comparable<Node> {
    
    private String data;
    private String value;
    private int scopeId;
    private Node parent;
    private Node left;
    private Node right;
    private int balanceFactor;

    public Node() {
        this.data = null;
        this.parent = null;
        this.left = null;
        this.right = null;
        this.balanceFactor = 0;
        this.scopeId = 0;
    }

    public Node(String data) {
        this.data = data;
        this.parent = null;
        this.left = null;
        this.right = null;
        this.balanceFactor = 0;
        this.scopeId = 0;
    }

    public Node(String data, int scopeId) {
        this.data = data;
        this.scopeId = scopeId;
        this.parent = null;
        this.left = null;
        this.right = null;
        this.balanceFactor = 0;
    }

    public Node(String data, Node parent) {
        this(data);
        this.parent = parent;
    }

    public Node(String data, int scopeId, Node parent) {
        this(data, scopeId);
        this.parent = parent;
    }

    public Node(String data, Node left, Node right, Node parent) {
        this(data, parent);
        this.left = left;
        this.right = right;
    }

    public String getData() { return data; }
    
    public String getValue() { return value; }
    
    public void setValue(String newValue) { this.value = newValue; }
    
    public int getScopeId() { return scopeId; }

    public void setScopeId(int scopeId) { this.scopeId = scopeId; }

    public void setData(String data) { this.data = data; }

    public Node getParent() { return parent; }

    public void setParent(Node parent) { this.parent = parent; }

    public Node getLeft() { return left; }
    
    

    public void setLeft(Node left) { 
        this.left = left; 
        if (this.left != null) {// atualiza a referencia do no filho automaticamente
            this.left.setParent(this);
        }
        this.updateBalanceFactor();
    }

    public Node getRight() { return right; }

    public void setRight(Node right) { 
        this.right = right;
        if (this.right != null) {// atualiza a referencia do no filho automaticamente
            this.right.setParent(this);
        }
        this.updateBalanceFactor();
    }
    
    public int getBalanceFactor() {
        updateBalanceFactor(); 
        return balanceFactor; // retorna o fb atualizado
    }

    private void updateBalanceFactor() { // calculo do fb a partir das alturas das sub-árvores
        int leftHeight = (this.left != null) ? this.left.getHeight() : -1;
        int rightHeight = (this.right != null) ? this.right.getHeight() : -1;
        this.balanceFactor = rightHeight - leftHeight; 
    }

    public boolean isRoot() {//se não tiver parente, é uma raiz
        return(this.parent == null);
    }
    
    public boolean isLeaf() {//se não possuir filhos, então é uma folha
        return(this.left == null && this.right == null);
    }
    
    public int getDegree() {// calcula o numero de filhos do nó
        int count = 0;
        
        if(this.left != null) {
            count++;
        }
        if(this.right != null) {
            count++;
        }
        
        return count;
    }
    
    public int getLevel() { return getLevel(this); }
    
    private int getLevel(Node node) {//percorre recursivamente do nó ate a raiz da arvore para saber o nivel
        if(node.parent == null) {
            return 0;
        }else {
        
            return 1 + getLevel(node.parent);
        }
    }
    
    public int getHeight() { return getHeight(this); }
    
    private int getHeight(Node node) { //percorre recursivamente ate encontrar o nó folha do maior caminho e então soma 1
        if(node == null) {
            return -1;
        }else {
            
            int leftHeight = getHeight(node.left);
            int rightHeight = getHeight(node.right);
            
            if(leftHeight > rightHeight) { return 1 + leftHeight;}
            else { return 1 + rightHeight; }
        }
        
    }

    @Override
    public int compareTo(Node other) {
        int dataComparison = this.data.compareTo(other.data);
        if (dataComparison != 0) {
            return dataComparison;
        } else {
            return Integer.compare(this.scopeId, other.scopeId);
        }
    }

    @Override
    public String toString() {
        return "node: " + data
                + ", scopeId: " + scopeId
                + ", isRoot?: " + (this.isRoot())
                + ", isLeaf?: " + (this.isLeaf())
                + ", Degree: " + (this.getDegree())
                + ", Level: " + (this.getLevel())
                + ", Height: " + (this.getHeight());
    }
    
}
