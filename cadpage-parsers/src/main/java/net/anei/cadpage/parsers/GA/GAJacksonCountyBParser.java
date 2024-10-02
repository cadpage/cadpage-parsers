package net.anei.cadpage.parsers.GA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;

public class GAJacksonCountyBParser extends FieldProgramParser {

  public GAJacksonCountyBParser() {
    super("JACKSON COUNTY", "GA",
          "DATETIME NAME_PHONE CODE_CALL ADDRCITY PLACE! INFO/N+? ID END");
  }

  @Override
  public String getFilter() {
    return "cadpage@jacksoncountygov.com";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  private static final Pattern DELIM = Pattern.compile(" /|\n");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (subject.equals("Text Message") && body.startsWith("CAD Page:")) {
      body = body.substring(9).trim();
    } else if (!subject.equals("CAD Page")) return false;
    return parseFields(DELIM.split(body), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("NAME_PHONE")) return new MyNamePhoneField();
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("ID")) return new IdField("\\d{4}-\\d{8}\\b.*", true);
    return super.getField(name);
  }

  private static final Pattern NAME_PHONE_PTN = Pattern.compile("(.*?) +(\\d{10})");
  private class MyNamePhoneField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = NAME_PHONE_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        data.strPhone = match.group(2);
      }
      data.strName = field;
    }

    @Override
    public String getFieldNames() {
      return "NAME PHONE";
    }
  }

  private class MyCodeCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf("/ ");
      if (pt < 0) abort();
      data.strCode = field.substring(0,pt).trim();
      data.strCall = field.substring(pt+2).trim();
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.endsWith("/")) {
        data.strPlace = field.substring(0, field.length()-1).trim();
      } else {
        int pt = field.indexOf("/ ");
        if (pt < 0) abort();
        data.strPlace = field.substring(0, pt).trim();
        data.strSupp = field.substring(pt+2).trim();
      }
    }
  }
}
