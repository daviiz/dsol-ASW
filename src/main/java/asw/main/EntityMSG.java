package asw.main;

public class EntityMSG implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 230299176521011753L;
	/**
	 * ������1���췽��-1 �������� 0��������
	 */
	public int belong; 
	/**
	 * ״̬��true:���棬false:����
	 */
	public boolean status; 
	/**
	 * λ����Ϣ -x
	 */
	public double x;
	/**
	 * λ����Ϣ -y
	 */
	public double y;
	
	/**
	 * ʵ������
	 */
	public String name;
	
	public EntityMSG(String name,int _belong, boolean _status,double _x,double _y){
		this.name = name;
		this.belong = _belong;
		this.status = _status;
		this.x = _x;
		this.y = _y;
	}
}
