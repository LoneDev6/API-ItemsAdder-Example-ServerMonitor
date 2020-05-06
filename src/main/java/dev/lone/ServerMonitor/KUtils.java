package dev.lone.ServerMonitor;

import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Admin on 24/7/2015.
 */
public class KUtils
{
	public String b = "%%__USER__%%";

	static DecimalFormat f = new DecimalFormat("##.00");


	static HashMap<String, Class> nmsClassCache = new HashMap<>();

	public static void removeEnchantments(ItemStack item)
	{
		for(Enchantment e : item.getEnchantments().keySet())
		{
			item.removeEnchantment(e);
		}
	}
	public static String addSpace(String s){
		return s.replace("-"," ");
	}
	public static float getRandom(String level) {
		if(level.contains("-")){
			String[] spl = level.split("-");
			return round(randomNumber(Float.parseFloat(spl[0]), Float.parseFloat(spl[1])),2);
		}
		else return Integer.parseInt(level);
	}
	public static int getRandomInt(String level) {
		if(level.contains("-")){
			String[] spl = level.split("-");
			return getRandomInt(Integer.parseInt(spl[0]), Integer.parseInt(spl[1]));
		}
		else return Integer.parseInt(level);
	}
	public static float round(float d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(Float.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.floatValue();
	}

	public static String round(double value, int places, boolean showZeroes)
	{
		if (places < 0) throw new IllegalArgumentException();

		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);

		String result = bd.doubleValue()+"";
		if(!showZeroes)
			result = result
					.replace(".00", "")
					.replace(".0", "");
		return result;
	}

	@SuppressWarnings("unused")
	public static float randomNumber(float f,float g){
		Random random = new Random();
		float number = random.nextFloat()* (g - f) + f;
		return random.nextFloat()* (g - f) + f;
	}
	public static int getRandomInt(int min, int max){
		Random random = new Random();
		return random.nextInt((max - min)+1) + min;
	}
	public static boolean getSuccess(int percent){
		int i = getRandomInt(1,100);
		if (i<=percent) return true;
		return false;
	}
	public static boolean hasPermmision(Player p, String perm){
		if(p.hasPermission(perm)) return true;
		if(p.isOp()) return true;
		return false;
	}
	public static String backColor(String name) {
		return name.replace("\u00A7","&");
	}
	public static String convertColor(String name){
		return name.replace("&","\u00A7");
	}

	public static boolean isColored(String str)
	{
		return str.contains("\u00A7([0-fk-or])") || str.contains("&([0-fk-or])");
	}

	/**
	 * @param s check if it is numeric
	 * @return
	 */
	public static boolean isN(String s) {//isNumeric
		return s.matches("\\d+");
	}

	public static int[] HexColorToRGB(final String hex)
	{
		////System.out.println(hex);
		final int[] ret = new int[3];
		for (int i = 0; i < 3; i++)
		{
			ret[i] = (int) Integer.parseInt(hex.substring(i * 2, i * 2 + 2), 16);
		}
		return ret;
	}
	public static int hexToInt(String hex)
	{
		try
		{
			return Integer.parseInt(new StringBuffer(hex).reverse().toString(), 16);
		}
		catch (NumberFormatException ex)
		{
			return 0;
		}
	}

