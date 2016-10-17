package net.anei.cadpage.parsers.MN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchPrintrakParser;


/**
 * Minneapolis/St Paul, MN
 */
public class MNMinneapolisStPaulAParser extends DispatchPrintrakParser {
  
  private static final Pattern ID_PTN = Pattern.compile("^[A-Z]{3}\\d{12} ");
  
  public MNMinneapolisStPaulAParser() {
    super("MINNEAPOLIS", "MN");
  }
  
  @Override
  public String getLocName() {
    return "Minneapolis/St Paul, MN";
  }

  @Override
  public String getFilter() {
    return "@logis.org";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    body = stripFieldStart(body, "RESPONSE INFORMATION PAGE //");
    if (ID_PTN.matcher(body).find()) {
      body = "INC: " + body;
    } else if (!body.contains("TYP:")) {
      body = "TYP: " + body;
    }
    return super.parseMsg(body, data);
  }
  
  public String getProgram() {
    return super.getProgram().replace("CALL", "CALL UNIT");
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("PLACE")) return new MyPlaceField();
    return super.getField(name);
  }
  
  private static final Pattern PLACE_ID_PTN = Pattern.compile("(.*) \\(ID:(.*)\\)");
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = PLACE_ID_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strCallId = match.group(2).trim();
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE ID";
    }
  }
}
