package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Morrow County, OH
 */
public class OHMorrowCountyBParser extends FieldProgramParser {

  public OHMorrowCountyBParser() {
    super("MORROW COUNTY", "OH",
          "( SRC UNIT ID_CALL ADDRCITYST APT_PLACE GPS1 GPS2 X CH INFO! " +
          "| ADDRCITYST! APT_PLACE GPS X CH_INFO! " +
          ") END");
    setupGpsLookupTable(OHMorrowCountyParser.GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "911text@mcems.net";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    data.strCall = subject;

    if (!body.startsWith("LOCATION:")) return false;
    body = body.substring(9).trim();

    if (!parseFields(body.split("\\|"), data)) return false;

    OHMorrowCountyParser.fixCity(data);

    // Sometimes they save GPS coordinates in the cross street field :(
    if (data.strGPSLoc.isEmpty()) {
      data.strCross = setGPSLoc(data.strCross, data);
    }
    return true;
  }

  @Override
  public String getProgram() {
    return "CALL " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new MySourceField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("ID_CALL")) return new MyIdCallField();
    if (name.equals("APT_PLACE")) return new MyAptPlaceField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("CH")) return new MyChannelField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("CH_INFO")) return new MyChannelInfoField();
    return super.getField(name);
  }

  private static final Pattern SRC_PTN = Pattern.compile("Coroner|[A-Z]{1,2}(?:PD|EMS|SO|PD)|(ST ?\\d+)\\b.*");
  private class MySourceField extends SourceField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      StringBuilder result = new StringBuilder();
      for (String src : field.split(";")) {
        src = src.trim();
        Matcher match = SRC_PTN.matcher(src);
        if (!match.matches()) return false;
        String tmp = match.group(1);
        if (tmp != null) src = tmp.replace(" ", "");
        if (result.length() > 0) result.append(',');
        result.append(src);
      }
      data.strSource = result.toString();
      return true;
    }
  }

  private static final Pattern DELIM = Pattern.compile(" *; *");
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = DELIM.matcher(field).replaceAll(",");
      super.parse(field, data);
    }
  }

  private static final Pattern ID_CALL_PTN = Pattern.compile("(\\d\\d-\\d{6})(?: *(\\w.*))?");
  private class MyIdCallField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = ID_CALL_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCallId = match.group(1);
      data.strCall = getOptGroup(match.group(2));
    }

    @Override
    public String getFieldNames() {
      return "ID CALL";
    }
  }

  private static final Pattern APT_PTN = Pattern.compile("(?:APT|RM|ROOM|LOT|UNIT) *(.*)|\\d{1,4}|[A-Z]", Pattern.CASE_INSENSITIVE);
  private class MyAptPlaceField extends Field {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) {
        String apt = match.group(1);
        if (apt == null) apt = field;
        data.strApt = append(data.strApt, "-", apt);
      } else {
        data.strPlace = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "APT PLACE";
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }

  private class MyChannelField extends ChannelField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[ ;]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      field = stripFieldStart(field, "None ");
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);
    }
  }

  private class MyChannelInfoField extends MyInfoField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String ch = p.get(' ');
      if (!ch.equals("None")) data.strChannel = ch;
      super.parse(p.get(), data);
    }

    @Override
    public String getFieldNames() {
      return "CH " + super.getFieldNames();
    }
  }

  @Override
  public String adjustMapAddress(String sAddress) {
    return OHMorrowCountyParser.doAdjustMapAddress(sAddress);
  }
}
