package net.anei.cadpage.parsers.KS;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
/**
 * Johnson County, KS
 */
public class KSJohnsonCountyParser extends FieldProgramParser {

  public KSJohnsonCountyParser() {
    super("JOHNSON COUNTY", "KS",
          "( Address_UpdateIncident_#:ID! Add:ADDR! Apt:APT! Units:UNIT " +
          "| Comment:INFO Nature:CALL! Add:ADDR! Apt:APT! Lat:GPS1/d! Lon:GPS2/d! Incident:ID " +
          "| Nature:CALL! incident#:ID! Unit:UNIT_INFO! " +
          "| SRC Add:ADDR! Apt:APT Loc:PLACE Nature:CALL! Grid:MAP! Incident:ID Cross:X TAC:CH City:CITY Units:UNIT LAT:GPS1/d LON:GPS2/d Bn:SRC ) END");
  }

  @Override
  public String getFilter() {
    return "93001,ecc1@jocogov.org,ecc2@jocogov.org,ecc3@jocogov.org,ecc4@jocogov.org,@jocofd1.org,@jocoems.org,2183500185";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern POST_PTN = Pattern.compile("([A-Z0-9]+) {2,}(GO ON THE AIR TO) +(.+)");
  private static final Pattern RUN_REPORT_PTN = Pattern.compile("(?:Call Times|RUN REPORT) +(.*?) +incident#:(.*)");
  private static final Pattern RUN_REPORT_BRK = Pattern.compile("(?<![ a-z]) +");


  @Override
  protected boolean parseMsg(String body, Data data) {

    Matcher match = POST_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("UNIT CALL ADDR");
      data.msgType = MsgType.GEN_ALERT;
      data.strUnit = match.group(1);
      data.strCall = match.group(2);
      data.strAddress = match.group(3);
      return true;
    }
    match = RUN_REPORT_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("INFO ID");
      data.msgType = MsgType.RUN_REPORT;
      data.strSupp = RUN_REPORT_BRK.matcher(match.group(1)).replaceAll("\n");
      data.strCallId = match.group(2);
      return true;
    }

    body = body.replaceAll("Incident#", "Incident:").replace("Apt:", " Apt:");
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new MySourceField();
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("MAP")) return new MyMapField();
    if (name.equals("UNIT_INFO")) return new MyUnitInfoField();
    return super.getField(name);
  }

  private class MySourceField extends SourceField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ":");
      super.parse(field, data);
    }
  }

  private static final Pattern APT_PTN = Pattern.compile("(?:APT|RM|ROOM|LOT|SUITE|STE)[# ]*(.*)", Pattern.CASE_INSENSITIVE);
  private class MyAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      super.parse(field, data);
    }


  }

  private class MyMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ".");
      super.parse(field, data);
    }
  }

  private static final Pattern UNIT_INFO_PTN = Pattern.compile("(\\S+) *(\\d\\).*)");
  private class MyUnitInfoField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = UNIT_INFO_PTN.matcher(field);
      if (!match.matches()) abort();
      data.msgType = MsgType.GEN_ALERT;
      data.strUnit = match.group(1);
      data.strSupp = match.group(2);
    }

    @Override
    public String getFieldNames() {
      return "UNIT INFO";
    }

  }
}
