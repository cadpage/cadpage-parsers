package net.anei.cadpage.parsers.CO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class COJeffersonCountyEParser extends FieldProgramParser {

  public COJeffersonCountyEParser() {
    super("JEFFERSON COUNTY", "CO",
          "CALL! ADDR! Apt:APT! PLACE! X GPS1 GPS2 MAP CH! DATETIME UNIT INFO+");
  }

  @Override
  public String getFilter() {
    return "cadpage@jeffcom911.org,messaging@iamresponding.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {

    // They **REALLY** mangled this
    int pt = body.indexOf("\n\n<html>");
    if (pt >= 0) body = body.substring(0, pt).trim();
    return super.parseHtmlMsg(subject, body, data);
  }

  private static final Pattern PREFIX_PTN = Pattern.compile("Unit: (\\S+) Incident (\\d{4}[A-Z]{2}-\\d{7}): *");

  private String delim;

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (subject.equals("Post Dipatch")) data.msgType = MsgType.RUN_REPORT;

    if (body.startsWith("CAUTION: ")) {
      int pt = body.indexOf('\n');
      if (pt < 0) return false;
      body = body.substring(pt+1).trim();
    }

    int pt = body.indexOf("\n\nThis email");
    if (pt >= 0) body = body.substring(0,pt).trim();

    Matcher match = PREFIX_PTN.matcher(body);
    if (!match.lookingAt()) return false;
    data.strUnit = match.group(1);
    data.strCallId = match.group(2);
    body = body.substring(match.end());

    delim = body.contains("\nApt:") ? "\n" : ",";
    String[] flds = body.split(delim);
    if (delim.equals(",")) delim = ", ";
    return parseFields(flds, data);
  }

  @Override
  public String getProgram() {
    return "UNIT? ID " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("MAP")) return new MapField("[A-Z]-\\d{1,2}-[A-Z](?:-[A-Z]+)?|", true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("x(\\S+) *- *(.*)");
  private class  MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match =  CODE_CALL_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        field = match.group(2);
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      if (!field.startsWith("Loc:")) abort();
      field = field.substring(4).trim();
      int pt = field.indexOf(" - ");
      if (pt >= 0) field = field.substring(0,pt).trim();
      super.parse(field, data);
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("Unk Cross Street", "").trim();
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("//", ",");
      data.strUnit = field;
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      data.strSupp = append(data.strSupp, delim, field);
    }
  }
}
