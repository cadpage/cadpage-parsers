package net.anei.cadpage.parsers.SC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class SCLaurensCountyParser extends FieldProgramParser {

  public SCLaurensCountyParser() {
    super("LAURENS COUNTY", "SC",
          "Location:PLACE! Address:ADDRCITYST/S6! Cross_Streets:X! Address_Details:PLACE! Call_Type:CALL! Call_Details:INFO! CFS_Number:ID? Responding_Units:UNIT! END");
  }

  @Override
  public String getFilter() {
    return "laurn911@laurenscountysc.gov,laurn911@co.laurens.sc.us";
  }

  private static final Pattern ST_PTN = Pattern.compile("([A-Z]{2})(?: +\\d{5})?(?: +(.*))?");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.equals("CANCEL PAGE")) {
      setFieldList("CALL ADDR APT CITY ST");
      Parser p = new Parser(body);
      String extra = "";
      String city = p.getLastOptional(',');
      Matcher match = ST_PTN.matcher(city);
      if (match.matches()) {
        data.strState = match.group(1);
        extra = getOptGroup(match.group(2));
        city = p.getLastOptional(',');
      }
      data.strCity = city;
      parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_RECHECK_APT | FLAG_ANCHOR_END, p.get(), data);
      data.strCall = append(data.strCall, " - ", extra);
    } else {
      if (!super.parseMsg(body, data)) return false;
    }
    if (!data.strApt.isEmpty() && data.strAddress.endsWith(" BYPASS")) {
      data.strAddress = data.strAddress + ' ' + data.strApt;
      data.strApt = "";
    }
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ", None");
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - Log - *| +(?=\\d{1,2}\\. +)");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("; ", ",");
      super.parse(field, data);
    }
  }
}
