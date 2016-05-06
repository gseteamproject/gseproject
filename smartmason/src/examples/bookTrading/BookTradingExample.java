/**
 * http://jade.tilab.com/doc/tutorials/JADEProgramming-Tutorial-for-beginners.pdf
 * 
 * before closing example all user-agents should be killed
 * otherwise Eclipse must be restarted
 */

package examples.bookTrading;

import jade.Boot;

public class BookTradingExample
{
	public static void main(String[] p_args)
	{
		System.out.println("Book trading example started ...");		
		
		String[] parameters = new String[ 2 ];
		parameters[ 0 ] = "-gui";
		parameters[ 1 ] = "reader:examples.bookTrading.BookBuyerAgent(" + BookTitle.LORD_OF_THE_RINGS + ");";
		parameters[ 1 ] += "noviceProgrammer:examples.bookTrading.BookBuyerAgent(" + BookTitle.JAVA_TUTORIAL + ");";
		parameters[ 1 ] += "advancedProgrammer:examples.bookTrading.BookBuyerAgent(" + BookTitle.JADE_PROGRAMMING_TUTORIAL + ");";
		parameters[ 1 ] += "seller1:examples.bookTrading.BookSellerAgent;";
		parameters[ 1 ] += "seller2:examples.bookTrading.BookSellerAgent;";

		Boot.main( parameters );
	}
}