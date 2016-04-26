/**
 * http://jade.tilab.com/doc/tutorials/JADEProgramming-Tutorial-for-beginners.pdf
 * 
 * before closing example all user-agents should be killed
 * otherwise Eclipse must be restarted
 */

package examples.bookTrading;

import jade.Boot;

public class BookTrading
{
	public static void main(String[] p_args)
	{
		System.out.println("Book trading example started ...");		
		
		String[] parameters = new String[ 2 ];
		parameters[ 0 ] = "-gui";
		parameters[ 1 ] = "buyer1:examples.bookTrading.BookBuyerAgent(The-Lord-of-the-rings);";
		parameters[ 1 ] += "seller1:examples.bookTrading.BookSellerAgent;";

		Boot.main( parameters );
	}
}