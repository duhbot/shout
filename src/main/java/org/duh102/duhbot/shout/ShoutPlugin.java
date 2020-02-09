package org.duh102.duhbot.shout;

import java.util.*;

import org.duh102.duhbot.shout.db.DBConfig;
import org.duh102.duhbot.shout.db.dao.ShoutDAO;
import org.duh102.duhbot.shout.db.dto.Shout;
import org.pircbotx.Colors;
import org.pircbotx.hooks.*;
import org.pircbotx.hooks.events.*;

import org.duh102.duhbot.functions.*;
import org.pircbotx.hooks.types.GenericMessageEvent;

public class ShoutPlugin extends ListenerAdapter implements ListeningPlugin
{
  private DBConfig db;
  private ShoutDAO dao;
  public ShoutPlugin() {
    db = new DBConfig();
    dao = new ShoutDAO(db);
  }
  public ShoutPlugin(DBConfig db, ShoutDAO dao) {
    this.db = db;
    this.dao = dao;
  }

  public void onMessage(MessageEvent event) {
    String message = cleanMessage(event.getMessage());
    if(! isCandidateMessage(message) ) {
      System.out.printf("Detected '%s' not interesting\n", message);
      return;
    }
    String username = event.getUser().getNick();
    String channel = event.getChannel().getName();
    long now = System.currentTimeMillis();
    Shout newShout = new Shout(0L, message, channel, username, false, now);
    respondToShout(event, newShout);
  }

  public void onAction(ActionEvent event) {
    String message = cleanMessage(event.getMessage());
    if(! isCandidateMessage(message) ) {
      System.out.printf("Detected '%s' not interesting\n", message);
      return;
    }
    String username = event.getUser().getNick();
    String channel = event.getChannel().getName();
    long now = System.currentTimeMillis();
    Shout newShout = new Shout(0L, message, channel, username, true, now);
    respondToShout(event, newShout);
  }

  private void respondToShout(GenericMessageEvent event, Shout newShout) {
    if( !dao.save(newShout) ) {
      event.respond("Unable to save shout :(");
    }
    Shout response = dao.get(newShout.getChannel(), newShout.isAction(), newShout.getId());
    if(response != null && !response.equals(newShout)) {
      event.respond(response.getShout());
    }
  }

  private String cleanMessage(String message) {
    return Colors.removeFormattingAndColors(message).trim();
  }

  private boolean isCandidateMessage(String message) {
    boolean longEnough = message.length() > 4;
    boolean allCaps = message.toUpperCase().equals(message) && !message.toLowerCase().equals(message);
    return longEnough && allCaps;
  }

  public HashMap<String,String> getHelpFunctions() {
    HashMap<String,String> helpFunctions = new HashMap<String,String>();
    helpFunctions.put("YELLING", "Typing an all-caps message, either in an action (/me) or regular chat, " +
            "will prompt the bot to respond with one in kind. May include numbers and punctuation and stuff, " +
            "but all alphabetical characters must be uppercase.");
    return helpFunctions;
  }

  public String getPluginName() {
    return "Shout Plugin";
  }

  public ListenerAdapter getAdapter() {
    return this;
  }
}
