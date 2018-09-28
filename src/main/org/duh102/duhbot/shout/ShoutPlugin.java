package org.duh102.duhbot.shout;

import java.util.*;

import org.pircbotx.Colors;
import org.pircbotx.User;
import org.pircbotx.Channel;
import org.pircbotx.hooks.*;
import org.pircbotx.hooks.types.GenericMessageEvent;
import org.pircbotx.hooks.events.*;

import org.duh102.duhbot.functions.*;

import org.duh102.duhbot.shout.exceptions.*;

public class ShoutPlugin extends ListenerAdapter implements DuhbotFunction
{
  ShoutDB db;
  public ShoutPlugin() {
    try {
      db = ShoutDB.getDBInstance();
    } catch( InvalidDBConfiguration | InvalidEnvironment idc ) {
      idc.printStackTrace();
      db = null;
    }
  }
  public ShoutPlugin(ShoutDB db) {
    this.db = db;
  }

  public void onMessage(MessageEvent event) {
    String message = Colors.removeFormattingAndColors(event.getMessage()).trim();
    String username = event.getuser.getnick();
    long now = System.currentTimeMillis();
  }

  public void onAction(ActionEvent event) {
    String message = Colors.removeFormattingAndColors(event.getMessage()).trim();
    String username = event.getuser.getnick();
    long now = System.currentTimeMillis();
  }

  public HashMap<String,String> getHelpFunctions() {
    HashMap<String,String> helpFunctions = new HashMap<String,String>();
    helpFunctions.put("c", "h");
    return helpFunctions;
  }

  public String getPluginName() {
    return "Shout Plugin";
  }

  public ListenerAdapter getAdapter() {
    return this;
  }
}
