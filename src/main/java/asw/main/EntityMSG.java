package asw.main;

public class EntityMSG implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 230299176521011753L;
	/**
	 * 属方：1：红方，-1 ：蓝方， 0：中立方
	 */
	public int belong; 
	/**
	 * 状态：true:生存，false:阵亡
	 */
	public boolean status; 
	/**
	 * 位置信息 -x
	 */
	public double x;
	/**
	 * 位置信息 -y
	 */
	public double y;
	
	/**
	 * 实体名称
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
