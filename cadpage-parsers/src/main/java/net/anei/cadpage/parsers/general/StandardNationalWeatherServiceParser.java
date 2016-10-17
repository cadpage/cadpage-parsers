package net.anei.cadpage.parsers.general;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class StandardNationalWeatherServiceParser extends MsgParser {
  
  private static final Pattern URL_PTN = Pattern.compile("(.*)\\bMore information(?: at)? *<?(http://.*)>?\\.?");
  
  public StandardNationalWeatherServiceParser() {
    super("", "");
    setFieldList("CALL URL");
  }

  @Override
  public String getLocName() {
    return "National Weather Service";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("iNWS Alert")) return false;
    Matcher match = URL_PTN.matcher(body);
    if (match.matches()) {
      body= match.group(1).trim();
      data.strInfoURL = match.group(2);
    }
    data.strCall = body;
    return true;
  }
}
