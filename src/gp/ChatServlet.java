package gp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.xmpp.MessageBuilder;

import javax.jdo.PersistenceManager;
import javax.servlet.http.*;

import org.json.JSONObject;
import org.mortbay.log.Log;

import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.Message;
import com.google.appengine.api.xmpp.SendResponse;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;



@SuppressWarnings("serial")
public class ChatServlet extends HttpServlet {
	private static final Logger log = Logger
			.getLogger(UploadPhotosServlet.class.getName());

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		XMPPService xmpp = XMPPServiceFactory.getXMPPService();
		Message message = xmpp.parseMessage(req);

		JID fromJid = message.getFromJid();

		String body = message.getBody();
		String msgBody = "Oops!!";
		String type = body.substring(0, 4);
		if (type.equalsIgnoreCase("BOOK")) {
			msgBody = "Sorry No Book Found";
			body = body.trim();
			PersistenceManager pm = PMF.get().getPersistenceManager();
			String query = "select from " + Books.class.getName()
					+ " where book == bookNameParam "
					+ " parameters String bookNameParam";
			List<Books> books = (List<Books>) pm.newQuery(query).execute(
					body.toLowerCase());
			if (!books.isEmpty()) {
				for (Books bk : books) {
					msgBody += "Book:" + body.substring(4) + "\nAuthor: "
							+ bk.getAuthor();
					break;
				}
			} else {
				msgBody = "No Books found";
			}
		} else if (type.equalsIgnoreCase("IMDB")) {
			String q = body.substring(5);
			q = q.replaceAll(" ", "+");
			log.info(" Movie "+q);
			try {
				URL url = new URL("http://www.deanclatworthy.com/imdb/?q=" + q);
				BufferedReader in = new BufferedReader(new InputStreamReader(
						url.openStream(), "UTF-8"));
				String ch = in.readLine();
				String JSONString = new String(ch);
				while (ch != null) {
					ch = in.readLine();
					JSONString += ch;
				}

				log.info("JSON String "+JSONString);
				JSONObject json = new JSONObject(JSONString);
				StringBuffer buffer = new StringBuffer("Movie Details");
				
				buffer.append("\nTitle:" + json.get("title"));
				buffer.append("\nYear:" + json.get("year"));
				buffer.append("\nRating:" + json.get("rating"));
				buffer.append("\nVotes:" + json.get("votes"));
				buffer.append("\nGenre:" + json.get("genres"));
				buffer.append("\nLanguages:" + json.get("languages"));
				buffer.append("\nURL:" + json.get("imdburl"));

				log.info("Title:" + json.get("title"));
				log.info("Rating:" + json.get("rating"));
				log.info("URL:" + json.get("imdburl"));
				log.info("Languages:" + json.get("languages"));
				msgBody = buffer.toString();
				in.close();
			} catch (Exception e) {
				System.out.println(" Error" + e.getMessage());
			}
		}
		log.info("Jid " + fromJid.getId());
		log.info("Jid.toString " + fromJid.toString());
		Message msg = new MessageBuilder().withRecipientJids(fromJid).withBody(
				msgBody).build();

		boolean messageSent = false;
		if (xmpp.getPresence(fromJid).isAvailable()) {
			SendResponse status = xmpp.sendMessage(msg);
			messageSent = (status.getStatusMap().get(fromJid) == SendResponse.Status.SUCCESS);
		}

	}
}