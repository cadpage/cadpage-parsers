package net.anei.cadpage.parsers.WI;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;



public class WICalumetCountyBParser extends DispatchProQAParser {
  
  public WICalumetCountyBParser() {
    super("CALUMET COUNTY", "WI",
          "ADDR PLACE APT? CITY CALL CALL+? ( NONE ID! EMPTY? TIME | ID! ( EMPTY NONE! TIME | ) ) INFO+",
          true);
  }
  
  @Override
  public String getFilter() {
    return "zoll@goldcross.org,donotreply@goldcross.org";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Gold Cross Alert")) return false;
    return parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("APT")) return new AptField("\\d{1,4}[A-Z]?|[A-Z]", true);
    if (name.equals("NONE")) return new SkipField("<None>", true);
    if (name.equals("ID")) return new MyIdField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
  
  private class MyIdField extends IdField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.startsWith("Run# ")) {
        super.parse(field.substring(5).trim(), data);
        return true;
      } else {
        return (field.length() > 0 && "Run# ".startsWith(field));
      }
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
}