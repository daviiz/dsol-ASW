package asw.om;

/**
 * 
 * @author daiwenzhi
 * @DATATIME 2018年12月25日 下午4:18:28
 */
public class SimParameter {

	/// Surface ship:

	/**
	 * (m) The volume of the surface ship .
	 */
	public static int Surface_Ship = 1335;

	/**
	 * (knts) ,The movement speed of the surface ship. 1 Knot = 0.51444444 Meters
	 * per Second.
	 */
	public static int Speed_Ship = (int) (15 * 0.514);

	/**
	 * (degree/s) The revolution angle per second of the surface ship.
	 */
	public static int Revolution_angle_Ship = 3;

	/**
	 * (m) The detection range of the surface ship.
	 */
	public static int Detection_range_Ship = 3000;

	/// Torpedo:
	/**
	 * (s) The live time of the torpedo.
	 */
	public static int Live_time_Torpedo = 2400;

	/**
	 * (degree) The sonar beam angle of the torpedo. 鱼雷的声纳射束角度
	 */
	public static int Sonar_beam_Torpedo = 24;

	/**
	 * (degree) The sonar beam steering angle of the torpedo. 鱼雷的声纳波束转向角
	 */
	public static int Sonar_beam_steering_Torpedo = 0;

	/**
	 * (degree) 从攻击平台发射的角度，受随机变量的影响。
	 */
	public static int Angle_to_launch_Torpedo = 10;

	/**
	 * The maximum turning angle when the snake search pattern is used.
	 */
	public static int Angle_of_sweep_forward_Torpedo = 40;

	/**
	 * (degree) The revolution angle per second of the Torpedo.
	 */
	public static int Revolution_angle_per_second_Torpedo = 3;

	/**
	 * (knts) Low speed:18, Middle speed:23, High speed:35, The different speed is
	 * applied according to the pattern of torpedo movement.
	 */
	public static int Speed_Torpedo = (int) (35 * 0.514);

	/// Submarine:
	/**
	 * (m) The volume of the submarine.
	 */
	public static int Length_Submarine = 23010;

	/**
	 * (knts) The movement speed of the submarine.
	 */
	public static int Speed_Submarine = (int) (12 * 0.514);

	/**
	 * (degree/s) The revolution angle per second of the submarine.
	 */
	public static int Revolution_angle_per_second_Submarine = 3;
	/**
	 * (m) The detection range of the submarine.
	 */
	public static int Detection_range_Submarine = 15000;

	//////////////// Decoy
	/**
	 * The motion types are mobile and static decoy. 1: static 2: mobile
	 */
	public static int Motion_type_Decoy = 1;
	/**
	 * The launch types are rocket and air pressed. 1 : rocket 2 : air pressed
	 */
	public static int Launch_type_Decoy = 1;
	/**
	 * The number of decoys
	 */
	public static int Num_of_decoys = 2;
	/**
	 * (knts) The speed of the mobile decoys
	 */
	public static int Speed_of_decoy = (int) (12 * 0.514);

	/**
	 * (%) The probability of working normally (influenced by the random variable).
	 */
	public static int Reliability_Decoy = 90;

	/**
	 * (degree/s) The delay time after launch command.
	 */
	public static int Launch_time_Decoy = 3;

	/**
	 * (s) Time period from launch to expiration 鱼雷诱饵的生命周期
	 */
	public static int Operation_time_Decoy = 540;

	/**
	 * (dB) The source level of the decoy.
	 */
	public static int Source_level_Decoy = 140;

}
