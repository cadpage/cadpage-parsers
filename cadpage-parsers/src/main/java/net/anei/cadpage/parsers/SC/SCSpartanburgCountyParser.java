package net.anei.cadpage.parsers.SC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchPrintrakParser;

/**
 * Spartanburg County, SC
 */
public class SCSpartanburgCountyParser extends DispatchPrintrakParser {
  
  public SCSpartanburgCountyParser() {
    super("SPARTANBURG COUNTY", "SC");
  }
  
  @Override
  public String getFilter() {
    return "911info@spartanburgcounty.org";
  }
  
  private static final Pattern SRC_PTN = Pattern.compile("([A-Z0-9]+) *- ");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = SRC_PTN.matcher(body);
    if (match.lookingAt()) body = match.group(1) + " INC:" + body.substring(match.end());
    return super.parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  // Call fields have silly ... brackets
  private static final Pattern CALL_PTN = Pattern.compile("\\.*(.*?)\\.*");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      super.parse(field, data);
    }
  }
  
  // Comment fields have silly angle brackets
  private static final Pattern INFO_PTN = Pattern.compile("<* *(.*?) *>*");
  private class MyInfoField extends InfoField {
    
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      super.parse(field, data);
    }
  }
}
