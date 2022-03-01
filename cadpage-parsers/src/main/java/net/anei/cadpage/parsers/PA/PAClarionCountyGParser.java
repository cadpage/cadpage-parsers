package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAClarionCountyGParser extends FieldProgramParser {

  public PAClarionCountyGParser() {
    super("CLARION COUNTY", "PA",
          "Alert_Code:SRC! Address:ADDRCITY! Type:CALL! Channel:CH? Xstreets:X Narrative:INFO/N+ Common_Name:PLACE! Loc_Info:APT2! Caller_Name:NAME! Caller_Phone:PHONE! Units:UNIT! GPS:GPS! END");
  }

  @Override
  public String getFilter() {
    return "noreply@ntr911sa.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("!NTR!")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("PLACE"))  return new MyPlaceField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);
      PAClarionCountyParser.fixCity(data);
    }
  }

  private static final Pattern PLACE_PTN = Pattern.compile("\\d+ +(.*)");
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = PLACE_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_CODE_PTN = Pattern.compile("Dispatch Code: *([A-Z0-9]*)\\b.*");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_CODE_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
      } else {
        super.parse(field, data);
      }
    }

    @Override
    public String getFieldNames() {
      return "CODE " + super.getFieldNames();
    }
  }
}
