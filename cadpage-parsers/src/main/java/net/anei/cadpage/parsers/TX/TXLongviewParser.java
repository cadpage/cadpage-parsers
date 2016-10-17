package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;



public class TXLongviewParser extends DispatchProQAParser {
  
  public TXLongviewParser() {
    super("LONGVIEW", "TX", 
          "PRI P_EXT? CALL CALL/L+? ADDR/Z CITY TIME! INFO/L+");
  }
  
  @Override
  public String getFilter() {
    return "nettechs@championems.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("P_EXT")) return new MyPriorityExtendField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyPriorityExtendField extends PriorityField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!data.strPriority.endsWith(" P")) return false;
      if (field.length() != 1) return false;
      if (!Character.isDigit(field.charAt(0))) return false;
      data.strPriority = 'P' + field;
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.equalsIgnoreCase("<None>")) return;
      super.parse(field, data);
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equalsIgnoreCase("<Unknown>")) return;
      super.parse(field, data);
    }
  }
}
