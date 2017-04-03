package net.anei.cadpage.parsers.MD;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MDMontgomeryCountyCParser extends FieldProgramParser {
  
  public MDMontgomeryCountyCParser() {
    super("MONTGOMERY COUNTY", "MD", 
          "ID CALL ADDR! Apt/Suite:APT? PLACE Box_Area:BOX! Units:UNIT! END");
  }
  
  @Override
  public String getFilter() {
    return "noreply@everbridge.net";
  }
  
  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    
    if (body.startsWith("<!doctype html>\n")) {
      int pt1 = body.indexOf("CAD MSG:");
      if (pt1 < 0) return false;
      int pt2 = body.indexOf("</p>\n");
      if (pt2 < 0) return false;
      body = body.substring(pt1, pt2).trim();
      return parseMsg(body, data);
    }
    
    return super.parseHtmlMsg(subject, body, data);
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("CAD MSG:")) return false;
    body = body.substring(8).trim();
    return parseFields(body.split(" \\* "), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("F\\d{10}");
    if (name.equals("APT")) return new MyAptField();
    return super.getField(name);
  }
  
  private static final Pattern APT_PTN = Pattern.compile("(?:APT|SUITE|RM|ROOM|UNIT)[ #]*(.*)", Pattern.CASE_INSENSITIVE);
  private class MyAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      super.parse(field, data);
    }
  }
}
