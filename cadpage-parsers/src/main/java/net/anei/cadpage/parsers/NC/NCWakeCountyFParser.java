package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NCWakeCountyFParser extends FieldProgramParser {

  public NCWakeCountyFParser() {
    super("WAKE COUNTY", "NC",
          "CALL:CALL! ADDR:ADDRCITYST! ID:ID! PRI:PRI INFO/N+");
  }

  @Override
  public String getFilter() {
    return "sparta@ncdps.gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("NCEM 24HR WATCH NOTIFICATION")) return false;
    int pt = body.indexOf("\n DO NOT REPLY");
    if (pt >= 0) body = body.substring(0, pt).trim();
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(", USA,");
      if (pt > 0) field = field.substring(0, pt).trim();
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_HDR_PTN = Pattern.compile("(?:INFO|UPDATE): *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_HDR_PTN.matcher(field);
      if (match.lookingAt()) field = field.substring(match.end());
      super.parse(field, data);
    }
  }
}
