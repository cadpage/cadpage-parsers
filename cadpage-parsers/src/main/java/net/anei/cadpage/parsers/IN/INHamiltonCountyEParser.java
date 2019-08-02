package net.anei.cadpage.parsers.IN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.MsgParser;

public class INHamiltonCountyEParser extends MsgParser {
  
  public INHamiltonCountyEParser() {
    super("HAMILTON COUNTY", "IN");
    setFieldList("UNIT INFO");
  }
  
  @Override
  public String getFilter() {
    return "notification@smartsheet.com";
  }
  
  private static final Pattern STATUS_CHANGE_PTN = Pattern.compile("(\\S+) Status Changed to .*");
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = STATUS_CHANGE_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.msgType = MsgType.GEN_ALERT;
    data.strUnit = match.group(1);
    data.strSupp = subject;
    return true;
  }

}
