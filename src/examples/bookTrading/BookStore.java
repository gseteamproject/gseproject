package examples.bookTrading;

import java.util.Hashtable;

public class BookStore
{
	private Hashtable<String, Integer> books = new Hashtable<String, Integer>();
	
	public boolean addBook(Book p_book)
	{
		books.put(p_book.title, p_book.price);
		return true;
	}
	
	public Book findBookByTitle(String p_title)
	{
		Integer price = (Integer) books.get(p_title);
		if( price != null)
		{
			return new Book(p_title, price);
		}
		return null;
	}
	
	public boolean removeBook(Book p_book)
	{
		Integer price = (Integer) books.remove(p_book.title);
		if( price != null)
		{
			return true;
		}
		return false;
	}
}