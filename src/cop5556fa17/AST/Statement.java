package cop5556fa17.AST;

import cop5556fa17.Scanner.Token;
import cop5556fa17.TypeUtils.Type;

public abstract class Statement extends ASTNode {

	private Type nodeType;

	public Statement(Token firstToken) {
		super(firstToken);
	}

	/**
	 * @return the nodeType
	 */
	public Type getNodeType() {
		return nodeType;
	}

	/**
	 * @param nodeType
	 *            the nodeType to set
	 */
	public void setNodeType(Type nodeType) {
		this.nodeType = nodeType;
	}

}
