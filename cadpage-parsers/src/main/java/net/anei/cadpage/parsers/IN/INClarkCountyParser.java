package net.anei.cadpage.parsers.IN;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class INClarkCountyParser extends DispatchH05Parser {

  public INClarkCountyParser() {
    super("CLARK COUNTY", "IN",
          "( DATETIME CALL CALL/SDS+? SRC PLACE? ADDRCITY/S6 APT2? X? PLACE? UNIT ( ID! | PHONE ID! | NAME ID! | NAME SKIP PHONE ID! | NAME PHONE ID/Y! ) https:SKIP! " +
          "| ( SKIP UNIT | UNIT ) ADDRCITY/S6 CALL ID! X? ( ST_INFO_BLK | PHONE ST_INFO_BLK | NAME ST_INFO_BLK | NAME PHONE ST_INFO_BLK ) INFO_BLK+ https:SKIP! GPS1! GPS2! PLACE " +
          ") END");
  }

  @Override
  public String getFilter() {
    return "alert@clarkcounty911.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("SRC")) return new SourceField("[A-Z]+\\d+(?:-\\d)?", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("APT2")) return new MyApt2Field();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("UNIT")) return new UnitField("(?:\\b(?:[A-Z][A-Za-z]*\\d+|UEMS)\\b[, ]*)+", true);
    if (name.equals("NAME")) return new MyNameField();
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public boolean checkParse(String field, Data data) {
      field = field.replace('@', '&');
      return super.checkParse(field, data);
    }

    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
    }
  }

  private static final Pattern APT_PTN = Pattern.compile("\\d{1,4}[A-Z]?|[A-Z]");

  private class MyApt2Field extends SkipField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (field.equals(data.strApt)) return true;
      if (!APT_PTN.matcher(field).matches()) return false;
      data.strApt = append(data.strApt, "-", field);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return true;
      if (!field.contains(" / ")) return false;
      super.parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, ",");
      super.parse(field, data);
    }
  }
}
