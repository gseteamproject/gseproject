package examples.bookTrading;

import java.util.Random;

public class Book
{	
	public String title;
	
	public Integer price;

	public Book(String p_title, Integer p_price)
	{
		title = p_title;
		price = p_price;
	}
	
	public Book(String p_title)
	{
		this(p_title, new Random().nextInt(100));		
	}
}