	private static java.awt.Color hex2Rgb(String colorStr) {
		try
		{
			return new java.awt.Color(
					Integer.valueOf(colorStr.substring(1, 3), 16),
					Integer.valueOf(colorStr.substring(3, 5), 16),
					Integer.valueOf(colorStr.substring(5, 7), 16)
			);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public static Color hexStrToBukkitColor(String colorStr)
	{
		if(colorStr == null)
			return null;
		//fix java.lang.NumberFormatException: For input string: ".0"
		if(colorStr.endsWith(".0"))
			colorStr = colorStr.replace(".0", "");
		colorStr = colorStr.replace(".", "");
		//fix java.lang.StringIndexOutOfBoundsException: String index out of range: 7
		while(colorStr.length() < 7)
			colorStr = colorStr + "0";
		if(!colorStr.startsWith("#"))
			colorStr = "#" + colorStr;

		java.awt.Color javaColor = hex2Rgb(colorStr);
		return Color.fromRGB(javaColor.getRed(), javaColor.getGreen(), javaColor.getBlue());
	}


	public static int ceilDivision(float a, float b)
	{
		return (int) Math.ceil(a / b);

	}

	public static ItemStack renameItemStack(ItemStack item, String name)
	{
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}


	public static int parseInt(String number, int defaultValue)
	{
		try
		{
			return Integer.parseInt(number);
		}catch(Exception e){}
		return defaultValue;
	}


	public static void sendPacket(Player p, Object packet) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchFieldException, ClassNotFoundException {
		Object nmsPlayer = p.getClass().getMethod("getHandle").invoke(p);
		Object plrConnection = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
		plrConnection.getClass().getMethod("sendPacket", getNmsClass("Packet")).invoke(plrConnection, packet);
	}

	public static Class<?> getNmsClass(String nmsClassName) throws ClassNotFoundException {

		if(nmsClassCache.containsKey(nmsClassName))
		{
			return nmsClassCache.get(nmsClassName);
		}

		Class clazz = Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + "." + nmsClassName);
		nmsClassCache.put(nmsClassName, clazz);

		return clazz;
	}


	public static void destroyEntity(Player player, Entity entity)
	{
		Object packet = null;
		try
		{
			packet = dev.lone.itemsadder.Utils.KUtils.getNmsClass("PacketPlayOutEntityDestroy").getConstructor(int[].class).newInstance(new int[] {entity.getEntityId()});
			dev.lone.itemsadder.Utils.KUtils.sendPacket(player, packet);
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException | NoSuchFieldException e)
		{
			e.printStackTrace();
		}
	}
	public static void destroyEntity(Entity entity)
	{
		Object packet = null;
		try
		{
			packet = dev.lone.itemsadder.Utils.KUtils.getNmsClass("PacketPlayOutEntityDestroy").getConstructor(int[].class).newInstance(new int[] {entity.getEntityId()});
			for(Player p : Bukkit.getOnlinePlayers())
				dev.lone.itemsadder.Utils.KUtils.sendPacket(p, packet);
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException | NoSuchFieldException e)
		{
			e.printStackTrace();
		}
	}

	public static int booleansToHex(boolean ... m)
	{
		String binaryStr = "";
		for (boolean bit : m) {
			binaryStr = binaryStr + ((bit) ? "1" : "0" );
		}
		int decimal = Integer.parseInt(binaryStr , 2);
		return decimal;
		//String hexStr = Integer.toString(decimal , 16);
	}

	 /*public static Color hex2Rgb(String colorStr)
	 {
		 //System.out.println("prima " + colorStr);
		 
		 
		 //fix java.lang.NumberFormatException: For input string: ".0"
		 if(colorStr.endsWith(".0"))
		 {
			 colorStr = colorStr.replace(".0", "");
			 //fix java.lang.StringIndexOutOfBoundsException: String index out of range: 7
			 while(colorStr.length() < 7)
			 {
				 colorStr = "0" + colorStr;
			 }
		 }
		 
		 if(!colorStr.startsWith("#"))
			 colorStr = "#" + colorStr;
		 
		 //System.out.println("dopo " + colorStr);
		 //System.out.println("r " + Integer.valueOf( colorStr.substring( 1, 3 ), 16 ));
		 //System.out.println("g " + Integer.valueOf( colorStr.substring( 3, 5 ), 16  ));
		 //System.out.println("b " + Integer.valueOf( colorStr.substring( 5, 7 ), 16 ));
        return Color.fromBGR(
                Integer.valueOf( colorStr.substring( 1, 3 ), 16 ),
                Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
                Integer.valueOf( colorStr.substring( 5, 7 ), 16 ) );
	    }*/

	 public static Color bukkitColorFromString(String potionColorStr)
	 {
		 try
		 {
			 return (Color) FieldUtils.readStaticField(Color.class, potionColorStr);
		 } catch (IllegalAccessException | IllegalArgumentException e)
		 {
		 }
		 return null;
	 }
}
