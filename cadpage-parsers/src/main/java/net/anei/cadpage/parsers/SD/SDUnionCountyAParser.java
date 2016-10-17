package net.anei.cadpage.parsers.SD;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;


public class SDUnionCountyAParser extends SDUnionCountyBaseParser {
  
  private static final Pattern SUBJECT_MARKER = Pattern.compile("^(?:\\(\\d+\\) +)?J:");
  
  public SDUnionCountyAParser() {
    super("CALL+? ADDR/SXP X/Z? SRC UNIT! INFO+");
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!SUBJECT_MARKER.matcher(subject).find()) return false;
    return super.parseFields(body.split("/"), 5, data);
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      data.strCall = append(data.strCall, "/", field);
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.endsWith("&")) field = field.substring(0,field.length()-1).trim();
      super.parse(field, data);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }
}
